package com.sso.web.application.filter;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sso.common.entity.SsoUser;
import com.sso.common.mapper.SsoUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lvxiaozuo
 * @date 2022/2/28 20:25
 */
@Service("userDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private SsoUserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<SsoUser> ssoUserQueryWrapper=new QueryWrapper<>();
        ssoUserQueryWrapper.eq("user_no",username);
        SsoUser user=userMapper.selectOne(ssoUserQueryWrapper);
        if (ObjectUtil.isNull(user)){
            throw new UsernameNotFoundException("该用户不存在");
        }
        List<GrantedAuthority> authorityList= AuthorityUtils.commaSeparatedStringToAuthorityList("admin");

        return new User(username,passwordEncoder.encode(user.getPassword()),authorityList);
    }
}
