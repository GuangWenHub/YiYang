import request from '@/utils/request'

// 查询用户扩展信息列表
export function listProfile(query) {
  return request({
    url: '/profile/profile/list',
    method: 'get',
    params: query
  })
}

// 查询用户扩展信息详细
export function getProfile(userId) {
  return request({
    url: '/profile/profile/' + userId,
    method: 'get'
  })
}

// 新增用户扩展信息
export function addProfile(data) {
  return request({
    url: '/profile/profile',
    method: 'post',
    data: data
  })
}

// 修改用户扩展信息
export function updateProfile(data) {
  return request({
    url: '/profile/profile',
    method: 'put',
    data: data
  })
}

// 删除用户扩展信息
export function delProfile(userId) {
  return request({
    url: '/profile/profile/' + userId,
    method: 'delete'
  })
}
