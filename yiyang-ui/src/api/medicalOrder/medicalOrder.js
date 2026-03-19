import request from '@/utils/request'

// 查询医嘱开立列表
export function listMedicalOrder(query) {
  return request({
    url: '/medicalOrder/medicalOrder/list',
    method: 'get',
    params: query
  })
}

// 查询医嘱开立详细
export function getMedicalOrder(orderId) {
  return request({
    url: '/medicalOrder/medicalOrder/' + orderId,
    method: 'get'
  })
}

// 新增医嘱开立
export function addMedicalOrder(data) {
  return request({
    url: '/medicalOrder/medicalOrder',
    method: 'post',
    data: data
  })
}

// 修改医嘱开立
export function updateMedicalOrder(data) {
  return request({
    url: '/medicalOrder/medicalOrder',
    method: 'put',
    data: data
  })
}

// 删除医嘱开立
export function delMedicalOrder(orderId) {
  return request({
    url: '/medicalOrder/medicalOrder/' + orderId,
    method: 'delete'
  })
}
