import React from 'react'
import { Table, Card, Popconfirm, Row, Col, Button, Icon, message } from 'antd'
import OrgModal from './orgModal'


class org extends React.Component {
  state = {
    booleanTrue: true,
    booleanFalse: false,
    visible: false,
    confirmLoading: false,
    isloading: false,
    ModalTitle: '机构管理',
    pagination: {
      defaultPageSize: 12,
    },
    userData: [],
    columnsOrg: [{
      title: '机构名称',
      dataIndex: 'name',
      key: 'name',
      onCellClick: (record) => {
        console.log(record.key)
        fetch('/system/user/list', {
          method: 'post',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            orgId: record.key,
          }),
        }).then((response) => {
          return response.json()
        }).then((json) => {
          this.setState({
            userData: json,
          })
        })
      },
    }, {
      title: '操作',
      dataIndex: 'operation',
      render: (text, record, index) => {
        return (
          <div className="table-operation">
            <Popconfirm title="确认删除吗?" onConfirm={() => this.onDelete(record, index)}>
              <a>删除</a>
            </Popconfirm>
            <a onClick={this.showModal.bind(this)} title="编辑机构">编辑</a>
            <a onClick={this.showModal.bind(this)} title="添加下级机构">添加</a>
          </div>
        )
      },
    },
    ],
    columnsUser: [{
      title: '姓名',
      dataIndex: 'name',
      key: 'name',
    }, {
      title: '登陆账号',
      dataIndex: 'loginName',
      key: 'loginName',
    }, {
      title: '邮箱',
      dataIndex: 'email',
      key: 'email',
    }, {
      title: '手机号',
      dataIndex: 'mobilePhone',
      key: 'mobilePhone',
    }, {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      className: 'table-status',
      render: (text, record) => {
        let cls = 'close-circle-o'
        if (record.status) {
          cls = 'check-circle-o'
        }
        return <Icon type={cls} />
      },
    }, {
      title: '操作',
      dataIndex: 'operation',
      render: (text, record, index) => {
        return (
          <div className="table-operation">
            <Button>编辑</Button>
            <Popconfirm title="确认删除吗?" onConfirm={() => this.onDelete(record, index)}>
              <Button>删除</Button>
            </Popconfirm>
          </div>
        )
      },
    },
    ],
    onModalClose: () => {
      this.setState({
        visible: false,
      })
    },
    onModalSubmit: () => {
      this.setState({
        confirmLoading: true,
      })
    },
  }
  componentDidMount () {
    fetch('/system/org/list', {
      method: 'POST',
    }).then((response) => {
      return response.json()
    }).then((json) => {
      this.setState({
        orgData: json,
      })
    })
  }
  onDelete (record, index) {
    console.log(record)
    let type = 'org'
    if (record.loginName) {
      type = 'user'
    }
    fetch(`/system/${type}/del`, {
      method: 'post',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        id: record.key,
      }),
    }).then((res) => {
      return res.json()
    }).then((data) => {
      console.log(data)
      if (data.code === 200) {
        fetch(`/system/${type}/list`, {
          method: 'POST',
        }).then((response) => {
          return response.json()
        }).then((json) => {
          let dataSource = {}
          dataSource[`${type}Data`] = json
          this.setState(dataSource)
        })
        message.success(data.msg)
      } else {
        message.error(data.msg)
      }
    })
  }
  showModal (e) {
    this.setState({
      visible: true,
      ModalTitle: e.target.title || '机构管理',
    })
  }
  render () {
    const { booleanFalse, columnsOrg, orgData, pagination, columnsUser, userData } = this.state
    return (<Row>
      <Col span={9}>
        <Card title="机构管理" bordered={false} extra={<Button type="primary" size="small" onClick={this.showModal.bind(this)} title="创建机构">创建机构</Button>}>
          <Table pagination={booleanFalse} columns={columnsOrg} dataSource={orgData} />
        </Card>
      </Col>
      <Col span={14} offset={1}>
        <Card title="人员信息" bordered={false}>
          <Table bordered pagination={pagination} columns={columnsUser} dataSource={userData} size="small" />
        </Card>
      </Col>
      <OrgModal {...this.state} />
    </Row>
    )
  }
}

export default org
