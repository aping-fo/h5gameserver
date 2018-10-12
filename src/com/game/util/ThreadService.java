package com.game.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.game.SysConfig;

/**
 * 线程池统一管理
 * 
 * @author luojian
 * 
 */
@Component
public class ThreadService {

	private final ExecutorService scheduExec;

	public ThreadService() {
		scheduExec = Executors.newFixedThreadPool(SysConfig.serverThread,
				new TimerService.MyTheadFactory("ServerThread"));
	}

	/**
	 * 立即执行
	 * 
	 * @param command
	 */
	public void execute(final Runnable command) {
		scheduExec.execute(new Runnable() {
			@Override
			public void run() {
				try {
					command.run();
				} catch (Exception e) {
				}
			}
		});
	}
	


	public void shutdown(){
		scheduExec.shutdown();
		try {
			scheduExec.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
	}
}
