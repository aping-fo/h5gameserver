package com.game.util;

import com.game.SysConfig;
import com.game.event.ServiceDispose;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 定时执行的任务
 */
@Service
public class ServerTimer {

    private final ScheduledExecutorService scheduExec = Executors
            .newScheduledThreadPool(SysConfig.timerThread, new TimerService.MyTheadFactory(
                    "ServerTimer"));

    private List<TimerObject> timers = new ArrayList<TimerObject>();

    public void dispose() {
        scheduExec.shutdown();
    }

    /**
     * 将需要定时/定期执行的函数加入队列 参数1 cron:* 1/2 2-4 2 4,5 分别代表:分 时 日 月 星期 年，用空格分开每个选项
     * 1.n/m 表示n开始，递增m，直到这个域的上限 2.n-m 表示n到m之间的数值 3.n,m 表示 列举数字中的一个 4.星号*表示任意的
     * 5.具体的数值
     * <p>
     * 参数2 是service的名字，类名的首字母小写 参数3是方法名，该方法不带参数的
     */
    private void handleInit() {
        // 凌晨0点执行
        timers.add(new TimerObject("0 0 * * * *", "playerService", "schedule"));
        //timers.add(new TimerObject("0 * * * * *", "masterMatchService", "schedule"));

        updateTimeStr();
    }

    public String toTimerFormat(String time) {
        String[] data = time.split("\\:");
        return String.format("%s %s * * * *", data[1], data[0]);
    }

    // 中途增加timer
    public void addTimer(String cron, String service, String function) {
        timers.add(new TimerObject(cron, service, function));
    }

    public void updateTimeStr() {
        // 更新每天的时间字符串
        Date nowTime = new Date();
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
        TimeUtil.CUR_TIME_FORMAT = time.format(nowTime);

        SysConfig.updateOpenDays();
    }

    public void start() {
        handleInit();
        int second = Calendar.getInstance().get(Calendar.SECOND);
        scheduExec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Calendar now = Calendar.getInstance();
                int time[] = new int[]{now.get(Calendar.MINUTE),
                        now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.DATE),
                        now.get(Calendar.MONTH) + 1,
                        now.get(Calendar.DAY_OF_WEEK), now.get(Calendar.YEAR)};
                for (TimerObject timer : timers) {
                    try {
                        if (timer.check(time)) {
                            long begin = System.currentTimeMillis();
                            timer.getMethod().invoke(timer.getService());
                            long end = System.currentTimeMillis();
                            if ((end - begin) > 4000) {

                            }
                        }
                    } catch (Exception e) {

                    }
                }
            }
        }, 60 - second + 1, 60, TimeUnit.SECONDS);
    }

    /**
     * 保留系列化数据
     */
    public void saveData() {
        for (ServiceDispose dispose : BeanManager.getApplicationCxt()
                .getBeansOfType(ServiceDispose.class).values()) {
            try {
                dispose.serviceDispse();
            } catch (Exception e) {

            }
        }
    }

}
