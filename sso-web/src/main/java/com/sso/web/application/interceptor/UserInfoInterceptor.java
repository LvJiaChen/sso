package com.sso.web.application.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sso.common.entity.SsoUser;
import com.sso.common.mapper.SsoUserMapper;
import com.sso.common.vo.UserInfo;
import com.sso.web.application.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lvxiaozuo
 * @date 2022/1/22 17:08
 */
@Slf4j
public class UserInfoInterceptor implements HandlerInterceptor {

    @Autowired
    private SsoUserMapper ssoUserMapper;
    /**
     * 请求执行前执行的，将用户信息放入ThreadLocal
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = TokenUtil.getRequestToken(request);
        //1. 根据token，查询用户信息
        QueryWrapper<SsoUser> userQueryWrapper=new QueryWrapper<>();
        userQueryWrapper.eq("token",token);
        SsoUser user = ssoUserMapper.selectOne(userQueryWrapper);
        UserInfo.setUser(user);
        return true;
    }

    /**
     * 接口访问结束后，从ThreadLocal中删除用户信息
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserInfo.removeUser();
    }
}
