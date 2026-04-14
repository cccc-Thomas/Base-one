package cn.chen;

import cn.chen.entity.User;
import cn.chen.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@SpringBootTest
public class CacheTest {

    @Autowired
    private UserService userService;

    // 测试 @Cacheable - 第一次查询会查数据库，后续从缓存获取
    @Test
    public void testQueryUser() {
        Long userId = 1L;  // 确保数据库里有这个id的用户

        // 第一次查询 - 会查数据库
        System.out.println("========== 第1次查询 ==========");
        User user1 = userService.getByIdWithCache(userId);
        System.out.println("结果: " + user1);

        // 第二次查询 - 应该从缓存获取
        System.out.println("\n========== 第2次查询 ==========");
        User user2 = userService.getByIdWithCache(userId);
        System.out.println("结果: " + user2);

        // 第三次查询 - 还是从缓存获取
        System.out.println("\n========== 第3次查询 ==========");
        User user3 = userService.getByIdWithCache(userId);
        System.out.println("结果: " + user3);
    }
}
