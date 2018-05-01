import React from 'react'
import PropTypes from 'prop-types'
import { Modal, Form, Input, message, Table, Col, Tag, Button } from 'antd'
import Upload from '../../upload'

const config = require('../../../../utils/config')
const { activeUrl } = config

const urlObj = {
  add: `${activeUrl}/api/actResource/deploy`,
  queryResource: `${activeUrl}/api/actResource/queryDeployResource`,
  downloadResource: `${activeUrl}/api/actResource/downloadResource`,
}

const FormItem = Form.Item
const formItemLayout = {
  labelCol: {
    xs: { span: 24 },
    sm: { span: 6 },
  },
  wrapperCol: {
    xs: { span: 24 },
    sm: { span: 14 },
  },
}

class actModelModal extends React.Component {
  state = {
    maskClosable: false,
    visible: true,
    confirmLoading: false,
    file: false,
    selectedR: {},
  }
  onCancel = () => {
    let { handleModal, form } = this.props
    form.resetFields()
    this.setState({ file: {} })
    handleModal('hide')
  }
  onUpload = () => {
    let { file } = this.state
    let { handleModal, form } = this.props
    if (!file) {
      message.warning('请选择文件')
      return false
    }
    form.validateFields((errors) => {
      if (errors) { return }
    })
    let addData = {
      ...form.getFieldsValue(),
    }
    this.setState({
      confirmLoading: true,
    })
    let formDate = new FormData()

    formDate.append('file', file)
    formDate.append('fileName', file.name)
    formDate.append('name', addData.name)
    formDate.append('category', addData.category)
    fetch(urlObj.add, {
      method: 'post',
      body: formDate,
    }).then((response) => {
      return response.json()
    }).then((data) => {
      setTimeout(() => {
        if (data.code === 200 || '200') {
          message.success(data.msg)
          this.setState({
            confirmLoading: false,
            file: false,
          })
          form.resetFields()
          handleModal('hide')
          handleModal('refilter')
        } else {
          message.error(data.msg)
          this.setState({
            confirmLoading: false,
          })
        }
      }, 1000)
    })
    return true
  }

  getFileObj = (fileObj) => {
    this.setState({ file: fileObj })
  }
  uploadProps = {
    text: '添加/修改流程文件',
    name: 'file',
    getFile: (fileObj) => {
      this.getFileObj(fileObj)
    },
  }
  render () {
    const { visible, modalType, dRTable, form: { getFieldDecorator } } = this.props
    console.log(dRTable)
    let modalProps = {
      title: '新增部署',
      maskClosable: false,
      onOk: this.onUpload,
      onCancel: this.onCancel,
      okText: '提交',
      cancelText: '取消',
      confirmLoading: this.state.confirmLoading,
    }
    let ModalContent = ''
    let TableProps = {
      dataSource: dRTable,
      columns: [
        { title: '资源名称', dataIndex: 'name', key: 'name', render: (text, record, index) => {
          return <a href={`${urlObj.downloadResource}?deployId=${this.props.deployId}&name=${text}`} >{text}</a>
        } },
      ],
    }
    switch (modalType) {
      case 'download':
        modalProps.footer = [
          <Button key="back" size="large" onClick={this.onCancel}>关闭</Button>,
        ]
        ModalContent = (<Table {...TableProps} />)
        break
      default:
        ModalContent = (<Form layout="horizontal">
          <FormItem label="流程名称" hasFeedback {...formItemLayout}>
            {getFieldDecorator('name', { rules: [{ required: true, message: '请输入流程名称' }], initialValue: '' })(<Input />)}
          </FormItem>
          <FormItem label="流程类别" hasFeedback {...formItemLayout}>
              {getFieldDecorator('category', { initialValue: '' })(<Input />)}
          </FormItem>
          <FormItem label="部署文件" hasFeedback {...formItemLayout}>
            <Upload {...this.uploadProps} />
            <Col span="24" >支持文件格式: zip, bar, bpmn, bpmn20.xml </Col>
            {this.state.file && this.state.file.name ? <Tag>{this.state.file.name}</Tag> : ''}
          </FormItem>
        </Form>)
    }
    return (
      <Modal {...modalProps} visible={visible} >
          {ModalContent}
      </Modal>
    )
  }
}

const ModalForm = Form.create()(actModelModal)

actModelModal.propTypes = {
  visible: PropTypes.bool,
  confirmLoading: PropTypes.bool,
  form: PropTypes.object,
  modalType: PropTypes.string,
  handleModal: PropTypes.func,
  dRTable: PropTypes.array,
}
export default ModalForm
