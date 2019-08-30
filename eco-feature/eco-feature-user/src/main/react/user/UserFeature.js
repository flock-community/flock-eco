import React, {useEffect, useState} from 'react'
import AddIcon from '@material-ui/icons/Add'
import {UserTable} from './UserTable'
import {UserDialog} from './UserDialog'
import Paper from '@material-ui/core/es/Paper/Paper'
import Fab from '@material-ui/core/Fab'
import {makeStyles} from '@material-ui/styles'
import UserClient from './UserClient'

const useStyles = makeStyles(theme => ({
  root:{
    position: 'relative',
  },
  content:{
    height: '100%',
    overflow:'scroll'
  },
  tablePaper: {
    marginBottom: 50,
    height: '100%',
  },
  button: {
    position: 'absolute',
    right: 20,
    bottom: 20,
  },
}))

export function UserFeature() {

  const classes = useStyles()

  const [dialogState, setDialogState] = useState({
    open: false,
    code: null
  })

  const [reload, setReload] = useState(false)

  const handleRowClick = (ev, item) => {
    setDialogState({
      open: true,
      code: item.code
    })
  }

  const handleNewClick = () => {
    setDialogState({
      open: true,
      code: null
    })
  }

  const handleComplete = value => {
    setDialogState({
      open: false,
      code: null
    })
  }

  return (
    <div className={classes.root}>
      <div className={classes.content}>

        <UserTable
          onRowClick={handleRowClick}
        />
      </div>

      <UserDialog open={dialogState.open} code={dialogState.code} onComplete={handleComplete}/>

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
