import React from 'react'
import PropTypes from 'prop-types'
import { Form, Col, Row, Input, Select, DatePicker, Button } from 'antd'
import moment from 'moment'
const Option = Select.Option
const FormItem = Form.Item
const { RangePicker } = DatePicker
const formItemLayout = {
  labelCol: { span: 4 },
  wrapperCol: { span: 14 },
}
class Filter extends React.Component {
  // 获取一些值
  handleFields = (fields) => {
    let { deployTimes } = fields
    if (deployTimes.length) {
        // fields.createTimeS = [createTimeS[0].format('YYYY-MM-DD'), createTimeS[1].format('YYYY-MM-DD')]
      fields.startTime = deployTimes[0].format('YYYY-MM-DD')
      fields.endTime = deployTimes[1].format('YYYY-MM-DD')
    }
    delete fields.deployTimes
    return fields
  }
      // 提交
  handleSubmit = () => {
    let { onFilterChange, form: { getFieldsValue } } = this.props
    let fields = getFieldsValue()
    fields = this.handleFields(fields)
    onFilterChange(fields)
  }
  handleReset = () => {
    let { form: { getFieldsValue, setFieldsValue } } = this.props
    let fields = getFieldsValue()
    for (let item in fields) {
      if ({}.hasOwnProperty.call(fields, item)) {
        if (fields[item] instanceof Array) {
          fields[item] = []
        } else {
          fields[item] = undefined
        }
      }
    }
    setFieldsValue(fields)
    this.handleSubmit()
  }
  render () {
    const { filter, form: { getFieldDecorator } } = this.props
    let { key, name, suspensionState, startTime, endTime } = filter
    // 初始化创建时间
    let initialDeployTimes = []
    if (startTime) {
      initialDeployTimes[0] = moment(startTime)
    }
    if (endTime) {
      initialDeployTimes[1] = moment(endTime)
    }
    // key: '', name: '', startTime: '', endTime: '', suspensionState: ''
    return (
      <Form >
        <Row gutter={0}>
          <Col span={8}>
            <FormItem label="流程标识:" labelCol={{ span: 5 }} wrapperCol={{ span: 16 }}>
              {getFieldDecorator('key', { initialValue: key })(<Input placeholder="流程标识" />)}
            </FormItem>
          </Col>
          <Col span={8}>
            <FormItem label="流程名称:" labelCol={{ span: 5 }} wrapperCol={{ span: 16 }}>
              {getFieldDecorator('name', { initialValue: name })(<Input placeholder="流程名称" />)}
            </FormItem>
          </Col>
          <Col span={4}>
            <FormItem label="流程定义状态:" labelCol={{ span: 12 }} wrapperCol={{ span: 12 }}>
              {getFieldDecorator('suspensionState', { initialValue: suspensionState })(
                <Select>
                  <Option value="">全部</Option>
                  <Option value="1">激活</Option>
                  <Option value="2">挂起</Option>
                </Select>)}
            </FormItem>
          </Col>
        </Row>
        <Row gutter={0}>
          <Col span={8}>
            <FormItem label="部署时间:" labelCol={{ span: 5 }} wrapperCol={{ span: 16 }}>
              {getFieldDecorator('deployTimes', { initialValue: initialDeployTimes })(<RangePicker />)}
            </FormItem>
          </Col>
          <Col span={4}>
            <div>
              <Button className="margin-right" type="primary" icon="search" onClick={this.handleSubmit} >查询</Button>
              <Button onClick={this.handleReset}>重置</Button>
            </div>
          </Col>
        </Row>
      </Form>
    )
  }
}

const WrappedRegistrationForm = Form.create()(Filter)

Filter.propTypes = {
  form: PropTypes.object,
  filter: PropTypes.object,
  onFilterChange: PropTypes.func,
}

export default WrappedRegistrationForm
