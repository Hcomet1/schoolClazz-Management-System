package com.jerryMouse.service;

import com.jerryMouse.pojo.ClassQueryParm;
import com.jerryMouse.pojo.Clazz;
import com.jerryMouse.pojo.PageResult;

import java.util.List;

public interface ClazzService {
    PageResult<Clazz> page(ClassQueryParm classQueryParm);

    void deleteById(Integer id);

    List<Clazz> list();

    void save(Clazz clazz);

    Clazz getInfo(Integer id);

    void update(Clazz clazz);
}
