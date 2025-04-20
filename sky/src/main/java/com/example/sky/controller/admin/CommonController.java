package com.example.sky.controller.admin;

import com.example.sky.service.CommonService;
import com.example.sky.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {

    @Autowired
    private CommonService commonService;

    /**
     * 上传文件
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        log.info("文件上传：{}", file);
        String url = commonService.upload(file);
        return Result.success(url);
    }
}
