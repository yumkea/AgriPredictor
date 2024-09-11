package com.agripredictor.user.controller;


import com.agripredictor.common.annotation.AutoFill;
import com.agripredictor.common.po.UserVO;
import com.agripredictor.user.constant.ErrorConstant;
import com.agripredictor.common.constant.UserConstant;
import com.agripredictor.common.enumeration.AutoFillType;
import com.agripredictor.common.po.Result;
import com.agripredictor.user.po.User;
import com.agripredictor.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户类
 * @author yuum
 * @since 2024-08-23
 */
@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    /**
     * 用户注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        log.info("注册 user {}",user);
        String res = userService.saveMe(user);
        if(res != null){
            return Result.error(res);
        } else {
            return Result.success();
        }
    }

    /**
     * 登录
     * @param user
     * @return
     */

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        log.info("登录 user {}",user);
        String jwt = userService.login(user);
        if(jwt != null){
            return Result.success(jwt);
        } else {
            return Result.error(ErrorConstant.LOGIN_FAIL);
        }
    }

    /**
     * 用户修改
     * @param user
     * @return
     */
    @PutMapping("/update")
    @AutoFill(value = AutoFillType.UPDATE)
    public Result update(@RequestBody User user, @RequestHeader(UserConstant.USER_ID) String userId) {
        user.setId(userId);
        log.info("用户修改 user {}",user);
        return userService.updateById(user) ? Result.success() : Result.error(ErrorConstant.UPDATE_FAIL);
    }

    /**
     * 用户注销
     * @param user
     * @return
     */
    @DeleteMapping("/delete")
    public Result delete(@RequestBody User user, @RequestHeader(UserConstant.USER_ID) String userId) {
        user.setId(userId);
        log.info("用户注销 user {}",user);
        return userService.removeById(user) ? Result.success() : Result.error(ErrorConstant.DELETE_FAIL);
    }


    /**
     * 获取用户信息ByIds
     * @param userIds
     * @return
     */
    @GetMapping("/getUserVOByUserIds")
    public Result<List<UserVO>> getUserVOByUserIds(@RequestParam("ids") List<String> userIds) {
        log.info("获取用户信息ByIds {}",userIds);
        return Result.success(userService.getUsersByIds(userIds));
    }
}
