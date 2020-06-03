import React, { Component } from 'react'
import PropTypes from 'prop-types'

class Input extends Component {
    constructor(props){
        super(props)
        this.state = {
            value: props.value? props.value : '',
            className: props.className? props.className : '',
            error: false
        }
    }

    //...

    render () {
        const {handleError, ...opts} = this.props
        this.handleError = handleError
        return (
            <input {...opts} value={this.state.value}
                   onChange={this.inputChange} className={this.state.className} />
        )
    }
}

Input.propTypes = {
    name: PropTypes.string,
    placeholder: PropTypes.string,
    type: PropTypes.string,
    className: PropTypes.string,
    value: PropTypes.string,
    handleError: PropTypes.func
}

export default Input
