package org.sample.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色和权限关联表
 * @TableName sys_permission_role
 */
@TableName(value ="sys_permission_role")
@Data
public class PermissionRole implements Serializable {
    /**
     * 主键，自增
     */
    @TableId(value = "inner_id", type = IdType.AUTO)
    private Long innerId;

    /**
     * 权限id
     */
    @TableField(value = "permission_id")
    private Long permissionId;

    /**
     * 角色id
     */
    @TableField(value = "role_id")
    private Long roleId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        PermissionRole other = (PermissionRole) that;
        return (this.getInnerId() == null ? other.getInnerId() == null : this.getInnerId().equals(other.getInnerId()))
            && (this.getPermissionId() == null ? other.getPermissionId() == null : this.getPermissionId().equals(other.getPermissionId()))
            && (this.getRoleId() == null ? other.getRoleId() == null : this.getRoleId().equals(other.getRoleId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getInnerId() == null) ? 0 : getInnerId().hashCode());
        result = prime * result + ((getPermissionId() == null) ? 0 : getPermissionId().hashCode());
        result = prime * result + ((getRoleId() == null) ? 0 : getRoleId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", innerId=").append(innerId);
        sb.append(", permissionId=").append(permissionId);
        sb.append(", roleId=").append(roleId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}