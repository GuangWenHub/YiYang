package com.yiyang.task.mapper;

import java.util.List;
import com.yiyang.task.domain.Task;

/**
 * 任务执行Mapper接口
 * 
 * @author GuangWenLI
 * @date 2025-12-18
 */
public interface TaskMapper 
{
    /**
     * 查询任务执行
     * 
     * @param taskId 任务执行主键
     * @return 任务执行
     */
    public Task selectTaskByTaskId(Long taskId);

    /**
     * 查询任务执行列表
     * 
     * @param task 任务执行
     * @return 任务执行集合
     */
    public List<Task> selectTaskList(Task task);

    /**
     * 新增任务执行
     * 
     * @param task 任务执行
     * @return 结果
     */
    public int insertTask(Task task);

    /**
     * 修改任务执行
     * 
     * @param task 任务执行
     * @return 结果
     */
    public int updateTask(Task task);

    /**
     * 删除任务执行
     * 
     * @param taskId 任务执行主键
     * @return 结果
     */
    public int deleteTaskByTaskId(Long taskId);

    /**
     * 批量删除任务执行
     * 
     * @param taskIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTaskByTaskIds(Long[] taskIds);
}
