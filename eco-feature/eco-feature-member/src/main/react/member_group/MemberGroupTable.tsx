import React from 'react'

import Table from '@material-ui/core/Table'
import TableBody from '@material-ui/core/TableBody'
import TableCell from '@material-ui/core/TableCell'
import TableHead from '@material-ui/core/TableHead'
import TableRow from '@material-ui/core/TableRow'
import {MemberGroup} from './MemberGroupClient'
import {makeStyles, Theme} from '@material-ui/core'

const useStyles = makeStyles((theme: Theme) => ({
  row: {
    cursor: 'pointer',
  },
  cell: {
    color: theme.palette.grey[400],
  },
}))

type MemberGroupTableProps = {
  list: MemberGroup[]
  onRowClick?: (item: MemberGroup) => void
}
export const MemberGroupTable = ({list, onRowClick}: MemberGroupTableProps) => {
  const classes = useStyles()
  return (
    <Table>
      <TableHead>
        <TableRow>
          <TableCell>Code</TableCell>
          <TableCell>Name</TableCell>
        </TableRow>
      </TableHead>
      <TableBody>
        {list.map((it) => (
          <TableRow
            className={classes.row}
            key={it.code}
            hover
            onClick={() => {
              onRowClick?.(it)
            }}
          >
            <TableCell component="th" scope="row">
              {it.code}
            </TableCell>
            <TableCell>{it.name}</TableCell>
          </TableRow>
        ))}
      </TableBody>
    </Table>
  )
}
