package com.jerryMouse.controller;

import com.jerryMouse.pojo.PageResult;
import com.jerryMouse.pojo.Result;
import com.jerryMouse.pojo.Student;
import com.jerryMouse.pojo.StudentQueryParm;
import com.jerryMouse.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public Result page(StudentQueryParm studentQueryParm) {
        log.info("分页查询学生: {}", studentQueryParm);
        PageResult<Student> pageResult = studentService.page(studentQueryParm);
        return Result.success(pageResult);
    }

    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable List<Integer> ids) {
        log.info("删除学生: {}", ids);
        studentService.deleteByIds(ids);
        return Result.success();
    }

    @PostMapping
    public Result save(@RequestBody Student student) {
        log.info("新增学生: {}", student);
        studentService.save(student);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        log.info("根据 id 查询学生: {}", id);
        Student student = studentService.getInfo(id);
        return Result.success(student);
    }

    @PutMapping
    public Result update(@RequestBody Student student) {
        log.info("更新学生信息: {}", student);
        studentService.update(student);
        return Result.success();
    }

    @PutMapping("/violation/{id}/{score}")
    public Result dealViolation(@PathVariable Integer id, @PathVariable Integer score) {
        log.info("处理学生违纪: id={}, score={}", id, score);
        studentService.updateViolation(id, score);
        return Result.success();
    }
}
