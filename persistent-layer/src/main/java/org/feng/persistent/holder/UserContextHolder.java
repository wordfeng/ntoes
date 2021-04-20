package org.feng.persistent.holder;

import org.feng.persistent.entity.UserInfo;

public class UserContextHolder {

    private static final ThreadLocal<UserInfo> USER_INFO = new ThreadLocal<>();

    private UserContextHolder() {
    }

    public static void set(UserInfo userInfo) {
        USER_INFO.set(userInfo);
    }

    public static void remove() {
        USER_INFO.remove();
    }

    public static String getTenant() {
        return USER_INFO.get().getTenant();
    }

    public static boolean isLogin() {
        return true;
    }

}
