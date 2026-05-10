package com.jerryMouse.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jerryMouse.mapper.EmpExprMapper;
import com.jerryMouse.mapper.EmpMapper;
import com.jerryMouse.pojo.*;
import com.jerryMouse.service.EmpService;
import com.jerryMouse.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EmpServiceimpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private EmpExprMapper empExprMapper;

    @Override
    public PageResult<Emp> page(EmpQueryParm empQueryParm) {
        PageHelper.startPage(empQueryParm.getPage(), empQueryParm.getPageSize());
        List<Emp> list = empMapper.list(empQueryParm);
        Page<Emp> page = (Page<Emp>) list;
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Emp emp) {
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        if (emp.getPassword() == null || emp.getPassword().isBlank()) {
            emp.setPassword("123456");
        }
        empMapper.insert(emp);

        List<EmpExpr> exprList = emp.getExprList();
        if (!CollectionUtils.isEmpty(exprList)) {
            exprList.forEach(item -> item.setEmpId(emp.getId()));
            empExprMapper.insertBatch(exprList);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByIds(List<Integer> ids) {
        empMapper.deleteByIds(ids);
        empExprMapper.deleteByEmpIds(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Emp emp) {
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.updateById(emp);

        empExprMapper.deleteByEmpIds(Arrays.asList(emp.getId()));
        List<EmpExpr> exprList = emp.getExprList();
        if (!CollectionUtils.isEmpty(exprList)) {
            exprList.forEach(item -> item.setEmpId(emp.getId()));
            empExprMapper.insertBatch(exprList);
        }
    }

    @Override
    public Emp getInfo(Integer id) {
        return empMapper.selectById(id);
    }

    @Override
    public List<Emp> list() {
        return empMapper.selectAll();
    }

    @Override
    public LoginInfo login(Emp emp) {
        Emp loginEmp = empMapper.selectByUsernamePassword(emp);
        if (loginEmp == null) {
            return null;
        }

        log.info("登录成功: {}", loginEmp.getUsername());
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", loginEmp.getId());
        claims.put("name", loginEmp.getName());
        claims.put("username", loginEmp.getUsername());
        String jwt = JwtUtils.generateJwt(claims);
        return new LoginInfo(loginEmp.getId(), loginEmp.getUsername(), loginEmp.getName(), jwt);
    }
}
