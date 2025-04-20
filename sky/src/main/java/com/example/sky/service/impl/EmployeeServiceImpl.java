package com.example.sky.service.impl;

import com.example.sky.constant.BaseContextConstant;
import com.example.sky.constant.DefaultPasswordConstant;
import com.example.sky.constant.EmployeeStatusConstant;
import com.example.sky.constant.ExceptionTipConstant;
import com.example.sky.context.BaseContext;
import com.example.sky.exception.*;
import com.example.sky.mapper.EmployeeMapper;
import com.example.sky.pojo.dto.*;
import com.example.sky.pojo.entity.Employee;
import com.example.sky.pojo.vo.EmployLoginVO;
import com.example.sky.pojo.vo.PageQueryVO;
import com.example.sky.properties.JwtProperties;
import com.example.sky.service.EmployeeService;
import com.example.sky.util.JwtUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;


import java.util.HashMap;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @Override
    public EmployLoginVO login(EmployeeLoginDTO employeeLoginDTO) {
        // 根据用户名获取记录
        Employee employee = employeeMapper.getByUserName(employeeLoginDTO.getUsername());

        // 判断记录是否存在
        if (employee == null)
            throw new EmployeeNotFoundException(ExceptionTipConstant.EMPLOYEE_NOT_FOUND);

        // 密码对比
        if (!employee.getPassword().equals(DigestUtils.md5DigestAsHex(employeeLoginDTO.getPassword().getBytes())))
            throw new PasswordNotEqualsException(ExceptionTipConstant.PASSWORD_NOT_EQUALS);

        // 判断员工账号状态：是否被禁用
        if (employee.getStatus().equals(EmployeeStatusConstant.DISABLE))
            throw new AccountDisabledException(ExceptionTipConstant.ACCOUNT_DISABLE);

        // 创建返回对象
        EmployLoginVO employLoginVO = new EmployLoginVO();
        // 获取token
        HashMap<String, Object> map = new HashMap<>();
        map.put(BaseContextConstant.ID, employee.getId());
        employLoginVO.setToken(JwtUtil.getToken(jwtProperties.getAdminSecretKey(), jwtProperties.getAdminTimeout(), map));

        // 拷贝属性
        BeanUtils.copyProperties(employee, employLoginVO);

        return employLoginVO;
    }

    /**
     * 员工分页查询
     *
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageQueryVO page(EmployeePageQueryDTO employeePageQueryDTO) {
        // PageHelper：页数和记录数
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        // 开始查询
        Page<Employee> result = employeeMapper.page(employeePageQueryDTO.getName());

        // 创建返回对象
        PageQueryVO pageQueryVO = new PageQueryVO();

        // 拷贝属性
        pageQueryVO.setTotal(result.getTotal());
        pageQueryVO.setRecords(result.getResult());

        return pageQueryVO;
    }

    /**
     * 新增员工
     *
     * @param employeeAddDTO
     */
    @Override
    public void add(EmployeeAddDTO employeeAddDTO) {
        // 判断用户名是否重复
        Employee employee = employeeMapper.getByUserName(employeeAddDTO.getUsername());
        if (employee != null)
            throw new UserNameDuplicateException(ExceptionTipConstant.USERNAME_DUPLICATE);

        // TODO 手机号、身份证不能重复

        // 设置默认密码：123456
        employee = new Employee();
        employee.setPassword(DigestUtils.md5DigestAsHex(DefaultPasswordConstant.PASSWORD.getBytes()));

        // 拷贝属性
        BeanUtils.copyProperties(employeeAddDTO, employee);

        employee.setStatus(EmployeeStatusConstant.DISABLE); // 默认是禁用状态

        // 执行插入操作
        employeeMapper.add(employee);
    }

    /**
     * 修改员工信息
     *
     * @param employeeUpdateDTO
     */
    @Override
    public void update(EmployeeUpdateDTO employeeUpdateDTO) {
        // 判断用户名是否重复
        Employee employee = employeeMapper.getByUserNameExcludeId(employeeUpdateDTO.getUsername(), employeeUpdateDTO.getId());
        if (employee != null)
            throw new UserNameDuplicateException(ExceptionTipConstant.USERNAME_DUPLICATE);
        // 判断手机号是否重复
        employee = employeeMapper.getByPhoneExcludeId(employeeUpdateDTO.getPhone(), employeeUpdateDTO.getId());
        if (employee != null)
            throw new PhoneDuplicateException(ExceptionTipConstant.PHONE_DUPLICATE);
        // 判断身份证号是否重复
        employee = employeeMapper.getByIdNumberExcludeId(employeeUpdateDTO.getIdNumber(), employeeUpdateDTO.getId());
        if (employee != null)
            throw new IdNumberDuplicateException(ExceptionTipConstant.ID_NUMBER_DUPLICATE);

        // 创建实体类对象
        employee = new Employee();
        // 拷贝属性
        BeanUtils.copyProperties(employeeUpdateDTO, employee);

        // 执行更新操作
        employeeMapper.update(employee);
    }

    /**
     * 获取员工信息
     *
     * @param id
     * @return
     */
    @Override
    public Employee getById(Long id) {
        return employeeMapper.getById(id);
    }

    /**
     * 编辑员工账号状态
     *
     * @param id
     * @param status
     */
    @Override
    public void editStatus(Long id, Integer status) {
        // 创建对象
        Employee employee = new Employee();

        // 拷贝属性
        employee.setId(id);
        employee.setStatus(status);

        // 执行更新操作
        employeeMapper.editStatus(employee);
    }

    /**
     * 修改密码
     *
     * @param editPasswordDTO
     */
    @Override
    public void editPassword(EditPasswordDTO editPasswordDTO) {
        // 获取员工id
        Long id = BaseContext.getEmpId();
        // 判断输入的旧密码是否和原密码相同
        Employee employee = employeeMapper.getById(id);

        if (!employee.getPassword().equals(DigestUtils.md5DigestAsHex(editPasswordDTO.getOldPassword().getBytes())))
            throw new PasswordNotEqualsException(ExceptionTipConstant.OLD_PASSWORD_NOT_EQUALS);

        // 加密密码
        String newPassword = DigestUtils.md5DigestAsHex(editPasswordDTO.getNewPassword().getBytes());

        // 更新信息
        employee.setId(id);
        employee.setPassword(newPassword);

        // 更新密码
        employeeMapper.editPassword(employee);
    }
}
