import React from 'react'

import Grid from '@material-ui/core/Grid'
import TextField from '@material-ui/core/TextField'

class MemberForm extends React.Component {
  constructor(props) {
    super(props)

    const item = {
      code: '',
      name: '',
    }

    this.state = Object.assign(item, props.item)
  }

  render() {
    return (
      <Grid
        container
        direction="column"
        justify="space-evenly"
        alignItems="stretch"
      >
        <TextField
          label="Code"
          value={this.state.code}
          onChange={this.handleChange('code')}
        />

        <TextField
          label="Name"
          value={this.state.name}
          onChange={this.handleChange('name')}
        />
      </Grid>
    )
  }

  handleChange(name) {
    return event => {
      this.setState({[name]: event.target.value}, () => {
        this.props.onChange(this.state)
      })
    }
  }
}

export default MemberForm
