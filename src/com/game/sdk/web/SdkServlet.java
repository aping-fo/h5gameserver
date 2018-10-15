package com.game.sdk.web;

import com.game.util.JsonUtils;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by lucky on 2018/2/28.
 */
public class SdkServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(SdkServlet.class);


    public void render(HttpServletResponse resp, int cmd, String code) {
        render(resp, cmd, code, "");
    }

    public void render(HttpServletResponse resp, int cmd, String code, String message) {
        try {
            resp.addHeader("Pragma", "no-cache");
            resp.addHeader("Accept", "*/*");
            resp.addHeader("Access-Control-Allow-Origin", "*");
            resp.addHeader("content-type","text/html;charset=UTF-8");
            resp.setCharacterEncoding("UTF-8");

            Map<String, String> result = Maps.newHashMap();
            result.put("cmd", String.valueOf(cmd));
            result.put("code", code);
            result.put("data", message);

            if (logger.isInfoEnabled()) {
                logger.info(JsonUtils.map2String(result));
            }
            resp.getWriter().write(JsonUtils.map2String(result));
            resp.getWriter().flush();
        } catch (Exception e) {
            logger.error(message, e);
        }
    }
}
