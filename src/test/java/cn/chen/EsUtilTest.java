package cn.chen;

import cn.chen.entity.User;
import cn.chen.util.EsUtil;
import com.alibaba.fastjson.JSON;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootTest
public class EsUtilTest {

    @MockBean
    private ServerEndpointExporter serverEndpointExporter;

    @Resource
    private EsUtil esUtil;

    // 测试1：添加 User 数据到 ES
    @Test
    void testAddUserToEs() throws IOException {
        // 1. 构造 User 对象（对应你的实体）
        User user = new User();
        user.setId(1L);
        user.setUserName("zhangsan");
        user.setPassword("123456");
        user.setRealName("张三");
        user.setRole("admin");

        // 2. 调用 EsUtil 存入 ES，索引名：user_index
        String docId = esUtil.add("user_index", user.getId().toString(), user);

        // 3. 打印结果，验证成功
        System.out.println("数据存入ES成功，文档ID：" + docId);
        System.out.println("存入的用户信息：" + JSON.toJSONString(user));
    }

    // 测试2：根据用户名搜索 ES 中的 User 数据
    @Test
    void testSearchUserByUserName() throws IOException {
        // 1. 调用 EsUtil 搜索，索引：user_index，字段：userName，关键词：zhangsan
        SearchResponse response = esUtil.search("user_index", "userName", "zhangsan");

        // 2. 解析搜索结果
        System.out.println("=== 搜索结果 ===");
        for (SearchHit hit : response.getHits().getHits()) {
            // 打印源文档（完整JSON）
            System.out.println("文档ID：" + hit.getId());
            System.out.println("用户信息：" + hit.getSourceAsString());

            // 也可以直接转成 User 对象
            User user = JSON.parseObject(hit.getSourceAsString(), User.class);
            System.out.println("反序列化后的User对象：" + user);
        }

        // 3. 打印命中总数
        System.out.println("总命中数：" + response.getHits().getTotalHits().value);
    }

    // 测试3：删除 ES 中的 User 数据
    @Test
    void testDeleteUserFromEs() throws IOException {
        // 删除ID为1的用户文档
        String deletedId = esUtil.delete("user_index", "1");
        System.out.println("删除成功，被删除的文档ID：" + deletedId);
    }

    // 测试4：完整流程（增-查-删）
    @Test
    void testFullFlow() throws IOException {
        // 1. 新增
        User user = new User(2L, "lisi", "123456", "李四", "user");
        String addId = esUtil.add("user_index", "2", user);
        System.out.println("新增成功，ID：" + addId);

        // 2. 查询
        SearchResponse searchResponse = esUtil.search("user_index", "userName", "lisi");
        System.out.println("查询到" + searchResponse.getHits().getTotalHits().value + "条数据");

        // 3. 删除
        String deleteId = esUtil.delete("user_index", "2");
        System.out.println("删除成功，ID：" + deleteId);
    }
}