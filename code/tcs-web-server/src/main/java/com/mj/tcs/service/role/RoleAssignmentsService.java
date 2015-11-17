package com.mj.tcs.service.role;

/**
 * @author Wang Zhen
 */
public interface RoleAssignmentsService {
    public void AddRolesInUser(String userName, String[] roleNames);
    public void RemoveRolesFromUser(String userName, String[] roleNames);
}
