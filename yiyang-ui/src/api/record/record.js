import request from '@/utils/request'

// 查询健康档案记录列表
export function listRecord(query) {
  return request({
    url: '/record/record/list',
    method: 'get',
    params: query
  })
}

// 查询健康档案记录详细
export function getRecord(recordId) {
  return request({
    url: '/record/record/' + recordId,
    method: 'get'
  })
}

// 新增健康档案记录
export function addRecord(data) {
  return request({
    url: '/record/record',
    method: 'post',
    data: data
  })
}

// 修改健康档案记录
export function updateRecord(data) {
  return request({
    url: '/record/record',
    method: 'put',
    data: data
  })
}

// 删除健康档案记录
export function delRecord(recordId) {
  return request({
    url: '/record/record/' + recordId,
    method: 'delete'
  })
}
