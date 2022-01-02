import React from 'react'

import Table from '@material-ui/core/Table'
import TableBody from '@material-ui/core/TableBody'
import TableCell from '@material-ui/core/TableCell'
import TableHead from '@material-ui/core/TableHead'
import TableRow from '@material-ui/core/TableRow'
import {MemberField} from './MemberFieldClient'
import {makeStyles, Theme} from '@material-ui/core'

const useStyles = makeStyles((theme: Theme) => ({
  row: {
    cursor: 'pointer',
  },
  cell: {
    color: theme.palette.grey[400],
  },
}))

type MemberTableProps = {
  list: MemberField[]
  onRowClick: (item: MemberField) => void
}
export const MemberFieldTable = ({list, onRowClick}: MemberTableProps) => {
  const classes = useStyles()

  function handleClick(item) {
    if (!item.disabled) {
      onRowClick?.(item)
    }
  }

  return (
    <Table>
      <TableHead>
        <TableRow>
          <TableCell>Name</TableCell>
          <TableCell>Label</TableCell>
          <TableCell>Type</TableCell>
        </TableRow>
      </TableHead>
      <TableBody>
        {list.map(it => (
          <TableRow
            className={classes.row}
            key={it.name}
            hover
            onClick={() => handleClick(it)}
          >
            <TableCell className={it.disabled ? classes.cell : 'null'}>
              {it.name}
            </TableCell>
            <TableCell className={it.disabled ? classes.cell : 'null'}>
              {it.label}
            </TableCell>
            <TableCell className={it.disabled ? classes.cell : 'null'}>
              {it.type}
            </TableCell>
          </TableRow>
        ))}
      </TableBody>
    </Table>
  )
}
