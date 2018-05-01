# Upload

上传文件。

---------
##  何时使用

- 当要上传文件时
- 要展示上传的文件时

##  基本使用

```jsx
import React from 'react'
import PropTypes from 'prop-types'
import Upload from 'components'

class Index extends React.Component {
  state = {
    loading: false,
    fileList: [],
  }

  getLoading = (loading) => {
    this.setState({
      loading
    })
  }
  getFileList = (fileList) => {
    this.setState({
      fileList
    })
  }
  render () {
    return (<Upload
      value='1234' // batchId 值
      title='这里是【选择文件】按钮的说明。'  // 鼠标悬浮后弹出tip气泡信息
      getLoading={this.getLoading}
      getFileList={this.getFileList}
      autoUpload={true} // 是否自动上传  默认为true
      // disabled  // 是否禁用 默认为false
    />)
  }
}

export default Index

```

## API

### Upload

| 参数              | 说明                     | 类型             |  默认值   |  
|------------------|--------------------------|-----------------|---------------------|
| value | bantchId的值,当有值时展示该bantId下的文件列表 | number | -  |
| disabled | 是否禁用操作 | bool | false  |
| getLoading | 文件是否处于上传中 | Function(loading) | -  |
| getFileList | 获取文件列表 | Function(fileList) | -   |
| autoUpload | 是否自动上传 | bool | true  |
| title | 鼠标悬浮【选择文件】按钮上弹出tip气泡信息 | string | -   |
| setBatchId | 可以从外部获取bantchId的值 | Function(bantchId) | -   |
