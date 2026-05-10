package com.jerryMouse.service;


import com.jerryMouse.pojo.ClazzStudentCount;
import com.jerryMouse.pojo.EmpJob;
import com.jerryMouse.pojo.StudentStageStat;

import java.util.List;
import java.util.Map;

public interface ReportService {
    EmpJob getEmpJob();

    List<Map<String, Object>> getEmpGender();

    ClazzStudentCount getStudentCount();

    List<StudentStageStat> getStudentStageStat();
}
