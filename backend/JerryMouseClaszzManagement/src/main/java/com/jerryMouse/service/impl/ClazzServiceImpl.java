package com.jerryMouse.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jerryMouse.mapper.ClazzMapper;
import com.jerryMouse.pojo.ClassQueryParm;
import com.jerryMouse.pojo.Clazz;
import com.jerryMouse.pojo.PageResult;
import com.jerryMouse.pojo.Student;
import com.jerryMouse.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClazzServiceImpl implements ClazzService {

    @Autowired
    private ClazzMapper clazzMapper;

    @Override
    public PageResult page(ClassQueryParm classQueryParm) {
        PageHelper.startPage(classQueryParm.getPage(), classQueryParm.getPageSize());
        List<Clazz> list = clazzMapper.page(classQueryParm);
        fillStatus(list);
        Page<Clazz> page = (Page<Clazz>) list;
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Integer id) {
        List<Student> list = clazzMapper.selectClazzStudents(id);
        if (!CollectionUtils.isEmpty(list)) {
            throw new RuntimeException("该班级下仍有学生，不能直接删除");
        }
        clazzMapper.deleteById(id);
    }

    @Override
    public List<Clazz> list() {
        List<Clazz> list = clazzMapper.list();
        fillStatus(list);
        return list;
    }

    @Override
    public void save(Clazz clazz) {
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.insert(clazz);
    }

    @Override
    public Clazz getInfo(Integer id) {
        return clazzMapper.selectById(id);
    }

    @Override
    public void update(Clazz clazz) {
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.updateById(clazz);
    }

    private void fillStatus(List<Clazz> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        LocalDate now = LocalDate.now();
        list.forEach(clazz -> {
            if (now.isBefore(clazz.getBeginDate())) {
                clazz.setStatus("未开学");
            } else if (now.isAfter(clazz.getEndDate())) {
                clazz.setStatus("已毕业");
            } else {
                clazz.setStatus("在读中");
            }
        });
    }
}
