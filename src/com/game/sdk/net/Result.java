package com.game.sdk.net;

import com.game.util.JsonUtils;

/**
 * Created by lucky on 2018/10/11.
 */
public class Result {
    public String code;
    public String data;

    public static Result valueOf(String code, Object object) {
        Result result = new Result();
        result.code = code;
        result.data = JsonUtils.object2String(object);
        return result;
    }
}
