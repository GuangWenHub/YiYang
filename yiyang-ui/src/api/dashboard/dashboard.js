import request from '@/utils/request'

// 获取首页统计数据
export function getStatistics() {
  return request({
    url: '/dashboard/statistics',
    method: 'get'
  })
}

// 获取待办事项列表
export function getTodoList() {
  return request({
    url: '/dashboard/todos',
    method: 'get'
  })
}

// 获取AI助手数据
export function getAiAssistantData() {
  return request({
    url: '/dashboard/ai-assistant',
    method: 'get'
  })
}
