package com.yiyang.dashboard.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 待办事项
 *
 * @author yiyang
 */
public class TodoItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 待办ID */
    private Long id;

    /** 待办类型：care-护理任务, medication-用药任务, handover-交班, projectOrder-项目单审核, feedback-留言回复 */
    private String type;

    /** 类型名称 */
    private String typeName;

    /** 待办标题 */
    private String title;

    /** 待办描述 */
    private String description;

    /** 关联老人姓名 */
    private String elderlyName;

    /** 截止时间 */
    private Date deadline;

    /** 优先级：high-高, medium-中, low-低 */
    private String priority;

    /** 跳转路由 */
    private String route;

    /** 关联记录ID */
    private Long recordId;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getElderlyName() {
        return elderlyName;
    }

    public void setElderlyName(String elderlyName) {
        this.elderlyName = elderlyName;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }
}
