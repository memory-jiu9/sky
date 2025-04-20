package com.example.sky.controller.admin;

import com.example.sky.context.BaseContext;
import com.example.sky.pojo.dto.CategoryAddDTO;
import com.example.sky.pojo.dto.CategoryPageQueryDTO;
import com.example.sky.pojo.dto.CategoryUpdateDTO;
import com.example.sky.pojo.entity.Category;
import com.example.sky.pojo.vo.PageQueryVO;
import com.example.sky.service.CategoryService;
import com.example.sky.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminCategoryController")
@RequestMapping("/admin/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分类分页查询
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageQueryVO> page(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("员工分类分页查询：{}", categoryPageQueryDTO);
        PageQueryVO pageQueryVO = categoryService.page(categoryPageQueryDTO);
        return Result.success(pageQueryVO);
    }

    /**
     * 新增分类
     *
     * @param categoryAddDTO
     * @return
     */
    @PostMapping
    public Result add(@RequestBody CategoryAddDTO categoryAddDTO) {
        Long id = BaseContext.getEmpId();
        log.info("员工id：{}，新增分类：{}", id, categoryAddDTO);
        categoryService.add(categoryAddDTO);
        return Result.success();
    }

    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    @DeleteMapping
    public Result delete(Long id) {
        log.info("员工删除分类，分类id：{}", id);
        categoryService.delete(id);
        return Result.success();
    }

    /**
     * 编辑分类状态
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result editStatus(@PathVariable Integer status, Long id) {
        log.info("编辑分类状态，分类id：{}，分类状态：{}", id, status);
        categoryService.editStatus(id, status);
        return Result.success();
    }

    /**
     * 修改分类
     *
     * @param categoryUpdateDTO
     * @return
     */
    @PutMapping
    public Result update(@RequestBody CategoryUpdateDTO categoryUpdateDTO) {
        log.info("修改分类：{}", categoryUpdateDTO);
        categoryService.update(categoryUpdateDTO);
        return Result.success();
    }

    /**
     * 获取分类列表
     * @param type
     * @return
     */
    @GetMapping("/list")
    public Result<List<Category>> list(Integer type) {
        log.info("员工获取分类列表，分类类型：{}", type);
        List<Category> categoryList = categoryService.listForAdmin(type);
        return Result.success(categoryList);
    }
}
