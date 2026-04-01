package com.yiyang.dashboard.domain;

import java.io.Serializable;

/**
 * 首页统计数据
 *
 * @author yiyang
 */
public class DashboardStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 老人总数 */
    private Long elderlyCount;

    /** 今日新增老人数 */
    private Long todayElderlyCount;

    /** 待执行护理任务数 */
    private Long pendingCareCount;

    /** 今日已完成护理任务数 */
    private Long completedCareCount;

    /** 待执行用药任务数 */
    private Long pendingMedicationCount;

    /** 今日已完成用药任务数 */
    private Long completedMedicationCount;

    /** 待审核项目单数 */
    private Long pendingProjectOrderCount;

    /** 待交班记录数 */
    private Long pendingHandoverCount;

    /** 异常健康记录数 */
    private Long abnormalHealthCount;

    /** 今日新增留言数 */
    private Long todayFeedbackCount;

    /** 未读留言数 */
    private Long unreadFeedbackCount;

    /** 床位使用率 */
    private Double bedUsageRate;

    /** 空闲床位数 */
    private Long freeBedCount;

    /** 总床位数 */
    private Long totalBedCount;

    // Getters and Setters
    public Long getElderlyCount() {
        return elderlyCount;
    }

    public void setElderlyCount(Long elderlyCount) {
        this.elderlyCount = elderlyCount;
    }

    public Long getTodayElderlyCount() {
        return todayElderlyCount;
    }

    public void setTodayElderlyCount(Long todayElderlyCount) {
        this.todayElderlyCount = todayElderlyCount;
    }

    public Long getPendingCareCount() {
        return pendingCareCount;
    }

    public void setPendingCareCount(Long pendingCareCount) {
        this.pendingCareCount = pendingCareCount;
    }

    public Long getCompletedCareCount() {
        return completedCareCount;
    }

    public void setCompletedCareCount(Long completedCareCount) {
        this.completedCareCount = completedCareCount;
    }

    public Long getPendingMedicationCount() {
        return pendingMedicationCount;
    }

    public void setPendingMedicationCount(Long pendingMedicationCount) {
        this.pendingMedicationCount = pendingMedicationCount;
    }

    public Long getCompletedMedicationCount() {
        return completedMedicationCount;
    }

    public void setCompletedMedicationCount(Long completedMedicationCount) {
        this.completedMedicationCount = completedMedicationCount;
    }

    public Long getPendingProjectOrderCount() {
        return pendingProjectOrderCount;
    }

    public void setPendingProjectOrderCount(Long pendingProjectOrderCount) {
        this.pendingProjectOrderCount = pendingProjectOrderCount;
    }

    public Long getPendingHandoverCount() {
        return pendingHandoverCount;
    }

    public void setPendingHandoverCount(Long pendingHandoverCount) {
        this.pendingHandoverCount = pendingHandoverCount;
    }

    public Long getAbnormalHealthCount() {
        return abnormalHealthCount;
    }

    public void setAbnormalHealthCount(Long abnormalHealthCount) {
        this.abnormalHealthCount = abnormalHealthCount;
    }

    public Long getTodayFeedbackCount() {
        return todayFeedbackCount;
    }

    public void setTodayFeedbackCount(Long todayFeedbackCount) {
        this.todayFeedbackCount = todayFeedbackCount;
    }

    public Long getUnreadFeedbackCount() {
        return unreadFeedbackCount;
    }

    public void setUnreadFeedbackCount(Long unreadFeedbackCount) {
        this.unreadFeedbackCount = unreadFeedbackCount;
    }

    public Double getBedUsageRate() {
        return bedUsageRate;
    }

    public void setBedUsageRate(Double bedUsageRate) {
        this.bedUsageRate = bedUsageRate;
    }

    public Long getFreeBedCount() {
        return freeBedCount;
    }

    public void setFreeBedCount(Long freeBedCount) {
        this.freeBedCount = freeBedCount;
    }

    public Long getTotalBedCount() {
        return totalBedCount;
    }

    public void setTotalBedCount(Long totalBedCount) {
        this.totalBedCount = totalBedCount;
    }
}
