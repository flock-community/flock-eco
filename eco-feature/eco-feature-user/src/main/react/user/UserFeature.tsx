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

type UserFeature = {
  enablePassword?: boolean
}
export function UserFeature({enablePassword}) {
  const classes = useStyles()

  const [searchState, setSearchState] = useState<string>('')

  const [dialogState, setDialogState] = useState({
    open: false,
    id: null,
  })

  const [reload, setReload] = useState(false)

  const handleRowClick = item => {
    setDialogState({
      open: true,
      id: item.id,
    })
  }

  const handleSearchChange = ev => {
    setSearchState(ev.target.value)
  }

  const handleNewClick = () => {
    setDialogState({
      open: true,
      id: null,
    })
  }

  const handleComplete = () => {
    setDialogState({
      open: false,
      id: null,
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
        id={dialogState.id}
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
