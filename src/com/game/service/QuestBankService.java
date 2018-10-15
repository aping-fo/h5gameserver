package com.game.service;

import com.game.data.QuestionCfg;
import com.game.domain.quest.Matcher;

import com.game.sdk.net.Result;
import com.game.sdk.proto.QuestBankResp;
import com.game.sdk.proto.vo.QuestVO;
import com.game.sdk.utils.ErrorCode;
import com.game.util.ConfigData;
import com.game.util.RandomUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Collection;

@Service
public class QuestBankService extends AbstractService {
    private static Logger logger = Logger.getLogger(QuestBankService.class);

    @Autowired
    private QuestService questService;

    public Result getQuests(String openId) throws Exception{
        QuestBankResp resp = new QuestBankResp();

        Matcher matcher = questService.getMatcher(openId);
        if(matcher == null){
            return Result.valueOf(ErrorCode.ROLE_NOT_EXIST, resp);
        }

        if(matcher.getRoomId() == 0 || questService.getRoom(matcher.getRoomId()) == null){
            return Result.valueOf(ErrorCode.ROLE_NOT_IN_GAME, resp);
        }

        List<QuestVO> questions = questService.getRoom(matcher.getRoomId()).getQuestions();
        if(questions.size() == 0){
            //随机题库
            Collection<Object> configs = ConfigData.getConfigs(QuestionCfg.class);
            Object[] array = configs.toArray();
            List<Integer> indexs = RandomUtil.getRandomIndexs(configs.size(), 9);

            for(int index: indexs){
                QuestionCfg config = (QuestionCfg)array[index];
                QuestVO vo = new QuestVO();

                vo.setId(config.id);
                vo.setContent(config.content);
                vo.setOptions(config.options);
                vo.setAnswer(config.answerIndex);
                vo.setCategory(config.catergory);
                vo.setDifficulty(config.difficulty);
                questions.add(vo);
            }
        }
        resp.setQuestions(questions);

        return Result.valueOf(ErrorCode.OK,resp);
    }

}
