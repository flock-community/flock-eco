import React from "react";

import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableHead from "@material-ui/core/TableHead";
import TableFooter from "@material-ui/core/TableFooter";
import TableRow from "@material-ui/core/TableRow";
import { PageableClient } from "@flock-eco/core";
import TablePagination from "@material-ui/core/TablePagination";

class UserGroupTable extends React.Component {

  client = new PageableClient("/api/user_groups", { size: 25 });

  state = {
    list: [],
    count: 0,
    page: 0
  };

  handleChangePage = (event, page) => {
    this.setState({ page }, () => {
      this.loadList();
      this.props.onChangePage && this.props.onChangePage(event, page);
    });
  };

  handleRowClick = (ev) => (item) => {
    this.props.onRowClick && this.props.onRowClick(event, item);
  };

  componentDidMount() {
    this.loadList();
  }

  componentDidUpdate(prevProps) {
    if (prevProps.reload !== this.props.reload)
      this.loadList();
  }

  loadList = () => {
    const { page } = this.state;
    return this.client.findAll(page)
      .then(data => this.setState({
        list: data.list,
        count: data.total
      }));
  };

  render() {
    const { size } = this.client;
    const { list, count, page } = this.state;

    return (
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Name</TableCell>
            <TableCell>Users</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {list.map(it => (
            <TableRow
              key={it.name}
              hover
              onClick={ev => this.handleRowClick(ev)(it)}
            >
              <TableCell component="th" scope="row">
                {it.name}
              </TableCell>
              <TableCell>{it.users.length}</TableCell>
            </TableRow>
          ))}
        </TableBody>
        <TableFooter>
          <TableRow>
            <TablePagination
              count={count}
              rowsPerPage={size}
              page={page}
              rowsPerPageOptions={[]}
              onChangePage={this.handleChangePage}
            />
          </TableRow>
        </TableFooter>
      </Table>
    );
  }
}

export default UserGroupTable;
