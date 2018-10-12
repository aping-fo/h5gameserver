package com.game.sdk.handler;

import com.game.sdk.annotation.Command;
import com.game.sdk.annotation.Handler;
import com.game.sdk.http.HttpClient;
import com.game.sdk.net.Result;
import com.game.sdk.proto.OpenIDReq;
import com.game.sdk.net.Cmd;
import com.game.sdk.utils.ErrorCode;

/**
 * Created by lucky on 2018/10/11.
 */
@Handler
public class LoginHandler {

    @Command(cmd = Cmd.GET_OPENID, description = "请求OPENID")
    public Result getOpenID(String openId, OpenIDReq req) throws Exception {
        //String url = "https://api.weixin.qq.com/sns/jscode2session?appid=wx7004cb8d1a5b3df5&secret=80ab0be6003cec743ea964dedfad101c&grant_type=authorization_code&js_code=" + req.getCode();
        //String json = HttpClient.sendGetRequest(url);
        return Result.valueOf(ErrorCode.OK, "ssssssssssssss");
    }
}
