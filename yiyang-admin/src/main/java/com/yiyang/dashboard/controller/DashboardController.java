package com.yiyang.dashboard.controller;

import com.yiyang.common.core.controller.BaseController;
import com.yiyang.common.core.domain.AjaxResult;
import com.yiyang.dashboard.service.IDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页仪表盘控制器
 *
 * @author yiyang
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController extends BaseController {

    @Autowired
    private IDashboardService dashboardService;

    /**
     * 获取首页统计数据
     */
    @GetMapping("/statistics")
    public AjaxResult getStatistics() {
        return AjaxResult.success(dashboardService.getStatistics());
    }

    /**
     * 获取待办事项列表
     */
    @GetMapping("/todos")
    public AjaxResult getTodoList() {
        return AjaxResult.success(dashboardService.getTodoList());
    }

    /**
     * 获取AI助手数据（预留Dify接口）
     */
    @GetMapping("/ai-assistant")
    public AjaxResult getAiAssistantData() {
        return AjaxResult.success(dashboardService.getAiAssistantData());
    }
}
