package com.sso.common.vo;

import com.sso.common.entity.WmsUser;

/**
 * @author lvxiaozuo
 * @date 2022/1/22 14:39
 */
public class UserInfo {
    private static ThreadLocal<WmsUser> userInfo = new ThreadLocal();
    public UserInfo() {
    }
    public static WmsUser getUser() {
        return (WmsUser)userInfo.get();
    }

    public static void setUser(WmsUser user) {
        userInfo.set(user);
    }

    public static void removeUser() {
        userInfo.remove();
    }
}
