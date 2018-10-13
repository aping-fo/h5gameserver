package com.game.sdk.web;

import com.game.SysConfig;
import com.game.sdk.annotation.WebHandler;
import com.game.sdk.net.Exector;
import com.game.sdk.net.Result;
import com.game.sdk.utils.EncoderHandler;
import com.game.sdk.utils.ErrorCode;
import com.game.sdk.utils.ExectorManager;
import com.game.util.JsonUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Base64;

/**
 * Created by lucky on 2018/2/28.
 */
@WebHandler(url = "/medicine", description = "医药世家")
public class MessageHandlerServlet extends SdkServlet {
    private static Logger logger = Logger.getLogger(MessageHandlerServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            int cmd = Integer.parseInt(req.getParameter("cmd")); // 指令
            String sign = req.getParameter("s"); // 签名
            String data = req.getParameter("data"); // 请求参数
            String openId = req.getParameter("openId"); // openID


            if (logger.isInfoEnabled()) {
                logger.info("receive req, [data]= " + data + ",[s] = " + sign + ",[openId] = " + openId + ",[cmd] = " + cmd);
            }

            if (sign == null ||
                    cmd < 0 ||
                    data == null) {
                render(resp, 0, ErrorCode.PARAM_ERROR);
                return;
            }
            //data = URLDecoder.decode(data, "UTF-8");
            data = new String(Base64.getDecoder().decode(data), Charset.forName("UTF-8"));
            String md5Str = SysConfig.oauthsecret + "&" + data;
            String mySign = EncoderHandler.md5(md5Str);
            if (!sign.equals(mySign)) {
                render(resp, 0, ErrorCode.SIGN_ERROR);
                return;
            }

            Exector exector = ExectorManager.getExector(cmd);
            if (exector == null) {
                render(resp, 0, ErrorCode.EXEC_ERROR);
                return;
            }

            Result result;
            if (exector.paramType == null) { //无请求参数的处理
                result = (Result) exector.invoke(openId);
            } else {
                Object paramObject = JsonUtils.string2Object(data, exector.paramType);
                result = (Result) exector.invoke(openId, paramObject);
            }

            if (logger.isInfoEnabled()) {
                logger.info("send data ==> " + JsonUtils.object2String(result));
            }
            render(resp, cmd, result.code, result.data);
        } catch (Throwable e) {
            render(resp, 0, ErrorCode.PARAM_ERROR);
            logger.error("exector error", e);
        }
    }
}
