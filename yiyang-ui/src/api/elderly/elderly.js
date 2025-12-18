import request from '@/utils/request'

// 查询老人档案列表
export function listElderly(query) {
  return request({
    url: '/elderly/elderly/list',
    method: 'get',
    params: query
  })
}

// 查询老人档案详细
export function getElderly(elderlyId) {
  return request({
    url: '/elderly/elderly/' + elderlyId,
    method: 'get'
  })
}

// 新增老人档案
export function addElderly(data) {
  return request({
    url: '/elderly/elderly',
    method: 'post',
    data: data
  })
}

// 修改老人档案
export function updateElderly(data) {
  return request({
    url: '/elderly/elderly',
    method: 'put',
    data: data
  })
}

// 删除老人档案
export function delElderly(elderlyId) {
  return request({
    url: '/elderly/elderly/' + elderlyId,
    method: 'delete'
  })
}
