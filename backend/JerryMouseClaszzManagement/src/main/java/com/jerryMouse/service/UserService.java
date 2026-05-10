package com.jerryMouse.service;

public interface UserService {
    /**
     * 修改密码。
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void updatePassword(String oldPassword, String newPassword);
}
