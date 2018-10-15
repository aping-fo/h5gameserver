package com.game.service;

import com.game.data.QuestionCfg;
import com.game.domain.quest.Answer;
import com.game.domain.quest.Matcher;
import com.game.domain.quest.Room;
import com.game.sdk.net.Result;
import com.game.sdk.proto.AnswerResultResp;
import com.game.sdk.proto.QuestBankResp;
import com.game.sdk.proto.vo.AnswerVO;
import com.game.sdk.proto.vo.QuestVO;
import com.game.sdk.utils.ErrorCode;
import com.game.util.ConfigData;
import com.game.util.RandomUtil;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class QuestBankService extends AbstractService {
    private static Logger logger = Logger.getLogger(QuestBankService.class);

    @Autowired
    private QuestService questService;

    public Result getQuests(String openId) throws Exception {
        QuestBankResp resp = new QuestBankResp();

        Matcher matcher = questService.getMatcher(openId);
        if (matcher == null) {
            return Result.valueOf(ErrorCode.ROLE_NOT_EXIST, resp);
        }

        Room room = questService.getRoom(matcher.getRoomId());
        if (matcher.getRoomId() == 0 || room == null) {
            return Result.valueOf(ErrorCode.ROLE_NOT_IN_GAME, resp);
        }

        List<QuestVO> questions = room.getQuestions();
        if (questions.size() == 0) {
            //随机题库
            Collection<Object> configs = ConfigData.getConfigs(QuestionCfg.class);
            Object[] array = configs.toArray();
            List<Integer> indexs = RandomUtil.getRandomIndexs(configs.size(), 9);

            for (int index : indexs) {
                QuestionCfg config = (QuestionCfg) array[index];
                QuestVO vo = new QuestVO();

                vo.setId(config.id);
                vo.setContent(config.content);
                vo.setOptions(config.options);
                vo.setAnswer(config.answerIndex);
                vo.setCategory(config.catergory);
                vo.setDifficulty(config.difficulty);
                questions.add(vo);
            }
            room.getCurrentQuestions().addAll(questions);
        }
        resp.setQuestions(questions);

        return Result.valueOf(ErrorCode.OK, resp);
    }

    public Result getQuest(String openId) throws Exception {
        Matcher matcher = questService.getMatcher(openId);
        if (matcher == null) {
            return Result.valueOf(ErrorCode.ROLE_NOT_EXIST, "0");
        }

        Room room = questService.getRoom(matcher.getRoomId());
        if (room == null) {
            return Result.valueOf(ErrorCode.ROLE_NOT_IN_GAME, "0");
        }

        QuestVO vo = room.randomQuest();
        int cfg = 0;
        if (vo != null) {
            cfg = vo.getId();
        }

        return Result.valueOf(ErrorCode.OK, String.valueOf(cfg));
    }

    /**
     * 抢答
     *
     * @param openId
     * @return
     */
    public Result robAnswer(String openId) {
        Matcher matcher = questService.getMatcher(openId);
        if (matcher == null) {
            return Result.valueOf(ErrorCode.ROLE_NOT_EXIST, "0");
        }

        Room room = questService.getRoom(matcher.getRoomId());
        if (room == null) {
            return Result.valueOf(ErrorCode.ROLE_NOT_IN_GAME, "0");
        }

        String answerOpenid = room.robAnswer(openId);
        boolean result = false;
        if (answerOpenid.equals(openId)) {
            result = true;
        }
        Map<String, Object> resp = Maps.newHashMapWithExpectedSize(1);
        resp.put("result", result);
        return Result.valueOf(ErrorCode.OK, resp);
    }

    /**
     * 答题
     *
     * @param openId
     * @param answer
     * @return
     */
    public Result answerTheQuestion(String openId, int answer) {
        Matcher matcher = questService.getMatcher(openId);
        if (matcher == null) {
            return Result.valueOf(ErrorCode.ROLE_NOT_EXIST, "0");
        }

        Room room = questService.getRoom(matcher.getRoomId());
        if (room == null) {
            return Result.valueOf(ErrorCode.ROLE_NOT_IN_GAME, "0");
        }

        if (!room.getAnswerOpendid().equals(openId)) {
            return Result.valueOf(ErrorCode.ROLE_NOT_IN_GAME, "0");
        }

        Answer answerResult = new Answer();
        answerResult.setOpenid(openId);
        answerResult.setResult(false);
        answerResult.setCfgid(room.getCurrentQuest().getId());
        boolean result = false;
        if (room.getCurrentQuest().getAnswer() == answer) { //正确
            result = true;
            answerResult.setResult(true);

        }
        room.getAnswers().put(answerResult.getCfgid(), answerResult);
        room.cleanQuest();
        Map<String, Object> resp = Maps.newHashMapWithExpectedSize(1);
        resp.put("result", result);

        return Result.valueOf(ErrorCode.OK, resp);
    }

    /**
     * 获得答案
     *
     * @param openId
     * @param cfgId
     * @return
     */
    public Result getAnswer(String openId, int cfgId) {
        Map<String, Object> resp = Maps.newHashMapWithExpectedSize(1);
        resp.put("answer", 0);

        QuestionCfg cfg = ConfigData.getConfig(QuestionCfg.class, cfgId);
        if (cfg == null) {
            return Result.valueOf(ErrorCode.PARAM_ERROR, resp);
        }

        resp.put("answer", cfg.answerIndex);

        return Result.valueOf(ErrorCode.OK, resp);
    }

    public Result getRoomAnswers(String openId) {
        AnswerResultResp resp = new AnswerResultResp();

        Matcher matcher = questService.getMatcher(openId);
        if (matcher == null) {
            return Result.valueOf(ErrorCode.ROLE_NOT_EXIST, "0");
        }

        Room room = questService.getRoom(matcher.getRoomId());
        if (room == null) {
            return Result.valueOf(ErrorCode.ROLE_NOT_IN_GAME, "0");
        }

        for (Answer answer : room.getAnswers().values()) {
            AnswerVO vo = new AnswerVO();
            vo.setCfgid(answer.getCfgid());
            vo.setOpenid(answer.getOpenid());
            vo.setResult(answer.isResult());

            resp.getAnswerVOS().add(vo);
        }

        return Result.valueOf(ErrorCode.OK, resp);
    }

    public Result sumbitResult(String openId) {
        AnswerResultResp resp = new AnswerResultResp();

        Matcher matcher = questService.getMatcher(openId);
        if (matcher == null) {
            return Result.valueOf(ErrorCode.ROLE_NOT_EXIST, "0");
        }

        Room room = questService.getRoom(matcher.getRoomId());
        if (room == null) {
            return Result.valueOf(ErrorCode.ROLE_NOT_IN_GAME, "0");
        }

        room.setVictoryOpenid(openId);

        return Result.valueOf(ErrorCode.OK, resp);
    }
}
