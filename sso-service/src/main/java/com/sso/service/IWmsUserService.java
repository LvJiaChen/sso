package com.sso.service;

import com.sso.common.entity.WmsUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lvxiaozuo
 * @since 2022-01-06
 */
public interface IWmsUserService extends IService<WmsUser> {

    String createToken(WmsUser user);

    void logout(String token);
}
