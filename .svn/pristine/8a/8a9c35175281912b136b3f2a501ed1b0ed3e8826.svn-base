package com.game.event;


import com.game.util.BeanManager;
import com.game.util.ServerTimer;


public class StartHandler {

	public static void dispose() {
	}

	/**
	 * 一些公共的初始化操作可以放在这里，server启动的时候会调用 在所有service的init完毕后调用
	 * @throws Exception 
	 */
	public static void start() throws Exception {
		BeanManager.getBean(ServerTimer.class).start();

	}
}
