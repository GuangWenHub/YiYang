import request from '@/utils/request'

// 查询房间信息列表
export function listRoom(query) {
  return request({
    url: '/room/room/list',
    method: 'get',
    params: query
  })
}

// 查询房间信息详细
export function getRoom(roomId) {
  return request({
    url: '/room/room/' + roomId,
    method: 'get'
  })
}

// 新增房间信息
export function addRoom(data) {
  return request({
    url: '/room/room',
    method: 'post',
    data: data
  })
}

// 修改房间信息
export function updateRoom(data) {
  return request({
    url: '/room/room',
    method: 'put',
    data: data
  })
}

// 删除房间信息
export function delRoom(roomId) {
  return request({
    url: '/room/room/' + roomId,
    method: 'delete'
  })
}
