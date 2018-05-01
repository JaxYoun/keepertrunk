import pathToRegexp from 'path-to-regexp'
import { query } from '../../services/activitiTable/actModel'
import { parse } from 'qs'

export default {

  namespace: 'actModel',

  state: {

      list: [],
    currentItem: {},
    modalVisible: false,
    modalType: 'create',
    selectedRowKeys: [],
    pagination: {
      showSizeChanger: true,
      showQuickJumper: true,
      showTotal: total => `共 ${total} 条`,
      current: 1,
      total: null,
    },
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen(location => {
          dispatch({
            type: 'query',
            payload: location.query,
          })
      })
    },

  },

  effects: {
    *query ({
      payload,
    }, { call, put }) {
      const data = yield call(query, payload)
      const { success, message, status, ...other } = data
      if (success) {
        yield put({
          type: 'querySuccess',
          payload: {
             current:  1,
             pageSize:  10,
             total: data.total,
          },
        })
      } else {
        throw data
      }
    },
  *create ({ payload }, { call, put }) {
      const data = yield call(create, payload)
      if (data.success) {
        yield put({ type: 'hideModal' })
        yield put({ type: 'query' })
      } else {
        throw data
      }
    },
  },

  reducers: {
    querySuccess (state, { payload }) {
      return {
        ...state,
        ...payload,
      }
    },
  },
  
}
