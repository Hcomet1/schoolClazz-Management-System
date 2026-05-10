package com.jerryMouse.controller;

import com.jerryMouse.anno.Log;
import com.jerryMouse.pojo.ClassQueryParm;
import com.jerryMouse.pojo.Clazz;
import com.jerryMouse.pojo.PageResult;
import com.jerryMouse.pojo.Result;
import com.jerryMouse.service.ClazzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/clazzs")
public class ClassController {

    @Autowired
    private ClazzService clazzService;

    @GetMapping
    public Result page(ClassQueryParm classQueryParm) {
        log.info("查询班级列表，参数: {}", classQueryParm);
        PageResult<Clazz> pageResult = clazzService.page(classQueryParm);
        return Result.success(pageResult);
    }

    @Log
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        log.info("删除 id 为 {} 的班级", id);
        clazzService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/list")
    public Result list() {
        log.info("查询所有班级");
        List<Clazz> list = clazzService.list();
        return Result.success(list);
    }

    @Log
    @PostMapping
    public Result save(@RequestBody Clazz clazz) {
        log.info("新增班级: {}", clazz);
        clazzService.save(clazz);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        log.info("查询 id 为 {} 的班级", id);
        Clazz clazz = clazzService.getInfo(id);
        return Result.success(clazz);
    }

    @Log
    @PutMapping
    public Result update(@RequestBody Clazz clazz) {
        log.info("更新班级数据: {}", clazz);
        clazzService.update(clazz);
        return Result.success();
    }
}
