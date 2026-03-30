import request from '@/utils/request'

// 查询沟通反馈列表
export function listFeedback(query) {
  return request({
    url: '/feedback/feedback/list',
    method: 'get',
    params: query
  })
}

// 查询用户收到的反馈
export function getReceivedFeedback() {
  return request({
    url: '/feedback/feedback/my/received',
    method: 'get'
  })
}

// 查询用户发送的反馈
export function getSentFeedback() {
  return request({
    url: '/feedback/feedback/my/sent',
    method: 'get'
  })
}

// 查询所有反馈（管理员使用）
export function getAllFeedback() {
  return request({
    url: '/feedback/feedback/all',
    method: 'get'
  })
}

// 查询沟通反馈详细
export function getFeedback(feedbackId) {
  return request({
    url: '/feedback/feedback/' + feedbackId,
    method: 'get'
  })
}

// 新增沟通反馈
export function addFeedback(data) {
  return request({
    url: '/feedback/feedback',
    method: 'post',
    data: data
  })
}

// 修改沟通反馈
export function updateFeedback(data) {
  return request({
    url: '/feedback/feedback',
    method: 'put',
    data: data
  })
}

// 删除沟通反馈
export function delFeedback(feedbackId) {
  return request({
    url: '/feedback/feedback/' + feedbackId,
    method: 'delete'
  })
}

// 更新沟通反馈状态
export function updateFeedbackStatus(feedbackId, status) {
  return request({
    url: '/feedback/feedback/status/' + feedbackId,
    method: 'put',
    data: status
  })
}

// 查询反馈的回复列表
export function getFeedbackReplies(parentId) {
  return request({
    url: '/feedback/feedback/replies/' + parentId,
    method: 'get'
  })
}

// 回复反馈
export function replyFeedback(data) {
  return request({
    url: '/feedback/feedback/reply',
    method: 'post',
    data: data
  })
}
