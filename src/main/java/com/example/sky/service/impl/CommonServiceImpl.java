package com.example.sky.service.impl;

import com.example.sky.constant.ExceptionTipConstant;
import com.example.sky.exception.FileUploadException;
import com.example.sky.properties.AliOSSProperties;
import com.example.sky.service.CommonService;
import com.example.sky.util.AliOSSUtil;
import org.aspectj.apache.bcel.ExceptionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private AliOSSProperties aliOSSProperties;

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @Override
    public String upload(MultipartFile file) {
        // 获取文件名
        String originalFilename = file.getOriginalFilename();

        // 获取格式后缀
        int lastIndexOf = originalFilename.lastIndexOf(".");

        // 随机生成文件名
        String key = UUID.randomUUID().toString() + originalFilename.substring(lastIndexOf);

        String url;
        // 调用工具类方法
        try {
            url = AliOSSUtil.upload(aliOSSProperties.getEndPoint(), aliOSSProperties.getAccessId(), aliOSSProperties.getAccessKey(), aliOSSProperties.getBucketName(), key, file.getBytes());
        } catch (IOException e) {
            throw new FileUploadException(ExceptionTipConstant.FILE_UPLOAD_FAIL);
        }
        return url;
    }
}
