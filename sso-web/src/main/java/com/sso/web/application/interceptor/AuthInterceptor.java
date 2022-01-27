package com.sso.web.application.interceptor;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.sso.common.entity.SsoUser;
import com.sso.service.ISsoUserService;
import com.sso.web.application.utils.Result;
import com.sso.web.application.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author lvxiaozuo
 * @date 2022/1/6 14:47
 */
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    private ISsoUserService iSsoUserService;

    // 目标方法执行之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url=request.getRequestURI();
        log.info("拦截的路径是{}",url);

        String token = TokenUtil.getRequestToken(request);
        //如果token为空
        if (StringUtils.isBlank(token)) {
            setReturn(response,401,"用户未登录，请先登录");
            return false;
        }
        //1. 根据token，查询用户信息
        QueryWrapper<SsoUser> userQueryWrapper=new QueryWrapper<>();
        userQueryWrapper.eq("token",token);
        SsoUser user = iSsoUserService.getOne(userQueryWrapper);
        //2. 若用户不存在,
        if (user == null) {
            setReturn(response,401,"用户不存在");
            return false;
        }
        //3. token失效
        if (user.getExpireTime().isBefore(LocalDateTime.now())) {
            setReturn(response,401,"用户登录凭证已失效，请重新登录");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }


    //返回错误信息
    private static void setReturn(HttpServletResponse response, int status, String msg) throws IOException {
        //UTF-8编码
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        Result build = Result.build(status, msg);
        String json = JSON.toJSONString(build);
        response.getWriter().print(json);
    }
}
