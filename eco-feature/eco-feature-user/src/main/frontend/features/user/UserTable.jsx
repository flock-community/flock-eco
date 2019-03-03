"use strict";
var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
exports.__esModule = true;
var react_1 = require("react");
var Table_1 = require("@material-ui/core/Table");
var TableBody_1 = require("@material-ui/core/TableBody");
var TableCell_1 = require("@material-ui/core/TableCell");
var TableHead_1 = require("@material-ui/core/TableHead");
var TableFooter_1 = require("@material-ui/core/TableFooter");
var TableRow_1 = require("@material-ui/core/TableRow");
var TablePagination_1 = require("@material-ui/core/TablePagination");
var UserTable = /** @class */ (function (_super) {
    __extends(UserTable, _super);
    function UserTable() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.handleChangePage = function (event, page) {
            if (_this.props.onChangePage)
                return _this.props.onChangePage(event, page);
        };
        _this.handleRowClick = function (event, user) {
            if (_this.props.onRowClick)
                return _this.props.onRowClick(event, user);
        };
        return _this;
    }
    UserTable.prototype.render = function () {
        var _this = this;
        var _a = this.props, data = _a.data, page = _a.page, count = _a.count;
        if (data == null)
            return null;
        return (<Table_1.default>
        <TableHead_1.default>
          <TableRow_1.default>
            <TableCell_1.default>Name</TableCell_1.default>
            <TableCell_1.default>Email</TableCell_1.default>
            <TableCell_1.default>Authorities</TableCell_1.default>
          </TableRow_1.default>
        </TableHead_1.default>
        <TableBody_1.default>
          {data.map(function (it) { return (<TableRow_1.default key={it.name} hover onClick={function (event) { return _this.handleRowClick(it); }}>
              <TableCell_1.default component="th" scope="row">
                {it.name}
              </TableCell_1.default>
              <TableCell_1.default>{it.email}</TableCell_1.default>
              <TableCell_1.default>{it.authorities.length}</TableCell_1.default>
            </TableRow_1.default>); })}
        </TableBody_1.default>
        <TableFooter_1.default>
          <TableRow_1.default>
            <TablePagination_1.default count={count} rowsPerPage={this.props.size} page={page} rowsPerPageOptions={[]} onChangePage={this.handleChangePage}/>
          </TableRow_1.default>
        </TableFooter_1.default>
      </Table_1.default>);
    };
    return UserTable;
}(react_1.Component));
exports["default"] = UserTable;
