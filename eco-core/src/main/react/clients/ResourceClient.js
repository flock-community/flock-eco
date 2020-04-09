import {responseValidation} from './index'

export function ResourceClient(path) {

  const all = () => {
    const opts = {
      method: "GET",
    }
    return fetch(`${path}`, opts)
      .then(responseValidation)
  }

  const get = id => {
    const opts = {
      method: "GET",
    }
    return fetch(`${path}/${id}`, opts)
      .then(responseValidation)
  }

  const post = item => {
    const opts = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(item),
    }
    return fetch(path, opts)
      .then(responseValidation)
  }

  const put = (id, item) => {
    const opts = {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(item),
    }
    return fetch(`${path}/${id}`, opts)
      .then(responseValidation)
  }

  const del = id => {
    const opts = {
      method: "DELETE",
    }
    return fetch(`${path}/${id}`, opts).then(responseValidation)
  }

  return {all, get, post, put, delete: del}

}
