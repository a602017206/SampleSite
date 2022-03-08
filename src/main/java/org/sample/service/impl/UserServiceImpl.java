package org.sample.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sample.domain.User;
import org.sample.mapper.UserMapper;
import org.sample.service.UserService;
import org.springframework.stereotype.Service;

/**
* @author dingwei
* @description 针对表【user(用户信息表)】的数据库操作Service实现
* @createDate 2022-03-08 11:52:33
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




