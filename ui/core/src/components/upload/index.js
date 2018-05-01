import React from 'react'
import PropTypes from 'prop-types'
import axios from 'axios'
import { request } from 'utils'
import { Upload, Button, Icon, message, Tooltip } from 'antd'

const fileUrls = {
  uploadaction: '/uploadFiles',
  delaction: '/upload/delFileByAttachFileId',
  queryaction: '/upload/queryFileByBatchId',
  downloadaction: '/upload/downloadFileById',
}

class MyUpload extends React.Component {
  static defaultProps = {
    value: '',
    disabled: false,
    title: '',
    autoUpload: true,
  }
  constructor (props) {
    super(props)
    const { value, disabled } = props
    this.state = {
      fileList: [],
      defaultFileList: [],
      uploading: false,
      batchId: value,
      disabled,
      hasNewFile: false,
    }
  }

  componentDidMount () {
    const { value } = this.props
    value && value !== '' && this.handleRequest(value)
  }
  componentDidUpdate (prevProps, prevState) {
    const { uploading, fileList } = this.state
    const { uploading: prevUploading, fileList: prevFileList } = prevState
    const { getLoading, getFileList } = this.props
    if (uploading !== prevUploading && getLoading) {
      getLoading(uploading)
    }
    if (JSON.stringify(fileList) !== JSON.stringify(prevFileList)) {
      let hasNewFile = false
      fileList.forEach(item => {
        if (!item.url) {
          hasNewFile = true
        }
      })
      getFileList && getFileList(fileList, hasNewFile)
      this.setHasNewFile(hasNewFile)
    }
  }

  onRemove = (file) => {
    const { disabled, batchId } = this.state
    if (disabled) {
      return
    }
    this.setState(({ fileList }) => {
      const index = fileList.indexOf(file)
      const newFileList = fileList.slice()
      if (batchId !== '') {
        const id = newFileList[index].uid
        request(fileUrls.queryaction, {
          body: JSON.stringify({
            id: batchId,
          }),
        }).then((json) => {
          if (json.success) {
            let jsonfileList = json.data.fileList
            for (let [, elem] of jsonfileList.entries()) {
              if (elem.id === id) {
                this.onRemovedel(id)
                break
              }
            }
          }
        })
      }
      newFileList.splice(index, 1)
      return {
        fileList: newFileList,
      }
    })
  }
  onRemovedel = (id) => {
    request(fileUrls.delaction, {
      body: JSON.stringify({
        id,
      }),
    })
  }
  setValue = (batchId) => {
    const { onChange } = this.props
    onChange && onChange(batchId)
  }
  setHasNewFile = (hasNewFile) => {
    const { autoUpload } = this.props
    this.setState({ hasNewFile })
    autoUpload && hasNewFile && this.handleUpload()
  }
  handleRequest = (batchId) => {
    request(fileUrls.queryaction, {
      body: JSON.stringify({
        id: batchId,
      }),
      showMsg: false,
    }).then((json) => {
      let defaultFileList = []
      if (json.success && json.data) {
        let jsonfileList = json.data.fileList
        for (let [, elem] of jsonfileList.entries()) {
          let defaultfile = {
            uid: elem.id,
            name: elem.fileName,
            status: 'done',
            url: `/api${fileUrls.downloadaction}?id=${elem.id}`,
          }
          defaultFileList.push(defaultfile)
        }
        this.setState({
          batchId,
          defaultFileList,
          fileList: defaultFileList,
        })
        this.setValue(batchId)
        // ↓这段即将废弃
        const { setBatchId } = this.props
        setBatchId && setBatchId(batchId)
      }
    })
  }
  beforeUpload = (file) => {
    const { name } = file
    if (name.length > 100) {
      message.info('文件名不能超过100个字符')
    } else {
      this.setState(({ fileList }) => {
        return {
          fileList: [...fileList, file],
        }
      })
    }
    return false
  }

  handleUpload = () => {
    const { fileList, batchId } = this.state
    let formData = new FormData()
    for (let [index, elem] of fileList.entries()) {
      formData.append(`files[${index}]`, elem)
    }
    formData.append('fileTypeId', '1')
    formData.append('batchId', batchId)
    this.setState({
      uploading: true,
    })
    axios.post('/api/upload/uploadFiles', formData,
      {
        headers: { 'Content-Type': 'multipart/form-data' },
        credentials: 'include',
      }
    ).then((response) => {
      return response.data
    }).then((json) => {
      if (Number(json.code) === 200) {
        this.setState({
          uploading: false,
        })
        const { data } = json
        const { id: BatchId } = data
        this.handleRequest(BatchId)
        message.success(json.msg)
      } else {
        this.setState({
          uploading: false,
        })
        message.info(json.msg)
      }
    })
  }
  clearList = () => {
    this.setState({
      fileList: [],
    })
  }

  render () {
    const {
      title,
      autoUpload,
    } = this.props
    const {
      disabled,
      fileList = [],
      batchId,
      hasNewFile,
      defaultFileList,
      uploading,
    } = this.state
    const Uploaddisabled = (disabled || !hasNewFile) ? 'true' : fileList.length === 0
    const props = {
      action: fileUrls.uploadaction,
      onRemove: this.onRemove,
      beforeUpload: this.beforeUpload,
      fileList,
      defaultFileList,
      data: {
        fileTypeId: '1',
        files: fileList,
        batchId,
      },
    }
    const selectBotton = (
      <Button disabled={disabled} >
        <Icon type="upload" /> 选择文件
      </Button>)
    return (
      <div >
        <Upload {...props} withCredentials multiple>
          {
            title ?
            (<Tooltip placement="top" title={title}>
              {selectBotton}
            </Tooltip>)
            : (selectBotton)
          }
        </Upload>
        {!autoUpload
          && (<Button
            className="upload-demo-start"
            type="primary"
            onClick={this.handleUpload}
            disabled={Uploaddisabled}
            loading={uploading}
          >
            {uploading ? '上传中' : '上传文件'}
          </Button>)
        }
      </div>
    )
  }
}

MyUpload.propTypes = {
  disabled: PropTypes.bool,
  setBatchId: PropTypes.func,
  title: PropTypes.string,
  getLoading: PropTypes.func,
  getFileList: PropTypes.func,
  value: PropTypes.number,
  onChange: PropTypes.func,
  autoUpload: PropTypes.bool,
}

export default MyUpload
