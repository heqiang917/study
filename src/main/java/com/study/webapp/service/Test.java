package com.study.webapp.service;

public class Test {
    /**
     * 
     */
    private Long id;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 1系统管理员2普通成员
     */
    private Long roleType;

    /**
     * 部门id
     */
    private Long departmentId;

    /**
     * 
     */
    private String password;

    /**
     * 0删除1默认
     */
    private Byte status;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 
     * @return id 
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 邮箱
     * @return email 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 邮箱
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 真实姓名
     * @return real_name 真实姓名
     */
    public String getRealName() {
        return realName;
    }

    /**
     * 真实姓名
     * @param realName 真实姓名
     */
    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    /**
     * 1系统管理员2普通成员
     * @return role_type 1系统管理员2普通成员
     */
    public Long getRoleType() {
        return roleType;
    }

    /**
     * 1系统管理员2普通成员
     * @param roleType 1系统管理员2普通成员
     */
    public void setRoleType(Long roleType) {
        this.roleType = roleType;
    }

    /**
     * 部门id
     * @return department_id 部门id
     */
    public Long getDepartmentId() {
        return departmentId;
    }

    /**
     * 部门id
     * @param departmentId 部门id
     */
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * 
     * @return password 
     */
    public String getPassword() {
        return password;
    }

    /**
     * 
     * @param password 
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 0删除1默认
     * @return status 0删除1默认
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 0删除1默认
     * @param status 0删除1默认
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 创建时间
     * @return create_time 创建时间
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     * @param createTime 创建时间
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    /**
     * 更新时间
     * @return update_time 更新时间
     */
    public Long getUpdateTime() {
        return updateTime;
    }

    /**
     * 更新时间
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}