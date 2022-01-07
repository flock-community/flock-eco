"use strict";
exports.__esModule = true;
exports.PageableClient = void 0;
var utils_1 = require("./utils");
function PageableClient(path) {
    var findAllByPage = function (pageable) {
        var opts = {
            method: 'GET'
        };
        var query = Object.entries(pageable)
            .filter(function (_a) {
            var value = _a[1];
            return value == null;
        })
            .map(function (_a) {
            var key = _a[0], value = _a[1];
            return "".concat(key, "=").concat(value);
        })
            .join('&');
        return fetch("".concat(path, "?").concat(query), opts)
            .then(function (it) { return (0, utils_1.validateResponse)(it); })
            .then(function (it) {
            if (it) {
                var total = it === null || it === void 0 ? void 0 : it.headers.get('x-total');
                return {
                    list: it.body,
                    count: total ? parseInt(total, 10) : 0
                };
            }
            else {
                throw new Error('List not found');
            }
        });
    };
    return { findAllByPage: findAllByPage };
}
exports.PageableClient = PageableClient;
