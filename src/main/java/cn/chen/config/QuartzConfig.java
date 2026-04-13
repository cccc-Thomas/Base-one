package cn.chen.config;

import cn.chen.quartz.UserSyncJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Quartz 定时任务配置类
 * 作用：定义任务详情、触发器，将任务交给 Spring 管理
 */
@Configuration
public class QuartzConfig {

    /**
     * 1. 定义 JobDetail（任务详情）
     * 作用：绑定我们自定义的 Job 类，指定任务名称、是否持久化
     * @return 任务详情对象
     */
    @Bean
    public JobDetail userSyncJobDetail() {
        return JobBuilder.newJob(UserSyncJob.class)  // 绑定自定义任务类
                .withIdentity("userSyncJob")         // 任务唯一标识（名字）
                .storeDurably()                      // 即使没有触发器绑定，任务也保留
                .build();
    }

    /**
     * 2. 定义 Trigger（触发器）
     * 作用：设置任务什么时候执行、执行频率
     * @return 触发器对象
     */
    @Bean
    public Trigger userSyncTrigger() {
        // 定义 Cron 表达式：每 5 秒执行一次
        CronScheduleBuilder cronSchedule = CronScheduleBuilder.cronSchedule("*/5 * * * * ?");

        return TriggerBuilder.newTrigger()
                .forJob(userSyncJobDetail())         // 关联上面的任务
                .withIdentity("userSyncTrigger")     // 触发器唯一标识
                .withSchedule(cronSchedule)          // 设置执行策略
                .build();
    }
}