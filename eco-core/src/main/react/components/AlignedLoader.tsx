import React from 'react'
import {CircularProgress, Grid, Theme} from '@material-ui/core'
import {makeStyles} from '@material-ui/core/styles'

const useStyles = makeStyles(theme => ({
  root: {
    height: '100%',
  },
}))

type AlignedLoaderProps = {
  height?: number
}

export function AlignedLoader({height}: AlignedLoaderProps) {
  const classes = useStyles()

  return (
    <Grid
      style={{height}}
      className={classes.root}
      container
      alignItems="center"
      item
      justify="center"
    >
      <Grid>
        <CircularProgress />
      </Grid>
    </Grid>
  )
}
