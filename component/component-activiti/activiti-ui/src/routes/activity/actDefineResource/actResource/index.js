import React from 'react'
import { Row, Col, Card, Table, Popconfirm, message, Button } from 'antd'
import Modal from './Modal'
import Filter from './Filter'

const config = require('../../../../utils/config')
const { activeUrl } = config

const urlObj = {
  query: `${activeUrl}/api/actResource/queryDeployments`,
  del: `${activeUrl}/api/actResource/delete`,
  add: `${activeUrl}/api/actResource/deploy`,
  download: `${activeUrl}/api/actResource/downloadResource`,
  queryResource: `${activeUrl}/api/actResource/queryDeployResource`,
}

class ActResource extends React.Component {
  state = {
    booleanTrue: true,
    booleanFalse: false,
    resourceData: [],
    loading: false,
    filterCase: {
      name: '', startTime: '', endTime: '' },
    pagination: {
      showSizeChanger: true,
      showQuickJumper: true,
      showTotal: total => `共 ${total} 条`,
      total: 100 },
    modalState: {
      values: { name: '', category: '', file: {}, fileName: '' },
      visible: false,
      modalType: '',
      deployId: '',
    },
  }
  componentDidMount () {
    this.getList()
  }
  onItemOperate = (type, record, index) => {
    let url = urlObj[`${type}`]
    fetch(url, {
      method: 'post',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ deployId: record.id }),
    }).then((response) => {
      return response.json()
    }).then((json) => {
      setTimeout(() => {
        json.code === 200 || json.code === '200' ? message.success(json.msg) : message.error(json.msg)
        type === 'download' ? true : this.getList()
      }, 200)
    })
  }
  getList = (filter = false) => {
    let { filterCase } = this.state
    filterCase = filter ? filter : filterCase
    this.setState({ loading: true })
    fetch(urlObj.query, {
      method: 'post',
      headers: {
        'Content-Type': 'application/json',
        'Cache-Control': 'no-cache',
      },
      body: JSON.stringify(filterCase),
    }).then((response) => {
      return response.json()
    }).then((json) => {
      let data = json.data
      setTimeout(() => {
        this.setState({
          loading: false,
          resourceData: data,
          pagination: {
            total: data.length },
          filterCase: { ...filterCase },
        })
      }, 200)
    })
  }
  queryDeployResourceList = (record) => {
    let { id } = record
    fetch(urlObj.queryResource, {
      method: 'post',
      headers: {
        'Content-Type': 'application/json',
        'Cache-Control': 'no-cache',
      },
      body: JSON.stringify({ deployId: id }),
    }).then((response) => {
      return response.json()
    }).then((json) => {
      let data = json.data
      setTimeout(() => {
        record.data = data
        this.handleModal('download', record)
      }, 200)
    })
  }
  handleModal = (opt, record, event) => {
    let newModal = {
      visible: false,
      confirmLoading: false,
      modalType: '',
      values: { name: '', description: '' },
      deployId: '',
    }
    switch (opt) {
      case 'add':
        newModal.visible = true
        newModal.modalType = 'add'
        break
      case 'download':
        newModal.visible = true
        newModal.modalType = 'download'
        newModal.deployId = record.id
        newModal.dRTable = record.data
        break
      case 'loading':
        newModal = { confirmLoading: true,
        }
        break
      case 'loaded':
        newModal.confirmLoading = false
        break
      case 'show':
        newModal.visible = true
        newModal.confirmLoading = false
        break
      case 'hide':
        newModal.visible = false
        newModal.confirmLoading = false
        break
      case 'refilter':
        this.getList()
        break
      default:
        newModal = {
          visible: false,
        }
    }
    this.setState({ modalState: newModal })
  }
  render () {
    let { pagination, loading, resourceData, filterCase, modalState } = this.state
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
      dataSource: resourceData,
      loading,
      rowKey: 'id',
      columns: [
        { title: '操作', key: 'operator',
          render: (text, record, index) => {
            return (<div className="table-operation">
              <Popconfirm title="确认删除吗?" onConfirm={() => this.onItemOperate('del', record, index)}>
                <a>删除</a>
              </Popconfirm>
              &nbsp;|&nbsp;
              <a onClick={() => { this.queryDeployResourceList(record) }}>下载资源</a>
            </div>)
          },
        },
        { title: '部署ID', dataIndex: 'id', key: 'id' },
        { title: '部署名称', dataIndex: 'name', key: 'name' },
        { title: '部署时间', dataIndex: 'deployTime', key: 'deployTime' },
      ],
    }
      // 模态框属性
    const modalProps = {
      ...modalState,
      handleModal: this.handleModal,
    }
    const TableExtra = (<div>
      <Button type="primary" className="margin-right" onClick={() => { this.handleModal('add') }} title="新增部署">新增部署</Button>
    </div>)
    return (<Row>
      <Col lg={24} md={48}>
        <Card title="条件选择">
          <Filter {...filterProps} />
        </Card>
      </Col>
      <Col lg={24} md={48}>
        <Card title="部署包管理" extra={TableExtra}>
          <Table {...TableProps} />
        </Card>
      </Col>
      <Modal {...modalProps} />
    </Row>)
  }
}

export default ActResource
