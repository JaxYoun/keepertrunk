import React from 'react'
import { Row, Col, Card, Table, Popconfirm, Button, message } from 'antd'
import Filter from './Filter'
import Modal from './Modal'

const config = require('../../../utils/config')
const { activeUrl } = config
const urlObj = {
  query: `${activeUrl}/api/actModel/queryModel`,
  del: `${activeUrl}/api/actModel/delModel`,
  add: `${activeUrl}/api/actModel/newModel`,
  deploy: `${activeUrl}/api/actModel/deploy`,
  download: `${activeUrl}/api/actModel/export`,
}

class actModel extends React.Component {
  state = {
    booleanTrue: true,
    booleanFalse: false,
    modelData: [],
    loading: false,
    filterCase: {
      id: '', name: '', startTime: '', endTime: '' },
    pagination: {
      showSizeChanger: true,
      showQuickJumper: true,
      showTotal: total => `共 ${total} 条`,
      total: 0 },
    modalState: {
      values: { name: '', description: '' },
      visible: false,
      modalType: '',
    },
  }
  componentDidMount () {
    this.getData()
  }
  onItemOperate = (record, index, type) => {
    let url = urlObj[`${type}`]
    if (type === 'del' || type === 'deploy') {
      fetch(url, {
        method: 'post',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ id: record.id, key: record.key }),
      }).then((response) => {
        return response.json()
      }).then((json) => {
        setTimeout(() => {
          json.code === 200 || json.code === '200' ? message.success(json.msg) : message.error(json.msg)
          this.getData()
        }, 200)
      })
    } else if (type === 'download') {
      console.log(url)
      window.location.href = `${url}?id=${record.id}`
    }
  }
  getData = (filter = false) => {
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
          modelData: data,
          pagination: {
            total: data.length },
          filterCase: { ...filterCase },
        })
      }, 200)
    })
  }

  handleModal = (opt, record, event) => {
    let newModal = {
      visible: false,
      confirmLoading: false,
      modalType: '',
      values: { name: '', description: '' },
    }
    switch (opt) {
      case 'add':
        newModal.visible = true
        newModal.modalType = 'add'
        break
      case 'import':
        newModal.visible = true
        newModal.modalType = 'import'
        break
      case 'loading':
        newModal = {
          confirmLoading: true,
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
        this.getData()
        break
      default:
        newModal = {
          visible: false,
        }
    }
    this.setState({ modalState: newModal })
  }
  render () {
    const { pagination, modelData, loading, filterCase, modalState } = this.state
    const modelColumns = [
      { title: '操作', key: 'operation', dataIndex: 'id',
        render: (text, record, index) => {
          return (
            <div className="table-operation">
              <Popconfirm title="确认删除吗?" onConfirm={() => this.onItemOperate(record, index, 'del')}>
                <a>删除</a>
              </Popconfirm>
              &nbsp;|&nbsp;
              <a href={`/modeler.html?modelId=${record.id}`} target="_blank" title="编辑模型">编辑</a>
              &nbsp;|&nbsp;
              <Popconfirm title="确认部署吗?" onConfirm={() => this.onItemOperate(record, index, 'deploy')}>
                <a>部署</a>
              </Popconfirm>
              &nbsp;|&nbsp;
              <a onClick={() => { this.onItemOperate(record, index, 'download') }}>导出xml</a>
            </div>)
        },
      },
      { title: '模型ID', dataIndex: 'id', key: 'id' },
      { title: '模型名称', dataIndex: 'name', key: 'name' },
      { title: '版本', dataIndex: 'version', key: 'version' },
      { title: '创建时间', dataIndex: 'createTime', key: 'createTime' },
    ]
        // 筛选属性
    const filterProps = {
      filter: {
        ...filterCase,
      },
      onFilterChange: (value) => {
        this.getData(value)
      },
    }
      // 模态框属性
    const modalProps = {
      ...modalState,
      handleModal: this.handleModal,
    }
    const TableExtra = (<div>
      <Button type="primary" className="margin-right" onClick={this.handleModal.bind(this, 'add')} title="创建模型">创建模型</Button>
      <Button type="primary" className="margin-right" onClick={this.handleModal.bind(this, 'import')} title="创建模型">从XML文件导入</Button>
    </div>)

    return (<Row>
      <Col lg={24} md={48}>
        <Card title="条件选择" >
          <Filter {...filterProps} />
        </Card>
      </Col>
      <Col lg={24} md={48}>
        <Card title="模型管理" extra={TableExtra} >
          <Table pagination={pagination} columns={modelColumns} loading={loading} dataSource={modelData} rowKey={'id'} />
        </Card>
      </Col>
      <Modal {...modalProps} />
    </Row>)
  }
}

export default actModel
