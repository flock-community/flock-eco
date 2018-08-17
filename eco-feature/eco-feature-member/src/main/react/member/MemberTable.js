import React from "react";

import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableFooter from '@material-ui/core/TableFooter';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import TablePagination from '@material-ui/core/TablePagination';

import Paper from '@material-ui/core/Paper';

class MemberTable extends React.Component {


  render() {

    const {data} = this.props;

    return (
      <Paper>
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
              onClick={event => this.handleClick(it)}
            >
              <TableCell component="th" scope="row">{this.memberToName(it)}</TableCell>
              <TableCell>{it.email}</TableCell>
            </TableRow>
          ))}
          </TableBody>
          <TableFooter>
            <TableRow>
              <TablePagination
                colSpan={0}
                count={14}
                rowsPerPage={25}
                page={0}
                rowsPerPageOptions={[]}
                onChangePage={this.handleChangePage}
              />
            </TableRow>
          </TableFooter>
        </Table>
      </Paper>
    )
  }

  handleClick(event, user) {
    if(this.props.handleRowClick)
      return this.props.handleRowClick(event, user)
    console.log(user)
  }

  handleChangePage(){

  }

  memberToName(it){
    if(it.infix){
      return `${it.firstName} ${it.infix} ${it.surName}`
    }
    return `${it.firstName} ${it.surName}`
  }

}

export default MemberTable;