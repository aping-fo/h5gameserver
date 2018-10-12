package com.game.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.game.util.TimerService;
import org.springframework.stereotype.Service;

import com.game.SysConfig;
import com.game.util.BeanManager;

/**
 * 清除缓存
 */
@Service
public class DisposeHandler implements InitHandler {
	
	public static final ScheduledExecutorService scheduExec = Executors
			.newScheduledThreadPool(SysConfig.disposeThread,
					new TimerService.MyTheadFactory("Dispose"));

	private static List<Dispose> disposeHandlers = new ArrayList<Dispose>();

	public static void dispose() {
		scheduExec.shutdown();
	}

	public static void dispose(final int playerId) {
		scheduExec.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					removeCache(playerId);
				} catch (Exception e) {
				}
			}
		}, SysConfig.delayDispose, TimeUnit.SECONDS);
	}

	public static boolean removeCache(int playerId) {

		return true;
	}

	@Override
	public void handleInit() {
		disposeHandlers.addAll(BeanManager.getApplicationCxt()
				.getBeansOfType(Dispose.class).values());
	}

}
