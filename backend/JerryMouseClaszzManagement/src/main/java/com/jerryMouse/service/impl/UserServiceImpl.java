package com.jerryMouse.service.impl;

import com.jerryMouse.mapper.EmpMapper;
import com.jerryMouse.pojo.Emp;
import com.jerryMouse.service.UserService;
import com.jerryMouse.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private EmpMapper empMapper;

    /**
     * 修改密码。
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    @Override
    public void updatePassword(String oldPassword, String newPassword) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("token");

        Claims claims = JwtUtils.parseJwt(token);
        Integer empId = (Integer) claims.get("id");

        Emp emp = empMapper.findById(empId);
        if (!oldPassword.equals(emp.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }

        empMapper.updatePassword(empId, newPassword);
    }
}
