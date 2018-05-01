const config = require('../utils/config')
const { apiPrefix } = config

const data = []
module.exports = {
  [`GET ${apiPrefix}/user`] (req, res) {
    res.json(data)
  },
}
