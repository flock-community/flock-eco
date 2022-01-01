import React, {useEffect, useState} from 'react'

import Grid from '@material-ui/core/Grid'

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
import {Specification} from './MemberModel'

const useStyles = makeStyles(theme => ({
  popoverContent: {
    width: 250,
    margin: theme.spacing(2),
  },
}))

type Filter = Pick<Specification, 'groups' | 'statuses'>
const init: Filter = {
  groups: [],
  statuses: ['NEW', 'ACTIVE', 'DISABLED'],
}

type MemberFilterState = {
  name: string[]
  anchorEl: any | null
  groups: {name: string; code: string}[]
  statuses: string[]
  filter: Filter
}

type MemberFilterProps = {
  filter: Filter
  onChange: (value: Filter) => void
}
export function MemberFilter({filter, onChange}: MemberFilterProps) {
  const classes = useStyles()

  const [state, setState] = useState<MemberFilterState>({
    name: [],
    anchorEl: null,
    groups: [],
    statuses: ['NEW', 'ACTIVE', 'DISABLED', 'DELETED', 'MERGED'],
    filter: init,
  })

  useEffect(() => {
    onChange?.(init)
  }, [])

  useEffect(() => {
    setState(prev => ({
      ...prev,
      filter: filter || init,
    }))
  }, [filter])

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

  const handleGroupsChange = event => {
    const filter = {
      ...state.filter,
      groups: event.target.value,
    }
    setState(state => ({
      ...state,
      filter,
    }))
    onChange?.(filter)
  }

  const handleStatusesChange = event => {
    const filter = {
      ...state.filter,
      statuses: event.target.value,
    }
    setState(state => ({
      ...state,
      filter,
    }))
    onChange?.(filter)
  }

  const handleFilterClick = event => {
    setState(state => ({
      ...state,
      anchorEl: event.currentTarget,
    }))
  }

  const handleFilterClose = () => {
    setState(state => ({
      ...state,
      anchorEl: null,
    }))
  }

  return (
    <>
      <Tooltip title="Filter list">
        <IconButton
          aria-owns={state.anchorEl ? 'simple-menu' : ''}
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
              <FormControl fullWidth>
                <InputLabel htmlFor="select-multiple-checkbox">
                  Groups
                </InputLabel>
                <Select
                  multiple
                  value={state.filter.groups}
                  onChange={handleGroupsChange}
                  input={<Input id="select-multiple-checkbox" />}
                  renderValue={(selected: string[]) =>
                    selected
                      .map(key =>
                        state.groups.find(group => group.code === key),
                      )
                      .map(group => group.name)
                      .join(', ')
                  }
                >
                  {state.groups.map(group => (
                    <MenuItem key={group.code} value={group.code}>
                      <Checkbox
                        checked={state.filter.groups.indexOf(group.code) > -1}
                      />
                      <ListItemText primary={group.name} />
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={12}>
              <FormControl fullWidth>
                <InputLabel htmlFor="select-multiple-checkbox">
                  Statuses
                </InputLabel>
                <Select
                  multiple
                  value={state.filter.statuses}
                  onChange={handleStatusesChange}
                  input={<Input id="select-multiple-checkbox" />}
                  renderValue={(selected: string[]) => selected.join(', ')}
                >
                  {state.statuses.map(status => (
                    <MenuItem key={status} value={status}>
                      <Checkbox
                        checked={state.filter.statuses.indexOf(status) > -1}
                      />
                      <ListItemText primary={status} />
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>
          </Grid>
        </div>
      </Popover>
    </>
  )
}
