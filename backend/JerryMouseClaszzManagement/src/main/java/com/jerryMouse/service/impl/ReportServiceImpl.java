package com.jerryMouse.service.impl;

import com.jerryMouse.mapper.ClazzMapper;
import com.jerryMouse.mapper.EmpMapper;
import com.jerryMouse.mapper.StudentMapper;
import com.jerryMouse.pojo.ClazzStudentCount;
import com.jerryMouse.pojo.EmpJob;
import com.jerryMouse.pojo.StudentStageStat;
import com.jerryMouse.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ClazzMapper clazzMapper;

    @Override
    public EmpJob getEmpJob() {
        List<Map<String, Object>> empJobData = empMapper.countEmpJob();
        List<Object> jobList = empJobData.stream().map(dataMap -> dataMap.get("pos")).toList();
        List<Object> dataList = empJobData.stream().map(dataMap -> dataMap.get("num")).toList();
        return new EmpJob(jobList, dataList);
    }

    @Override
    public List<Map<String, Object>> getEmpGender() {
        return empMapper.countEmpGender();
    }

    @Override
    public ClazzStudentCount getStudentCount() {
        List<Map<String, Object>> list = clazzMapper.getStudentCount();
        List<Object> clazzList = list.stream().map(dataMap -> dataMap.get("name")).toList();
        List<Object> dataList = list.stream().map(dataMap -> dataMap.get("num")).toList();
        return new ClazzStudentCount(clazzList, dataList);
    }

    @Override
    public List<StudentStageStat> getStudentStageStat() {
        return studentMapper.getStudentStageStat();
    }
}
