package cn.chen;

import cn.chen.entity.User;
import cn.chen.util.EsUtil;
import org.elasticsearch.action.search.SearchResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootTest
class BaseOneApplicationTests {

    @MockBean
    private ServerEndpointExporter serverEndpointExporter;

    @Test
    void contextLoads() {
        System.out.println("niuhao ");
    }


}
