package com.jerryMouse.service;

import com.jerryMouse.pojo.*;

import java.util.List;

public interface StudentService {
    PageResult<Student> page(StudentQueryParm studentQueryParm);

    void deleteByIds(List<Integer> ids);

    void save(Student student);

    Student getInfo(Integer id);

    void update(Student student);

    void updateViolation(Integer id, Integer score);
}
