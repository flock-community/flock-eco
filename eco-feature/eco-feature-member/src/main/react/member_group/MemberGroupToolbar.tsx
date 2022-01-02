import React from 'react'
import Box from '@material-ui/core/Box'
import Toolbar from '@material-ui/core/Toolbar'
import Typography from '@material-ui/core/Typography'
import Tooltip from '@material-ui/core/Tooltip'
import IconButton from '@material-ui/core/IconButton'
import AddIcon from '@material-ui/icons/Add'
import {createStyles, lighten, makeStyles} from '@material-ui/core/styles'

const useStyles = makeStyles(theme =>
  createStyles({
    root: {
      paddingLeft: theme.spacing(2),
      paddingRight: theme.spacing(1),
    },
    spacer: {
      flex: '1 1 100%',
    },
    actions: {
      color: theme.palette.text.secondary,
    },
    title: {
      flex: '0 0 auto',
    },
  }),
)

type MemberFieldToolbarProps = {
  onAdd: () => void
}

export function MemberGroupToolbar({onAdd}: MemberFieldToolbarProps) {
  const classes = useStyles()

  return (
    <Toolbar className={classes.root}>
      <Box className={classes.title}>
        <Typography variant="h6" id="tableTitle">
          Groups
        </Typography>
      </Box>
      <Box className={classes.spacer} />
      <Box className={classes.actions}>
        <Tooltip title="Add">
          <IconButton aria-label="Add" onClick={() => onAdd?.()}>
            <AddIcon />
          </IconButton>
        </Tooltip>
      </Box>
    </Toolbar>
  )
}
