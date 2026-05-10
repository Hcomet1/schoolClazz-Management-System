package com.jerryMouse.service;

import com.jerryMouse.pojo.OperateLog;
import com.jerryMouse.pojo.PageResult;

public interface LogService {
    PageResult<OperateLog> page(Integer page, Integer pageSize);
}
