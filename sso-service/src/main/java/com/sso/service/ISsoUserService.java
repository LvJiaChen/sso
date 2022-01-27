package com.sso.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sso.common.entity.SsoUser;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lvxiaozuo
 * @since 2022-01-06
 */
public interface ISsoUserService extends IService<SsoUser> {

    String createToken(SsoUser user);

    void logout(String token);
}
