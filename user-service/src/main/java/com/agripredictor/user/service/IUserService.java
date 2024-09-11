package com.agripredictor.user.service;

import com.agripredictor.common.po.UserVO;
import com.agripredictor.user.po.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author yuum
 * @since 2024-08-23
 */
public interface IUserService extends IService<User> {

    String saveMe(User user);

    String login(User user);

    List<UserVO> getUsersByIds(List<String> userIds);
}
