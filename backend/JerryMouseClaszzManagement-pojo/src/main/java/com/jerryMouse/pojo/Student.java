package com.jerryMouse.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private Integer id;
    private String name;
    private String no;
    private Integer gender;
    private String phone;
    private String idCard;
    private Integer isBoarding;
    private String address;
    private Integer gradeLevel;
    private LocalDate enrollmentDate;
    private Integer clazzId;
    private Short violationCount;
    private Short violationScore;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String clazzName;
}
