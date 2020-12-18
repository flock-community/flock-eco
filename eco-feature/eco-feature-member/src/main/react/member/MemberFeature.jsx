import React, {useState} from 'react'

import Fab from '@material-ui/core/Fab'
import Card from '@material-ui/core/Card'
import CardContent from '@material-ui/core/CardContent'

import Grid from '@material-ui/core/Grid'

import AddIcon from '@material-ui/icons/Add'

import {MemberTable} from './MemberTable'
import {MemberFilter} from './MemberFilter'
import {MemberDialog} from './MemberDialog'
import {MemberMerger} from './MemberMerger'
import makeStyles from '@material-ui/core/styles/makeStyles'

const useStyles = makeStyles(theme => ({
  root: {
    paddingBottom: theme.spacing(10),
  },

  button: {
    position: 'fixed',
    right: theme.spacing(2),
    bottom: theme.spacing(2),
    margin: theme.spacing(1),
  },
}))

export function MemberFeature({search}) {
  const classes = useStyles()

  const [state, setState] = useState({
    page: 0,
    size: 10,
    search: search || '',
    openDialog: false,
    item: null,
    filter: {},
    refresh: false,
    mergeMemberIds: null,
    specification: null,
  })

  const handleRowClick = item => {
    setState({
      ...state,
      item,
      openDialog: true,
    })
  }

  const handleNewClick = () => {
    setState(prev => ({
      ...prev,
      item: null,
      openDialog: true,
    }))
  }

  const handleFilterChange = specification => {
    setState(prev => ({
      ...prev,
      page: 0,
      specification,
    }))
  }

  const handleComplete = () => {
    setState(prev => ({
      ...prev,
      item: null,
      openDialog: false,
      refresh: !prev.refresh,
      mergeMemberIds: null,
    }))
  }

  const handleMergeComplete = () =>
    setState(prev => ({
      ...prev,
      mergeMemberIds: null,
      refresh: !prev.refresh,
    }))

  const mergeMembers = mergeMemberIds =>
    setState(prev => ({
      ...prev,
      mergeMemberIds,
    }))

  const handleMergerCancel = () =>
    setState(prev => ({
      ...prev,
      mergeMemberIds: null,
    }))

  return (
    <div className={classes.root}>
      <Grid container direction="column" spacing={1}>
        <Grid item>
          <Card>
            <CardContent>
              <MemberFilter
                specification={state.specification}
                onChange={handleFilterChange}
              />
            </CardContent>
          </Card>
        </Grid>
        <Grid item>
          <Card>
            <MemberTable
              specification={state.specification}
              page={state.page}
              size={state.size}
              refresh={state.refresh}
              onRowClick={handleRowClick}
              onMergeMembers={mergeMembers}
            />
          </Card>
        </Grid>
      </Grid>

      <MemberDialog
        id={state.item && state.item.id}
        open={state.openDialog}
        onComplete={handleComplete}
      />

      <MemberMerger
        mergeMemberIds={state.mergeMemberIds}
        onComplete={handleMergeComplete}
        onCancel={handleMergerCancel}
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
