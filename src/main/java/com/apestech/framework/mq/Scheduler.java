package com.apestech.framework.mq;

import com.apestech.framework.cache.HazelcastCache;
import com.apestech.framework.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 功能：定时器
 *
 * @author xul
 * @create 2017-12-08 11:58
 */
@Component("scheduler")
public class Scheduler {

    private Timer timer;

    @Autowired
    private Environment env;

    @Autowired
    private HazelcastCache cache;

    // 设定指定任务task在指定延迟delay后进行固定延迟peroid的执行
    public void run(ApplicationContext context) {
        Object master = cache.get("mq.master");
        if (Tools.isNull(master)) {
            cache.put("mq.master", "1");
            master = cache.get("mq.master");
        } else{
            return;
        }
        boolean isMaster = master.equals("1") ? true : false;
        if (!isMaster) {
            return;
        }
        String period = env.getProperty("mq.period");
        if (Tools.isNull(period)) {
            period = "10";
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                try {
                    Channel channel = (Channel) context.getBean("messageChannel");
                    channel.invoke();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1000, Integer.valueOf(period) * 60 * 1000);
    }

    public void cancel() {
        if (timer != null) {
            timer.cancel();
        }
    }

}
