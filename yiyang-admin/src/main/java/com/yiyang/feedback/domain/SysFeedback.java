package com.yiyang.feedback.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yiyang.common.annotation.Excel;
import com.yiyang.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 沟通反馈对象 sys_feedback
 * 
 * @author ruoyi
 * @date 2026-03-30
 */
public class SysFeedback extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 反馈ID */
    private Long feedbackId;

    /** 发送者ID */
    @Excel(name = "发送者ID")
    private Long senderId;

    /** 发送者名称 */
    @Excel(name = "发送者名称")
    private String senderName;

    /** 发送者角色 */
    @Excel(name = "发送者角色")
    private String senderRole;

    /** 接收者ID（null表示全体成员） */
    @Excel(name = "接收者ID")
    private Long receiverId;

    /** 接收者名称 */
    @Excel(name = "接收者名称")
    private String receiverName;

    /** 接收者角色 */
    @Excel(name = "接收者角色")
    private String receiverRole;

    /** 类型（0-留言，1-建议） */
    @Excel(name = "类型", readConverterExp = "0=留言,1=建议")
    private String type;

    /** 内容 */
    @Excel(name = "内容")
    private String content;

    /** 状态（0-未读，1-已读，2-已回复，3-有回复未读） */
    @Excel(name = "状态", readConverterExp = "0=未读,1=已读,2=已回复,3=有回复未读")
    private String status;

    /** 父反馈ID，用于回复功能 */
    private Long parentId;

    /** 回复列表 */
    private List<SysFeedback> replies;

    public void setFeedbackId(Long feedbackId)
    {
        this.feedbackId = feedbackId;
    }

    public Long getFeedbackId()
    {
        return feedbackId;
    }
    public void setSenderId(Long senderId)
    {
        this.senderId = senderId;
    }

    public Long getSenderId()
    {
        return senderId;
    }
    public void setSenderName(String senderName)
    {
        this.senderName = senderName;
    }

    public String getSenderName()
    {
        return senderName;
    }
    public void setSenderRole(String senderRole)
    {
        this.senderRole = senderRole;
    }

    public String getSenderRole()
    {
        return senderRole;
    }
    public void setReceiverId(Long receiverId)
    {
        this.receiverId = receiverId;
    }

    public Long getReceiverId()
    {
        return receiverId;
    }
    public void setReceiverName(String receiverName)
    {
        this.receiverName = receiverName;
    }

    public String getReceiverName()
    {
        return receiverName;
    }
    public void setReceiverRole(String receiverRole)
    {
        this.receiverRole = receiverRole;
    }

    public String getReceiverRole()
    {
        return receiverRole;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public Long getParentId()
    {
        return parentId;
    }
    public void setReplies(List<SysFeedback> replies)
    {
        this.replies = replies;
    }

    public List<SysFeedback> getReplies()
    {
        return replies;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("feedbackId", getFeedbackId())
            .append("senderId", getSenderId())
            .append("senderName", getSenderName())
            .append("senderRole", getSenderRole())
            .append("receiverId", getReceiverId())
            .append("receiverName", getReceiverName())
            .append("receiverRole", getReceiverRole())
            .append("type", getType())
            .append("content", getContent())
            .append("status", getStatus())
            .append("parentId", getParentId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
