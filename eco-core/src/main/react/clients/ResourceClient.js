"use strict";
exports.__esModule = true;
exports.ResourceClient = void 0;
var utils_1 = require("./utils");
function ResourceClient(path) {
    var checkResponse = function (it) {
        if (it) {
            return it;
        }
        else {
            throw new Error('No response');
        }
    };
    var all = function () {
        var opts = {
            method: 'GET'
        };
        return fetch("".concat(path), opts)
            .then(function (it) { return (0, utils_1.validateResponse)(it); })
            .then(checkResponse);
    };
    var get = function (id) {
        var opts = {
            method: 'GET'
        };
        return fetch("".concat(path, "/").concat(id), opts)
            .then(function (it) { return (0, utils_1.validateResponse)(it); })
            .then(checkResponse);
    };
    var post = function (input) {
        var opts = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(input)
        };
        return fetch(path, opts)
            .then(function (it) { return (0, utils_1.validateResponse)(it); })
            .then(checkResponse);
    };
    var put = function (id, input) {
        var opts = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(input)
        };
        return fetch("".concat(path, "/").concat(id), opts)
            .then(function (it) { return (0, utils_1.validateResponse)(it); })
            .then(checkResponse);
    };
    var del = function (id) {
        var opts = {
            method: 'DELETE'
        };
        return fetch("".concat(path, "/").concat(id), opts)
            .then(function (it) { return (0, utils_1.validateResponse)(it); })
            .then(function () { return undefined; });
    };
    return { all: all, get: get, post: post, put: put, "delete": del };
}
exports.ResourceClient = ResourceClient;
