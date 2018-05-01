import React from 'react'
import PropTypes from 'prop-types'
import { Modal, Form, Input, message, Col, Tag } from 'antd'
import Upload from '../upload'

const config = require('../../../utils/config')
const { activeUrl } = config

const urlObj = {
  upload: `${activeUrl}/api/actModel/import`,
  add: `${activeUrl}/api/actModel/newModel`,
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
    file: {},
  }
  onCancel = () => {
    let { handleModal, form } = this.props
    form.resetFields()
    handleModal('hide')
  }
  onUpload = () => {
    let { file } = this.state
    let { handleModal } = this.props
    if (typeof(file) !== 'object') {
      message.warning('请选择文件')
      return false
    }
    this.setState({
      confirmLoading: true,
    })
    let formDate = new FormData()
    formDate.append('file', file)
    formDate.append('fileName', file.name)
    // let xhr = new XMLHttpRequest()

    // xhr.open('POST', uploadUrl, true)
    // xhr.setRequestHeader('Content-Type', file.type)
    // xhr.send(formDate)
    fetch(urlObj.upload, {
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
            file: {},
          })
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
  onAdd = () => {
    let { handleModal, form } = this.props
    form.validateFields((errors) => {
      if (errors) { return }
    })
    let addData = {
      ...form.getFieldsValue(),
    }
    this.setState({
      confirmLoading: true,
    })
    fetch(urlObj.add, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(addData),
    }).then((response) => {
      return response.json()
    }).then((data) => {
      setTimeout(() => {
        if (Number(data.code) === 200) {
          handleModal('refilter')
          form.resetFields()
          message.success(data.msg)
          let modelId = data.data.id
          window.open(`/modeler.html?modelId=${modelId}`)
        } else {
          message.error(data.msg)
        }
        this.setState({
          confirmLoading: false,
        })
      }, 1000)
    })
  }
  getFileObj = (fileObj) => {
    this.setState({ file: fileObj })
  }
  render () {
    const { visible, modalType, form: { getFieldDecorator } } = this.props

    let ModalContent = ''
    let modalProps = {
      title: '创建模型',
      maskClosable: false,
      onOk: this.onAdd,
      onCancel: this.onCancel,
      okText: '提交',
      cancelText: '取消',
      confirmLoading: this.state.confirmLoading,
    }
    let uploadProps = {}
    switch (modalType) {
      case 'import':
        uploadProps = {
          text: '添加/修改流程文件',
          name: 'file',
          getFile: (fileObj) => {
            this.getFileObj(fileObj)
          },
        }
        modalProps.title = '导入XML文件'
        modalProps.onOk = this.onUpload
        ModalContent = (<Col offset="6">
          <FormItem hasFeedback {...formItemLayout}>
            <Upload {...uploadProps} />
            <Col span="24" >支持文件格式: xml </Col>
            {this.state.file && this.state.file.name ? <Tag>{this.state.file.name}</Tag> : ''}
          </FormItem>
        </Col>)
        break
      default :
        modalProps.title = '创建模型'
        ModalContent = (<div>
          <FormItem label="模型名称" hasFeedback {...formItemLayout}>
              {getFieldDecorator('name', { rules: [{ required: true, message: '请输入模型名称' }], initialValue: '' })(<Input />)}
          </FormItem>
          <FormItem label="模型描述" hasFeedback {...formItemLayout}>
              {getFieldDecorator('description', { initialValue: '' })(<Input />)}
          </FormItem>
        </div>)
    }

    return (
      <div>
        <Modal {...modalProps} visible={visible} >
          <Form layout="horizontal">
            {ModalContent}
          </Form>
        </Modal>
      </div>
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
}
export default ModalForm
