import request from '@/utils/request'

// 查询任务执行列表
export function listTask(query) {
  return request({
    url: '/task/task/list',
    method: 'get',
    params: query
  })
}

// 查询任务执行详细
export function getTask(taskId) {
  return request({
    url: '/task/task/' + taskId,
    method: 'get'
  })
}

// 新增任务执行
export function addTask(data) {
  return request({
    url: '/task/task',
    method: 'post',
    data: data
  })
}

// 修改任务执行
export function updateTask(data) {
  return request({
    url: '/task/task',
    method: 'put',
    data: data
  })
}

// 删除任务执行
export function delTask(taskId) {
  return request({
    url: '/task/task/' + taskId,
    method: 'delete'
  })
}
