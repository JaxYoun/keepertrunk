import { save } from '../services/org'

export default {
  namespace: 'org',
  state: {},
  reducers: {
    hideModal (state) {
      return { ...state, visilbe: false }
    },
  },
  effects: {
    *save ({ payload }, { put, call }) {
      const data = yield call(save, payload)
      if (data.success) {
        yield put({ type: 'hideModal' })
      } else {
        throw data
      }
    },
  },
}
