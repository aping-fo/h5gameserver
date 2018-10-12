package com.game.sdk.utils;

/**
 * Created by lucky on 2018/10/11.
 */
public class ErrorCode {
    //成功
    public static final String OK = "200";
    //服务器内部错误
    public static final String SERVER_INTERNAL_ERROR = "500";

    //参数错误
    public static final String PARAM_ERROR = "501";

    //签名错误
    public static final String SIGN_ERROR = "502";

    //找不到对应的处理接口
    public static final String EXEC_ERROR = "503";

    //角色不存在
    public static final String ROLE_NOT_EXIST = "504";

    //历史题库记录为空
    public static final String Hitory_Question_Empty = "600";

}
