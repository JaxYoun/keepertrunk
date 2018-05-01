
module.exports = {
  name: 'troy',
  prefix: 'troy',
  footerText: 'troy  © 2017 Troy',
  logo: '/logo.png',
  logo2: '/logo2.png',
  iconFontCSS: '/iconfont.css',
  iconFontJS: '/iconfont.js',
  CORS: [],
  openPages: ['/login'],
  apiPrefix: '/api',
  api: {
    userLogin: '/system/login/doLogin',
    userLogout: '/system/login/loginOut',
    userInfo: '/userInfo',
    users: '/users',
    user: '/system/login/getUserByUserIdToCache',
    menus: '/system/menu/list',
    switchPost: '/system/login/switchPost',
    queryPostList: '/system/post/queryPostList',
  },
}
