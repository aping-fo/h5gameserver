package com.game.sdk.net;

/**
 * Created by lucky on 2018/10/11.
 */
public class Cmd {
    //请求openid
    public static final int GET_OPENID = 1001;
    //请求匹配
    public static final int START_MATCH = 2001;
    //退出匹配
    public static final int END_MATCH = 2002;
    //请求匹配结果
    public static final int GET_MATCH_RESULT = 2003;
    //请求题库
    public static final int GET_QUEST_BANK = 2004;

    //请求历史题库
    public static final int GET_HISTORY_QUESTION = 3000;
}
