package cn.chen.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 自定义定时任务
 * 作用：需要定时执行的业务逻辑都写在这里
 * 必须实现 Job 接口，重写 execute 方法
 */
public class UserSyncJob implements Job {

    /**
     * 定时任务被触发时，会自动执行该方法
     * @param context 任务执行上下文（可获取任务名称、参数等）
     * @throws JobExecutionException 任务执行异常
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 格式化时间，方便日志查看
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(formatter);

        // ==================== 定时业务逻辑 ====================
        System.out.println("【Quartz定时任务执行】当前时间：" + now);
        System.out.println("任务内容：同步 MySQL 用户数据到 Elasticsearch......");

        // 后续你可以在这里调用 service 方法
        // userService.syncUserToEs();
        // ======================================================
    }
}