package com.yiyang.dashboard.service;

import com.yiyang.dashboard.domain.AiAssistantData;
import com.yiyang.dashboard.domain.DashboardStatistics;
import com.yiyang.dashboard.domain.TodoItem;

import java.util.List;

/**
 * 首页仪表盘服务接口
 *
 * @author yiyang
 */
public interface IDashboardService {

    /**
     * 获取首页统计数据
     *
     * @return 统计数据
     */
    DashboardStatistics getStatistics();

    /**
     * 获取待办事项列表
     *
     * @return 待办事项列表
     */
    List<TodoItem> getTodoList();

    /**
     * 获取AI助手数据（预留Dify接口）
     *
     * @return AI助手数据
     */
    AiAssistantData getAiAssistantData();
}
