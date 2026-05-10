package com.jerryMouse.controller;

import com.jerryMouse.anno.Log;
import com.jerryMouse.pojo.Emp;
import com.jerryMouse.pojo.EmpQueryParm;
import com.jerryMouse.pojo.PageResult;
import com.jerryMouse.pojo.Result;
import com.jerryMouse.service.EmpService;
import com.jerryMouse.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {

    @Autowired
    private EmpService empService;

    @Autowired
    private UserService userService;

    @GetMapping
    public Result list(EmpQueryParm empQueryParm) {
        log.info("查询员工列表，参数: {}", empQueryParm);
        PageResult<Emp> pageResult = empService.page(empQueryParm);
        return Result.success(pageResult);
    }

    @Log
    @PostMapping
    public Result save(@RequestBody Emp emp) {
        log.info("新增员工: {}", emp);
        empService.save(emp);
        return Result.success();
    }

    @Log
    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids) {
        log.info("按 id 批量删除员工: {}", ids);
        empService.deleteByIds(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        log.info("根据 id 查询员工信息: {}", id);
        Emp emp = empService.getInfo(id);
        return Result.success(emp);
    }

    @Log
    @PutMapping
    public Result update(@RequestBody Emp emp) {
        log.info("修改员工数据: {}", emp);
        empService.update(emp);
        return Result.success();
    }

    @GetMapping("/list")
    public Result listAll() {
        log.info("查询所有员工");
        List<Emp> list = empService.list();
        return Result.success(list);
    }

    @PostMapping("/password")
    public Result updatePassword(@RequestBody Map<String, String> params) {
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        log.info("修改密码请求");
        userService.updatePassword(oldPassword, newPassword);
        return Result.success();
    }
}
