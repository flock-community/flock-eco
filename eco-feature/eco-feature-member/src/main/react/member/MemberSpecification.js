import React from 'react'

import {withStyles} from '@material-ui/core/styles'

import Grid from '@material-ui/core/Grid'

import TextField from '@material-ui/core/TextField'

import IconButton from '@material-ui/core/IconButton'
import Tooltip from '@material-ui/core/Tooltip'

import FormControl from '@material-ui/core/FormControl'
import InputLabel from '@material-ui/core/InputLabel'
import Select from '@material-ui/core/Select'
import Input from '@material-ui/core/Input'
import Checkbox from '@material-ui/core/Checkbox'

import Popover from '@material-ui/core/Popover'
import MenuItem from '@material-ui/core/MenuItem'
import ListItemText from '@material-ui/core/ListItemText'

import FilterListIcon from '@material-ui/icons/FilterList'

const styles = theme => ({
  popoverContent: {
    width: 250,
    margin: theme.spacing.unit * 2,
  },
})

class MemberSpecification extends React.Component {
  state = {
    name: [],
    anchorEl: null,
    groups: [],
    statuses: ['NEW', 'ACTIVE', 'DISABLED', 'DELETED', 'MERGED'],
    specifications: {
      search: [''],
      groups: [],
      statuses: [],
    },
  }

  componentDidMount() {
    fetch(`/api/member_groups`)
      .then(res => {
        return res.json()
      })
      .then(json => {
        this.setState({groups: json})
      })
  }

  handleSearchChange = event => {
    const specifications = {
      ...this.state.specifications,
      ...{search: [event.target.value]},
    }
    this.setState({specifications}, this.handleOnChange)
  }

  handleGroupsChange = name => event => {
    const specifications = {
      ...this.state.specifications,
      ...{groups: event.target.value},
    }
    this.setState({specifications}, this.handleOnChange)
  }

  handleStatusesChange = name => event => {
    const specifications = {
      ...this.state.specifications,
      ...{statuses: event.target.value},
    }
    this.setState({specifications}, this.handleOnChange)
  }

  handleOnChange = () => {
    this.props.onChange && this.props.onChange(this.state.specifications)
  }

  handleFilterClick = event => {
    this.setState({anchorEl: event.currentTarget})
  }

  handleFilterClose = event => {
    this.setState({anchorEl: null})
  }

  render() {
    const {classes} = this.props

    return (
      <Grid container>
        <Grid item style={{flexGrow: 1}}>
          <TextField
            fullWidth
            label="Search"
            value={this.state.specifications.search[0]}
            onChange={this.handleSearchChange}
          />
        </Grid>
        <Grid item>
          <Tooltip title="Filter list">
            <IconButton
              aria-owns={this.state.anchorEl ? 'simple-menu' : undefined}
              aria-haspopup="true"
              onClick={this.handleFilterClick}
            >
              <FilterListIcon />
            </IconButton>
          </Tooltip>
          <Popover
            id="filter-menu"
            anchorEl={this.state.anchorEl}
            open={Boolean(this.state.anchorEl)}
            onClose={this.handleFilterClose}
          >
            <div className={classes.popoverContent}>
              <Grid container spacing={1}>
                <Grid item xs={12}>
                  {this.renderGroupsSelect()}
                </Grid>
                <Grid item xs={12}>
                  {this.renderStatusesSelect()}
                </Grid>
              </Grid>
            </div>
          </Popover>
        </Grid>
      </Grid>
    )
  }

  renderStatusesSelect() {
    const {classes} = this.props

    return (
      <FormControl className={classes.formControl} fullWidth>
        <InputLabel htmlFor="select-multiple-checkbox">Statuses</InputLabel>
        <Select
          multiple
          value={this.state.specifications.statuses}
          onChange={this.handleStatusesChange()}
          input={<Input id="select-multiple-checkbox" />}
          renderValue={selected => selected.join(', ')}
        >
          {this.state.statuses.map(status => (
            <MenuItem key={status} value={status}>
              <Checkbox
                checked={
                  this.state.specifications.statuses.indexOf(status) > -1
                }
              />
              <ListItemText primary={status} />
            </MenuItem>
          ))}
        </Select>
      </FormControl>
    )
  }

  renderGroupsSelect() {
    const {classes} = this.props

    return (
      <FormControl className={classes.formControl} fullWidth>
        <InputLabel htmlFor="select-multiple-checkbox">Groups</InputLabel>
        <Select
          multiple
          value={this.state.specifications.groups}
          onChange={this.handleGroupsChange()}
          input={<Input id="select-multiple-checkbox" />}
          renderValue={selected =>
            selected
              .map(key => this.state.groups.find(group => group.code === key))
              .map(group => group.name)
              .join(', ')
          }
        >
          {this.state.groups.map(group => (
            <MenuItem key={group.code} value={group.code}>
              <Checkbox
                checked={
                  this.state.specifications.groups.indexOf(group.code) > -1
                }
              />
              <ListItemText primary={group.name} />
            </MenuItem>
          ))}
        </Select>
      </FormControl>
    )
  }
}

export default withStyles(styles)(MemberSpecification)
