import {responseValidation} from './index'

type ID = string

export function ResourceClient<Output, Input>(path: string) {

  const all = (): Promise<Output[]> => {
    const opts = {
      method: 'GET',
    }
    return fetch(`${path}`, opts)
      .then(it => responseValidation<Output[]>(it))
      .then(it => it.body)
  }

  const get = (id: ID): Promise<Output> => {
    const opts = {
      method: 'GET',
    }
    return fetch(`${path}/${id}`, opts)
      .then(it => responseValidation<Output>(it))
      .then(it => it.body)
  }

  const post = (input: Input): Promise<Output> => {
    const opts = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(input),
    }
    return fetch(path, opts)
      .then(it => responseValidation<Output>(it))
      .then(it => it.body)
  }

  const put = (id:ID, input:Input): Promise<Output> => {
    const opts = {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(input),
    }
    return fetch(`${path}/${id}`, opts)
      .then(it => responseValidation<Output>(it))
      .then(it => it.body)
  }

  const del = (id:ID): Promise<void> => {
    const opts = {
      method: 'DELETE',
    }
    return fetch(`${path}/${id}`, opts)
      .then(it => responseValidation<void>(it))
      .then(() => {})
  }

  return {all, get, post, put, delete: del}

}
