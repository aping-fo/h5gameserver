package com.game.service;

import com.game.domain.player.Player;
import com.game.sdk.net.Result;
import com.game.sdk.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lucky on 2018/10/16.
 * 大师赛
 */
@Service
public class MasterMatchService {

    @Autowired
    private ServerDataService serverDataService;
    @Autowired
    private PlayerService playerService;

    @Autowired
    private QuestService questService;


    /**
     * 报名大师赛
     *
     * @param openid
     * @return
     */
    public Result signup(String openid) {
        Player player = playerService.getPlayer(openid);
        player.victory = true;

        //TODO 判断是否可以报名
        serverDataService.addMasterFighter(player);
        return Result.valueOf(ErrorCode.OK, "");
    }


    public void schedule() {
        //TODO

    }
}
