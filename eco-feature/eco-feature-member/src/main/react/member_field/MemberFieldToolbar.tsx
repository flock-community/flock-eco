import React, {useState} from 'react'
import classNames from 'classnames'
import Box from '@material-ui/core/Box'
import Toolbar from '@material-ui/core/Toolbar'
import Typography from '@material-ui/core/Typography'
import Tooltip from '@material-ui/core/Tooltip'
import IconButton from '@material-ui/core/IconButton'
import MergeIcon from '@material-ui/icons/CallMerge'
import AddIcon from '@material-ui/icons/Add'
import {createStyles, lighten, makeStyles} from '@material-ui/core/styles'
import {Search} from '@flock-community/flock-eco-core'
import {MemberFilter} from './MemberFilter'
import {Specification} from './MemberModel'

const useStyles = makeStyles(theme =>
  createStyles({
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
  }),
)

type MemberFieldToolbarProps = {
  specification: Specification
  selectedIds: string[]
  onMergeMembers: (memberIds: string[]) => void
  onAddMembers: () => void
  onSpecificationChange: (specification: Specification) => void
}

export function MemberFieldToolbar({
  specification,
  selectedIds,
  onMergeMembers,
  onAddMembers,
  onSpecificationChange,
}: MemberFieldToolbarProps) {
  const classes = useStyles()
  const numSelected = selectedIds.length

  const [state, setState] = useState<Specification>(specification)

  const handleSearchChange = (search: String) => {
    const value = {...state, search}
    setState(value)
    onSpecificationChange(value)
  }

  const handleFilterChange = (
    filter: Pick<Specification, 'groups' | 'statuses'>,
  ) => {
    const value = {...state, ...filter}
    setState(value)
    onSpecificationChange(value)
  }

  return (
    <Toolbar
      className={classNames(classes.root, {
        [classes.highlight]: numSelected > 0,
      })}
    >
      <Box className={classes.title}>
        {numSelected > 0 ? (
          <Typography color="inherit" variant="subtitle1">
            {numSelected} selected
          </Typography>
        ) : (
          <Typography variant="h6" id="tableTitle">
            Members
          </Typography>
        )}
      </Box>
      <Box className={classes.spacer} />
      <Box className={classes.actions}>
        <Search onChange={handleSearchChange} />
      </Box>

      <Box className={classes.actions}>
        <MemberFilter onChange={handleFilterChange} />
      </Box>
      {onAddMembers && (
        <Box className={classes.actions}>
          <Tooltip title="Add">
            <IconButton
              aria-label="Add"
              onClick={() => onAddMembers && onAddMembers()}
            >
              <AddIcon />
            </IconButton>
          </Tooltip>
        </Box>
      )}
      {onMergeMembers && numSelected >= 2 && (
        <Box className={classes.actions}>
          <Tooltip title="Merge">
            <IconButton
              aria-label="Merge"
              onClick={() => onMergeMembers && onMergeMembers(selectedIds)}
            >
              <MergeIcon />
            </IconButton>
          </Tooltip>
        </Box>
      )}
    </Toolbar>
  )
}
