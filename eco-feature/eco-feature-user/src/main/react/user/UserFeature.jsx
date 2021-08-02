import React, {useState} from 'react'
import AddIcon from '@material-ui/icons/Add'
import {UserTable} from './UserTable'
import {UserDialog} from './UserDialog'
import Fab from '@material-ui/core/Fab'
import Grid from '@material-ui/core/Grid'
import {TextField} from '@material-ui/core'
import makeStyles from '@material-ui/core/styles/makeStyles'
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

export function UserFeature({enablePassword}) {
  const classes = useStyles()

  const [searchState, setSearchState] = useState('')

  const [dialogState, setDialogState] = useState({
    open: false,
    code: null,
  })

  const [reload, setReload] = useState(false)

  const handleRowClick = (ev, item) => {
    setDialogState({
      open: true,
      code: item.code,
    })
  }

  const handleSearchChange = ev => {
    setSearchState(ev.target.value)
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
              <UserTable
                reload={reload}
                search={searchState}
                onRowClick={handleRowClick}
              />
            </Paper>
          </Grid>
        </Grid>
        <div className={classes.spacer} />
      </div>

      <UserDialog
        open={dialogState.open}
        code={dialogState.code}
        onComplete={handleComplete}
        enablePassword={enablePassword}
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
