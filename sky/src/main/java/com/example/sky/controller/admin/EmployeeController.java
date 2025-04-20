package com.example.sky.controller.admin;

import com.example.sky.context.BaseContext;
import com.example.sky.pojo.dto.*;
import com.example.sky.pojo.entity.Employee;
import com.example.sky.pojo.entity.Student;
import com.example.sky.pojo.vo.EmployLoginVO;
import com.example.sky.pojo.vo.PageQueryVO;
import com.example.sky.service.EmployeeService;
import com.example.sky.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);
        EmployLoginVO employLoginVO = employeeService.login(employeeLoginDTO);
        return Result.success(employLoginVO);
    }

    /**
     * 员工分页查询
     *
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageQueryVO> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询：{}", employeePageQueryDTO);
        PageQueryVO pageQueryVO = employeeService.page(employeePageQueryDTO);
        return Result.success(pageQueryVO);
    }

    /**
     * 新增员工
     *
     * @param employeeAddDTO
     * @return
     */
    @PostMapping
    public Result add(@RequestBody EmployeeAddDTO employeeAddDTO) {
        log.info("新增员工：{}", employeeAddDTO);
        employeeService.add(employeeAddDTO);
        return Result.success();
    }

    /**
     * 修改员工信息
     *
     * @param employeeUpdateDTO
     * @return
     */
    @PutMapping
    public Result update(@RequestBody EmployeeUpdateDTO employeeUpdateDTO) {
        log.info("修改员工信息：{}", employeeUpdateDTO);
        employeeService.update(employeeUpdateDTO);
        return Result.success();
    }

    /**
     * 获取员工信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id) {
        log.info("获取员工信息，员工id：{}", id);
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * 编辑员工账号状态
     *
     * @param id
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    public Result editStatus(Long id, @PathVariable Integer status) {
        log.info("编辑员工账号状态，员工id：{}，员工状态：{}", id, status);
        employeeService.editStatus(id, status);
        return Result.success();
    }

    /**
     * 修改密码
     *
     * @param editPasswordDTO
     * @return
     */
    @PutMapping("/editPassword")
    public Result editPassword(@RequestBody EditPasswordDTO editPasswordDTO) {
        log.info("修改密码：{}", editPasswordDTO);
        employeeService.editPassword(editPasswordDTO);
        return Result.success();
    }

    /**
     * 退出登录
     *
     * @return
     */
    @PostMapping("/logout")
    public Result logout() {
        log.info("员工退出登录");
        return Result.success();
    }


    @PostMapping("/test2")
    public Result test2(@RequestBody Student student) {
        log.info("员工登录：{}", student);
        return Result.success();
    }
}
