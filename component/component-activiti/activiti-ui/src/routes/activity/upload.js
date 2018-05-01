import React, { PureComponent } from 'react'
import PropTypes from 'prop-types'
import { Button, Icon } from 'antd'

class Upload extends PureComponent {
  constructor (props) {
    super(props)
    this.handleChange = this.handleChange.bind(this)
    this.handleClick = this.handleClick.bind(this)
  }
  state = {
    value: {
      name: '',
    },
  }
  handleChange = (e) => {
    const target = e.currentTarget
    let { getFile } = this.props
    let file = target.files[0]
    getFile(file)
  }
  handleClick = () => {
    const input = this.input
    if (input.click) {
      input.click()
    } else {
      const e = document.createEvent('MouseEvents')
      e.initEvent('click', true, true)
      input.dispatchEvent(e)
    }
  }
  render () {
    return (
      <div>
        <input
          {...this.props}
          type="file"
          onChange={this.handleChange}
          ref={input => (this.input = input)}
          style={{ display: 'none' }}
        />
        <Button onClick={this.handleClick}>
          <Icon type="upload" /> {this.props.text}
        </Button>
      </div>
    )
  }
}

Upload.propTypes = {
  onChange: PropTypes.func,
  text: PropTypes.string,
  getFile: PropTypes.func,
}

Upload.defaultProps = {
  text: '上传文件',
}

export default Upload

