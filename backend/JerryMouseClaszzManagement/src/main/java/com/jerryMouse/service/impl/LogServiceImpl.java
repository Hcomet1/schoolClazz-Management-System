package com.jerryMouse.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jerryMouse.mapper.OperateLogMapper;
import com.jerryMouse.pojo.OperateLog;
import com.jerryMouse.pojo.PageResult;
import com.jerryMouse.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Override
    public PageResult<OperateLog> page(Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<OperateLog> list = operateLogMapper.page(page, pageSize);
        Page<OperateLog> p = (Page<OperateLog>) list;
        return new PageResult<>(p.getTotal(), p.getResult());
    }
}
