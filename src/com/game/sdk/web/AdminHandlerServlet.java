package com.game.sdk.web;

import com.game.SysConfig;
import com.game.sdk.annotation.WebHandler;
import com.game.sdk.net.Exector;
import com.game.sdk.net.Result;
import com.game.sdk.utils.ErrorCode;
import com.game.sdk.utils.ExectorManager;
import com.game.util.JsonUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by lucky on 2018/2/28.
 */
@WebHandler(url = "/m/admin", description = "admin")
public class AdminHandlerServlet extends SdkServlet {
    private static Logger logger = Logger.getLogger(AdminHandlerServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            int cmd = Integer.parseInt(req.getParameter("cmd")); // 指令
            String s = req.getParameter("s"); // 签名
            String data = req.getParameter("data"); // 参数
            String openid = req.getParameter("openid"); // 管理员账号


            logger.info("receive req, [s] = " + s + ",[cmd] = " + cmd);


            if (s == null ||
                    openid == null ||
                    cmd < 10000) {// admin 10000开始
                render(resp, 0, ErrorCode.PARAM_ERROR);
                return;
            }

            if (!s.equals(SysConfig.oauthsecret)) {
                render(resp, 0, ErrorCode.SIGN_ERROR);
                return;
            }

            if (!openid.equals(SysConfig.oauthkey)) {
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
                result = (Result) exector.invoke(openid);
            } else {
                Object paramObject = JsonUtils.string2Object(data, exector.paramType);
                result = (Result) exector.invoke(openid, paramObject);
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
