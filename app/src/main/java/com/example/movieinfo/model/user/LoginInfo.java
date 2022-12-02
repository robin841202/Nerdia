package com.example.movieinfo.model.user;

public class LoginInfo {

    /**
     * User account Id
     */
    private final long userId;

    /**
     * Valid session
     */
    private final String session;

    /**
     * Login status
     */
    private final boolean isLogin;

    public LoginInfo() {
        this.userId = 0;
        this.session = "";
        this.isLogin = false;
    }

    public LoginInfo(long userId, String session, boolean isLogin) {
        this.userId = userId;
        this.session = session;
        this.isLogin = isLogin;
    }

    public long getUserId() {
        return userId;
    }

    public String getSession() {
        return session;
    }

    public boolean isLogin() {
        return isLogin;
    }
}
