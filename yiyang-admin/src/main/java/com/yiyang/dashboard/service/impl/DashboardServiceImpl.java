package com.yiyang.dashboard.service.impl;

import com.yiyang.common.core.domain.entity.SysUser;
import com.yiyang.common.utils.SecurityUtils;
import com.yiyang.dashboard.domain.AiAssistantData;
import com.yiyang.dashboard.domain.DashboardStatistics;
import com.yiyang.dashboard.domain.TodoItem;
import com.yiyang.dashboard.mapper.DashboardMapper;
import com.yiyang.dashboard.service.IDashboardService;
import com.yiyang.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 首页仪表盘服务实现
 *
 * @author yiyang
 */
@Service
public class DashboardServiceImpl implements IDashboardService {

    @Autowired
    private DashboardMapper dashboardMapper;

    @Autowired
    private ISysUserService userService;

    @Override
    public DashboardStatistics getStatistics() {
        DashboardStatistics statistics = new DashboardStatistics();
        
        // 获取当前用户信息
        Long userId = null;
        boolean isCaregiver = false;
        boolean isDoctor = false;
        boolean isAdmin = false;
        
        try {
            userId = SecurityUtils.getUserId();
            isCaregiver = SecurityUtils.hasRole("caregiver");
            isDoctor = SecurityUtils.hasRole("doctor");
            isAdmin = SecurityUtils.hasRole("admin");
        } catch (Exception e) {
            // 用户未登录或权限信息不完整，使用默认值
        }
        
        // 获取今日日期字符串
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        
        // 老人统计（管理员和医生可见）
        if (isAdmin || isDoctor) {
            statistics.setElderlyCount(dashboardMapper.selectElderlyCount());
            statistics.setTodayElderlyCount(dashboardMapper.selectTodayElderlyCount(today));
        }
        
        // 护理任务统计（护工、医生、管理员可见）
        if (isCaregiver || isDoctor || isAdmin) {
            statistics.setPendingCareCount(dashboardMapper.selectPendingCareCount(userId, isCaregiver));
            statistics.setCompletedCareCount(dashboardMapper.selectCompletedCareCount(today, userId, isCaregiver));
        }
        
        // 用药任务统计（护工、医生、管理员可见）
        if (isCaregiver || isDoctor || isAdmin) {
            statistics.setPendingMedicationCount(dashboardMapper.selectPendingMedicationCount(userId, isCaregiver));
            statistics.setCompletedMedicationCount(dashboardMapper.selectCompletedMedicationCount(today, userId, isCaregiver));
        }
        
        // 项目单审核统计（医生、管理员可见）
        if (isDoctor || isAdmin) {
            statistics.setPendingProjectOrderCount(dashboardMapper.selectPendingProjectOrderCount());
        }
        
        // 交班记录统计（医生可见）
        if (isDoctor) {
            statistics.setPendingHandoverCount(dashboardMapper.selectPendingHandoverCount(userId));
        }
        
        // 健康记录统计（医生、管理员可见）
        if (isDoctor || isAdmin) {
            statistics.setAbnormalHealthCount(dashboardMapper.selectAbnormalHealthCount());
        }
        
        // 留言统计（所有用户）
        statistics.setTodayFeedbackCount(dashboardMapper.selectTodayFeedbackCount(today));
        statistics.setUnreadFeedbackCount(dashboardMapper.selectUnreadFeedbackCount(userId));
        
        // 床位统计（管理员可见）
        if (isAdmin) {
            Long totalBeds = dashboardMapper.selectTotalBedCount();
            Long freeBeds = dashboardMapper.selectFreeBedCount();
            statistics.setTotalBedCount(totalBeds);
            statistics.setFreeBedCount(freeBeds);
            if (totalBeds > 0) {
                statistics.setBedUsageRate((double) (totalBeds - freeBeds) / totalBeds * 100);
            }
        }
        
        return statistics;
    }

    @Override
    public List<TodoItem> getTodoList() {
        List<TodoItem> todoList = new ArrayList<>();
        
        // 获取当前用户信息
        Long userId = null;
        boolean isCaregiver = false;
        boolean isDoctor = false;
        boolean isAdmin = false;
        
        try {
            userId = SecurityUtils.getUserId();
            isCaregiver = SecurityUtils.hasRole("caregiver");
            isDoctor = SecurityUtils.hasRole("doctor");
            isAdmin = SecurityUtils.hasRole("admin");
        } catch (Exception e) {
            // 用户未登录或权限信息不完整，使用默认值
            return todoList;
        }
        
        // 根据角色获取待办事项
        // 这里简化处理，实际可以从数据库查询
        
        // 护工待办
        if (isCaregiver) {
            TodoItem careTodo = new TodoItem();
            careTodo.setId(1L);
            careTodo.setType("care");
            careTodo.setTypeName("护理任务");
            careTodo.setTitle("待执行的护理任务");
            careTodo.setDescription("您有" + dashboardMapper.selectPendingCareCount(userId, true) + "个待执行的护理任务");
            careTodo.setPriority("high");
            todoList.add(careTodo);
            
            TodoItem medicationTodo = new TodoItem();
            medicationTodo.setId(2L);
            medicationTodo.setType("medication");
            medicationTodo.setTypeName("用药任务");
            medicationTodo.setTitle("待执行的用药任务");
            medicationTodo.setDescription("您有" + dashboardMapper.selectPendingMedicationCount(userId, true) + "个待执行的用药任务");
            medicationTodo.setPriority("high");
            todoList.add(medicationTodo);
        }
        
        // 医生待办
        if (isDoctor) {
            Long pendingHandover = dashboardMapper.selectPendingHandoverCount(userId);
            if (pendingHandover > 0) {
                TodoItem handoverTodo = new TodoItem();
                handoverTodo.setId(3L);
                handoverTodo.setType("handover");
                handoverTodo.setTypeName("交班记录");
                handoverTodo.setTitle("待处理的交班记录");
                handoverTodo.setDescription("您有" + pendingHandover + "条待处理的交班记录");
                handoverTodo.setPriority("medium");
                todoList.add(handoverTodo);
            }
            
            Long pendingOrders = dashboardMapper.selectPendingProjectOrderCount();
            if (pendingOrders > 0) {
                TodoItem orderTodo = new TodoItem();
                orderTodo.setId(4L);
                orderTodo.setType("projectOrder");
                orderTodo.setTypeName("项目单审核");
                orderTodo.setTitle("待审核的项目单");
                orderTodo.setDescription("您有" + pendingOrders + "个项目单待审核");
                orderTodo.setPriority("medium");
                todoList.add(orderTodo);
            }
        }
        
        // 所有用户的留言待办
        Long unreadFeedback = dashboardMapper.selectUnreadFeedbackCount(userId);
        if (unreadFeedback > 0) {
            TodoItem feedbackTodo = new TodoItem();
            feedbackTodo.setId(5L);
            feedbackTodo.setType("feedback");
            feedbackTodo.setTypeName("留言回复");
            feedbackTodo.setTitle("未读留言");
            feedbackTodo.setDescription("您有" + unreadFeedback + "条未读留言");
            feedbackTodo.setPriority("low");
            todoList.add(feedbackTodo);
        }
        
        return todoList;
    }

    @Override
    public AiAssistantData getAiAssistantData() {
        AiAssistantData aiData = new AiAssistantData();
        
        // 获取当前用户信息
        String userName = "用户";
        boolean isCaregiver = false;
        boolean isDoctor = false;
        boolean isAdmin = false;
        
        try {
            SysUser user = SecurityUtils.getLoginUser().getUser();
            userName = user.getNickName() != null ? user.getNickName() : user.getUserName();
            isCaregiver = SecurityUtils.hasRole("caregiver");
            isDoctor = SecurityUtils.hasRole("doctor");
            isAdmin = SecurityUtils.hasRole("admin");
        } catch (Exception e) {
            // 用户未登录或权限信息不完整，使用默认值
        }
        
        // 设置欢迎语
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        String currentTime = sdf.format(new Date());
        aiData.setCurrentTime(currentTime);
        
        // 根据时间设置不同的欢迎语
        int hour = new Date().getHours();
        String greeting;
        if (hour < 6) {
            greeting = "凌晨好";
        } else if (hour < 9) {
            greeting = "早上好";
        } else if (hour < 12) {
            greeting = "上午好";
        } else if (hour < 14) {
            greeting = "中午好";
        } else if (hour < 18) {
            greeting = "下午好";
        } else {
            greeting = "晚上好";
        }
        
        aiData.setWelcomeMessage(greeting + "，" + userName + "！我是您的智能助手，很高兴为您服务。");
        
        // 设置AI建议列表（模拟数据，后续可对接Dify）
        List<AiAssistantData.AiSuggestion> suggestions = new ArrayList<>();
        
        if (isCaregiver) {
            AiAssistantData.AiSuggestion suggestion1 = new AiAssistantData.AiSuggestion();
            suggestion1.setType("task");
            suggestion1.setTitle("今日护理任务提醒");
            suggestion1.setContent("建议您优先处理高优先级的护理任务，确保老人得到及时照料。");
            suggestion1.setAction("查看任务");
            suggestions.add(suggestion1);
            
            AiAssistantData.AiSuggestion suggestion2 = new AiAssistantData.AiSuggestion();
            suggestion2.setType("health");
            suggestion2.setTitle("老人健康关注");
            suggestion2.setContent("近期有老人健康指标异常，请特别关注血压和血糖数据。");
            suggestion2.setAction("查看健康档案");
            suggestions.add(suggestion2);
        }
        
        if (isDoctor) {
            AiAssistantData.AiSuggestion suggestion3 = new AiAssistantData.AiSuggestion();
            suggestion3.setType("medical");
            suggestion3.setTitle("医嘱审核提醒");
            suggestion3.setContent("有新的医嘱需要您审核，请及时处理。");
            suggestion3.setAction("审核医嘱");
            suggestions.add(suggestion3);
            
            AiAssistantData.AiSuggestion suggestion4 = new AiAssistantData.AiSuggestion();
            suggestion4.setType("handover");
            suggestion4.setTitle("交班事项");
            suggestion4.setContent("请查看待处理的交班记录，了解前一班次的情况。");
            suggestion4.setAction("查看交班");
            suggestions.add(suggestion4);
        }
        
        if (isAdmin) {
            AiAssistantData.AiSuggestion suggestion5 = new AiAssistantData.AiSuggestion();
            suggestion5.setType("system");
            suggestion5.setTitle("系统运行状态");
            try {
                suggestion5.setContent("系统运行正常，今日新增" + dashboardMapper.selectTodayElderlyCount(new SimpleDateFormat("yyyy-MM-dd").format(new Date())) + "位老人入住。");
            } catch (Exception e) {
                suggestion5.setContent("系统运行正常");
            }
            suggestion5.setAction("查看详情");
            suggestions.add(suggestion5);
        }
        
        // 通用建议
        AiAssistantData.AiSuggestion suggestion6 = new AiAssistantData.AiSuggestion();
        suggestion6.setType("general");
        suggestion6.setTitle("工作建议");
        suggestion6.setContent("保持良好的工作状态，及时记录工作日志，有助于提高工作效率。");
        suggestion6.setAction("了解更多");
        suggestions.add(suggestion6);
        
        aiData.setSuggestions(suggestions);
        
        // Dify配置（预留）
        AiAssistantData.DifyConfig difyConfig = new AiAssistantData.DifyConfig();
        difyConfig.setApiUrl("https://api.dify.ai/v1");
        difyConfig.setEnabled(false); // 默认不启用，需要配置API Key后启用
        aiData.setDifyConfig(difyConfig);
        
        return aiData;
    }
}
