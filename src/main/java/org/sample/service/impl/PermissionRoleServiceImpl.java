package org.sample.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sample.domain.PermissionRole;
import org.sample.mapper.PermissionRoleMapper;
import org.sample.service.PermissionRoleService;
import org.springframework.stereotype.Service;

/**
* @author dingwei
* @description 针对表【sys_permission_role(角色和权限关联表)】的数据库操作Service实现
* @createDate 2022-03-08 11:52:33
*/
@Service
public class PermissionRoleServiceImpl extends ServiceImpl<PermissionRoleMapper, PermissionRole>
    implements PermissionRoleService{

}




