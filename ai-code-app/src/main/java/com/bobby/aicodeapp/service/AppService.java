package com.bobby.aicodeapp.service;

import com.bobby.aicode.model.dto.app.AppQueryRequest;
import com.bobby.aicode.model.entity.User;
import com.bobby.aicode.model.vo.AppVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.bobby.aicode.model.entity.App;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author <a href="https://github.com/daydayde">Bobby</a>
 */
public interface AppService extends IService<App> {

    AppVO getAppVO(App app);

    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    List<AppVO> getAppVOList(List<App> appList);

    Flux<String> chatToGenCode(Long appId, String prompt, User loginUser);

    /**
     * 应用部署
     * 返回可访问的部署地址
     * */
    String deployApp(Long appId,User loginUser);

    void generateAppScreenshotAsync(Long appId, String appDeployUrl);
}
