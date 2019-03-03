var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
const internalize = (it) => {
    return {
        name: it.name,
        email: it.email,
        authorities: it.authorities,
    };
};
export class AuthoritiesClient {
}
AuthoritiesClient.cache = {};
AuthoritiesClient.findAuthorities = () => __awaiter(this, void 0, void 0, function* () {
    return fetch(`/api/authorities`)
        .then((res) => __awaiter(this, void 0, void 0, function* () {
        return {
            page: parseInt(res.headers.get('x-page')),
            total: parseInt(res.headers.get('x-total')),
            data: yield res.json()
                .then(json => json.map(internalize)),
        };
    }));
});
