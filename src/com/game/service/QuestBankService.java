package com.game.service;

import com.game.data.QuestionCfg;
import com.game.domain.player.Player;
import com.game.domain.quest.Answer;
import com.game.domain.quest.Fighter;
import com.game.domain.quest.Room;
import com.game.sdk.net.Result;
import com.game.sdk.proto.AnswerResultResp;
import com.game.sdk.proto.QuestBankResp;
import com.game.sdk.proto.QuestCategoryResp;
import com.game.sdk.proto.vo.AnswerVO;
import com.game.sdk.proto.vo.QuestVO;
import com.game.sdk.utils.ErrorCode;
import com.game.util.ConfigData;
import com.game.util.RandomUtil;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class QuestBankService extends AbstractService {
    private static Logger logger = Logger.getLogger(QuestBankService.class);

    @Autowired
    private QuestService questService;
    @Autowired
    private PlayerService playerService;

    public Result getQuestCategorys(String openId) throws Exception {
        QuestCategoryResp resp = new QuestCategoryResp();

        Fighter matcher = questService.getMatcher(openId);
        if (matcher == null) {
            return Result.valueOf(ErrorCode.ROLE_NOT_EXIST, resp);
        }

        Room room = questService.getRoom(matcher.getRoomId());
        if (matcher.getRoomId() == 0 || room == null) {
            return Result.valueOf(ErrorCode.ROLE_NOT_IN_GAME, resp);
        }

        List<Integer> questionCategorys = room.getQuestionCategorys();
        if (questionCategorys.size() == 0) {
            //随机题型
            Collection<Object> configs = ConfigData.getConfigs(QuestionCfg.class);
            int difficulty = 6;
//            List<Integer> indexs = RandomUtil.getRandomIndexs(configs.size(), 9);

            for (int i = 0; i < 9; i++) {
                questionCategorys.add(RandomUtil.randInt(1, difficulty));
            }
//            room.getCurrentQuestions().addAll(questions);

        }
        resp.setQuestionCategorys(questionCategorys);

        return Result.valueOf(ErrorCode.OK, resp);
    }

    public Result getQuestIndex(String openId) throws Exception {
        Fighter matcher = questService.getMatcher(openId);
        if (matcher == null) {
            return Result.valueOf(ErrorCode.ROLE_NOT_EXIST, "0");
        }

        Room room = questService.getRoom(matcher.getRoomId());

        if (room == null) {
            return Result.valueOf(ErrorCode.ROLE_NOT_IN_GAME, "0");
        }
        int vo;
        QuestVO currentQuest = room.getCurrentQuest();
        if (currentQuest != null) {
            vo = currentQuest.getIndex();
        } else {
            vo = room.randomEmptyIndexs();
        }

        return Result.valueOf(ErrorCode.OK, String.valueOf(vo));
    }

    /**
     * 抢答
     *
     * @param openId
     * @return
     */
    public Result robAnswer(String openId) {
        Fighter matcher = questService.getMatcher(openId);
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

        QuestVO question = room.getCurrentQuest();
        if (question == null) {
            //随机派题
            int currentQuestIndex = room.getCurrentIndex();
            if (currentQuestIndex != -1) {
                int questCategory = room.getQuestionCategorys().get(currentQuestIndex);
                Collection<Object> cfgs = ConfigData.getConfigs(QuestionCfg.class);
                Object[] array = cfgs.toArray();

                while (true) {
                    int index = RandomUtil.randInt(array.length);
                    QuestionCfg config = (QuestionCfg) array[index];
                    boolean isOld = false;

                    for (QuestVO oldVo : room.getCurrentQuestions()) {
                        if (oldVo.getId() == config.id) {
                            isOld = true;
                            break;
                        }
                    }

                    if (config.catergory == questCategory && !isOld) {
                        question = new QuestVO();
                        question.setId(config.id);
                        question.setContent(config.content);
                        question.setAnswer(config.answerIndex);
                        question.setOptions(config.options);
                        question.setCategory(config.catergory);
                        question.setDifficulty(config.difficulty);
                        question.setIndex(currentQuestIndex);

                        room.setCurrentQuest(question);
                        room.getCurrentQuestions().add(question);
                        break;
                    }
                }
            }
        }

        Map<String, Object> resp = Maps.newHashMapWithExpectedSize(2);
        resp.put("result", result);
        resp.put("question", question);
        return Result.valueOf(ErrorCode.OK, resp);
    }

    /**
     * 检查棋子是否已经被抢
     *
     * @param openId
     * @return
     */
    public Result checkRob(String openId) {
        Fighter matcher = questService.getMatcher(openId);
        if (matcher == null) {
            return Result.valueOf(ErrorCode.ROLE_NOT_EXIST, "0");
        }

        Room room = questService.getRoom(matcher.getRoomId());
        if (room == null) {
            return Result.valueOf(ErrorCode.ROLE_NOT_IN_GAME, "0");
        }

        String answerOpenid = room.getAnswerOpendid();
        boolean result = false;
        if (answerOpenid != null && !answerOpenid.equals(openId)) {
            result = true;
        }
        Map<String, Object> resp = Maps.newHashMapWithExpectedSize(1);
        resp.put("result", result);
        if (result) {
            QuestVO quest = room.getCurrentQuest();
            resp.put("question", quest);
        }
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
        Fighter matcher = questService.getMatcher(openId);
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

        if (!matcher.isRobot()) {
            Player player = playerService.getPlayer(openId);
            player.addHistoryQuestion(room.getCurrentQuest().getId());
            playerService.answerResult(openId, result);
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

        Fighter matcher = questService.getMatcher(openId);
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

        Fighter fighter = questService.getMatcher(openId);
        if (fighter == null) {
            return Result.valueOf(ErrorCode.ROLE_NOT_EXIST, "0");
        }

        Room room = questService.getRoom(fighter.getRoomId());
        if (room == null) {
            return Result.valueOf(ErrorCode.ROLE_NOT_IN_GAME, "0");
        }

        if (!fighter.isRobot()) {
            playerService.roundResult(openId, true);
        }

        fighter.victory = true;

        questService.removeRoom(room.getId());
        room.setVictoryOpenid(openId);
        Map<String, Object> resp = Maps.newHashMapWithExpectedSize(1);
        resp.put("result", true);
        return Result.valueOf(ErrorCode.OK, resp);
    }
}
