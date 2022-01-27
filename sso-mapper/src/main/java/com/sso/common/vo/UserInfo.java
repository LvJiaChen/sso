package com.sso.common.vo;

import com.sso.common.entity.SsoUser;

/**
 * @author lvxiaozuo
 * @date 2022/1/22 14:39
 */
public class UserInfo {
    private static ThreadLocal<SsoUser> userInfo = new ThreadLocal();
    public UserInfo() {
    }
    public static SsoUser getUser() {
        return (SsoUser)userInfo.get();
    }

    public static void setUser(SsoUser user) {
        userInfo.set(user);
    }

    public static void removeUser() {
        userInfo.remove();
    }
}
