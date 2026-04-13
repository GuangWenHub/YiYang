package com.yiyang.ai.mapper;

import com.yiyang.ai.domain.SysChatSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 聊天会话 Mapper
 */
@Mapper
public interface SysChatSessionMapper {
    /**
     * 查询所有会话
     */
    List<SysChatSession> selectAllSessions();

    /**
     * 根据用户 ID 查询会话
     */
    List<SysChatSession> selectSessionsByUserId(@Param("userId") Long userId);

    /**
     * 根据 ID 查询会话
     */
    SysChatSession selectSessionById(@Param("id") String id);

    /**
     * 新增会话
     */
    int insertSession(SysChatSession session);

    /**
     * 更新会话
     */
    int updateSession(SysChatSession session);

    /**
     * 删除会话
     */
    int deleteSession(@Param("id") String id);
}