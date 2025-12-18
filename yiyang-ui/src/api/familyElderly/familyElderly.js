import request from '@/utils/request'

// 查询家属-老人关系列表
export function listFamilyElderly(query) {
  return request({
    url: '/familyElderly/familyElderly/list',
    method: 'get',
    params: query
  })
}

// 查询家属-老人关系详细
export function getFamilyElderly(id) {
  return request({
    url: '/familyElderly/familyElderly/' + id,
    method: 'get'
  })
}

// 新增家属-老人关系
export function addFamilyElderly(data) {
  return request({
    url: '/familyElderly/familyElderly',
    method: 'post',
    data: data
  })
}

// 修改家属-老人关系
export function updateFamilyElderly(data) {
  return request({
    url: '/familyElderly/familyElderly',
    method: 'put',
    data: data
  })
}

// 删除家属-老人关系
export function delFamilyElderly(id) {
  return request({
    url: '/familyElderly/familyElderly/' + id,
    method: 'delete'
  })
}
