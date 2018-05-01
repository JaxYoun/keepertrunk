import React from 'react'
import { connect } from 'dva'
import { table } from 'antd'
const columns = [
  {
    title: 'ss',
    dataIndex: 'name',
  }, {
    title: 'yy',
    dataIndex: 'id',
  },
]
const data = []
function Demo () {
  return (<div>
    <table columns={columns} datasource={data} />
  </div>
  )
}

export default connect()(Demo)

