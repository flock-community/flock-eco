"use strict";
exports.__esModule = true;
exports.validateResponse = void 0;
var validateResponse = function (res) {
    if (res.ok) {
        if (res.status === 204) {
            return Promise.resolve(undefined);
        }
        return res.json().then(function (body) { return ({
            status: res.status,
            headers: res.headers,
            body: body
        }); });
    }
    return res.text().then(function (text) {
        throw new Error(text);
    });
};
exports.validateResponse = validateResponse;
