package com.jerryMouse.aop;

import com.jerryMouse.mapper.OperateLogMapper;
import com.jerryMouse.pojo.OperateLog;
import com.jerryMouse.utils.CurrentHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LogOperationAspect {

    @Autowired
    private OperateLogMapper operateLogMapper;

    /**
     * 切入点：拦截所有带 @Log 注解的方法。
     */
    @Pointcut("@annotation(com.jerryMouse.anno.Log)")
    public void logPointcut() {
    }

    /**
     * 环绕通知：记录操作日志。
     */
    @Around("logPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String methodParams = Arrays.toString(args);

        Object result = null;
        String returnValue = "";
        try {
            result = joinPoint.proceed();
            returnValue = String.valueOf(result);
        } catch (Throwable throwable) {
            returnValue = "异常: " + throwable.getMessage();
            throw throwable;
        } finally {
            long costTime = System.currentTimeMillis() - startTime;

            OperateLog operateLog = new OperateLog();
            operateLog.setOperateEmpId(getCurrentEmpId());
            operateLog.setOperateTime(LocalDateTime.now());
            operateLog.setClassName(className);
            operateLog.setMethodName(methodName);
            operateLog.setMethodParams(methodParams);
            operateLog.setReturnValue(returnValue);
            operateLog.setCostTime((int) costTime);

            operateLogMapper.insert(operateLog);
            log.info("操作日志记录成功: {}.{}, 耗时: {}ms", className, methodName, costTime);
        }
        return result;
    }

    private Integer getCurrentEmpId() {
        return CurrentHolder.getCurrentId();
    }
}
