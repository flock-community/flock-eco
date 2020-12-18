import React, {useEffect, useState} from 'react'

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
import makeStyles from '@material-ui/core/styles/makeStyles'

const useStyles = makeStyles(theme => ({
  popoverContent: {
    width: 250,
    margin: theme.spacing(2),
  },
}))

const init = {
  search: '',
  groups: [],
  statuses: ['NEW', 'ACTIVE', 'DISABLED'],
}

export function MemberFilter({onChange, specification}) {
  const classes = useStyles()

  const [state, setState] = useState({
    name: [],
    anchorEl: null,
    groups: [],
    statuses: ['NEW', 'ACTIVE', 'DISABLED', 'DELETED', 'MERGED'],
    specification: init,
  })

  useEffect(() => {
    onChange && onChange(init)
  }, [])

  useEffect(() => {
    setState(prev => ({
      ...prev,
      specification: specification || init,
    }))
  }, [specification])

  useEffect(() => {
    fetch(`/api/member_groups`)
      .then(res => {
        return res.json()
      })
      .then(json => {
        setState(state => ({
          ...state,
          groups: json,
        }))
      })
  }, [])

  const handleSearchChange = event => {
    const specification = {
      ...state.specification,
      ...{search: event.target.value},
    }
    setState(state => ({
      ...state,
      specification,
    }))
    onChange && onChange(specification)
  }

  const handleGroupsChange = name => event => {
    const specification = {
      ...state.specification,
      ...{groups: event.target.value},
    }
    setState(state => ({
      ...state,
      specification,
    }))
    onChange && onChange(specification)
  }

  const handleStatusesChange = name => event => {
    const specification = {
      ...state.specification,
      ...{statuses: event.target.value},
    }
    setState(state => ({
      ...state,
      specification,
    }))
    onChange && onChange(specification)
  }

  const handleFilterClick = event => {
    setState(state => ({
      ...state,
      anchorEl: event.currentTarget,
    }))
  }

  const handleFilterClose = event => {
    setState(state => ({
      ...state,
      anchorEl: null,
    }))
  }

  const renderStatusesSelect = () => (
    <FormControl className={classes.formControl} fullWidth>
      <InputLabel htmlFor="select-multiple-checkbox">Statuses</InputLabel>
      <Select
        multiple
        value={state.specification.statuses}
        onChange={handleStatusesChange()}
        input={<Input id="select-multiple-checkbox" />}
        renderValue={selected => selected.join(', ')}
      >
        {state.statuses.map(status => (
          <MenuItem key={status} value={status}>
            <Checkbox
              checked={state.specification.statuses.indexOf(status) > -1}
            />
            <ListItemText primary={status} />
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  )

  const renderGroupsSelect = () => (
    <FormControl className={classes.formControl} fullWidth>
      <InputLabel htmlFor="select-multiple-checkbox">Groups</InputLabel>
      <Select
        multiple
        value={state.specification.groups}
        onChange={handleGroupsChange()}
        input={<Input id="select-multiple-checkbox" />}
        renderValue={selected =>
          selected
            .map(key => state.groups.find(group => group.code === key))
            .map(group => group.name)
            .join(', ')
        }
      >
        {state.groups.map(group => (
          <MenuItem key={group.code} value={group.code}>
            <Checkbox
              checked={state.specification.groups.indexOf(group.code) > -1}
            />
            <ListItemText primary={group.name} />
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  )
  return (
    <Grid container>
      <Grid item style={{flexGrow: 1}}>
        <TextField
          fullWidth
          label="Search"
          value={state.specification.search}
          onChange={handleSearchChange}
        />
      </Grid>
      <Grid item>
        <Tooltip title="Filter list">
          <IconButton
            aria-owns={state.anchorEl ? 'simple-menu' : undefined}
            aria-haspopup="true"
            onClick={handleFilterClick}
          >
            <FilterListIcon />
          </IconButton>
        </Tooltip>
        <Popover
          id="filter-menu"
          anchorEl={state.anchorEl}
          open={Boolean(state.anchorEl)}
          onClose={handleFilterClose}
        >
          <div className={classes.popoverContent}>
            <Grid container spacing={1}>
              <Grid item xs={12}>
                {renderGroupsSelect()}
              </Grid>
              <Grid item xs={12}>
                {renderStatusesSelect()}
              </Grid>
            </Grid>
          </div>
        </Popover>
      </Grid>
    </Grid>
  )
}
