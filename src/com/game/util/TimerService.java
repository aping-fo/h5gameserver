package com.game.util;

import com.game.SysConfig;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 定时器服务
 *
 * @author luojian
 */
@Component
public class TimerService {

    private final ScheduledExecutorService scheduledService;

    public TimerService() {
        scheduledService = Executors.newScheduledThreadPool(
                SysConfig.scheduledThread, new MyTheadFactory("xxxxxxx"));
    }

    /**
     * 以一定的频率执行
     */
    public ScheduledFuture<?> scheduleAtFixedRate(final Runnable runnable,
                                                  long initDelay, long period, TimeUnit timeUnit) {
        return scheduledService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                }

            }
        }, initDelay, period, timeUnit);
    }

    /**
     * 延迟多少时间后执行
     */
    public ScheduledFuture<?> scheduleDelay(final Runnable runnable,
                                            long delay, TimeUnit timeUnit) {
        return scheduledService.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                }
            }
        }, delay, timeUnit);
    }

    /**
     * 每次执行完再间隔时间执行
     */
    public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable runnable,
                                                     int initDelay, long delay, TimeUnit unit) {
        return scheduledService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                }

            }
        }, initDelay, delay, unit);
    }

    public static class MyTheadFactory implements ThreadFactory {

        final ThreadGroup group;
        final AtomicInteger threadNumber = new AtomicInteger(1);
        final String namePrefix;

        public MyTheadFactory(String name) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = name.concat("-thread-");
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}
