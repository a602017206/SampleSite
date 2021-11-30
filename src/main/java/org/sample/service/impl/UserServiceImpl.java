package org.sample.service.impl;

import lombok.extern.log4j.Log4j2;
import org.sample.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author dingwei
 */
@Log4j2
@Service("userService")
public class UserServiceImpl implements UserService {

    @Override
    public String getUserName(String userid) {
        log.info("userid: {}", userid);
        return "admin";
    }

    @Override
    public String getCompontent() {
        return null;
    }

}
