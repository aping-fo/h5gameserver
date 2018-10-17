package com.game.dao;

import com.game.domain.player.Player;
import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import java.util.List;

@DAO
public interface PlayerDAO {

    @SQL("select * from t_u_player where openId = :openId")
    public Player queryPlayer(@SQLParam("openId") String openId);

    @SQL("insert into t_u_player(openId,nickName,level,totalQuestions,answerSuccess,historyCatergorysStr,historyQuestionsStr,exp) values(:player.openId,:player.nickName,:player.level,:player.totalQuestions,:player.answerSuccess,:player.historyCatergorysStr,:player.historyQuestionsStr,:player.exp)")
    public void insert(@SQLParam("player") Player player);


    @SQL("REPLACE INTO t_u_player VALUES (:player.openId,:player.nickName,:player.level,:player.totalQuestions,:player.answerSuccess,:player.historyCatergorysStr,:player.historyQuestionsStr,:player.exp)")
    public void saveOrUpdate(@SQLParam("player") Player player);

    @SQL("SELECT openId, nickName, level,exp FROM t_u_player ORDER BY level DESC,exp DESC LIMIT 100")
    public List<Player> queryPlayerRank();

    @SQL("select count(1) from t_u_player")
    public Integer selectPlayerCount();

}
