package com.example.sky.service;

import com.example.sky.pojo.dto.AddressBookAddDTO;
import com.example.sky.pojo.dto.AddressBookSetDefaultDTO;
import com.example.sky.pojo.dto.AddressBookUpdateDTO;
import com.example.sky.pojo.entity.AddressBook;

public interface AddressBookService {
    /**
     * 用户获取所有的地址列表
     * @return
     */
    AddressBook[] list();

    /**
     * 获取默认地址
     * @return
     */
    AddressBook getDefault();

    /**
     * 根据id获取地址
     * @param id
     * @return
     */
    AddressBook get(Long id);

    /**
     * 设置默认地址
     * @param addressBookSetDefaultDTO
     */
    void setDefault(AddressBookSetDefaultDTO addressBookSetDefaultDTO);

    /**
     * 根据id删除地址
     * @param id
     */
    void delete(Long id);

    /**
     * 新增地址
     * @param addressBookAddDTO
     */
    void add(AddressBookAddDTO addressBookAddDTO);

    /**
     * 修改地址
     * @param addressBookUpdateDTO
     */
    void update(AddressBookUpdateDTO addressBookUpdateDTO);
}
