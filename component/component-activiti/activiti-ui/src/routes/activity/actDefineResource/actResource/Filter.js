import React from 'react'
import PropTypes from 'prop-types'
import { Form, Col, Row, Input, DatePicker, Button } from 'antd'
import moment from 'moment'
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
    let { name, startTime, endTime } = filter
    // 初始化创建时间
    let initialDeployTimes = []
    if (startTime) {
      initialDeployTimes[0] = moment(startTime)
    }
    if (endTime) {
      initialDeployTimes[1] = moment(endTime)
    }

    return (
      <Form>
        <Row gutter={0}>
          <Col span={10}>
            <FormItem label="部署名称:" {...formItemLayout}>
              {getFieldDecorator('name', { initialValue: name })(<Input placeholder="部署名称" />)}
            </FormItem>
          </Col>
          <Col span={10}>
            <FormItem label="部署时间:" {...formItemLayout}>
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
