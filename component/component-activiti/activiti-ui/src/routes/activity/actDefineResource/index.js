import React from 'react'
import Define from './actDefine'
import Resource from './actResource'
import {
  Tabs,
  Icon,
} from 'antd'

const TabPane = Tabs.TabPane
const Tindex = (props) => {
  return (
    <div>
      <Tabs defaultActiveKey="1">
        <TabPane tab={<span><Icon type="fork" />流程定义</span>} key="1">
          <Define />
        </TabPane>
        <TabPane tab={<span><Icon type="cloud-o" />部署包管理</span>} key="2">
          <Resource />
        </TabPane>
      </Tabs>
    </div>
  )
}
export default Tindex
