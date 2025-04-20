package com.example.sky.mapper;

import com.example.sky.annotation.CreateAndUpdateAutoFill;
import com.example.sky.constant.OperationTypeConstant;
import com.example.sky.pojo.entity.Employee;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface EmployeeMapper {
    /**
     * 根据用户名获取记录
     *
     * @param username
     * @return employee实例对象
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUserName(String username);

    /**
     * 员工分页查询
     *
     * @param name
     * @return
     */
    Page<Employee> page(String name);

    /**
     * 新增员工
     *
     * @param employee
     */
    @CreateAndUpdateAutoFill(operationType = OperationTypeConstant.INSERT)
    void add(Employee employee);

    /**
     * 根据手机号获取记录，排除自己
     *
     * @param phone
     * @return
     */
    @Select("select * from employee where phone = #{phone} and id != #{id}")
    Employee getByPhoneExcludeId(String phone, Long id);

    /**
     * 根据身份证号获取记录，排除自己
     *
     * @param idNumber
     * @return
     */
    @Select("select * from employee where id_number = #{idNumber} and id != #{id}")
    Employee getByIdNumberExcludeId(String idNumber, Long id);

    /**
     * 根据用户名获取记录，排除自己
     *
     * @param username
     * @return employee实例对象
     */
    @Select("select * from employee where username = #{username} and id != #{id}")
    Employee getByUserNameExcludeId(String username, Long id);

    /**
     * 更新员工信息
     *
     * @param employee
     */
    @CreateAndUpdateAutoFill(operationType = OperationTypeConstant.UPDATE)
    void update(Employee employee);

    /**
     * 根据id获取记录
     *
     * @param id
     * @return
     */
    @Select("select * from employee where id = #{id}")
    Employee getById(Long id);

    /**
     * 编辑员工账号状态
     *
     * @param employee
     */
    @CreateAndUpdateAutoFill(operationType = OperationTypeConstant.UPDATE)
    @Update("update employee set status = #{status}, update_user = #{updateUser}, update_time = #{updateTime} where id = #{id}")
    void editStatus(Employee employee);

    /**
     * 修改密码
     *
     * @param employee
     */
    @Update("update employee set password = #{password}, update_time = #{updateTime}, update_user = #{updateUser} where id = #{id}")
    @CreateAndUpdateAutoFill(operationType = OperationTypeConstant.UPDATE)
    void editPassword(Employee employee);
}
