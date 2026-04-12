package com.yiyang.ai.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * AI 服务接口
 */
public interface IAiService {

    /**
     * 发送聊天消息
     */
    void sendChatMessage(String prompt, String chatId, String userRole, String userName, HttpServletResponse response) throws IOException;

    /**
     * 获取聊天历史列表
     */
    List<Map<String, Object>> getChatHistoryIds();

    /**
     * 获取聊天历史详情
     */
    List<Map<String, Object>> getChatHistoryDetail(String chatId);

    /**
     * 删除聊天历史
     */
    void deleteChatHistory(String chatId);
}
