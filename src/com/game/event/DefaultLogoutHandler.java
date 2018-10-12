package com.game.event;


import com.game.util.Context;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DefaultLogoutHandler {


    public void handleLogout(final int playerId) {
        Context.getThreadService().execute(new Runnable() {
            @Override
            public void run() {
                logout(playerId);
            }
        });
    }

    /**
     * 下线处理 要处理下线，就实现LogoutListener 接口， 不要在这边直接调用模块代码
     */
    public void logout(int playerId) {

    }

}
