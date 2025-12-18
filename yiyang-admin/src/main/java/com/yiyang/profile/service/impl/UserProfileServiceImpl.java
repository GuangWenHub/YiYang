package com.yiyang.profile.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yiyang.profile.mapper.UserProfileMapper;
import com.yiyang.profile.domain.UserProfile;
import com.yiyang.profile.service.IUserProfileService;

/**
 * 用户扩展信息Service业务层处理
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
@Service
public class UserProfileServiceImpl implements IUserProfileService 
{
    @Autowired
    private UserProfileMapper userProfileMapper;

    /**
     * 查询用户扩展信息
     * 
     * @param userId 用户扩展信息主键
     * @return 用户扩展信息
     */
    @Override
    public UserProfile selectUserProfileByUserId(Long userId)
    {
        return userProfileMapper.selectUserProfileByUserId(userId);
    }

    /**
     * 查询用户扩展信息列表
     * 
     * @param userProfile 用户扩展信息
     * @return 用户扩展信息
     */
    @Override
    public List<UserProfile> selectUserProfileList(UserProfile userProfile)
    {
        return userProfileMapper.selectUserProfileList(userProfile);
    }

    /**
     * 新增用户扩展信息
     * 
     * @param userProfile 用户扩展信息
     * @return 结果
     */
    @Override
    public int insertUserProfile(UserProfile userProfile)
    {
        return userProfileMapper.insertUserProfile(userProfile);
    }

    /**
     * 修改用户扩展信息
     * 
     * @param userProfile 用户扩展信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(UserProfile userProfile)
    {
        return userProfileMapper.updateUserProfile(userProfile);
    }

    /**
     * 批量删除用户扩展信息
     * 
     * @param userIds 需要删除的用户扩展信息主键
     * @return 结果
     */
    @Override
    public int deleteUserProfileByUserIds(Long[] userIds)
    {
        return userProfileMapper.deleteUserProfileByUserIds(userIds);
    }

    /**
     * 删除用户扩展信息信息
     * 
     * @param userId 用户扩展信息主键
     * @return 结果
     */
    @Override
    public int deleteUserProfileByUserId(Long userId)
    {
        return userProfileMapper.deleteUserProfileByUserId(userId);
    }
}
