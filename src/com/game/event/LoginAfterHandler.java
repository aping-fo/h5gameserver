package com.game.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.game.util.TimerService.MyTheadFactory;
import org.springframework.stereotype.Service;

import com.game.util.BeanManager;

@Service
public class LoginAfterHandler implements InitHandler {

	private static final ExecutorService scheduExec = Executors.newFixedThreadPool(2, new MyTheadFactory("Login"));

	private static List<ILogin> loginHandlers = new ArrayList<ILogin>();

	public void playerLogin(final int playerId) {
		for(ILogin login : loginHandlers){			
			scheduExec.submit(new Runnable() {
				@Override
				public void run() {
					try {
						login.playerLogined(playerId);
					} catch (Exception e) {
					}
				}
			});
		}
	}

	@Override
	public void handleInit() {
		loginHandlers.addAll(BeanManager.getApplicationCxt()
				.getBeansOfType(ILogin.class).values());
	}

}
