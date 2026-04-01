package com.yiyang.dashboard.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 首页仪表盘数据访问层
 *
 * @author yiyang
 */
public interface DashboardMapper {

    /**
     * 获取老人总数
     */
    Long selectElderlyCount();

    /**
     * 获取今日新增老人数
     */
    Long selectTodayElderlyCount(@Param("today") String today);

    /**
     * 获取待执行护理任务数
     */
    Long selectPendingCareCount(@Param("userId") Long userId, @Param("isCaregiver") boolean isCaregiver);

    /**
     * 获取今日已完成护理任务数
     */
    Long selectCompletedCareCount(@Param("today") String today, @Param("userId") Long userId, @Param("isCaregiver") boolean isCaregiver);

    /**
     * 获取待执行用药任务数
     */
    Long selectPendingMedicationCount(@Param("userId") Long userId, @Param("isCaregiver") boolean isCaregiver);

    /**
     * 获取今日已完成用药任务数
     */
    Long selectCompletedMedicationCount(@Param("today") String today, @Param("userId") Long userId, @Param("isCaregiver") boolean isCaregiver);

    /**
     * 获取待审核项目单数
     */
    Long selectPendingProjectOrderCount();

    /**
     * 获取待交班记录数
     */
    Long selectPendingHandoverCount(@Param("userId") Long userId);

    /**
     * 获取异常健康记录数
     */
    Long selectAbnormalHealthCount();

    /**
     * 获取今日新增留言数
     */
    Long selectTodayFeedbackCount(@Param("today") String today);

    /**
     * 获取未读留言数
     */
    Long selectUnreadFeedbackCount(@Param("userId") Long userId);

    /**
     * 获取总床位数
     */
    Long selectTotalBedCount();

    /**
     * 获取空闲床位数
     */
    Long selectFreeBedCount();
}
