import { request } from '../../utils'


export async function save (params) {
  return request({
    url: '/system/activitiTable/actModel/save',
    method: 'post',
    data: params,
  })
}

export async function query (params) {
  return request({
    url: '/system/activitiTable/actModel/list',
    method: 'get',
    data: params,
  })
}
