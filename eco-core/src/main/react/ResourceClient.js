export class ResourceClient {
  url = null

  constructor(url, opts) {
    this.url = url
  }

  findAll = () => fetch(`${this.url}`)

  findById = id => fetch(`${this.url}/${id}`)

  create = item => {
    const opts = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(item),
    }
    return fetch(`${this.url}`, opts)
  }

  update = (id, item) => {
    console.log(item)
    const opts = {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(item),
    }
    return fetch(`${this.url}/${id}`, opts)
  }

  delete = id => {
    const opts = {
      method: 'DELETE',
    }
    return fetch(`${this.url}/${id}`, opts)
  }
}
