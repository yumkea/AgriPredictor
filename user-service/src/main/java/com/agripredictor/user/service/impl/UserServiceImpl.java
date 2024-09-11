package com.agripredictor.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.agripredictor.common.annotation.AutoFill;
import com.agripredictor.common.po.UserVO;
import com.agripredictor.user.constant.ErrorConstant;
import com.agripredictor.common.enumeration.AutoFillType;
import com.agripredictor.user.mapper.UserMapper;
import com.agripredictor.user.po.User;
import com.agripredictor.user.service.IUserService;
import com.agripredictor.user.utlis.JWTUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户表 服务实现类
 *
 * @author yuum
 * @since 2024-08-23
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final UserMapper userMapper;

    @Override
    @AutoFill(value = AutoFillType.CREATE)
    public String saveMe(User user) {
        try {
            userMapper.insert(user);
            return null;
        } catch (DuplicateKeyException e) {
            return ErrorConstant.USER_EXISTS;
        }
    }

    @Override
    public String login(User user) {
        User one = lambdaQuery()
                .eq(User::getUsername, user.getUsername())
                .eq(User::getPassword, user.getPassword())
                .one();
        if(one != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", one.getId());
            return JWTUtils.generateJwt(claims);

        }else {
            return null;
        }
    }

    /**
     * 根据ids获取UserVO
     * @param userIds
     * @return
     */
    @Override
    public List<UserVO> getUsersByIds(List<String> userIds) {
        List<User> users = userMapper.selectBatchIds(userIds);
        List<UserVO> userVOs = new ArrayList<>();
        users.forEach(user ->
            userVOs.add(BeanUtil.copyProperties(user, UserVO.class))
        );
        return userVOs;
    }
}
