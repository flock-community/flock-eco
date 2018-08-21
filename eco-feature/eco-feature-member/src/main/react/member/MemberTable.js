import React from "react";

import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableFooter from '@material-ui/core/TableFooter';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import TablePagination from '@material-ui/core/TablePagination';

class MemberTable extends React.Component {

  constructor(props){
    super();

    this.handleChangePage = (event, page) => {
      if (props.onChangePage)
        return props.onChangePage(event, page)
    }

    this.handleRowClick = (event, user) => {
      if (this.props.onRowClick)
        return this.props.onRowClick(event, user)
    }
  }

  render() {

    const {data, page, count} = this.props;

    return (
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Name</TableCell>
            <TableCell>Email</TableCell>
          </TableRow>
        </TableHead>

        <TableBody>{data.map(it => (
          <TableRow
            key={it.id}
            hover
            onClick={event => this.handleRowClick(it)}
          >
            <TableCell component="th" scope="row">{this.memberToName(it)}</TableCell>
            <TableCell>{it.email}</TableCell>
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
    )
  }





  memberToName(it) {
    if (it.infix) {
      return `${it.firstName} ${it.infix} ${it.surName}`
    }
    return `${it.firstName} ${it.surName}`
  }

}

export default MemberTable;