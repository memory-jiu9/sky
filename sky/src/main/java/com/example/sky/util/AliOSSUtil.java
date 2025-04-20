package com.example.sky.util;

import com.aliyun.oss.*;

import java.io.ByteArrayInputStream;

public class AliOSSUtil {
    public static String upload(String endPoint, String accessId, String accessKey, String bucketName, String key, byte[] bytes) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessId, accessKey);

        try {
            // 创建PutObject请求。
            ossClient.putObject(bucketName, key, new ByteArrayInputStream(bytes));
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        // https://BucketName.Endpoint/key
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder
                .append(bucketName)
                .append(".")
                .append(endPoint)
                .append("/")
                .append(key);

        return stringBuilder.toString();
    }
}
