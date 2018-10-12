package com.game.util;

import org.springframework.stereotype.Service;


/**
 * 一些公共的bean可以放在这里，避免了@AutoWare
 * 
 * @author luojian
 */
@Service
public class Context {

	private static ThreadService threadService;// 线程管理
	private static TimerService timerService;// 定时器系统

	
	public static ThreadService getThreadService() {
		return Context.threadService;
	}

	public static TimerService getTimerService() {
		return timerService;
	}
}
