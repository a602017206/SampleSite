package org.sample.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sample.domain.Role;
import org.sample.mapper.RoleMapper;
import org.sample.service.RoleService;
import org.springframework.stereotype.Service;

/**
* @author dingwei
* @description 针对表【sys_role(系统角色表)】的数据库操作Service实现
* @createDate 2022-03-08 11:52:33
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

}




