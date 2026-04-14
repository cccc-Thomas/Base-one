package cn.chen.service.impl;

import cn.chen.entity.User;
import cn.chen.mapper.UserMapper;
import cn.chen.service.UserService;
import cn.chen.util.RedisCacheUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

    private final UserMapper userMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisCacheUtils redisCacheUtils;

    @Override
    public String getNameById(Long id) {
        //查指定id的用户名
        String name=userMapper.getNameById(id);
        //从redis获取feel的值
        String feel=stringRedisTemplate.opsForValue().get("feel");
        //写对象到redis
        User user=new User(100L,"chen","123456","陈","admin");
        redisCacheUtils.setObject("info",user,60, TimeUnit.SECONDS);
        //从redis获取对象
        User u = redisCacheUtils.getObject("info", User.class);

        return "id为"+id+"的name:"+name+",%nredis存储的对象:"+u.toString();
    }

    @Override
    public List<User> getEvenUser() {
        LambdaQueryWrapper<User> lambdaQuery = new LambdaQueryWrapper<>();
        // 正确：用 func() 自定义 SQL 条件，筛选 id 为偶数的用户
        lambdaQuery.func(wrapper -> wrapper.apply("id % 2 = 0"));

        List<User> list = userMapper.selectList(lambdaQuery);
        return list;
    }

    @Override
    public IPage<User> getTop10Users(Integer current) {
        // current：当前页
        // 10：每页10条
        Page<User> page = new Page<>(current, 10);

        // 执行分页查询
        Page<User> userPage = userMapper.selectPage(page, null);

        // 返回分页结果（包含 total、pages、records、current 等）
        return userPage;
    }

    // 添加这个简单的查询方法
    @Cacheable(value = "userCache", key = "#id")
    public User getByIdWithCache(Long id) {
        System.out.println(">>>>>> 正在查询数据库，用户ID: " + id);
        return this.getById(id);
    }
}
