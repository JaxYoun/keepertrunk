const path = require('path')
const express = require('express')
const app = express()
const indexRouter = require('./routes/index')
const proxy = require('http-proxy-middleware')
const config = require('./routes/config')

// 设置存放模板文件的目录
app.set('views', path.join(__dirname, 'views'))
// 设置模板引擎为 ejs
app.set('view engine', 'ejs')

// 静态资源
app.use('/', express.static(path.join(__dirname, 'dist')))


// 路由配置

app.use('/api', proxy({target: config.proxy,
onProxyReq:function(proxyReq, req, res) {
  // add custom header to request
  //console.log(proxyReq);
 // proxyReq.setHeader('x-added', 'foobar');
  // or log the req
}
, changeOrigin: true}))
app.use('/', indexRouter)


// 404
app.use((req, res) => {
  if (!res.headersSent) {
    res.render('404')
  }
})

// 监听端口
app.listen(config.port)
