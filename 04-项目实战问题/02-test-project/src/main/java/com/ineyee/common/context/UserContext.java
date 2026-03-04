package com.ineyee.common.context;

// 这个类用来全局记录从 token 里解析出来的业务信息
// 以便后续各个业务模块 Controller 或 Service 里的方法读取使用，比如大家都要获取 token 里的 userId 来请求该用户对应的数据
public class UserContext {
    private static final ThreadLocal<TokenInfo> USER_HOLDER = new ThreadLocal<>();

    public static void setTokenInfo(TokenInfo tokenInfo) {
        USER_HOLDER.set(tokenInfo);
    }

    public static TokenInfo getTokenInfo() {
        return USER_HOLDER.get();
    }

    public static void clearTokenInfo() {
        USER_HOLDER.remove();
    }
}
