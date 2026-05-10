package com.jerryMouse.controller;

import com.jerryMouse.pojo.ClazzStudentCount;
import com.jerryMouse.pojo.EmpJob;
import com.jerryMouse.pojo.Result;
import com.jerryMouse.pojo.StudentStageStat;
import com.jerryMouse.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/empJobData")
    public Result getEmpJobData() {
        log.info("统计教职工岗位分布");
        EmpJob empJobData = reportService.getEmpJob();
        return Result.success(empJobData);
    }

    @GetMapping("/empGenderData")
    public Result getEmpGenderData() {
        log.info("统计教职工性别分布");
        List<Map<String, Object>> genderList = reportService.getEmpGender();
        return Result.success(genderList);
    }

    @GetMapping("/studentStageData")
    public Result getStudentStageData() {
        log.info("统计学生年级层次分布");
        List<StudentStageStat> stats = reportService.getStudentStageStat();
        return Result.success(stats);
    }

    @GetMapping("/studentCountData")
    public Result getStudentCountData() {
        log.info("统计班级学生人数");
        ClazzStudentCount clazzStudentCount = reportService.getStudentCount();
        return Result.success(clazzStudentCount);
    }
}
