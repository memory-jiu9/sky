package com.example.sky.controller.user;

import com.example.sky.service.ShopService;
import com.example.sky.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@RequestMapping("/user/shop")
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
        log.info("用户获取店铺营业状态");
        Integer status = shopService.get();
        return Result.success(status);
    }
}
