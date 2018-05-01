const config = require('../utils/config')
const { apiPrefix } = config

const Menu = {
  mytext: {
    name: 'n',
    title: 'i',
    content: 'hao',
  },
  mytitle: {
    name: 'w',
    title: 'o',
    content: 'hao',
  },
}
module.exports = {
  [`GET ${apiPrefix}/menu/list`] (req, res) {
    res.json(Menu)
  },
}
