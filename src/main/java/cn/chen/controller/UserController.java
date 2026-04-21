package cn.chen.controller;

import cn.chen.entity.User;
import cn.chen.service.UserService;
import cn.chen.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 根据 id 查询用户名，并演示 Redis 的使用
     *
     * @param id 用户 ID
     * @return Result 包装的字符串，包含从数据库读取的用户名和 Redis 示例数据
     */
    @GetMapping("/{id}")
    public Result<String> test(@PathVariable Long id) {
        return Result.success(userService.getNameById(id));
    }

    /**
     * 获取 id 为偶数的用户信息
     *
     * @return Result 包装的用户列表（id为偶数）
     */
    @GetMapping("/even")
    public Result<List<User>> getEvenUser(){
        return Result.success(userService.getEvenUser());
    }

    /**
     * 获取第几页的用户信息，一页 10 条记录
     *
     * @param id 页码（从 1 开始）
     * @return Result 包装的分页结果（IPage<User>）
     */
    @GetMapping("/top10/{id}")
    public Result<IPage<User>> getTop10Users(@PathVariable Integer id){
        return Result.success(userService.getTop10Users(id));
    }

    /**
     * 查询数据库中所有用户信息
     *
     * @return Result 包装的用户列表
     */
    @GetMapping("/all")
    public Result<List<User>> getAllUsers() {
        return Result.success(userService.getAllUsers());
    }

    /**
     * 查询角色为 doctor 的用户列表
     *
     * @return Result 包装的用户列表（role = "doctor"）
     */
    @GetMapping("/doctor")
    public Result<List<User>> getDoctors() {
        return Result.success(userService.getUsersByRole("doctor"));
    }

}
