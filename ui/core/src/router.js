import React from 'react'
import PropTypes from 'prop-types'
import { Router } from 'dva/router'
import App from './routes/app'
import troyAdmin from 'troy-admin'


let registerModel = (app, model) => {
  if (!(app._models.filter(m => m.namespace === model.namespace).length === 1)) {
    app.model(model)
  }
}

const Routers = function ({ history, app }) {
  let routes = [
    {
      path: '/',
      component: App,
      getIndexRoute (nextState, cb) {
        require.ensure([], require => {
          cb(null, { component: require('./routes/index/') })
        }, 'index')
      },
      childRoutes: [
        {
          path: 'login',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/login'))
              cb(null, require('./routes/login/'))
            }, 'login')
          },
        }, {
          path: '*',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              cb(null, require('./routes/error/'))
            }, 'error')
          },
        },
      ],
    },
  ]

  // *****************加载路由 troy-admin *****************//
  for (let item of troyAdmin.routes.values()) {
    let childRoute = {
      path: `${troyAdmin.path}/${item}`,
      getComponent (nextState, cb) {
        require.ensure([], require => {
          registerModel(app, require(`troy-admin/lib/models/${item}.js`).default)
          cb(null, require(`troy-admin/lib/routes/${item}/index.js`).default)
        }, '')
      },
    }
    routes[0].childRoutes.splice(-1, 0, childRoute)
  }
  // *****************加载路由 troy-admin *****************//

  return <Router history={history} routes={routes} />
}

Routers.propTypes = {
  history: PropTypes.object,
  app: PropTypes.object,
}

export default Routers
