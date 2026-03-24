import request from '@/utils/request'

// 查询生活照料记录列表
export function listCareRecord(query) {
  return request({
    url: '/careRecord/careRecord/list',
    method: 'get',
    params: query
  })
}

// 查询生活照料记录详细
export function getCareRecord(recordId) {
  return request({
    url: '/careRecord/careRecord/' + recordId,
    method: 'get'
  })
}

// 新增生活照料记录
export function addCareRecord(data) {
  return request({
    url: '/careRecord/careRecord',
    method: 'post',
    data: data
  })
}

// 修改生活照料记录
export function updateCareRecord(data) {
  return request({
    url: '/careRecord/careRecord',
    method: 'put',
    data: data
  })
}

// 删除生活照料记录
export function delCareRecord(recordId) {
  return request({
    url: '/careRecord/careRecord/' + recordId,
    method: 'delete'
  })
}
