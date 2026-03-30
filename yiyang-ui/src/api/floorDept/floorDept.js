import request from '@/utils/request'

// 查询楼层科室管理列表
export function listFloorDept(query) {
  return request({
    url: '/floorDept/floorDept/list',
    method: 'get',
    params: query
  })
}

// 查询楼层科室管理详细
export function getFloorDept(floorDeptId) {
  return request({
    url: '/floorDept/floorDept/' + floorDeptId,
    method: 'get'
  })
}

// 新增楼层科室管理
export function addFloorDept(data) {
  return request({
    url: '/floorDept/floorDept',
    method: 'post',
    data: data
  })
}

// 修改楼层科室管理
export function updateFloorDept(data) {
  return request({
    url: '/floorDept/floorDept',
    method: 'put',
    data: data
  })
}

// 删除楼层科室管理
export function delFloorDept(floorDeptId) {
  return request({
    url: '/floorDept/floorDept/' + floorDeptId,
    method: 'delete'
  })
}
