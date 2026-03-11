import request from '@/utils/request'

// 查询查房记录列表
export function listRound(query) {
  return request({
    url: '/round/round/list',
    method: 'get',
    params: query
  })
}

// 查询查房记录详细
export function getRound(roundId) {
  return request({
    url: '/round/round/' + roundId,
    method: 'get'
  })
}

// 新增查房记录
export function addRound(data) {
  return request({
    url: '/round/round',
    method: 'post',
    data: data
  })
}

// 修改查房记录
export function updateRound(data) {
  return request({
    url: '/round/round',
    method: 'put',
    data: data
  })
}

// 删除查房记录
export function delRound(roundId) {
  return request({
    url: '/round/round/' + roundId,
    method: 'delete'
  })
}
