import React, {useState} from 'react'

import AddIcon from '@material-ui/icons/Add'

import {UserGroupTable} from './UserGroupTable'
import {UserGroupDialog} from './UserGroupDialog'
import Fab from '@material-ui/core/Fab'
import makeStyles from '@material-ui/core/styles/makeStyles'
import Grid from '@material-ui/core/Grid'
import {TextField} from '@material-ui/core'
import Paper from '@material-ui/core/Paper'

const useStyles = makeStyles(theme => ({
  root: {
    height: '100%',
    position: 'relative',
  },
  content: {
    height: '100%',
    overflowX: 'hidden',
    padding: theme.spacing(1),
  },
  spacer: {
    height: theme.spacing(8),
  },
  paper: {
    padding: theme.spacing(2),
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

  const handleSearchChange = ev => {
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
        <Grid container spacing={1}>
          <Grid item xs={12}>
            <Paper className={classes.paper}>
              <TextField
                fullWidth
                label="Search"
                value={searchState}
                onChange={handleSearchChange}
              />
            </Paper>
          </Grid>
          <Grid item xs={12}>
            <Paper>
              <UserGroupTable onRowClick={handleRowClick} reload={reload} />
            </Paper>
          </Grid>
        </Grid>
        <div className={classes.spacer} />
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
        onClick={handleNewClick}
      >
        <AddIcon />
      </Fab>
    </div>
  )
}
