import request from '@/utils/request'

// 查询服务项目库列表
export function listServiceItem(query) {
  return request({
    url: '/serviceItem/serviceItem/list',
    method: 'get',
    params: query
  })
}

// 查询服务项目库详细
export function getServiceItem(itemId) {
  return request({
    url: '/serviceItem/serviceItem/' + itemId,
    method: 'get'
  })
}

// 新增服务项目库
export function addServiceItem(data) {
  return request({
    url: '/serviceItem/serviceItem',
    method: 'post',
    data: data
  })
}

// 修改服务项目库
export function updateServiceItem(data) {
  return request({
    url: '/serviceItem/serviceItem',
    method: 'put',
    data: data
  })
}

// 删除服务项目库
export function delServiceItem(itemId) {
  return request({
    url: '/serviceItem/serviceItem/' + itemId,
    method: 'delete'
  })
}
