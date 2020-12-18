export class PageableClient {
  url = null
  size = 20

  constructor(url, opts) {
    this.url = url
    if (opts && opts.size) this.size = opts.size
  }

  findAll = (page, params, opts) => {
    const qs = this._queryString({
      ...params,
      page: page,
      size: this.size,
    })
    return fetch(`${this.url}?${qs}`, opts).then(res => {
      return res.json().then(json => {
        console.log(res.headers.get('x-page'))
        return {
          page: Number(res.headers.get('x-page')),
          total: Number(res.headers.get('x-total')),
          list: json,
        }
      })
    })
  }

  _queryString = params =>
    Object.keys(params)
      .map(key => key + '=' + params[key])
      .join('&')
}
