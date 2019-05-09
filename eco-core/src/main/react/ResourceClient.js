export class ResourceClient {
  url = null

  constructor(url, opts) {
    this.url = url
  }

  findAll = () => fetch(`${this.url}`)
    .then(res => res.json())

  findById = id => fetch(`${this.url}/${id}`)
    .then(res => res.json())

  create = item => {
    const opts = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(item),
    }
    return fetch(`${this.url}`, opts).then(res => res.json())
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
    return fetch(`${this.url}/${id}`, opts).then(res => res.json())
  }

  delete = id => {
    const opts = {
      method: 'DELETE',
    }
    return fetch(`${this.url}/${id}`, opts).then(res => res.json())
  }
}
