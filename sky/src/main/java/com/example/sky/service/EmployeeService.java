package com.example.sky.service;

import com.example.sky.pojo.dto.*;
import com.example.sky.pojo.entity.Employee;
import com.example.sky.pojo.vo.EmployLoginVO;
import com.example.sky.pojo.vo.PageQueryVO;

public interface EmployeeService {
    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    EmployLoginVO login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 员工分页查询
     *
     * @param employeePageQueryDTO
     * @return
     */
    PageQueryVO page(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 新增员工
     *
     * @param employeeAddDTO
     */
    void add(EmployeeAddDTO employeeAddDTO);

    /**
     * 修改员工信息
     *
     * @param employeeUpdateDTO
     */
    void update(EmployeeUpdateDTO employeeUpdateDTO);

    /**
     * 获取员工信息
     *
     * @param id
     * @return
     */
    Employee getById(Long id);

    /**
     * 编辑员工账号状态
     *
     * @param id
     * @param status
     */
    void editStatus(Long id, Integer status);

    /**
     * 修改密码
     *
     * @param editPasswordDTO
     */
    void editPassword(EditPasswordDTO editPasswordDTO);
}
