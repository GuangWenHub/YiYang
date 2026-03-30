package com.yiyang.feedback.service.impl;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yiyang.feedback.mapper.SysFeedbackMapper;
import com.yiyang.feedback.domain.SysFeedback;
import com.yiyang.feedback.service.ISysFeedbackService;

/**
 * 沟通反馈Service实现
 * 
 * @author ruoyi
 * @date 2026-03-30
 */
@Service
public class SysFeedbackServiceImpl implements ISysFeedbackService
{
    @Autowired
    private SysFeedbackMapper sysFeedbackMapper;

    /**
     * 查询沟通反馈
     * 
     * @param feedbackId 沟通反馈主键
     * @return 沟通反馈
     */
    @Override
    public SysFeedback selectSysFeedbackById(Long feedbackId)
    {
        return sysFeedbackMapper.selectSysFeedbackById(feedbackId);
    }

    /**
     * 查询沟通反馈列表
     * 
     * @param sysFeedback 沟通反馈
     * @return 沟通反馈集合
     */
    @Override
    public List<SysFeedback> selectSysFeedbackList(SysFeedback sysFeedback)
    {
        return sysFeedbackMapper.selectSysFeedbackList(sysFeedback);
    }

    /**
     * 新增沟通反馈
     * 
     * @param sysFeedback 沟通反馈
     * @return 结果
     */
    @Override
    public int insertSysFeedback(SysFeedback sysFeedback)
    {
        return sysFeedbackMapper.insertSysFeedback(sysFeedback);
    }

    /**
     * 修改沟通反馈
     * 
     * @param sysFeedback 沟通反馈
     * @return 结果
     */
    @Override
    public int updateSysFeedback(SysFeedback sysFeedback)
    {
        return sysFeedbackMapper.updateSysFeedback(sysFeedback);
    }

    /**
     * 批量删除沟通反馈
     * 
     * @param feedbackIds 需要删除的沟通反馈主键集合
     * @return 结果
     */
    @Override
    public int deleteSysFeedbackByIds(Long[] feedbackIds)
    {
        return sysFeedbackMapper.deleteSysFeedbackByIds(feedbackIds);
    }

    /**
     * 删除沟通反馈信息
     * 
     * @param feedbackId 沟通反馈主键
     * @return 结果
     */
    @Override
    public int deleteSysFeedbackById(Long feedbackId)
    {
        return sysFeedbackMapper.deleteSysFeedbackById(feedbackId);
    }

    /**
     * 更新沟通反馈状态
     * 
     * @param feedbackId 沟通反馈主键
     * @param status 状态
     * @return 结果
     */
    @Override
    public int updateSysFeedbackStatus(Long feedbackId, String status)
    {
        return sysFeedbackMapper.updateSysFeedbackStatus(feedbackId, status);
    }

    /**
     * 查询用户收到的反馈
     * 
     * @param userId 用户ID
     * @return 沟通反馈集合
     */
    @Override
    public List<SysFeedback> selectSysFeedbackByReceiverId(Long userId)
    {
        return sysFeedbackMapper.selectSysFeedbackByReceiverId(userId);
    }

    /**
     * 查询用户发送的反馈
     * 
     * @param userId 用户ID
     * @return 沟通反馈集合
     */
    @Override
    public List<SysFeedback> selectSysFeedbackBySenderId(Long userId)
    {
        return sysFeedbackMapper.selectSysFeedbackBySenderId(userId);
    }

    /**
     * 查询所有反馈（管理员使用）
     * 
     * @return 沟通反馈集合
     */
    @Override
    public List<SysFeedback> selectAllSysFeedback()
    {
        return sysFeedbackMapper.selectSysFeedbackList(new SysFeedback());
    }

    /**
     * 查询反馈的回复列表
     * 
     * @param parentId 父反馈ID
     * @return 沟通反馈集合
     */
    @Override
    public List<SysFeedback> selectSysFeedbackReplies(Long parentId)
    {
        return sysFeedbackMapper.selectSysFeedbackReplies(parentId);
    }

    /**
     * 查询反馈的回复数量
     * 
     * @param parentId 父反馈ID
     * @return 回复数量
     */
    @Override
    public int countSysFeedbackReplies(Long parentId)
    {
        return sysFeedbackMapper.countSysFeedbackReplies(parentId);
    }

    /**
     * 回复反馈
     * 
     * @param sysFeedback 反馈信息
     * @return 结果
     */
    @Override
    public int replySysFeedback(SysFeedback sysFeedback)
    {
        // 检查回复数量是否超过10条
        int replyCount = countSysFeedbackReplies(sysFeedback.getParentId());
        if (replyCount >= 10) {
            throw new RuntimeException("回复数量已达到上限，请新开留言");
        }
        
        // 插入回复
        int result = sysFeedbackMapper.insertSysFeedback(sysFeedback);
        
        // 将原反馈标记为有回复未读
        if (result > 0) {
            sysFeedbackMapper.updateSysFeedbackStatus(sysFeedback.getParentId(), "3");
        }
        
        return result;
    }
}
