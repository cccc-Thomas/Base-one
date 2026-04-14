package cn.chen.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.servlet.ServletContext;

@Configuration
public class WebSocketConfig {

    @Bean
    @ConditionalOnWebApplication
    public ServerEndpointExporter serverEndpointExporter() {
        // 检查是否在测试环境
        try {
            // 尝试获取 ServletContext，如果失败说明不在真正的 Web 环境
            ServletContext servletContext = org.springframework.web.context.ContextLoader.getCurrentWebApplicationContext().getServletContext();
            if (servletContext == null) {
                return null;
            }
        } catch (Exception e) {
            // 测试环境下会抛异常，返回 null 表示不创建这个 Bean
            return null;
        }
        return new ServerEndpointExporter();
    }
}