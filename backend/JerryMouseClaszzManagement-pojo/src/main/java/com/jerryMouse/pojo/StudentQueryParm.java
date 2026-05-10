package com.jerryMouse.pojo;

import lombok.Data;

@Data
public class StudentQueryParm {
    private String name;
    private Integer gradeLevel;
    private Integer clazzId;
    private Integer page;
    private Integer pageSize;
}
