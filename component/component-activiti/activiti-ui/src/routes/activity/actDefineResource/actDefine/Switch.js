import React from 'react'
import PropTypes from 'prop-types'
import { Switch, message } from 'antd'


const config = require('../../../../utils/config')
const { activeUrl } = config

const urlObj = {
  act: `${activeUrl}/api/actDefine/activate`,
  sus: `${activeUrl}/api/actDefine/suspend`,
}

class SwitchCom extends React.Component {
  constructor (props) {
    super(props)
    let { suspensionState } = this.props
    let checkedState = Number(suspensionState)
    this.state = { checked: checkedState }
  }
  handSuspension = (ProcDefId) => {
    let currentState = this.state.checked
    this.setState({ checked: !currentState })

    let oprateUrl = currentState ? urlObj.sus : urlObj.act
    fetch(oprateUrl, {
      method: 'post',
      headers: {
        'Content-Type': 'application/json',
        'Cache-Control': 'no-cache',
      },
      body: JSON.stringify({ procDefId: ProcDefId }),
    }).then((response) => {
      return response.json()
    }).then((json) => {
      setTimeout(() => {
        if (json.code === 200 || json.code === '200') {
          message.success(json.msg)
        } else {
          this.setState({ checked: currentState })
          message.error(json.msg)
        }
      }, 200)
    })
  }
  render () {
    let { ProcDefId } = this.props
    let Sprops = {
      checkedChildren: '激活',
      unCheckedChildren: '挂起',
      onChange: () => { this.handSuspension(ProcDefId) },
      checked: Boolean(this.state.checked),
    }
    return (<Switch ref={e => (this.susSwitch = e)} {...Sprops} />)
  }
}

SwitchCom.propTypes = {
  suspensionState: PropTypes.string,
  ProcDefId: PropTypes.string,
}
export default SwitchCom
