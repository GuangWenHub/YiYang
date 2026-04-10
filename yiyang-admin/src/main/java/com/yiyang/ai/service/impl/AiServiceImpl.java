package com.yiyang.ai.service.impl;

import com.yiyang.ai.service.IAiService;
import com.yiyang.ai.domain.SysChatSession;
import com.yiyang.ai.domain.SysChatHistory;
import com.yiyang.ai.mapper.SysChatSessionMapper;
import com.yiyang.ai.mapper.SysChatHistoryMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AI 服务实现
 */
@Service
public class AiServiceImpl implements IAiService {

    @Value("${dify.api.url:https://api.dify.ai/v1}")
    private String difyApiUrl;

    @Value("${dify.api.key:}")
    private String difyApiKey;

    @Autowired
    private SysChatSessionMapper sysChatSessionMapper;

    @Autowired
    private SysChatHistoryMapper sysChatHistoryMapper;

    private final ObjectMapper objectMapper;

    public AiServiceImpl() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void sendChatMessage(String prompt, String chatId, HttpServletResponse response) throws IOException {
        System.out.println("========================================");
        System.out.println("3. 开始处理聊天消息，prompt: " + prompt + ", chatId: " + chatId);
        
        // 设置响应头
        response.setContentType("text/event-stream;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("X-Accel-Buffering", "no"); // 禁用nginx缓冲

        // 创建或获取会话ID
        boolean isNewConversation = (chatId == null || chatId.isEmpty());
        final boolean finalIsNewConversation = isNewConversation; // 用于lambda表达式
        
        if (isNewConversation) {
            System.out.println("4. 创建新会话，不传递conversation_id");
        } else {
            System.out.println("4. 使用现有会话ID: " + chatId);
        }

        // 构建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("inputs", new HashMap<>());
        requestBody.put("query", prompt);
        requestBody.put("response_mode", "streaming");
        requestBody.put("user", "system_user");
        
        if (!isNewConversation) {
            requestBody.put("conversation_id", chatId);
        }
        
        requestBody.put("files", new ArrayList<>());
        String jsonBody = objectMapper.writeValueAsString(requestBody);
        System.out.println("5. 构建Dify API请求体: " + jsonBody);

        final String finalChatId = chatId;
        final String finalPrompt = prompt;
        final String[] conversationIdHolder = new String[1];
        final StringBuilder aiResponseBuilder = new StringBuilder();

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        
        try {
            // 创建连接
            URL url = new URL(difyApiUrl + "/chat-messages");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + difyApiKey);
            connection.setRequestProperty("Accept", "text/event-stream");
            
            // 发送请求
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
                os.flush();
            }
            
            System.out.println("6. Dify API响应码: " + connection.getResponseCode());
            
            // 读取流式响应
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String line;
            
            while ((line = reader.readLine()) != null) {
                System.out.println("7. 收到Dify API响应: " + line);
                
                if (line.startsWith("data: ")) {
                    String data = line.substring(6);
                    
                    // 写入前端
                    response.getWriter().write(line + "\n\n");
                    response.getWriter().flush();
                    System.out.println("8. 已转发到前端");
                    
                    // 如果是新会话，提取conversation_id
                    if (finalIsNewConversation && conversationIdHolder[0] == null && !data.equals("[DONE]")) {
                        try {
                            Map<String, Object> eventData = objectMapper.readValue(data, Map.class);
                            if (eventData.containsKey("conversation_id")) {
                                conversationIdHolder[0] = (String) eventData.get("conversation_id");
                                if (conversationIdHolder[0] != null && !conversationIdHolder[0].isEmpty()) {
                                    response.setHeader("X-Chat-Id", conversationIdHolder[0]);
                                    System.out.println("8.1 从 API响应获取conversation_id: " + conversationIdHolder[0]);
                                }
                            }
                        } catch (Exception e) {
                            // 解析失败，忽略
                        }
                    }
                    
                    // 提取AI回复内容
                    if (!data.equals("[DONE]")) {
                        try {
                            Map<String, Object> eventData = objectMapper.readValue(data, Map.class);
                            // 检查是否是OpenAI格式的响应
                            if (eventData.containsKey("choices")) {
                                List<Map<String, Object>> choices = (List<Map<String, Object>>) eventData.get("choices");
                                if (!choices.isEmpty()) {
                                    Map<String, Object> choice = choices.get(0);
                                    if (choice.containsKey("delta")) {
                                        Map<String, Object> delta = (Map<String, Object>) choice.get("delta");
                                        if (delta.containsKey("content")) {
                                            String text = (String) delta.get("content");
                                            if (text != null) {
                                                aiResponseBuilder.append(text);
                                            }
                                        }
                                    }
                                }
                            }
                            // 检查是否是Dify格式的响应
                            else if (eventData.containsKey("text")) {
                                String text = (String) eventData.get("text");
                                aiResponseBuilder.append(text);
                            }
                            // 检查是否是Dify的answer字段（优先使用）
                            else if (eventData.containsKey("answer")) {
                                String answer = (String) eventData.get("answer");
                                aiResponseBuilder.append(answer);
                            }
                            // 检查是否是Dify的content字段
                            else if (eventData.containsKey("content")) {
                                String content = (String) eventData.get("content");
                                aiResponseBuilder.append(content);
                            }
                            // 检查是否是Dify的thought字段（最后使用）
                            else if (eventData.containsKey("thought")) {
                                String thought = (String) eventData.get("thought");
                                aiResponseBuilder.append(thought);
                            }
                        } catch (Exception e) {
                            // 解析失败，忽略
                        }
                    }
                }
            }
            
            System.out.println("12. Dify API请求完成");
            
            // 确定使用哪个chatId保存历史
            String saveChatId = finalIsNewConversation && conversationIdHolder[0] != null ? conversationIdHolder[0] : finalChatId;
            if (saveChatId != null && !saveChatId.isEmpty()) {
                // 去除重复内容
                String responseText = aiResponseBuilder.toString();
                // 简单的去重逻辑：如果内容包含重复的段落，只保留一个
                if (responseText.length() > 100) {
                    // 检查是否有重复的段落
                    int mid = responseText.length() / 2;
                    String firstHalf = responseText.substring(0, mid);
                    String secondHalf = responseText.substring(mid);
                    if (secondHalf.contains(firstHalf)) {
                        responseText = secondHalf;
                    }
                }
                saveChatHistory(saveChatId, finalPrompt, responseText);
                System.out.println("13. 保存聊天历史，chatId: " + saveChatId);
            }
            
        } catch (Exception e) {
            System.err.println("14. 错误: " + e.getMessage());
            e.printStackTrace();
            
            try {
                response.getWriter().write("data: {\"error\": \"" + escapeJson(e.getMessage()) + "\"}\n\n");
                response.getWriter().write("data: [DONE]\n\n");
                response.getWriter().flush();
            } catch (Exception ex) {
                System.err.println("发送错误信息失败: " + ex.getMessage());
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Override
    public List<Map<String, Object>> getChatHistoryIds() {
        List<Map<String, Object>> historyList = new ArrayList<>();
        
        // 从数据库查询所有会话
        List<SysChatSession> sessions = sysChatSessionMapper.selectAllSessions();
        for (SysChatSession session : sessions) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", session.getId());
            item.put("name", session.getSessionName());
            historyList.add(item);
        }
        
        return historyList;
    }

    @Override
    public List<Map<String, Object>> getChatHistoryDetail(String chatId) {
        List<Map<String, Object>> historyList = new ArrayList<>();
        
        // 从数据库查询聊天历史
        List<SysChatHistory> histories = sysChatHistoryMapper.selectHistoryByChatId(chatId);
        for (SysChatHistory history : histories) {
            Map<String, Object> item = new HashMap<>();
            if (history.getQuery() != null) {
                item.put("query", history.getQuery());
            }
            if (history.getAnswer() != null) {
                item.put("answer", history.getAnswer());
            }
            historyList.add(item);
        }
        
        return historyList;
    }

    @Override
    public void deleteChatHistory(String chatId) {
        // 从数据库删除聊天会话（级联删除聊天历史）
        sysChatSessionMapper.deleteSession(chatId);
    }

    /**
     * 保存聊天历史
     */
    private void saveChatHistory(String chatId, String prompt, String response) {
        // 检查会话是否存在
        SysChatSession session = sysChatSessionMapper.selectSessionById(chatId);
        if (session == null) {
            // 创建新会话
            session = new SysChatSession();
            session.setId(chatId);
            // 使用第一条消息作为会话名称
            String sessionName = prompt.length() > 20 ? prompt.substring(0, 20) + "..." : prompt;
            session.setSessionName(sessionName);
            sysChatSessionMapper.insertSession(session);
        }
        
        // 保存用户消息和AI回复
        SysChatHistory history = new SysChatHistory();
        history.setChatId(chatId);
        history.setQuery(prompt);
        history.setAnswer(response);
        sysChatHistoryMapper.insertHistory(history);
    }

    /**
     * 转义JSON字符串
     */
    private String escapeJson(String input) {
        return input
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
