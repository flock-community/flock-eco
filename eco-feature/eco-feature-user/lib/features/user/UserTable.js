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
import { Pageable } from '../../utils/Pagination';
class UserTable extends Component {
    constructor() {
        super(...arguments);
        this.state = {
            count: 0,
            page: 0,
            size: 10,
            list: [],
            item: null,
        };
        this.handleChangePage = (event, page) => {
            this.setState({ page }, () => {
                this.props.onChangePage && this.props.onChangePage(event, page);
                this.loadData();
            });
        };
        this.handleRowClick = (event, item) => {
            item.reference;
            if (this.props.onRowClick)
                return this.props.onRowClick(event, item);
        };
    }
    loadData() {
        return __awaiter(this, void 0, void 0, function* () {
            const { page, size } = this.state;
            const pageable = new Pageable(page, size);
            const res = yield UserClient.findUsers(pageable);
            return this.setState({
                list: res.data,
                page: res.page,
                count: res.total,
            });
        });
    }
    componentDidMount() {
        this.loadData();
    }
    render() {
        const { list, page, count, size } = this.state;
        return (React.createElement(Table, null,
            React.createElement(TableHead, null,
                React.createElement(TableRow, null,
                    React.createElement(TableCell, null, "Name"),
                    React.createElement(TableCell, null, "Email"),
                    React.createElement(TableCell, null, "Authorities"))),
            React.createElement(TableBody, null, list.map(it => (React.createElement(TableRow, { key: it.name, hover: true, onClick: ev => this.handleRowClick(ev, it) },
                React.createElement(TableCell, null, it.name),
                React.createElement(TableCell, null, it.email),
                React.createElement(TableCell, null, it.authorities.length))))),
            React.createElement(TableFooter, null,
                React.createElement(TableRow, null,
                    React.createElement(TablePagination, { count: count, rowsPerPage: size, page: page, rowsPerPageOptions: [], onChangePage: this.handleChangePage })))));
    }
}
export default UserTable;
