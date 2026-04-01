package com.yiyang.dashboard.domain;

import java.io.Serializable;
import java.util.List;

/**
 * AI助手数据（预留Dify接口）
 *
 * @author yiyang
 */
public class AiAssistantData implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 欢迎语 */
    private String welcomeMessage;

    /** 当前时间 */
    private String currentTime;

    /** AI建议列表 */
    private List<AiSuggestion> suggestions;

    /** Dify API配置（预留） */
    private DifyConfig difyConfig;

    /**
     * AI建议
     */
    public static class AiSuggestion implements Serializable {
        private static final long serialVersionUID = 1L;

        /** 建议类型 */
        private String type;

        /** 建议标题 */
        private String title;

        /** 建议内容 */
        private String content;

        /** 相关操作 */
        private String action;

        // Getters and Setters
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }

    /**
     * Dify配置（预留）
     */
    public static class DifyConfig implements Serializable {
        private static final long serialVersionUID = 1L;

        /** API地址 */
        private String apiUrl;

        /** API密钥（后端存储，不返回给前端） */
        private String apiKey;

        /** 是否启用 */
        private Boolean enabled;

        // Getters and Setters
        public String getApiUrl() {
            return apiUrl;
        }

        public void setApiUrl(String apiUrl) {
            this.apiUrl = apiUrl;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }
    }

    // Getters and Setters
    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public List<AiSuggestion> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<AiSuggestion> suggestions) {
        this.suggestions = suggestions;
    }

    public DifyConfig getDifyConfig() {
        return difyConfig;
    }

    public void setDifyConfig(DifyConfig difyConfig) {
        this.difyConfig = difyConfig;
    }
}
