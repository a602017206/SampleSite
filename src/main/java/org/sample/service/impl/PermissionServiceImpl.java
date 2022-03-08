package org.sample.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sample.domain.Permission;
import org.sample.mapper.PermissionMapper;
import org.sample.service.PermissionService;
import org.springframework.stereotype.Service;

/**
* @author dingwei
* @description 针对表【sys_permission(系统权限表)】的数据库操作Service实现
* @createDate 2022-03-08 11:52:33
*/
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission>
    implements PermissionService{

}




