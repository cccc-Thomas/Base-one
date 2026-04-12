package cn.chen.mapper;

import cn.chen.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    //@Select("select username from user where id=#{id}")
    String getNameById(Long id);

}
