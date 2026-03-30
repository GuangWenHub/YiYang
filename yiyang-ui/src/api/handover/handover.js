import request from '@/utils/request'

// 查询医生交班记录列表
export function listHandover(query) {
  return request({
    url: '/handover/handover/list',
    method: 'get',
    params: query
  })
}

// 查询医生交班记录详细
export function getHandover(handoverId) {
  return request({
    url: '/handover/handover/' + handoverId,
    method: 'get'
  })
}

// 新增医生交班记录
export function addHandover(data) {
  return request({
    url: '/handover/handover',
    method: 'post',
    data: data
  })
}

// 修改医生交班记录
export function updateHandover(data) {
  return request({
    url: '/handover/handover',
    method: 'put',
    data: data
  })
}

// 删除医生交班记录
export function delHandover(handoverId) {
  return request({
    url: '/handover/handover/' + handoverId,
    method: 'delete'
  })
}
