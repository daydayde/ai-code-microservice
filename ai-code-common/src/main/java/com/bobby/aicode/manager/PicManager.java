package com.bobby.aicode.manager;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bobby.aicode.exception.BusinessException;
import com.bobby.aicode.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class PicManager {

    /**
     * @param filePath 需要上传的文件的路径
     * @param cdnDomin cdn域名
     *                 <p>
     *                 cdn域名有四种选择：
     *                 <p>
     *                 失控的防御系统      img.scdn.io
     *                 <p>
     *                 CloudFlare       cloudflareimg.cdn.sn
     *                 <p>
     *                 EdgeOne	        edgeoneimg.cdn.sn
     *                 <p>
     *                 ESA	            esaimg.cdn1.vip
     * @return 网络图床地址
     */
    public String uploadFile(String filePath, String cdnDomin) {
        File file = new File(filePath);
        // 检查文件是否有效
        if (!file.exists()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件不存在");
        }

        String apiUrl = "https://img.scdn.io/api/v1.php";
        HttpRequest request = HttpRequest.post(apiUrl)
                .form("image", file) // 文件参数
                .form("cdn_domain", cdnDomin) // CDN域名参数
                .timeout(50000); // 5秒超时设置

        try (HttpResponse response = request.execute()) {
            int statusCode = response.getStatus();
            // 检查HTTP状态码（非200视为异常）
            if (statusCode != 200) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "HTTP请求异常: " + statusCode + ", 原因是: " + response.body());
            }

            String responseBody = response.body();
            JSONObject json = JSONUtil.parseObj(responseBody);
            boolean success = json.getBool("success");

            // 检查JSON中的success字段
            if (!success) {
                String message = json.getStr("message");
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "上传文件失败: " + message);
            }
            String reponseUrl = json.getStr("url");
            log.warn("上传文件成功，{}  已保存至  {}", file.getName(), reponseUrl);
            return reponseUrl;
        } catch (Exception e) {
            log.error("上传文件失败");
            return null;
            //throw new BusinessException(ErrorCode.OPERATION_ERROR, "上传文件失败: " + e.getMessage());
        }
    }

    public String uploadFile(String filePath) {
        return uploadFile(filePath, "img.scdn.io");
    }
}