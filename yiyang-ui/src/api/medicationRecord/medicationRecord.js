import request from '@/utils/request'

// 查询用药跟踪列表
export function listMedicationRecord(query) {
  return request({
    url: '/medicationRecord/medicationRecord/list',
    method: 'get',
    params: query
  })
}

// 查询用药跟踪详细
export function getMedicationRecord(recordId) {
  return request({
    url: '/medicationRecord/medicationRecord/' + recordId,
    method: 'get'
  })
}

// 新增用药跟踪
export function addMedicationRecord(data) {
  return request({
    url: '/medicationRecord/medicationRecord',
    method: 'post',
    data: data
  })
}

// 修改用药跟踪
export function updateMedicationRecord(data) {
  return request({
    url: '/medicationRecord/medicationRecord',
    method: 'put',
    data: data
  })
}

// 删除用药跟踪
export function delMedicationRecord(recordId) {
  return request({
    url: '/medicationRecord/medicationRecord/' + recordId,
    method: 'delete'
  })
}
