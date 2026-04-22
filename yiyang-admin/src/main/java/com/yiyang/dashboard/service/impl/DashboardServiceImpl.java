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
        boolean isAdmin2 = false;
        
        try {
            userId = SecurityUtils.getUserId();
            isCaregiver = SecurityUtils.hasRole("caregiver");
            isDoctor = SecurityUtils.hasRole("doctor");
            isAdmin = SecurityUtils.hasRole("admin");
            isAdmin2 = SecurityUtils.hasRole("admin2");
        } catch (Exception e) {
            // 用户未登录或权限信息不完整，使用默认值
        }
        
        // 获取今日日期字符串
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        
        // 老人统计（管理员、管理员2、医生、护工可见）
        if (isAdmin || isAdmin2 || isDoctor || isCaregiver) {
            statistics.setElderlyCount(dashboardMapper.selectElderlyCount());
            statistics.setTodayElderlyCount(dashboardMapper.selectTodayElderlyCount(today));
        }
        
        // 护理任务统计（护工看自己的，管理员/管理员2看全部）
        if (isCaregiver) {
            statistics.setPendingCareCount(dashboardMapper.selectPendingCareCount(userId, true));
            statistics.setCompletedCareCount(dashboardMapper.selectCompletedCareCount(today, userId, true));
        }
        if (isAdmin || isAdmin2) {
            statistics.setPendingCareCount(dashboardMapper.selectTodayAllPendingCareCount(today));
            statistics.setCompletedCareCount(dashboardMapper.selectTodayAllCompletedCareCount(today));
        }
        
        // 用药任务统计（护工看自己的，管理员/管理员2看全部）
        if (isCaregiver) {
            statistics.setPendingMedicationCount(dashboardMapper.selectPendingMedicationCount(userId, true));
            statistics.setCompletedMedicationCount(dashboardMapper.selectCompletedMedicationCount(today, userId, true));
        }
        if (isAdmin || isAdmin2) {
            statistics.setPendingMedicationCount(dashboardMapper.selectTodayAllPendingMedicationCount(today));
            statistics.setCompletedMedicationCount(dashboardMapper.selectTodayAllCompletedMedicationCount(today));
        }
        
        // 项目单审核统计（医生、管理员、管理员2可见）
        if (isDoctor || isAdmin || isAdmin2) {
            statistics.setPendingProjectOrderCount(dashboardMapper.selectPendingProjectOrderCount());
        }
        
        // 交班记录统计（医生可见）
        if (isDoctor) {
            statistics.setPendingHandoverCount(dashboardMapper.selectPendingHandoverCount(userId));
        }
        
        // 健康记录统计（医生、管理员、管理员2可见）
        if (isDoctor || isAdmin || isAdmin2) {
            statistics.setAbnormalHealthCount(dashboardMapper.selectAbnormalHealthCount());
        }
        
        // 留言统计（所有用户）
        statistics.setTodayFeedbackCount(dashboardMapper.selectTodayFeedbackCount(today));
        statistics.setUnreadFeedbackCount(dashboardMapper.selectUnreadFeedbackCount(userId));
        // 床位统计（管理员、管理员2可见）
        if (isAdmin || isAdmin2) {
            Long totalBeds = dashboardMapper.selectTotalBedCount();
            Long freeBeds = dashboardMapper.selectFreeBedCount();
            statistics.setTotalBedCount(totalBeds);
            statistics.setFreeBedCount(freeBeds);
            if (totalBeds > 0) {
                statistics.setBedUsageRate((double) (totalBeds - freeBeds) / totalBeds * 100);
            }
        }
        
        // 超时和异常统计（护工看自己的，管理员/管理员2看全部）
        if (isCaregiver) {
            statistics.setTimeoutCareCount(dashboardMapper.selectTimeoutCareCount(userId, true));
            statistics.setAbnormalCareCount(dashboardMapper.selectAbnormalCareCount(userId, true));
            statistics.setTimeoutMedicationCount(dashboardMapper.selectTimeoutMedicationCount(userId, true));
            statistics.setAbnormalMedicationCount(dashboardMapper.selectAbnormalMedicationCount(userId, true));
        }
        if (isAdmin || isAdmin2) {
            statistics.setTimeoutCareCount(dashboardMapper.selectAllTimeoutCareCount());
            statistics.setAbnormalCareCount(dashboardMapper.selectAllAbnormalCareCount());
            statistics.setTimeoutMedicationCount(dashboardMapper.selectAllTimeoutMedicationCount());
            statistics.setAbnormalMedicationCount(dashboardMapper.selectAllAbnormalMedicationCount());
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
        Long userId = null;
        String userName = "用户";
        boolean isCaregiver = false;
        boolean isDoctor = false;
        boolean isAdmin = false;
        boolean isAdmin2 = false;
        
        try {
            SysUser user = SecurityUtils.getLoginUser().getUser();
            userName = user.getNickName() != null ? user.getNickName() : user.getUserName();
            userId = SecurityUtils.getUserId();
            isCaregiver = SecurityUtils.hasRole("caregiver");
            isDoctor = SecurityUtils.hasRole("doctor");
            isAdmin = SecurityUtils.hasRole("admin");
            isAdmin2 = SecurityUtils.hasRole("admin2");
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
        
        aiData.setWelcomeMessage(greeting + "，" + userName + "！我是您的工作助手，很高兴为您服务。");
        
        // 获取统计数据，动态生成建议
        List<AiAssistantData.AiSuggestion> suggestions = new ArrayList<>();
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        
        // 护理任务超时建议（护工、管理员）
        if (isCaregiver || isAdmin || isAdmin2) {
            Long timeoutCare = isCaregiver ? dashboardMapper.selectTimeoutCareCount(userId, true) : dashboardMapper.selectAllTimeoutCareCount();
            if (timeoutCare != null && timeoutCare > 0) {
                AiAssistantData.AiSuggestion suggestion = new AiAssistantData.AiSuggestion();
                suggestion.setType("warning");
                suggestion.setTitle("生活照料超时提醒");
                suggestion.setContent("您有" + timeoutCare + "个生活照料任务已超时，请尽快处理！");
                suggestion.setAction("查看生活照料");
                suggestion.setRouteKey("careRecord");
                suggestions.add(suggestion);
            }
        }
        
        // 用药任务超时建议（护工、管理员）
        if (isCaregiver || isAdmin || isAdmin2) {
            Long timeoutMed = isCaregiver ? dashboardMapper.selectTimeoutMedicationCount(userId, true) : dashboardMapper.selectAllTimeoutMedicationCount();
            if (timeoutMed != null && timeoutMed > 0) {
                AiAssistantData.AiSuggestion suggestion = new AiAssistantData.AiSuggestion();
                suggestion.setType("warning");
                suggestion.setTitle("用药任务超时提醒");
                suggestion.setContent("您有" + timeoutMed + "个用药任务已超时，请立即处理！");
                suggestion.setAction("查看用药跟踪");
                suggestion.setRouteKey("medicationRecord");
                suggestions.add(suggestion);
            }
        }
        
        // 待执行护理任务建议（护工）
        if (isCaregiver) {
            Long pendingCare = dashboardMapper.selectPendingCareCount(userId, true);
            if (pendingCare != null && pendingCare > 0) {
                AiAssistantData.AiSuggestion suggestion = new AiAssistantData.AiSuggestion();
                suggestion.setType("task");
                suggestion.setTitle("今日待执行护理");
                suggestion.setContent("您有" + pendingCare + "个护理任务待执行，请及时完成。");
                suggestion.setAction("查看任务");
                suggestion.setRouteKey("careRecord");
                suggestions.add(suggestion);
            }
        }
        
        // 管理员今日全部待执行任务
        if (isAdmin || isAdmin2) {
            Long allPendingCare = dashboardMapper.selectTodayAllPendingCareCount(today);
            if (allPendingCare != null && allPendingCare > 0) {
                AiAssistantData.AiSuggestion suggestion = new AiAssistantData.AiSuggestion();
                suggestion.setType("task");
                suggestion.setTitle("今日待执行护理");
                suggestion.setContent("今日共有" + allPendingCare + "个护理任务待执行，请关注任务进度。");
                suggestion.setAction("查看任务");
                suggestion.setRouteKey("careRecord");
                suggestions.add(suggestion);
            }
        }
        
        // 待执行用药任务建议（护工）
        if (isCaregiver) {
            Long pendingMed = dashboardMapper.selectPendingMedicationCount(userId, true);
            if (pendingMed != null && pendingMed > 0) {
                AiAssistantData.AiSuggestion suggestion = new AiAssistantData.AiSuggestion();
                suggestion.setType("task");
                suggestion.setTitle("今日待执行用药");
                suggestion.setContent("您有" + pendingMed + "个用药任务待执行，请按时完成。");
                suggestion.setAction("查看任务");
                suggestion.setRouteKey("medicationRecord");
                suggestions.add(suggestion);
            }
        }
        
        // 管理员今日全部待执行用药
        if (isAdmin || isAdmin2) {
            Long allPendingMed = dashboardMapper.selectTodayAllPendingMedicationCount(today);
            if (allPendingMed != null && allPendingMed > 0) {
                AiAssistantData.AiSuggestion suggestion = new AiAssistantData.AiSuggestion();
                suggestion.setType("task");
                suggestion.setTitle("今日待执行用药");
                suggestion.setContent("今日共有" + allPendingMed + "个用药任务待执行，请督促护工完成。");
                suggestion.setAction("查看任务");
                suggestion.setRouteKey("medicationRecord");
                suggestions.add(suggestion);
            }
        }
        
        // 生活照料异常建议（护工、管理员）
        if (isCaregiver || isAdmin || isAdmin2) {
            Long abnormalCare = isCaregiver ? dashboardMapper.selectAbnormalCareCount(userId, true) : dashboardMapper.selectAllAbnormalCareCount();
            if (abnormalCare != null && abnormalCare > 0) {
                AiAssistantData.AiSuggestion suggestion = new AiAssistantData.AiSuggestion();
                suggestion.setType("warning");
                suggestion.setTitle("生活照料异常");
                suggestion.setContent("有" + abnormalCare + "个生活照料任务记录为异常状态，请核实处理。");
                suggestion.setAction("查看详情");
                suggestion.setRouteKey("careRecord");
                suggestions.add(suggestion);
            }
        }
        
        // 用药异常建议（护工、管理员）
        if (isCaregiver || isAdmin || isAdmin2) {
            Long abnormalMed = isCaregiver ? dashboardMapper.selectAbnormalMedicationCount(userId, true) : dashboardMapper.selectAllAbnormalMedicationCount();
            if (abnormalMed != null && abnormalMed > 0) {
                AiAssistantData.AiSuggestion suggestion = new AiAssistantData.AiSuggestion();
                suggestion.setType("warning");
                suggestion.setTitle("用药异常");
                suggestion.setContent("有" + abnormalMed + "个用药任务记录为异常状态，请核实处理。");
                suggestion.setAction("查看详情");
                suggestion.setRouteKey("medicationRecord");
                suggestions.add(suggestion);
            }
        }
        
        // 待审核项目单（医生、管理员）
        if (isDoctor || isAdmin || isAdmin2) {
            Long pendingOrders = dashboardMapper.selectPendingProjectOrderCount();
            if (pendingOrders != null && pendingOrders > 0) {
                AiAssistantData.AiSuggestion suggestion = new AiAssistantData.AiSuggestion();
                suggestion.setType("review");
                suggestion.setTitle("项目单待审核");
                suggestion.setContent("有" + pendingOrders + "个项目单等待审核，请及时处理。");
                suggestion.setAction("审核项目单");
                suggestion.setRouteKey("projectOrder");
                suggestions.add(suggestion);
            }
        }
        
        // 待交班记录（医生）
        if (isDoctor) {
            Long pendingHandover = dashboardMapper.selectPendingHandoverCount(userId);
            if (pendingHandover != null && pendingHandover > 0) {
                AiAssistantData.AiSuggestion suggestion = new AiAssistantData.AiSuggestion();
                suggestion.setType("handover");
                suggestion.setTitle("交班提醒");
                suggestion.setContent("您有" + pendingHandover + "个交班记录待处理，请及时完成交班。");
                suggestion.setAction("查看交班");
                suggestion.setRouteKey("handover");
                suggestions.add(suggestion);
            }
        }
        
        // 异常健康记录（医生、管理员）
        if (isDoctor || isAdmin || isAdmin2) {
            Long abnormalHealth = dashboardMapper.selectAbnormalHealthCount();
            if (abnormalHealth != null && abnormalHealth > 0) {
                AiAssistantData.AiSuggestion suggestion = new AiAssistantData.AiSuggestion();
                suggestion.setType("health");
                suggestion.setTitle("异常健康记录");
                suggestion.setContent("有" + abnormalHealth + "条异常健康记录，请关注老人健康状况。");
                suggestion.setAction("查看健康档案");
                suggestion.setRouteKey("healthRecord");
                suggestions.add(suggestion);
            }
        }
        
        // 未读留言
        Long unreadFeedback = dashboardMapper.selectUnreadFeedbackCount(userId);
        if (unreadFeedback != null && unreadFeedback > 0) {
            AiAssistantData.AiSuggestion suggestion = new AiAssistantData.AiSuggestion();
            suggestion.setType("message");
            suggestion.setTitle("未读留言");
            suggestion.setContent("您有" + unreadFeedback + "条未读留言，请查看。");
            suggestion.setAction("查看留言");
            suggestion.setRouteKey("feedback");
            suggestions.add(suggestion);
        }
        
        // 如果没有任何建议，显示默认建议
        if (suggestions.isEmpty()) {
            AiAssistantData.AiSuggestion suggestion = new AiAssistantData.AiSuggestion();
            suggestion.setType("general");
            suggestion.setTitle("工作正常");
            suggestion.setContent("当前所有任务均在正常进行中，继续加油！");
            suggestion.setAction("了解更多");
            suggestions.add(suggestion);
        }
        
        aiData.setSuggestions(suggestions);
        
        // Dify配置（预留）
        AiAssistantData.DifyConfig difyConfig = new AiAssistantData.DifyConfig();
        difyConfig.setApiUrl("https://api.dify.ai/v1");
        difyConfig.setEnabled(false); // 默认不启用，需要配置API Key后启用
        aiData.setDifyConfig(difyConfig);
        
        return aiData;
    }
}
