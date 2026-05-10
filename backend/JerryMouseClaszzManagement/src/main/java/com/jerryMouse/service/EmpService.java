package com.jerryMouse.service;

import com.jerryMouse.pojo.Emp;
import com.jerryMouse.pojo.EmpQueryParm;
import com.jerryMouse.pojo.LoginInfo;
import com.jerryMouse.pojo.PageResult;

import java.util.List;

public interface EmpService {
    PageResult<Emp> page(EmpQueryParm empQueryParm);

    void save(Emp emp);

    void deleteByIds(List<Integer> ids);

    void update(Emp emp);

    Emp getInfo(Integer id);

    List<Emp> list();

    LoginInfo login(Emp emp);
}
