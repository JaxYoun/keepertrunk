package com.troy.keeper.system.util;

import com.troy.keeper.core.utils.JsonUtils;
import com.troy.keeper.system.domain.SmUser;
import com.troy.keeper.system.dto.UserInfoDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 用户数据Parameter
 * Created by SimonChu on 2017/6/12.
 */
public class UserParamter {

    // 用户ID
    private Long id;

    // 组织机构ID
    private Long orgId;

    // 名字
    private String userName;

    // 类型
    private Integer type;

    // 账号
    private String loginName;

    // 邮箱号
    private String email;

    // 手机号
    private String mobilePhone;

    // 菜单列表
    private List<Map<String, Object>> menuIds;

    // 当前的岗位ID
    private Long currentPostId;

    // 岗位列表
    private List<Map<String, Object>> posts;

    public List<Map<String, Object>> getPosts() {
        return posts;
    }

    public void setPosts(List<Map<String, Object>> posts) {
        this.posts = posts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public List<Map<String, Object>> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<Map<String, Object>> menuIds) {
        this.menuIds = menuIds;
    }

    public Long getCurrentPostId() {
        return currentPostId;
    }

    public void setCurrentPostId(Long currentPostId) {
        this.currentPostId = currentPostId;
    }

    /**
     * 返回当前对象的内容为Map类型
     *
     * @return
     */
    public Map<String, Object> getUserParamter() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", this.getId());
        data.put("orgId", this.getOrgId());
        data.put("userName", this.getUserName());
        data.put("type", this.getType());
        data.put("loginName", this.getLoginName());
        data.put("email", this.getEmail());
        data.put("mobilePhone", this.getMobilePhone());
        data.put("menuIds", this.getMenuIds());
        data.put("currentPostId", this.getCurrentPostId());
        data.put("posts", this.getPosts());
        return data;
    }

    /**
     * 返回当前对象的内容为UserInfoDTO类型
     *
     * @return
     */
    public UserInfoDTO getUserParamterDTO(){
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setId(this.getId());
        userInfoDTO.setOrgId(this.getOrgId());
        userInfoDTO.setUserName(this.getUserName());
        userInfoDTO.setType(this.getType());
        userInfoDTO.setLoginName(this.getLoginName());
        userInfoDTO.setEmail(this.getEmail());
        userInfoDTO.setMobilePhone(this.getMobilePhone());
        userInfoDTO.setMenuIds(this.getMenuIds());
        userInfoDTO.setCurrentPostId(this.getCurrentPostId());
        userInfoDTO.setPosts(this.getPosts());
        return userInfoDTO;
    }

    /**
     * 获取Parmter数据为Jackson实现的JSONObjectString 读取
     *
     * @return
     */
    public String getUserParamterToJackson() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", this.getId());
        data.put("orgId", this.getOrgId());
        data.put("userName", this.getUserName());
        data.put("type", this.getType());
        data.put("loginName", this.getLoginName());
        data.put("email", this.getEmail());
        data.put("mobilePhone", this.getMobilePhone());
        data.put("menuIds", this.getMenuIds());
        data.put("currentPostId", this.getCurrentPostId());
        data.put("posts", this.getPosts());
        return JsonUtils.toJson(data);
    }

    /**
     * 将Optional<SmUser>转换为UserParamter
     *
     * @param smUser
     */
    public void setUserParamter(Optional<SmUser> smUser) {
        if (smUser.isPresent()){
            this.id = smUser.get().getId();
            this.orgId = smUser.get().getOrgId();
            this.type = smUser.get().getType();
            this.userName = smUser.get().getUserName();
            this.loginName = smUser.get().getLoginName();
            this.email = smUser.get().getEmail();
            this.mobilePhone = smUser.get().getMobilePhone();
        }
    }

}
