import request from '@/utils/request'

// 查询项目单主列表
export function listProjectOrder(query) {
  return request({
    url: '/projectOrder/projectOrder/list',
    method: 'get',
    params: query
  })
}

// 查询项目单主详细
export function getProjectOrder(orderId) {
  return request({
    url: '/projectOrder/projectOrder/' + orderId,
    method: 'get'
  })
}

// 新增项目单主
export function addProjectOrder(data) {
  return request({
    url: '/projectOrder/projectOrder',
    method: 'post',
    data: data
  })
}

// 修改项目单主
export function updateProjectOrder(data) {
  return request({
    url: '/projectOrder/projectOrder',
    method: 'put',
    data: data
  })
}

// 删除项目单主
export function delProjectOrder(orderId) {
  return request({
    url: '/projectOrder/projectOrder/' + orderId,
    method: 'delete'
  })
}
