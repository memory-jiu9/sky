package com.example.sky.controller.user;

import com.example.sky.pojo.entity.Category;
import com.example.sky.service.CategoryService;
import com.example.sky.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userCategoryController")
@RequestMapping("/user/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 根据type字段获取分类列表
     *
     * @param type
     * @return
     */
    @GetMapping("/list")
    public Result<List<Category>> list(Integer type) {
        log.info("根据type字段获取分类列表，type：{}", type);
        List<Category> list = categoryService.listForUser(type);
        return Result.success(list);
    }
}
