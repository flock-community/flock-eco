import React from "react";

import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableFooter from '@material-ui/core/TableFooter';
import TableRow from '@material-ui/core/TableRow';
import TablePagination from '@material-ui/core/TablePagination';

import Paper from '@material-ui/core/Paper';

class UserTable extends React.Component {

  handleChangePage = (event, page) => {
    if (this.props.onChangePage)
      return this.props.onChangePage(event, page)
  }

  handleRowClick = (event, user) => {
    if (this.props.onRowClick)
      return this.props.onRowClick(event, user)
  }


  render() {

    const {data, page, count} = this.props;

    if(data == null)
      return null

    return (
      <Paper>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Name</TableCell>
              <TableCell>Email</TableCell>
              <TableCell>Authorities</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>{data.map(it => (
            <TableRow
              key={it.name}
              hover
              onClick={event => this.handleRowClick(it)}
            >
              <TableCell component="th" scope="row">{it.name}</TableCell>
              <TableCell>{it.email}</TableCell>
              <TableCell>{it.authorities.length}</TableCell>
            </TableRow>
          ))}
          </TableBody>
          <TableFooter>
            <TableRow>
              <TablePagination
                count={count}
                rowsPerPage={this.props.size}
                page={page}
                rowsPerPageOptions={[]}
                onChangePage={this.handleChangePage}
              />
            </TableRow>
          </TableFooter>
        </Table>
      </Paper>
    )
  }
}

export default UserTable;