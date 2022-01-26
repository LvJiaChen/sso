package com.sso.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sso.common.entity.WmsUser;
import com.sso.common.mapper.WmsUserMapper;
import com.sso.service.IWmsUserService;
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
public class WmsUserServiceImpl extends ServiceImpl<WmsUserMapper, WmsUser> implements IWmsUserService {
    @Autowired
    private WmsUserMapper wmsUserMapper;

    //12小时后失效
    private final static int EXPIRE = 12;

    @Override
    public String createToken(WmsUser user) {
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
        wmsUserMapper.updateById(user);
        return token;
    }

    @Override
    public void logout(String token) {
        QueryWrapper<WmsUser> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("token",token);
        WmsUser userEntity = wmsUserMapper.selectOne(queryWrapper);
        //用UUID生成token
        token = UUID.randomUUID().toString();
        userEntity.setToken(token);
        wmsUserMapper.updateById(userEntity);
    }
}
