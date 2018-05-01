import React from 'react'
import PropTypes from 'prop-types'
import { Form, Col, Row, Input, DatePicker, Button } from 'antd'
import moment from 'moment'

const FormItem = Form.Item
const { RangePicker } = DatePicker
const formItemLayout = {
  labelCol: { span: 6 },
  wrapperCol: { span: 14 },
}
class Filter extends React.Component {
  state = {}
  render () {
    const { onFilterChange, filter, form: { getFieldDecorator, getFieldsValue, setFieldsValue } } = this.props
    let { id, name } = filter
    // 初始化创建时间
    let initialCreateTimeS = []
    if (filter.createTimeS && filter.createTimeS[0]) {
      initialCreateTimeS[0] = moment(filter.createTimeS[0])
    }
    if (filter.createTimeS && filter.createTimeS[1]) {
      initialCreateTimeS[1] = moment(filter.createTimeS[1])
    }
    // 获取创建时间值
    const handleFields = (fields) => {
      let { createTimeS } = fields
      if (createTimeS.length) {
        // fields.createTimeS = [createTimeS[0].format('YYYY-MM-DD'), createTimeS[1].format('YYYY-MM-DD')]
        fields.startTime = createTimeS[0].format('YYYY-MM-DD')
        fields.endTime = createTimeS[1].format('YYYY-MM-DD')
      }
      delete fields.createTimeS
      return fields
    }
    // 提交
    const handleSubmit = () => {
      let fields = getFieldsValue()
      fields = handleFields(fields)
      onFilterChange(fields)
    }
    const handleReset = () => {
      const fields = getFieldsValue()
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
      handleSubmit()
    }
    return (
      <Form>
        <Row gutter={24}>
          <Col span={3}>
            <FormItem label="模型ID:" labelCol={{ span: 11 }} wrapperCol={{ span: 13 }}>
              {getFieldDecorator('id', { initialValue: id })(<Input placeholder="模型ID" />)}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="模型名称:" labelCol={{ span: 7 }} wrapperCol={{ span: 17 }}>
              {getFieldDecorator('name', { initialValue: name })(<Input placeholder="模型名称" />)}
            </FormItem>
          </Col>
          <Col span={9}>
            <FormItem label="创建时间:" labelCol={{ span: 4 }} wrapperCol={{ span: 16 }}>
              {getFieldDecorator('createTimeS', { initialValue: initialCreateTimeS })(<RangePicker />)}
            </FormItem>
          </Col>
          <Col span={4}>
            <div>
              <Button className="margin-right" type="primary" icon="search" onClick={handleSubmit} >查询</Button>
              <Button onClick={handleReset}>重置</Button>
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
