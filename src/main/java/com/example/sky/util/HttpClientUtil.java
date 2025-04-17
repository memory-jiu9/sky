package com.example.sky.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class HttpClientUtil {
    public static String get(String url, List<NameValuePair> list) {
        // 获取httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建get请求
        HttpGet httpGet = new HttpGet();

        String json = null;
        CloseableHttpResponse httpResponse = null;
        try {
            URI uri = new URIBuilder(new URI(url))
                    .addParameters(list)
                    .build();
            httpGet.setURI(uri);

            // 发送请求
            httpResponse = httpClient.execute(httpGet);

            // 获取返回内容
            HttpEntity entity = httpResponse.getEntity();

            // 得到json对象
            json = EntityUtils.toString(entity);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                httpResponse.close();
                httpClient.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return json;
    }
}
