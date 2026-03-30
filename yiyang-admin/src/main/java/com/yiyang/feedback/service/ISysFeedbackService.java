package com.yiyang.feedback.service;

import java.util.List;
import com.yiyang.feedback.domain.SysFeedback;

/**
 * 沟通反馈Service接口
 * 
 * @author ruoyi
 * @date 2026-03-30
 */
public interface ISysFeedbackService
{
    /**
     * 查询沟通反馈
     * 
     * @param feedbackId 沟通反馈主键
     * @return 沟通反馈
     */
    public SysFeedback selectSysFeedbackById(Long feedbackId);

    /**
     * 查询沟通反馈列表
     * 
     * @param sysFeedback 沟通反馈
     * @return 沟通反馈集合
     */
    public List<SysFeedback> selectSysFeedbackList(SysFeedback sysFeedback);

    /**
     * 新增沟通反馈
     * 
     * @param sysFeedback 沟通反馈
     * @return 结果
     */
    public int insertSysFeedback(SysFeedback sysFeedback);

    /**
     * 修改沟通反馈
     * 
     * @param sysFeedback 沟通反馈
     * @return 结果
     */
    public int updateSysFeedback(SysFeedback sysFeedback);

    /**
     * 批量删除沟通反馈
     * 
     * @param feedbackIds 需要删除的沟通反馈主键集合
     * @return 结果
     */
    public int deleteSysFeedbackByIds(Long[] feedbackIds);

    /**
     * 删除沟通反馈信息
     * 
     * @param feedbackId 沟通反馈主键
     * @return 结果
     */
    public int deleteSysFeedbackById(Long feedbackId);

    /**
     * 更新沟通反馈状态
     * 
     * @param feedbackId 沟通反馈主键
     * @param status 状态
     * @return 结果
     */
    public int updateSysFeedbackStatus(Long feedbackId, String status);

    /**
     * 查询用户收到的反馈
     * 
     * @param userId 用户ID
     * @return 沟通反馈集合
     */
    public List<SysFeedback> selectSysFeedbackByReceiverId(Long userId);

    /**
     * 查询用户发送的反馈
     * 
     * @param userId 用户ID
     * @return 沟通反馈集合
     */
    public List<SysFeedback> selectSysFeedbackBySenderId(Long userId);

    /**
     * 查询所有反馈（管理员使用）
     * 
     * @return 沟通反馈集合
     */
    public List<SysFeedback> selectAllSysFeedback();

    /**
     * 查询反馈的回复列表
     * 
     * @param parentId 父反馈ID
     * @return 沟通反馈集合
     */
    public List<SysFeedback> selectSysFeedbackReplies(Long parentId);

    /**
     * 查询反馈的回复数量
     * 
     * @param parentId 父反馈ID
     * @return 回复数量
     */
    public int countSysFeedbackReplies(Long parentId);

    /**
     * 回复反馈
     * 
     * @param sysFeedback 反馈信息
     * @return 结果
     */
    public int replySysFeedback(SysFeedback sysFeedback);
}
