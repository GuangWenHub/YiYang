package com.yiyang.ai.mapper;

import com.yiyang.ai.domain.SysChatHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 聊天历史Mapper
 */
@Mapper
public interface SysChatHistoryMapper {
    /**
     * 根据会话ID查询聊天历史
     */
    List<SysChatHistory> selectHistoryByChatId(@Param("chatId") String chatId);

    /**
     * 新增聊天历史
     */
    int insertHistory(SysChatHistory history);

    /**
     * 根据会话ID删除聊天历史
     */
    int deleteHistoryByChatId(@Param("chatId") String chatId);
}