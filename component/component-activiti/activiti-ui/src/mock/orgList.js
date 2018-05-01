const config = require('../utils/config')
const { apiPrefix } = config
const Mock = require('mockjs')
const resCode = () => {
  let randres = {}
  let randcode = Math.random()
  if (randcode > 0.5) {
    randres.code = 200
    randres.msg = '操作成功了！恭喜你！'
  } else {
    randres.code = 201
    randres.msg = '操作失败了！再试试吧！'
  }
  return randres
}

module.exports = {
  [`POST ${apiPrefix}/user/list`] (req, res) {
    console.log(req.body)
    const dataUser = Mock.mock({
      'data|10-50': [
        {
          key: '@id',
          name: '@cname',
          loginName: '@word',
          mobilePhone: /^1[0-9]{10}$/,
          email: '@email',
          status: '@boolean',
        },
      ],
    })
    res.json(dataUser.data)
  }, [`POST ${apiPrefix}/org/list`] (req, res) {
    console.log(req.body)
    const dataUser = {
      data: [
        {
          key: 11,
          name: '人力资源',
        }, {
          key: 21,
          name: '行政管理',
        }, {
          key: 31,
          name: '技术中心',
          children: [
            {
              name: '研发部',
              key: 31001,
            }, {
              name: '项目管理部',
              key: 31002,
            },
          ],
        },
      ],
    }
    res.json(dataUser.data)
  }, [`POST ${apiPrefix}/org/save`] (req, res) {
    console.log(req.body)
    const randomData = resCode()
    const data = {
      code: randomData.code,
      msg: randomData.msg,
      data: [],
    }
    res.json(data)
  }, [`POST ${apiPrefix}/:type/del`] (req, res) {
    console.log(req.body)
    const randomData = resCode()
    const data = {
      code: randomData.code,
      msg: randomData.msg,
    }
    console.log(data)
    res.json(data)
  },
}
