package com.sso.web.application.Controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sso.common.entity.WmsUser;
import com.sso.common.vo.TokenVO;
import com.sso.common.vo.UserInfo;
import com.sso.service.ISsoUserService;
import com.sso.web.application.utils.Result;
import com.sso.web.application.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lvxiaozuo
 * @since 2022-01-06
 */
@Controller
@RequestMapping("/wms-user")
@ResponseBody
public class SsoUserController {
    @Autowired
    private ISsoUserService userService;

    /**
     * 登录
     *
     * @param loginMap
     * @return token登录凭证
     */
    @PostMapping("/login")
    public Result login(@RequestBody Map loginMap) {
        String userNo = (String) loginMap.get("username");
        String password = (String) loginMap.get("password");
        //用户信息
        QueryWrapper<WmsUser> wrapper=new QueryWrapper<>();
        wrapper.eq("user_no",userNo);
        WmsUser user=userService.getOne(wrapper);
        //账号不存在、密码错误
        if (user == null || !user.getPassword().equals(password)) {
            return Result.build(400, "用户名或密码错误");
        } else {
            //登录不走 UserInfoInterceptor ，所以这里设置UserInfo
            UserInfo.setUser(user);
            //生成token，并保存到数据库
            String token = userService.createToken(user);
            TokenVO tokenVO = new TokenVO();
            tokenVO.setToken(token);
            return Result.ok(tokenVO);
        }
    }

    /**
     * 登出
     *
     * @param
     * @return
     */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        //从request中取出token
        String token = TokenUtil.getRequestToken(request);
        userService.logout(token);
        return Result.ok();
    }
}
