package com.jerryMouse.controller;

import com.jerryMouse.pojo.OperateLog;
import com.jerryMouse.pojo.PageResult;
import com.jerryMouse.pojo.Result;
import com.jerryMouse.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping("/page")
    public Result pageLog(Integer page, Integer pageSize) {
        log.info("分页查询日志信息, page={}, pageSize={}", page, pageSize);
        PageResult<OperateLog> pageResult = logService.page(page, pageSize);
        return Result.success(pageResult);
    }
}
