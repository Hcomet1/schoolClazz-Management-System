package com.jerryMouse.exception;

import com.jerryMouse.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result handleException(Exception e) {
        log.error("程序异常: {}", e.getMessage());
        return Result.error("服务端程序出错啦，请联系管理员");
    }

    @ExceptionHandler(value = DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException e) {
        log.error("唯一键重复异常: {}", e.getMessage());
        String message = e.getMessage();
        if (message.contains("Duplicate entry")) {
            int i = message.indexOf("Duplicate entry");
            String errMsg = message.substring(i);
            String[] arr = errMsg.split(" ");
            return Result.error(arr[2] + "已经存在");
        }
        return Result.error("数据重复，操作失败");
    }

    @ExceptionHandler(value = RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e) {
        log.error("运行时异常: {}", e.getMessage());
        return Result.error(e.getMessage());
    }
}
