import request from '../utils/request'

export function fetch () {
  return request('/system/user')
}
