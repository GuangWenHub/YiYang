package com.yiyang.profile.mapper;

import java.util.List;
import com.yiyang.profile.domain.UserProfile;

/**
 * 用户扩展信息Mapper接口
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
public interface UserProfileMapper 
{
    /**
     * 查询用户扩展信息
     * 
     * @param userId 用户扩展信息主键
     * @return 用户扩展信息
     */
    public UserProfile selectUserProfileByUserId(Long userId);

    /**
     * 查询用户扩展信息列表
     * 
     * @param userProfile 用户扩展信息
     * @return 用户扩展信息集合
     */
    public List<UserProfile> selectUserProfileList(UserProfile userProfile);

    /**
     * 新增用户扩展信息
     * 
     * @param userProfile 用户扩展信息
     * @return 结果
     */
    public int insertUserProfile(UserProfile userProfile);

    /**
     * 修改用户扩展信息
     * 
     * @param userProfile 用户扩展信息
     * @return 结果
     */
    public int updateUserProfile(UserProfile userProfile);

    /**
     * 删除用户扩展信息
     * 
     * @param userId 用户扩展信息主键
     * @return 结果
     */
    public int deleteUserProfileByUserId(Long userId);

    /**
     * 批量删除用户扩展信息
     * 
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserProfileByUserIds(Long[] userIds);
}
