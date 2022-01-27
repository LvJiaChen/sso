package com.sso.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sso.common.entity.SsoUser;
import com.sso.common.mapper.SsoUserMapper;
import com.sso.service.ISsoUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lvxiaozuo
 * @since 2022-01-06
 */
@Service
public class SsoUserServiceImpl extends ServiceImpl<SsoUserMapper, SsoUser> implements ISsoUserService {
    @Autowired
    private SsoUserMapper ssoUserMapper;

    //12小时后失效
    private final static int EXPIRE = 12;

    @Override
    public String createToken(SsoUser user) {
        //用UUID生成token
        String token = UUID.randomUUID().toString();
        //当前时间
        LocalDateTime now = LocalDateTime.now();
        //过期时间
        LocalDateTime expireTime = now.plusHours(EXPIRE);
        //保存到数据库
        user.setLoginTime(now);
        user.setExpireTime(expireTime);
        user.setToken(token);
        ssoUserMapper.updateById(user);
        return token;
    }

    @Override
    public void logout(String token) {
        QueryWrapper<SsoUser> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("token",token);
        SsoUser userEntity = ssoUserMapper.selectOne(queryWrapper);
        //用UUID生成token
        token = UUID.randomUUID().toString();
        userEntity.setToken(token);
        ssoUserMapper.updateById(userEntity);
    }
}
