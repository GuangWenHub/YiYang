package com.yiyang.profile.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yiyang.common.annotation.Excel;
import com.yiyang.common.core.domain.BaseEntity;

/**
 * 用户扩展信息对象 user_profile
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
public class UserProfile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 关联 sys_user 表的用户ID */
    private Long userId;

    /** 真实姓名 */
    @Excel(name = "真实姓名")
    private String realName;

    /** 身份证号 */
    private String idCard;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 性别 */
    @Excel(name = "性别")
    private String gender;

    /** 出生日期 */
    private Date birthDate;

    /** 用户角色类型：1-管理员,2-医生,3-护工,4-家属 */
    @Excel(name = "用户角色类型：1-管理员,2-医生,3-护工,4-家属")
    private Long roleType;

    /** 头像路径 */
    @Excel(name = "头像路径")
    private String avatar;

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setRealName(String realName) 
    {
        this.realName = realName;
    }

    public String getRealName() 
    {
        return realName;
    }

    public void setIdCard(String idCard) 
    {
        this.idCard = idCard;
    }

    public String getIdCard() 
    {
        return idCard;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setGender(String gender) 
    {
        this.gender = gender;
    }

    public String getGender() 
    {
        return gender;
    }

    public void setBirthDate(Date birthDate) 
    {
        this.birthDate = birthDate;
    }

    public Date getBirthDate() 
    {
        return birthDate;
    }

    public void setRoleType(Long roleType) 
    {
        this.roleType = roleType;
    }

    public Long getRoleType() 
    {
        return roleType;
    }

    public void setAvatar(String avatar) 
    {
        this.avatar = avatar;
    }

    public String getAvatar() 
    {
        return avatar;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("realName", getRealName())
            .append("idCard", getIdCard())
            .append("phone", getPhone())
            .append("gender", getGender())
            .append("birthDate", getBirthDate())
            .append("roleType", getRoleType())
            .append("avatar", getAvatar())
            .toString();
    }
}
