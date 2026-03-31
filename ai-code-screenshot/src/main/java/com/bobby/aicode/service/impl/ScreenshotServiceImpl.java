package com.bobby.aicode.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.bobby.aicode.exception.ErrorCode;
import com.bobby.aicode.exception.ThrowUtils;
import com.bobby.aicode.manager.PicManager;
import com.bobby.aicode.service.ScreenshotService;
import com.bobby.aicode.utils.WebScreenshotUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
public class ScreenshotServiceImpl implements ScreenshotService {
    @Resource
    private PicManager picManager;

    @Override
    public String generateAndUploadScreenshot(String WebUrl) {
        // 参数校验
        ThrowUtils.throwIf(StrUtil.isBlank(WebUrl), ErrorCode.PARAMS_ERROR, "截图网址不能为空");
        log.info("开始生成网络截图，URL：{}", WebUrl);
        // 本地截图
        String localScreenshotPath = WebScreenshotUtils.saveWebPageScreenshot(WebUrl);
        ThrowUtils.throwIf(StrUtil.isBlank(localScreenshotPath), ErrorCode.OPERATION_ERROR, "截图失败");
        // 上传图片到网络图床
        try {
            String uploadFileUrl = picManager.uploadFile(localScreenshotPath, "img.scdn.io");
            ThrowUtils.throwIf(StrUtil.isBlank(uploadFileUrl), ErrorCode.OPERATION_ERROR, "上传图片失败");
            log.info("截图上传成功，URL：{}", uploadFileUrl);
            return uploadFileUrl;
        } finally {
            // 清理本地图片
            cleanupLocalFile(localScreenshotPath);
        }
    }

    private void cleanupLocalFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            FileUtil.del(file);
            log.info("清理本地文件成功：{}", filePath);
        }
    }
}
