import { request, config } from '../utils'
const { api } = config
const { dashboard, mycity, myWeather } = api

export async function myCity (params) {
  return request({
    url: mycity,
    data: params,
  })
}

export async function queryWeather (params) {
  return request({
    url: myWeather,
    data: params,
  })
}

export async function query (params) {
  return request({
    url: dashboard,
    method: 'get',
    data: params,
  })
}
