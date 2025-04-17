package com.example.sky.controller.admin;

import com.example.sky.service.ShopService;
import com.example.sky.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Slf4j
public class ShopController {
    @Autowired
    private ShopService shopService;

    /**
     * 获取店铺营业状态
     *
     * @return
     */
    @GetMapping("/status")
    public Result<Integer> get() {
        log.info("员工获取店铺营业状态");
        Integer status = shopService.get();
        return Result.success(status);
    }

    /**
     * 设置店铺营业状态
     *
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    public Result set(@PathVariable Integer status) {
        log.info("设置店铺营业状态：{}", status);
        shopService.set(status);
        return Result.success();
    }
}
