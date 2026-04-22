package com.yiyang.ai.controller;

import com.yiyang.ai.service.IAiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * AI 助手 Controller
 */
@Api(tags = "AI 助手")
@RestController
@RequestMapping("/ai")
public class AiController {

    @Autowired
    private IAiService aiService;

    /**
     * 发送聊天消息
     */
    @ApiOperation("发送聊天消息")
    @PostMapping(value = "/chat", produces = "text/event-stream")
    public void chat(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("========================================");
        System.out.println("1. 收到前端请求，请求体：" + requestBody);
        String prompt = (String) requestBody.get("prompt");
        String chatId = (String) requestBody.get("chatId");
        String userRole = (String) requestBody.get("userRole");
        String userName = (String) requestBody.get("userName");
        String jwtToken = request.getHeader("Authorization");
        System.out.println("2. 提取参数，prompt: " + prompt + ", chatId: " + chatId + ", userRole: " + userRole + ", userName: " + userName);
        System.out.println("2.1 获取 JWT Token: " + jwtToken);
        System.out.println("========================================");
        aiService.sendChatMessage(prompt, chatId, userRole, userName, jwtToken, response);
        System.out.println("6. 处理请求完成");
    }

    /**
     * 获取聊天历史列表
     */
    @ApiOperation("获取聊天历史列表")
    @GetMapping("/history")
    public List<Map<String, Object>> getChatHistoryIds() {
        System.out.println("获取聊天历史列表");
        return aiService.getChatHistoryIds();
    }

    /**
     * 获取聊天历史详情
     */
    @ApiOperation("获取聊天历史详情")
    @GetMapping("/history/{chatId}")
    public List<Map<String, Object>> getChatHistoryDetail(@PathVariable String chatId) {
        System.out.println("获取聊天历史详情，chatId: " + chatId);
        return aiService.getChatHistoryDetail(chatId);
    }

    /**
     * 删除聊天历史
     */
    @ApiOperation("删除聊天历史")
    @DeleteMapping("/history/{chatId}")
    public int deleteChatHistory(@PathVariable String chatId) {
        System.out.println("删除聊天历史，chatId: " + chatId);
        return aiService.deleteChatHistory(chatId);
    }
}

