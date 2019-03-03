var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
import * as React from 'react';
import { Component } from 'react';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableFooter from '@material-ui/core/TableFooter';
import TableRow from '@material-ui/core/TableRow';
import TablePagination from '@material-ui/core/TablePagination';
import { UserClient } from '../../clients/userClient';
class UserTable extends Component {
    constructor() {
        super(...arguments);
        this.handleChangePage = (event, page) => {
            if (this.props.onChangePage)
                return this.props.onChangePage(event, page);
        };
        this.handleRowClick = (event, user) => {
            if (this.props.onRowClick)
                return this.props.onRowClick(event, user);
        };
    }
    loadData() {
        return __awaiter(this, void 0, void 0, function* () {
            const list = yield UserClient.findUsers()
                .then(it => it.data);
            return this.setState({ list });
        });
    }
    componentDidMount() {
        this.loadData();
    }
    render() {
        const { list, page, count } = this.state;
        return (<Table>
        <TableHead>
          <TableRow>
            <TableCell>Name</TableCell>
            <TableCell>Email</TableCell>
            <TableCell>Authorities</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {list.map(it => (<TableRow key={it.name} hover onClick={ev => this.handleRowClick(ev, it)}>
              <TableCell component="th" scope="row">
                {it.name}
              </TableCell>
              <TableCell>{it.email}</TableCell>
              <TableCell>{it.authorities.length}</TableCell>
            </TableRow>))}
        </TableBody>
        <TableFooter>
          <TableRow>
            <TablePagination count={count} rowsPerPage={this.props.size} page={page} rowsPerPageOptions={[]} onChangePage={this.handleChangePage}/>
          </TableRow>
        </TableFooter>
      </Table>);
    }
}
export default UserTable;
