package cn.chen.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.websocket.Session;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket 服务端核心类
 * 负责处理客户端连接、收发消息、断开连接等逻辑
 */
@Slf4j  // 日志注解，用于打印日志
@Component  // 交给 Spring 容器管理
@ServerEndpoint("/ws/{userId}") // 客户端连接地址：ws://ip:端口/ws/用户ID
public class WebSocketServer {

    // 线程安全的Set，用于存储当前所有在线的WebSocket连接
    private static final CopyOnWriteArraySet<WebSocketServer> WEB_SOCKET_SET = new CopyOnWriteArraySet<>();

    // 与客户端的会话对象，用于发送/接收数据
    private Session session;

    // 当前连接的用户ID（从路径中获取）
    private String userId;

    /**
     * 客户端与服务端**建立连接成功**时调用
     * @param session 会话对象
     * @param userId 路径上的用户ID
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        // 赋值给当前对象的session和userId
        this.session = session;
        this.userId = userId;
        // 将当前连接加入在线集合
        WEB_SOCKET_SET.add(this);
        // 打印日志
        log.info("用户【{}】连接成功，当前在线人数：{}", userId, WEB_SOCKET_SET.size());
    }

    /**
     * 客户端**发送消息**到服务端时调用
     * @param message 客户端发送的内容
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("收到用户【{}】的消息：{}", userId, message);

        // 演示：收到消息后，给当前用户回复一条消息
        sendMessage("服务端已收到：" + message);

        // 如果需要广播给所有人，用下面这行
        // WebSocketServer.broadcast("用户【"+userId+"】说：" + message);
    }

    /**
     * 客户端与服务端**断开连接**时调用
     */
    @OnClose
    public void onClose() {
        // 从在线集合中移除当前连接
        WEB_SOCKET_SET.remove(this);
        log.info("用户【{}】断开连接，当前在线人数：{}", userId, WEB_SOCKET_SET.size());
    }

    /**
     * 连接发生**错误异常**时调用
     * @param error 异常对象
     */
    @OnError
    public void onError(Throwable error) {
        log.error("用户【{}】连接发生异常", userId);
        // 打印异常堆栈信息，方便排查问题
        error.printStackTrace();
    }

    /**
     * 服务端**主动发送消息**给当前客户端
     * @param message 要发送的内容
     */
    public void sendMessage(String message) {
        try {
            // 通过会话对象发送文本消息
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("发送消息给用户【{}】失败", userId);
            e.printStackTrace();
        }
    }

    /**
     * 服务端**广播消息**给【所有在线用户】
     * @param message 要发送的内容
     */
    public static void broadcast(String message) {
        // 遍历所有在线连接
        for (WebSocketServer ws : WEB_SOCKET_SET) {
            // 逐个发送消息
            ws.sendMessage(message);
        }
    }
}