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

    @GetMapping("/{id}")
    public Result<String> test(@PathVariable Long id) {
        return Result.success(userService.getNameById(id));
    }

    /*
     *获取id为偶数的用户信息
     */
    @GetMapping("/even")
    public Result<List<User>> getEvenUser(){
        return Result.success(userService.getEvenUser());
    }

    /**
     * 获取第几页的用户信息,一页10条
     */
    @GetMapping("/top10/{id}")
    public Result<IPage<User>> getTop10Users(@PathVariable Integer id){
        return Result.success(userService.getTop10Users(id));
    }

}
