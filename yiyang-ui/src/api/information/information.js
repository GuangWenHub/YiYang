import request from '@/utils/request'

// 查询养老院基本信息列表
export function listInformation(query) {
  return request({
    url: '/information/information/list',
    method: 'get',
    params: query
  })
}

// 查询养老院基本信息详细
export function getInformation(id) {
  return request({
    url: '/information/information/' + id,
    method: 'get'
  })
}

// 新增养老院基本信息
export function addInformation(data) {
  return request({
    url: '/information/information',
    method: 'post',
    data: data
  })
}

// 修改养老院基本信息
export function updateInformation(data) {
  return request({
    url: '/information/information',
    method: 'put',
    data: data
  })
}

// 删除养老院基本信息
export function delInformation(id) {
  return request({
    url: '/information/information/' + id,
    method: 'delete'
  })
}
