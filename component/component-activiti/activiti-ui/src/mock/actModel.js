const Mock = require('mockjs')
const config = require('../utils/config')
const { apiPrefix } = config
const qs = require('qs')

const TableListData = Mock.mock({
  'actModelList|11-30': [
    { key: '@id(1)',
      'id|+1': 1,
      name: '@name',
      'version|200-500': 1,
      createTime: '@date("yyyy-MM-dd")',
    },
  ],
  'actDefineList|11-30': [
    { key: '@id(1)',
      'id|+1': 1,
      name: '@name',
      'version|200-500.3': 1,
      'resourceName': function () {
        let name = Mock.mock('@word')
        return (`${name}.xml`)
      },
      'deployId|+1': 100,
      'dgrmResourceName': function () {
        let name = Mock.mock('@word')
        return (`${name}.png`)
      },
      deployTime: '@date("yyyy-MM-dd")',
      'suspensionState|1-2': true,
      'suspensionStateName': function () {
        return this.suspensionState ? '激活' : '挂起'
      },
    },
  ],
})

let randomSuccess = (words, rdata) => {
  let responsS = {
    msg: `${words}成功`,
    code: 200,
    data: rdata,
  }
  let responsE = {
    msg: `${words}失败`,
    code: '400',
  }
  let R = Math.random()
  let respons = R < 0.5 ? responsS : responsE
  return respons
}
let { actModelList, actDefineList } = TableListData

module.exports = {
  [`POST ${apiPrefix}/api/actModel/queryModel`] (req, res) {
    const { body } = req
    let { pageSize, page, ...other } = body
    // pageSize = pageSize || 10
    // page = page || 1
    let newData = actModelList
    let startTime = {}.hasOwnProperty.call(other, 'startTime') ? other.startTime : false
    let endTime = {}.hasOwnProperty.call(other, 'endTime') ? other.endTime : false
    startTime && endTime ? other.createTime = [startTime, endTime] : true
    for (let key in other) {
      // 遍历请求参数
      if ({}.hasOwnProperty.call(other, key)) {
        newData = newData.filter((item) => { // 遍历数据
          if ({}.hasOwnProperty.call(item, key)) {
            if (key === 'createTime') {
              const start = new Date(other[key][0]).getTime()
              const end = new Date(other[key][1]).getTime()
              const now = new Date(item[key]).getTime()
              if (start && end) {
                return now >= start && now <= end
              }
              return true
            }
            return String(item[key]).trim().indexOf(decodeURI(other[key]).trim()) > -1
          }
          return true
        })
      }
    }

    // 假装有删除
    let R = Math.random()
    R < 0.5 ? newData.slice(1) : false
    console.log(R)
    // newData[' page'] = page
    // newData[' pageSize'] = pageSize
    // res.status(200).json({
    //   data: newData.slice((page - 1) * pageSize, page * pageSize)
    // })
    res.status(200).json({ data: newData })
  },
  [`POST ${apiPrefix}/api/actModel/delModel`] (req, res) {
    const { body } = req
    let { key } = body
    console.log(key)
    let data = randomSuccess('删除', { key: key })
    res.status(200).json(data)
  },
  [`POST ${apiPrefix}/api/actModel/deploy`] (req, res) {
    const { body } = req
    let { key } = body
    console.log(key)
    let data = randomSuccess('部署', { key: key })
    res.status(200).json(data)
  },
  [`POST ${apiPrefix}/api/actModel/newModel`] (req, res) {
    const { body } = req
    console.log(body)
    let date = { id: 1 }
    let respons = randomSuccess('创建', date)
    res.status(200).json(respons)
  },
  [`POST ${apiPrefix}/api/actModel/export`] (req, res) {
    const { body } = req
    let { key } = body
    console.log(key)
    let data = randomSuccess('下载', { key: key })
    res.status(200).json(data)
  },
  [`POST ${apiPrefix}/api/actDefine/queryProcess`] (req, res) {
    const { body } = req
    let { pageSize, page, ...other } = body

    let newData = actDefineList
    let startTime = {}.hasOwnProperty.call(other, 'startTime') ? other.startTime : false
    let endTime = {}.hasOwnProperty.call(other, 'endTime') ? other.endTime : false
    startTime && endTime ? other.deployTime = [startTime, endTime] : true
    for (let key in other) {
      // 遍历请求参数
      if ({}.hasOwnProperty.call(other, key)) {
        newData = newData.filter((item) => { // 遍历数据
          if ({}.hasOwnProperty.call(item, key)) {
            if (key === 'deployTime') {
              const start = new Date(other[key][0]).getTime()
              const end = new Date(other[key][1]).getTime()
              const now = new Date(item[key]).getTime()
              if (start && end) {
                return now >= start && now <= end
              }
              return true
            }
            return String(item[key]).trim().indexOf(decodeURI(other[key]).trim()) > -1
          }
          return true
        })
      }
    }
    // 假装有删除
    let R = Math.random()
    R < 0.5 ? newData.slice(1) : false
    console.log(R)
    res.status(200).json({ data: newData })
  },
  [`POST ${apiPrefix}/api/actDefine/activate`] (req, res) {
    const { body } = req
    console.log(body)
    let date = { ProcDefId: 1 }
    let respons = randomSuccess('激活', date)
    res.status(200).json(respons)
  },
  [`POST ${apiPrefix}/api/actDefine/suspend`] (req, res) {
    const { body } = req
    console.log(body)
    let date = { ProcDefId: 1 }
    let respons = randomSuccess('挂起', date)
    res.status(200).json(respons)
  },
  [`POST ${apiPrefix}/workbench/queryHighRiskWarnCount`] (req, res) {
    res.status(200).json(Mock.mock({ 'count|1-300': 150 }))
  },
  [`POST ${apiPrefix}/workbench/queryHighRiskHoleCount`] (req, res) {
    res.status(200).json(Mock.mock({ 'count|1-300': 150 }))
  },
  [`POST ${apiPrefix}/workbench/queryBacklogPlanWorkCount`] (req, res) {
    res.status(200).json(Mock.mock({ 'count|1-300': 150 }))
  },
  [`POST ${apiPrefix}/workbench/queryBacklogOperationWorkCount`] (req, res) {
    res.status(200).json(Mock.mock({ 'count|1-300': 150 }))
  },
}
