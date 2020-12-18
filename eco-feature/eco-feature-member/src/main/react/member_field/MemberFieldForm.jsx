import React from 'react'

import Grid from '@material-ui/core/Grid'
import FormControl from '@material-ui/core/FormControl'
import InputLabel from '@material-ui/core/InputLabel'
import Select from '@material-ui/core/Select'
import TextField from '@material-ui/core/TextField'
import MenuItem from '@material-ui/core/MenuItem'

import {withStyles} from '@material-ui/core/styles'

const styles = theme => ({
  input: {
    width: '100%',
  },
})

class MemberForm extends React.Component {
  state = this.props.value || {}

  handleChange(name) {
    return event => {
      this.setState({[name]: event.target.value}, () => {
        this.props.onChange(this.state)
      })
    }
  }

  handleOptionsChange(value) {
    this.setState({options: value}, () => {
      this.props.onChange(this.state)
    })
  }

  render() {
    const {classes} = this.props

    return (
      <Grid
        container
        direction="column"
        justify="space-evenly"
        alignItems="stretch"
        spacing={1}
      >
        <Grid item sx={12}>
          <TextField
            className={classes.input}
            label="Name"
            value={this.state.name || ''}
            onChange={this.handleChange('name')}
          />
        </Grid>

        <Grid item sx={12}>
          <TextField
            className={classes.input}
            label="Label"
            value={this.state.label || ''}
            onChange={this.handleChange('label')}
          />
        </Grid>

        <Grid item sx={12}>
          <FormControl className={classes.input}>
            <InputLabel htmlFor="age-helper">Type</InputLabel>
            <Select
              className={classes.input}
              value={this.state.type || 'CHECKBOX'}
              onChange={this.handleChange('type')}
            >
              <MenuItem value="CHECKBOX">Checkbox</MenuItem>
              <MenuItem value="TEXT">Text</MenuItem>
              <MenuItem value="SINGLE_SELECT">Single Select</MenuItem>
              <MenuItem value="MULTI_SELECT">Multi Select</MenuItem>
            </Select>
          </FormControl>
        </Grid>

        {(this.state.type === 'SINGLE_SELECT' ||
          this.state.type === 'MULTI_SELECT') &&
          this.renderOptions()}
      </Grid>
    )
  }

  renderOptions() {
    const {classes} = this.props

    return (
      <Grid item sx={12}>
        <TextField
          className={classes.input}
          label="Options"
          value={this.state.options || ''}
          onChange={ev => {
            this.handleOptionsChange(ev.target.value.split(','))
          }}
        />
      </Grid>
    )
  }
}

export default withStyles(styles)(MemberForm)
