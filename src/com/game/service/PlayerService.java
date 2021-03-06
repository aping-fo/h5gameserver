package com.game.service;

import com.game.dao.PlayerDAO;
import com.game.data.LevelCfg;
import com.game.domain.player.Player;
import com.game.sdk.net.Result;
import com.game.sdk.proto.OpenIDResp;
import com.game.sdk.utils.ErrorCode;
import com.game.util.ConfigData;
import com.game.util.JsonUtils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class PlayerService {
    private static Logger logger = Logger.getLogger(PlayerService.class);

    @Autowired
    private PlayerDAO playerDAO;

    private final LoadingCache<String, Player> players = CacheBuilder.newBuilder()
            .expireAfterAccess(600, TimeUnit.SECONDS)
            .maximumSize(5000)
            .build(new CacheLoader<String, Player>() {
                @Override
                public Player load(String openId) throws Exception {
                    logger.info("Cache loaded for " + openId);
                    Player player = playerDAO.queryPlayer(openId);
                    if (player != null) {
                        String historyQuestionStr = player.getHistoryQuestionsStr();
                        if (historyQuestionStr != null && historyQuestionStr.length() > 0) {
                            List<Integer> historyQuestions = JsonUtils.string2Object(historyQuestionStr, List.class);
                            player.setHistoryQuestions(historyQuestions);
                        }
                        String categoryStr = player.getHistoryCatergorysStr();
                        if (categoryStr != null && categoryStr.length() > 0) {
                            List<Integer> historyCatergory = JsonUtils.string2Object(categoryStr, List.class);
                            player.setHistoryCatergorys(historyCatergory);
                        }

                    }

                    return player;
                }
            });


    /**
     * 获取玩家信息
     *
     * @param openId
     * @return
     */
    public Player getPlayer(String openId) {
        try {
            return players.getUnchecked(openId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 创建角色
     *
     * @param openId
     * @param nickName
     */
    public Result createPlayer(String openId, String nickName) {
        Player player = new Player();
        player.setNickName(nickName);
        player.setOpenId(openId);
        player.setLevel(1);
        player.setTotalQuestions(0);
        player.setAnswerSuccess(0);
        player.setHistoryCatergorysStr("");
        playerDAO.insert(player);
        players.put(openId, player);

        return Result.valueOf(ErrorCode.OK, "ok");
    }

    /**
     * 获取OPENID
     *
     * @param openId
     * @param code
     * @return
     * @throws Exception
     */
    public Result getOpenID(String openId, String code) throws Exception {
        String errorCode = ErrorCode.OK;
        OpenIDResp resp = new OpenIDResp();
//        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=wx7004cb8d1a5b3df5&secret=80ab0be6003cec743ea964dedfad101c&grant_type=authorization_code&js_code=" + code;
//        String json = HttpClient.sendGetRequest(url);
//        Map<String, Object> result = JsonUtils.string2Map(json);
//        openId = (String) result.get("openid");
        try {
            players.get(openId);
            resp.setHasRole(true);
        } catch (Exception e) {
            resp.setHasRole(false);
        }
        resp.setOpenId(openId);
        return Result.valueOf(errorCode, resp);
    }

    /**
     * 创建角色
     *
     * @param openId
     */
    public Result getRole(String openId) {
        String code = ErrorCode.OK;
        Map<String, Object> resp = Maps.newHashMap();
        Player player = players.getUnchecked(openId);
        if (player == null) {
            code = ErrorCode.ROLE_NOT_EXIST;
        } else {
            resp.put("openId", player.getOpenId());
            resp.put("level", player.getLevel());
            resp.put("nickName", player.getNickName());
        }
        return Result.valueOf(code, resp);
    }

    /**
     * 增加经验
     *
     * @param openId
     */
    public int checkForLevelup(String openId, int deltaExp) {
        Player player = players.getUnchecked(openId);
        LevelCfg cfg = ConfigData.getConfig(LevelCfg.class, player.getLevel());
        if (cfg == null) {
            return player.getLevel();
        }
        if (deltaExp < 0) {
            player.setExp(0);
        } else {
            int deltLevel = 0;
            while (deltaExp > 0) {
                int nextLevel = player.getLevel() + 1;
                cfg = ConfigData.getConfig(LevelCfg.class, nextLevel);
                if (cfg == null) {
                    return player.getLevel();
                }
                if (deltaExp < cfg.levelUpScore) {
                    break;
                }

                deltaExp -= cfg.levelUpScore;
                deltLevel += 1;
            }

            player.setExp(deltaExp);
            player.setLevel(player.getLevel() + deltLevel);
        }

        playerDAO.saveOrUpdate(player);
        return player.getLevel();
    }

    /**
     * 答题结果，奖励
     *
     * @param openId
     */
    public int answerResult(String openId, boolean victory) {
        Player player = players.getUnchecked(openId);
        LevelCfg cfg = ConfigData.getConfig(LevelCfg.class, player.getLevel());
        if (cfg == null) {
            return player.getLevel();
        }
        int exp = victory ? cfg.rightScore : cfg.wrongScore;
        return checkForLevelup(openId, exp);
    }

    /**
     * 每局胜利奖励
     *
     * @param openId
     * @param victory
     * @return
     */
    public int roundResult(String openId, boolean victory) {
        Player player = players.getUnchecked(openId);
        LevelCfg cfg = ConfigData.getConfig(LevelCfg.class, player.getLevel());
        if (cfg == null) {
            return player.getLevel();
        }

        return checkForLevelup(openId, 10);
    }

    public void schedule() {
        logger.warn("rank .....");
        List<Player> players = playerDAO.queryPlayerRank();

        int count = playerDAO.selectPlayerCount();

    }
}
