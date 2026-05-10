package com.jerryMouse.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jerryMouse.mapper.StudentMapper;
import com.jerryMouse.pojo.PageResult;
import com.jerryMouse.pojo.Student;
import com.jerryMouse.pojo.StudentQueryParm;
import com.jerryMouse.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public PageResult<Student> page(StudentQueryParm studentQueryParm) {
        PageHelper.startPage(studentQueryParm.getPage(), studentQueryParm.getPageSize());
        List<Student> list = studentMapper.page(studentQueryParm);
        Page<Student> page = (Page<Student>) list;
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    @Override
    public void deleteByIds(List<Integer> ids) {
        studentMapper.deleteByIds(ids);
    }

    @Override
    public void save(Student student) {
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.insert(student);
    }

    @Override
    public Student getInfo(Integer id) {
        return studentMapper.selectById(id);
    }

    @Override
    public void update(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.updateById(student);
    }

    @Override
    public void updateViolation(Integer id, Integer score) {
        studentMapper.updateViolation(id, score);
    }
}
