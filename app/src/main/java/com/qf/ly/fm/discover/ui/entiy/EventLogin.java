package com.qf.ly.fm.discover.ui.entiy;

/**
 * Created by Administrator on 2016/11/2 0002.12:11
 * 版权所有 盗版必究
 */

public class EventLogin {
    private String userName;
    private String userIcon;

    public EventLogin(String userName, String userIcon) {
        this.userName = userName;
        this.userIcon = userIcon;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
