const path = require('path')
const express = require('express')
const app = express()
const ejs = require('ejs')
const indexRouter = require('./server/index')
const config = require('./server/config')

// 模板配置
app.set('views', path.join(__dirname, '_site'))
app.engine('.html', ejs.__express);
app.set('view engine', 'html')

// 静态资源配置
app.use('/', express.static(path.join(__dirname, '_site')))


// 路由配置
app.use('/', indexRouter)

// 404
app.use('*', (req, res) => {
  if (!res.headersSent) {
    res.render('404')
  }
})

// 监听端口
app.listen(config.port)
