import request from '@/utils/request'
import { getToken } from '@/utils/auth'

// 发送聊天消息(使用原生fetch以支持流式响应)
export function sendChatMessage({ prompt, chatId }) {
  // 获取token - 使用若依框架的标准方法
  const token = getToken()
  
  if (!token) {
    console.error('未找到认证Token,请先登录')
    return Promise.reject(new Error('未登录'))
  }
  
  console.log('发送请求到:', process.env.VUE_APP_BASE_API + '/ai/chat')
  console.log('Token:', token ? token.substring(0, 20) + '...' : '无')
  
  return fetch(process.env.VUE_APP_BASE_API + '/ai/chat', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + token
    },
    body: JSON.stringify({
      prompt,
      chatId
    })
  })
}

// 获取聊天历史列表
export function getChatHistoryIds() {
  return request({
    url: '/ai/history',
    method: 'get'
  })
}

// 获取聊天历史详情
export function getChatHistoryDetail(chatId) {
  return request({
    url: `/ai/history/${chatId}`,
    method: 'get'
  })
}

// 删除聊天历史
export function deleteChatHistory(chatId) {
  return request({
    url: `/ai/history/${chatId}`,
    method: 'delete'
  })
}