import request from '@/utils/request'
import { getToken } from '@/utils/auth'

// 发送聊天消息 (使用原生 fetch 以支持流式响应)
export function sendChatMessage({ prompt, chatId, userRole, userName }) {
  // 获取 token - 使用若依框架的标准方法
  const token = getToken()
  
  if (!token) {
    console.error('未找到认证 Token,请先登录')
    return Promise.reject(new Error('未登录'))
  }
  
  // 将角色数组转换为逗号分隔的字符串 (例如：'admin,caregiver')
  const roleString = Array.isArray(userRole) ? userRole.join(',') : (userRole || '')
  
  console.log('发送请求到:', process.env.VUE_APP_BASE_API + '/ai/chat')
  console.log('Token:', token ? token.substring(0, 20) + '...' : '无')
  console.log('用户角色数组:', userRole)
  console.log('用户角色字符串:', roleString)
  console.log('用户名:', userName)
  
  return fetch(process.env.VUE_APP_BASE_API + '/ai/chat', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + token
    },
    body: JSON.stringify({
      prompt,
      chatId,
      userRole: roleString,  // 使用转换后的字符串
      userName
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