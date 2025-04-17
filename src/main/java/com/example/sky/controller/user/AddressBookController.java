package com.example.sky.controller.user;

import com.example.sky.pojo.dto.AddressBookAddDTO;
import com.example.sky.pojo.dto.AddressBookSetDefaultDTO;
import com.example.sky.pojo.dto.AddressBookUpdateDTO;
import com.example.sky.pojo.entity.AddressBook;
import com.example.sky.service.AddressBookService;
import com.example.sky.util.Result;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/addressBook")
@Slf4j
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 用户获取所有的地址列表
     *
     * @return
     */
    @GetMapping("/list")
    public Result<AddressBook[]> list() {
        log.info("用户获取所有的地址列表");
        AddressBook[] addressBooks = addressBookService.list();
        return Result.success(addressBooks);
    }

    /**
     * 获取默认地址
     *
     * @return
     */
    @GetMapping("/default")
    public Result<AddressBook> getDefault() {
        log.info("获取默认地址");
        AddressBook addressBook = addressBookService.getDefault();
        return Result.success(addressBook);
    }

    /**
     * 根据id获取地址
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<AddressBook> get(@PathVariable Long id) {
        log.info("根据id获取地址，id：{}", id);
        AddressBook addressBook = addressBookService.get(id);
        return Result.success(addressBook);
    }

    /**
     * 设置默认地址
     *
     * @param addressBookSetDefaultDTO
     * @return
     */
    @PutMapping("/default")
    public Result setDefault(@RequestBody AddressBookSetDefaultDTO addressBookSetDefaultDTO) {
        log.info("设置默认地址，地址id：{}", addressBookSetDefaultDTO);
        addressBookService.setDefault(addressBookSetDefaultDTO);
        return Result.success();
    }

    /**
     * 根据id删除地址
     * @param id
     * @return
     */
    @DeleteMapping
    public Result delete(Long id){
        log.info("根据id删除地址，id：{}",id);
        addressBookService.delete(id);
        return Result.success();
    }

    /**
     * 新增地址
     * @param addressBookAddDTO
     * @return
     */
    @PostMapping
    public Result add(@RequestBody AddressBookAddDTO addressBookAddDTO){
        log.info("新增地址：{}",addressBookAddDTO);
        addressBookService.add(addressBookAddDTO);
        return Result.success();
    }

    /**
     * 修改地址
     * @param addressBookUpdateDTO
     * @return
     */
    @PutMapping
    public Result update(@RequestBody AddressBookUpdateDTO addressBookUpdateDTO){
        log.info("修改地址：{}",addressBookUpdateDTO);
        addressBookService.update(addressBookUpdateDTO);
        return Result.success();
    }
}
