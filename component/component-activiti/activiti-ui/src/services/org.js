import { request } from '../utils'


export async function save (params) {
  return request({
    url: '/system/org/save',
    method: 'post',
    data: params,
  })
}

export async function query (params) {
  return request({
    url: '/system/org/list',
    method: 'post',
    data: params,
  })
}
