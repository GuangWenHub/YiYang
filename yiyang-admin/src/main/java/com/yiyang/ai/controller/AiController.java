package com.yiyang.ai.controller;

import com.yiyang.ai.service.IAiService;
import com.yiyang.common.core.controller.BaseController;
import com.yiyang.common.core.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * AI 控制器
 */
@Api(tags = "AI助手")
@RestController
@RequestMapping("/ai")
public class AiController extends BaseController {

    @Autowired
    private IAiService aiService;

    /**
     * 发送聊天消息
     */
    @ApiOperation("发送聊天消息")
    @PostMapping(value = "/chat", produces = "text/event-stream")
    public void chat(@RequestBody Map<String, Object> requestBody, HttpServletResponse response) throws IOException {
        System.out.println("========================================");
        System.out.println("1. 收到前端请求，请求体：" + requestBody);
        String prompt = (String) requestBody.get("prompt");
        String chatId = (String) requestBody.get("chatId");
        String userRole = (String) requestBody.get("userRole");
        String userName = (String) requestBody.get("userName");
        System.out.println("2. 提取参数，prompt: " + prompt + ", chatId: " + chatId + ", userRole: " + userRole + ", userName: " + userName);
        System.out.println("========================================");
        aiService.sendChatMessage(prompt, chatId, userRole, userName, response);
        System.out.println("6. 处理请求完成");
    }

    /**
     * 获取聊天历史列表
     */
    @ApiOperation("获取聊天历史列表")
    @GetMapping("/history")
    public AjaxResult getChatHistoryIds() {
        return success(aiService.getChatHistoryIds());
    }

    /**
     * 获取聊天历史详情
     */
    @ApiOperation("获取聊天历史详情")
    @GetMapping("/history/{chatId}")
    public AjaxResult getChatHistoryDetail(@PathVariable String chatId) {
        return success(aiService.getChatHistoryDetail(chatId));
    }

    /**
     * 删除聊天历史
     */
    @ApiOperation("删除聊天历史")
    @DeleteMapping("/history/{chatId}")
    public AjaxResult deleteChatHistory(@PathVariable String chatId) {
        aiService.deleteChatHistory(chatId);
        return success();
    }

    /**
     * 测试流式响应
     */
    @ApiOperation("测试流式响应")
    @GetMapping(value = "/test-stream", produces = "text/event-stream")
    public void testStream(HttpServletResponse response) throws IOException, InterruptedException {
        System.out.println("测试流式响应开始");
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");
        
        PrintWriter writer = response.getWriter();
        
        for (int i = 1; i <= 5; i++) {
            String msg = "这是第" + i + "个字";
            writer.write("data: " + msg + "\n\n");
            writer.flush();
            System.out.println("发送: " + msg);
            Thread.sleep(500); // 模拟延迟
        }
        
        writer.write("data: [DONE]\n\n");
        writer.flush();
        System.out.println("测试流式响应结束");
    }
}
