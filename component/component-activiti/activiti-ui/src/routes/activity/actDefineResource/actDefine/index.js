import React from 'react'
import { Row, Col, Card, Table } from 'antd'
import Switch from './Switch'
import Filter from './Filter'

const config = require('../../../../utils/config')
const { activeUrl } = config

const urlObj = {
  query: `${activeUrl}/api/actDefine/queryProcess`,
  act: `${activeUrl}/api/actDefine/activate`,
  sus: `${activeUrl}/api/actDefine/suspend`,
}

class ActDefine extends React.Component {
  state = {
    booleanTrue: true,
    booleanFalse: false,
    defineData: [],
    loading: false,
    filterCase: {
      key: '', name: '', startTime: '', endTime: '', suspensionState: '' },
    pagination: {
      showSizeChanger: true,
      showQuickJumper: true,
      showTotal: total => `共 ${total} 条`,
      total: 100 },
    modalState: {
      values: { name: '', description: '' },
      visible: false,
      modalType: '',
    },
  }

  componentDidMount () {
    this.getList()
  }

  getList = (filter = false) => {
    let { filterCase } = this.state
    filterCase = filter ? filter : filterCase
    this.setState({ loading: true })
    fetch(urlObj.query, {
      method: 'post',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(filterCase),
    }).then((response) => {
      return response.json()
    }).then((json) => {
      let data = json.data
      setTimeout(() => {
        this.setState({
          loading: false,
          defineData: data,
          pagination: {
            total: data.length },
          filterCase: { ...filterCase },
        })
      }, 200)
    })
  }
  render () {
    let { pagination, loading, defineData, filterCase } = this.state
            // 筛选属性
    const filterProps = {
      filter: {
        ...filterCase,
      },
      onFilterChange: (value) => {
        this.getList(value)
      },
    }
    const TableProps = {
      pagination,
      dataSource: defineData,
      loading,
      columns: [
        { title: '操作', key: 'operator', width: 80,
          render: (text, record, index) => {
            let { id, suspensionState } = record
            return (<div className="table-operation">
              <Switch ProcDefId={id} suspensionState={suspensionState} />
            </div>)
          },
        },
        { title: '流程ID', dataIndex: 'id', key: 'id' },
        { title: '流程标识', dataIndex: 'key', key: 'key' },
        { title: '流程名称', dataIndex: 'name', key: 'name' },
        { title: '流程版本', dataIndex: 'version', key: 'version' },
        { title: '流程XML', dataIndex: 'resourceName', key: 'resourceName' },
        { title: '流程图片', dataIndex: 'dgrmResourceName', key: 'dgrmResourceName' },
        { title: '部署时间', dataIndex: 'deployTime', key: 'deployTime' },
      ],
    }
    return (<Row>
      <Col lg={24} md={48}>
        <Card title="条件选择">
          <Filter {...filterProps} />
        </Card>
      </Col>
      <Col lg={24} md={48}>
        <Card title="流程定义管理">
          <Table {...TableProps} />
        </Card>
      </Col>
    </Row>)
  }
}

export default ActDefine
