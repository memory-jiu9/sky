package com.example.sky.service.impl;

import com.example.sky.constant.AddressBookDefaultConstant;
import com.example.sky.context.BaseContext;
import com.example.sky.mapper.AddressBookMapper;
import com.example.sky.pojo.dto.AddressBookAddDTO;
import com.example.sky.pojo.dto.AddressBookSetDefaultDTO;
import com.example.sky.pojo.dto.AddressBookUpdateDTO;
import com.example.sky.pojo.entity.AddressBook;
import com.example.sky.service.AddressBookService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 用户获取所有的地址列表
     *
     * @return
     */
    @Override
    public AddressBook[] list() {
        // 获取用户id
        Long userId = BaseContext.getUserId();

        return addressBookMapper.list(userId);
    }

    /**
     * 获取默认地址
     *
     * @return
     */
    @Override
    public AddressBook getDefault() {
        // 获取用户id
        Long userId = BaseContext.getUserId();
        return addressBookMapper.getDefault(userId);
    }

    /**
     * 根据id获取地址
     *
     * @param id
     * @return
     */
    @Override
    public AddressBook get(Long id) {
        return addressBookMapper.getById(id);
    }

    /**
     * 设置默认地址
     *
     * @param addressBookSetDefaultDTO
     */
    @Override
    public void setDefault(AddressBookSetDefaultDTO addressBookSetDefaultDTO) {
        // 获取用户id
        Long userId = BaseContext.getUserId();
        // 将该用户的所有地址的is_default设为0
        addressBookMapper.cancelDefault(userId);

        // 将指定的地址设为1
        addressBookMapper.setDefault(addressBookSetDefaultDTO.getId());
    }

    /**
     * 根据id删除地址
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        addressBookMapper.delete(id);
    }

    /**
     * 新增地址
     *
     * @param addressBookAddDTO
     */
    @Override
    public void add(AddressBookAddDTO addressBookAddDTO) {
        // 获取userId
        Long userId = BaseContext.getUserId();

        // 创建AddressBook实例
        AddressBook addressBook = new AddressBook();
        BeanUtils.copyProperties(addressBookAddDTO, addressBook);
        addressBook.setUserId(userId);
        addressBook.setIsDefault(AddressBookDefaultConstant.DEFAULT);

        // 插入操作
        addressBookMapper.add(addressBook);
    }

    /**
     * 修改地址
     * @param addressBookUpdateDTO
     */
    @Override
    public void update(AddressBookUpdateDTO addressBookUpdateDTO) {
        // 创建AddressBook实例
        AddressBook addressBook = new AddressBook();
        BeanUtils.copyProperties(addressBookUpdateDTO, addressBook);

        // 更新操作
        addressBookMapper.update(addressBook);
    }
}
