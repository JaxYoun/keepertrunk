import React from 'react'
import { Modal, Form, Input, message } from 'antd'
import PropTypes from 'prop-types'
const FormItem = Form.Item

const CollectionCreateForm = Form.create()(
  (props) => {
    const { visible, onCancel, onCreate, form, confirmLoading, ModalTitle } = props
    const { getFieldDecorator } = form
    return (
      <Modal
        visible={visible}
        title={ModalTitle}
        okText="确认"
        onCancel={onCancel}
        onOk={onCreate}
        confirmLoading={confirmLoading}
        maskClosable={false}
      >
        <Form layout="vertical">
          <FormItem label="机构名称">
            {getFieldDecorator('orgName', {
              rules: [{ required: true, message: '请填写机构名称!' }],
              setFieldsValue: '2',
            })(
              <Input />
            )}
          </FormItem>
          <FormItem label="上级机构">
            {getFieldDecorator('pId')(<Input />)}
          </FormItem>
          <FormItem label="排序编号">
            {getFieldDecorator('orderCode')(<Input type="number" />)}
          </FormItem>
        </Form>
      </Modal>
    )
  }
)

class OrgModal extends React.Component {
  state = {
    confirmLoading: false,
  }
  handleCancel = () => {
    const form = this.form
    this.props.onModalClose()
    form.resetFields()
  }
  handleCreate = () => {
    const form = this.form
    form.validateFields((err, values) => {
      if (err) {
        return
      }

      console.log('Received values of form: ', values)
      this.setState({
        confirmLoading: true,
      })
      fetch('/system/org/save', {
        method: 'post',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(values),
      }).then((response) => {
        return response.json()
      }).then((data) => {
        setTimeout(() => {
          if (data.code === 200) {
            this.setState({
              confirmLoading: false,
            })
            this.props.onModalClose()
            form.resetFields()
            message.success(data.msg)
          } else {
            this.setState({
              confirmLoading: false,
            })
            message.error(data.msg)
          }
          console.log(data)
        }, 2000)
      })
    })
  }
  saveFormRef = (form) => {
    this.form = form
  }
  componentDidsMount = () => {
    const form = this.form
    console.log(this.props.ModalTitle)
    if (this.props.ModalTitle === '编辑机构') {
      form.setFieldsValue({
        orgName: 'sa',
      })
    }
  }
  render () {
    const { visible, ModalTitle } = this.props
    return (
      <div>
        <CollectionCreateForm
          ref={this.saveFormRef}
          visible={visible}
          onCancel={this.handleCancel.bind(this)}
          onCreate={this.handleCreate}
          confirmLoading={this.state.confirmLoading}
          ModalTitle={ModalTitle}
          {...this.state}
        />
      </div>
    )
  }
}

OrgModal.propTypes = {
  visible: PropTypes.bool,
  confirmLoading: PropTypes.bool,
  onModalSubmit: PropTypes.func,
  onModalSuccess: PropTypes.func,
  onModalClose: PropTypes.func,
  ModalTitle: PropTypes.string,
}
export default OrgModal
