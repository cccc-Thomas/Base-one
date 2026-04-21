package cn.chen.service;

import cn.chen.entity.User;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserService extends IService<User> {
    String getNameById(Long id);

    List<User> getEvenUser();

    IPage<User> getTop10Users(Integer id);

    User getByIdWithCache(Long id);

    List<User> getAllUsers();

    List<User> getUsersByRole(String role);
}
