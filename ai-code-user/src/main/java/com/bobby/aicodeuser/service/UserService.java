package com.bobby.aicodeuser.service;

import com.bobby.aicode.model.dto.user.UserQueryRequest;
import com.bobby.aicode.model.vo.LoginUserVO;
import com.bobby.aicode.model.vo.UserVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.bobby.aicode.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 用户 服务层。
 *
 * @author <a href="https://github.com/daydayde">Bobby</a>
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);


    /**
     * 加密
     */
    String getEncryptPassword(String password);

    /**
     * 获得脱敏后的用户信息
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 用户登录
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取当前登录用户
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 获取脱敏后的用户信息
     * */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏后的用户信息列表
     * */
    List<UserVO> getUserVOList(List<User> userList);
    /**
     * 用户注销
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 根据查询条件构造查询参数
     */
    QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest);
}
