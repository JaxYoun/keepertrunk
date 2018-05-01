const config = require('../utils/config')
const { apiPrefix } = config
const Mock = require('mockjs')

module.exports = {
  [`POST ${apiPrefix}/mytest`] (req, res) {
    console.log(req.body)
    const dataUser = Mock.mock({
      'data|10-50': [
        {
          key: '@id',
          name: '@cname',
          loginName: '@word',
          mobilePhone: /^1[0-9]{10}$/,
          email: '@mail',
          status: '@boolean',
        },
      ],
    })
    res.json(dataUser.data)
  },
}
