export class Pageable {
    constructor(page, size) {
        this.page = page;
        this.size = size;
    }
    getQueryString() {
        return `page=${this.page}&size=${this.size}`;
    }
}
