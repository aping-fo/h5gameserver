package com.game.sdk.handler;

import com.game.sdk.annotation.Command;
import com.game.sdk.annotation.Handler;
import com.game.sdk.net.Cmd;
import com.game.sdk.net.Result;
import com.game.sdk.proto.AnswerQuestionReq;
import com.game.sdk.proto.GetAnswerReq;
import com.game.sdk.proto.JoinRoomReq;
import com.game.sdk.proto.StartMatchReq;
import com.game.service.QuestBankService;
import com.game.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lucky on 2018/10/11.
 */
@Handler
public class QuestHandler {

    @Autowired
    private QuestService questService;
    @Autowired
    private QuestBankService questBankService;

    @Command(cmd = Cmd.START_MATCH, description = "请求匹配")
    public Result starMatch(String openId, StartMatchReq req) throws Exception {
        Result result = questService.startMatch(openId, req);
        return result;
    }

    @Command(cmd = Cmd.END_MATCH, description = "退出匹配")
    public Result endMatch(String openId) throws Exception {
        Result result = questService.endMatch(openId);
        return result;
    }

    @Command(cmd = Cmd.GET_MATCH_RESULT, description = "获取匹配结果")
    public Result queryMatchResult(String openId) throws Exception {
        Result result = questService.queryMatchResult(openId);
        return result;
    }

    @Command(cmd = Cmd.GET_QUEST_BANK_CATEGORY, description = "请求题库类型")
    public Result getQuests(String openId) throws Exception {
        Result result = questBankService.getQuestCategorys(openId);
        return result;
    }

    @Command(cmd = Cmd.GET_QUEST_INDEX, description = "请求亮题")
    public Result getQuestContent(String openId) throws Exception {
        return questBankService.getQuestIndex(openId);
    }

    @Command(cmd = Cmd.ANSWER_QUEST, description = "请求答题")
    public Result answerQuestion(String openId, AnswerQuestionReq req) throws Exception {
        return questBankService.answerTheQuestion(openId, req.getAnswer());
    }

    @Command(cmd = Cmd.GET_ANSWER, description = "请求答案")
    public Result answerQuestion(String openId, GetAnswerReq req) throws Exception {
        return questBankService.getAnswer(openId, req.getCfgId());
    }

    @Command(cmd = Cmd.ROB_ANSWER, description = "抢答")
    public Result robAnswer(String openId) throws Exception {
        return questBankService.robAnswer(openId);
    }

    @Command(cmd = Cmd.CHECK_ROB, description = "检查是否被抢答")
    public Result checkRob(String openId) throws Exception {
        return questBankService.checkRob(openId);
    }

    @Command(cmd = Cmd.GET_ROOM_RESULT, description = "获取答题信息")
    public Result getRoomAnswers(String openId) throws Exception {
        return questBankService.getRoomAnswers(openId);
    }

    @Command(cmd = Cmd.SUBMIT_VICTORY, description = "提交胜利")
    public Result sumbitResult(String openId) throws Exception {
        return questBankService.sumbitResult(openId);
    }

    @Command(cmd = Cmd.CREATE_ROOM, description = "创建房间")
    public Result createRoom(String openId) throws Exception {
        return questService.createRoom(openId);
    }

    @Command(cmd = Cmd.JOIN_ROOM, description = "加入房间")
    public Result joinRoom(String openId, JoinRoomReq req) throws Exception {
        return questService.joinRoom(openId, req.getRoomID());
    }

//    @Command(cmd = Cmd.GET_HISTORY_QUESTION, description = "请求历史题库")
//    public Result getHistoryQuestion(String openId) throws Exception {
//        Result result = questService.queryistoryQuestion(openId);
//        return result;
//    }
}
