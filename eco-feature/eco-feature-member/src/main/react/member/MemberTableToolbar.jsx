import React from 'react'
import classNames from 'classnames'
import Toolbar from '@material-ui/core/Toolbar'
import Typography from '@material-ui/core/Typography'
import Tooltip from '@material-ui/core/Tooltip'
import IconButton from '@material-ui/core/IconButton'
import MergeIcon from '@material-ui/icons/CallMerge'
import {lighten} from '@material-ui/core/es/styles/colorManipulator'
import makeStyles from '@material-ui/core/styles/makeStyles'

const useStyles = makeStyles(theme => ({
  root: {
    paddingRight: theme.spacing(1),
  },
  highlight:
    theme.palette.type === 'light'
      ? {
          color: theme.palette.secondary.main,
          backgroundColor: lighten(theme.palette.secondary.light, 0.85),
        }
      : {
          color: theme.palette.text.primary,
          backgroundColor: theme.palette.secondary.dark,
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
}))

export function MemberTableToolbar({onMergeMembers, selectedIds}) {
  const classes = useStyles()
  const numSelected = selectedIds.length
  return (
    <Toolbar
      className={classNames(classes.root, {
        [classes.highlight]: numSelected > 0,
      })}
    >
      <div className={classes.title}>
        {numSelected > 0 ? (
          <Typography color="inherit" variant="subtitle1">
            {numSelected} selected
          </Typography>
        ) : (
          <Typography variant="h6" id="tableTitle">
            Members
          </Typography>
        )}
      </div>
      <div className={classes.spacer} />
      <div className={classes.actions}>
        {onMergeMembers && numSelected >= 2 && (
          <Tooltip title="Merge">
            <IconButton
              aria-label="Merge"
              onClick={() => onMergeMembers && onMergeMembers(selectedIds)}
            >
              <MergeIcon />
            </IconButton>
          </Tooltip>
        )}
      </div>
    </Toolbar>
  )
}
