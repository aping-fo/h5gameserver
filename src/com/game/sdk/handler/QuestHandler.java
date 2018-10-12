package com.game.sdk.handler;

import com.game.sdk.annotation.Command;
import com.game.sdk.annotation.Handler;
import com.game.sdk.net.Cmd;
import com.game.sdk.net.Result;
import com.game.sdk.proto.StartMatchReq;
import com.game.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lucky on 2018/10/11.
 */
@Handler
public class QuestHandler {

    @Autowired
    private QuestService questService;

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
        Result result = questService.endMatch(openId);
        return result;
    }
}
