package com.mj.tcs.service.role;

import java.util.List;

/**
 * @author Wang Zhen
 */
public interface RoleService
{
    public List<String> GetAllRoles();
    public void CreateRole(String roleName);
    public void DeleteRole(String roleName);
}