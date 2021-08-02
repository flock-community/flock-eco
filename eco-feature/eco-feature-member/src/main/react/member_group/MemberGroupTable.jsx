import React from 'react'

import Table from '@material-ui/core/Table'
import TableBody from '@material-ui/core/TableBody'
import TableCell from '@material-ui/core/TableCell'
import TableHead from '@material-ui/core/TableHead'
import TableRow from '@material-ui/core/TableRow'

class MemberTable extends React.Component {
  render() {
    const {list} = this.props

    return (
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Code</TableCell>
            <TableCell>Name</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {list.map(it => (
            <TableRow
              key={it.code}
              hover
              onClick={event => this.handleClick(it)}
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

  handleClick(event, item) {
    if (this.props.handleRowClick) return this.props.handleRowClick(event, item)
  }
}

export default MemberTable
