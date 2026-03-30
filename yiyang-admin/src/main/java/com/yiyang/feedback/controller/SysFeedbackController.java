package com.yiyang.feedback.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yiyang.common.annotation.Log;
import com.yiyang.common.core.controller.BaseController;
import com.yiyang.common.core.domain.AjaxResult;
import com.yiyang.common.enums.BusinessType;
import com.yiyang.feedback.domain.SysFeedback;
import com.yiyang.feedback.service.ISysFeedbackService;
import com.yiyang.common.utils.poi.ExcelUtil;
import com.yiyang.common.core.page.TableDataInfo;
import com.yiyang.common.core.domain.entity.SysUser;
import com.yiyang.common.utils.SecurityUtils;
import com.yiyang.system.service.ISysUserService;

/**
 * 沟通反馈Controller
 * 
 * @author ruoyi
 * @date 2026-03-30
 */
@RestController
@RequestMapping("/feedback/feedback")
public class SysFeedbackController extends BaseController
{
    @Autowired
    private ISysFeedbackService sysFeedbackService;

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 查询沟通反馈列表
     */
    @PreAuthorize("@ss.hasPermi('feedback:feedback:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysFeedback sysFeedback)
    {
        startPage();
        List<SysFeedback> list = sysFeedbackService.selectSysFeedbackList(sysFeedback);
        return getDataTable(list);
    }

    /**
     * 查询用户收到的反馈
     */
    @GetMapping("/my/received")
    public TableDataInfo getReceivedFeedback()
    {
        Long userId = SecurityUtils.getUserId();
        List<SysFeedback> list = sysFeedbackService.selectSysFeedbackByReceiverId(userId);
        return getDataTable(list);
    }

    /**
     * 查询用户发送的反馈
     */
    @GetMapping("/my/sent")
    public TableDataInfo getSentFeedback()
    {
        Long userId = SecurityUtils.getUserId();
        List<SysFeedback> list = sysFeedbackService.selectSysFeedbackBySenderId(userId);
        return getDataTable(list);
    }

    /**
     * 查询所有反馈（管理员使用）
     */
    @PreAuthorize("@ss.hasPermi('feedback:feedback:all')")
    @GetMapping("/all")
    public TableDataInfo getAllFeedback()
    {
        List<SysFeedback> list = sysFeedbackService.selectAllSysFeedback();
        return getDataTable(list);
    }

    /**
     * 导出沟通反馈列表
     */
    @PreAuthorize("@ss.hasPermi('feedback:feedback:export')")
    @Log(title = "沟通反馈", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysFeedback sysFeedback)
    {
        List<SysFeedback> list = sysFeedbackService.selectSysFeedbackList(sysFeedback);
        ExcelUtil<SysFeedback> util = new ExcelUtil<SysFeedback>(SysFeedback.class);
        return util.exportExcel(list, "沟通反馈数据");
    }

    /**
     * 获取沟通反馈详细信息
     */
    @PreAuthorize("@ss.hasPermi('feedback:feedback:query')")
    @GetMapping(value = "/{feedbackId}")
    public AjaxResult getInfo(@PathVariable("feedbackId") Long feedbackId)
    {
        // 标记为已读
        sysFeedbackService.updateSysFeedbackStatus(feedbackId, "1");
        return success(sysFeedbackService.selectSysFeedbackById(feedbackId));
    }

    /**
     * 新增沟通反馈
     */
    @PreAuthorize("@ss.hasPermi('feedback:feedback:add')")
    @Log(title = "沟通反馈", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysFeedback sysFeedback)
    {
        // 获取当前用户信息
        SysUser currentUser = sysUserService.selectUserById(SecurityUtils.getUserId());
        sysFeedback.setSenderId(currentUser.getUserId());
        sysFeedback.setSenderName(currentUser.getNickName());
        // 获取用户角色
        String roleGroup = sysUserService.selectUserRoleGroup(currentUser.getUserName());
        sysFeedback.setSenderRole(roleGroup);
        sysFeedback.setStatus("0"); // 初始状态为未读
        return toAjax(sysFeedbackService.insertSysFeedback(sysFeedback));
    }

    /**
     * 修改沟通反馈
     */
    @PreAuthorize("@ss.hasPermi('feedback:feedback:edit')")
    @Log(title = "沟通反馈", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysFeedback sysFeedback)
    {
        return toAjax(sysFeedbackService.updateSysFeedback(sysFeedback));
    }

    /**
     * 删除沟通反馈
     */
    @PreAuthorize("@ss.hasPermi('feedback:feedback:remove')")
    @Log(title = "沟通反馈", businessType = BusinessType.DELETE)
    @DeleteMapping("/{feedbackIds}")
    public AjaxResult remove(@PathVariable Long[] feedbackIds)
    {
        return toAjax(sysFeedbackService.deleteSysFeedbackByIds(feedbackIds));
    }

    /**
     * 更新沟通反馈状态
     */
    @PutMapping("/status/{feedbackId}")
    public AjaxResult updateStatus(@PathVariable Long feedbackId, @RequestBody String status)
    {
        return toAjax(sysFeedbackService.updateSysFeedbackStatus(feedbackId, status));
    }

    /**
     * 查询反馈的回复列表
     */
    @GetMapping("/replies/{parentId}")
    public TableDataInfo getReplies(@PathVariable Long parentId)
    {
        List<SysFeedback> list = sysFeedbackService.selectSysFeedbackReplies(parentId);
        return getDataTable(list);
    }

    /**
     * 回复反馈
     */
    @PreAuthorize("@ss.hasPermi('feedback:feedback:add')")
    @Log(title = "沟通反馈", businessType = BusinessType.INSERT)
    @PostMapping("/reply")
    public AjaxResult reply(@RequestBody SysFeedback sysFeedback)
    {
        // 获取当前用户信息
        SysUser currentUser = sysUserService.selectUserById(SecurityUtils.getUserId());
        sysFeedback.setSenderId(currentUser.getUserId());
        sysFeedback.setSenderName(currentUser.getNickName());
        // 获取用户角色
        String roleGroup = sysUserService.selectUserRoleGroup(currentUser.getUserName());
        sysFeedback.setSenderRole(roleGroup);
        sysFeedback.setStatus("0"); // 初始状态为未读
        return toAjax(sysFeedbackService.replySysFeedback(sysFeedback));
    }
}
