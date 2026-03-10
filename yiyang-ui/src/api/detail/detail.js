import request from '@/utils/request'

// 查询项目单明细列表
export function listDetail(query) {
  return request({
    url: '/detail/detail/list',
    method: 'get',
    params: query
  })
}

// 查询项目单明细详细
export function getDetail(detailId) {
  return request({
    url: '/detail/detail/' + detailId,
    method: 'get'
  })
}

// 新增项目单明细
export function addDetail(data) {
  return request({
    url: '/detail/detail',
    method: 'post',
    data: data
  })
}

// 修改项目单明细
export function updateDetail(data) {
  return request({
    url: '/detail/detail',
    method: 'put',
    data: data
  })
}

// 删除项目单明细
export function delDetail(detailId) {
  return request({
    url: '/detail/detail/' + detailId,
    method: 'delete'
  })
}
