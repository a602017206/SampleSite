package org.sample.service;

/**
 * @author dingwei
 */
public interface UserService {

    /**
     * 获取用户名称
     * @param userid 用户id
     * @return
     */
    String getUserName(String userid);

    /**
     * 获取配置
     * @return
     */
    String getCompontent();


}
