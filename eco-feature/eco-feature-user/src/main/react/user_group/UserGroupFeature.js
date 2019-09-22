import React, {useState} from 'react'

import AddIcon from '@material-ui/icons/Add'

import {UserGroupTable} from './UserGroupTable'
import {UserGroupDialog} from './UserGroupDialog'
import Fab from '@material-ui/core/Fab'
import makeStyles from '@material-ui/core/styles/makeStyles'
import {MultipleSelect} from 'react-select-material-ui'
import Grid from '@material-ui/core/Grid'
import {TextField} from '@material-ui/core'

const useStyles = makeStyles(theme => ({
  root: {
    height: '100%',
    position: 'relative',
  },
  content: {
    height: '100%',
    overflow: 'auto'
  },
  button: {
    position: 'absolute',
    right: theme.spacing(1),
    bottom: theme.spacing(1),
  },
}))


export function UserGroupFeature() {
  const classes = useStyles()

  const [reload, setReload] = useState(false)

  const [searchState, setSearchState] = useState('')

  const [dialogState, setDialogState] = useState({
    open: false,
    code: null,
  })

  const handleSearchChange = (ev) => {
    setSearchState(ev.target.value)
  }

  const handleRowClick = (ev, item) => {
    setDialogState({
      open: true,
      code: item.code,
    })
  }

  const handleNewClick = () => {
    setDialogState({
      open: true,
      code: null,
    })
  }

  const handleComplete = value => {
    setDialogState({
      open: false,
      code: null,
    })
    setReload(!reload)
  }

  const handleFormUpdate = value => {
    this.setState({item: value})
  }


  return (
    <div className={classes.root}>

      <div className={classes.content}>
        <Grid container>
          <Grid item xs={12}>
            <TextField
              fullWidth
              label="Search"
              value={searchState}
              onChange={handleSearchChange}/>
          </Grid>
          <Grid item xs={12}>
            <UserGroupTable
              onRowClick={handleRowClick}
              reload={reload}/>
          </Grid>
        </Grid>

      </div>

      <UserGroupDialog
        code={dialogState.code}
        open={dialogState.open}
        onComplete={handleComplete}
      />

      <Fab
        color="primary"
        aria-label="Add"
        className={classes.button}
        onClick={handleNewClick}>
        <AddIcon/>
      </Fab>

    </div>
  )
}
