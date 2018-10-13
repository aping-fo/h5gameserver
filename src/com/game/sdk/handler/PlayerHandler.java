package com.game.sdk.handler;

import com.game.sdk.annotation.Command;
import com.game.sdk.annotation.Handler;
import com.game.sdk.net.Cmd;
import com.game.sdk.net.Result;
import com.game.sdk.proto.CreateRoleReq;
import com.game.sdk.proto.OpenIDReq;
import com.game.sdk.utils.ErrorCode;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lucky on 2018/10/11.
 */
@Handler
public class PlayerHandler {

    @Autowired
    private PlayerService playerService;

    @Command(cmd = Cmd.GET_OPENID, description = "请求OPENID")
    public Result getOpenID(String openId, OpenIDReq req) throws Exception {
        Result result = playerService.getOpenID(openId, req.getCode());
        return result;
    }


    @Command(cmd = Cmd.GET_ROLE, description = "请求创角色")
    public Result createRole(String openId, CreateRoleReq req) throws Exception {
        Result result = playerService.createPlayer(openId, req.getNickName());
        return result;
    }

    @Command(cmd = Cmd.CREATE_ROLE, description = "请求创建角色")
    public Result getRole(String openId) throws Exception {
        Result result = playerService.getRole(openId);
        return result;
    }
}
