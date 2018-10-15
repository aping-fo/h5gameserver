package com.game.sdk.net;

/**
 * Created by lucky on 2018/10/11.
 */
public class Cmd {
    //请求openid
    public static final int GET_OPENID = 1001;
    //请求创建角色
    public static final int CREATE_ROLE = 1002;
    //请求角色数据
    public static final int GET_ROLE = 1003;

    //请求匹配
    public static final int START_MATCH = 2001;
    //退出匹配
    public static final int END_MATCH = 2002;
    //请求匹配结果
    public static final int GET_MATCH_RESULT = 2003;
    //请求题库
    public static final int GET_QUEST_BANK = 2004;
    //亮题
    public static final int GET_QUEST = 2005;
    //答题
    public static final int ANSWER_QUEST = 2006;
    //请求答案
    public static final int GET_ANSWER = 2007;
    //请求抢答
    public static final int ROB_ANSWER = 2008;
    //请求答题信息
    public static final int GET_ROOM_RESULT = 2009;
    //提交胜利
    public static final int SUBMIT_VICTORY = 2010;
    //请求历史题库
    public static final int GET_HISTORY_QUESTION = 3000;
}
