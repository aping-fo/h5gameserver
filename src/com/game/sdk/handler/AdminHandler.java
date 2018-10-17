package com.game.sdk.handler;

import com.game.sdk.annotation.Command;
import com.game.sdk.annotation.Handler;
import com.game.sdk.net.Cmd;
import com.game.sdk.net.Result;
import com.game.sdk.proto.CreateRoleReq;
import com.game.sdk.utils.ErrorCode;
import com.game.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lucky on 2018/10/11.
 */
@Handler
public class AdminHandler {

    @Autowired
    private AdminService adminService;

    @Command(cmd = Cmd.ADMIN_RELOAD_CFG, description = "重载配置")
    public Result createRole(String openId, CreateRoleReq req) throws Exception {
        adminService.reloadConfig();
        return Result.valueOf(ErrorCode.OK, "reload config success");
    }

}
