import {validateResponse, ValidResponse} from './utils'

type ID = string

export function ResourceClient<Out, In>(path: string) {
  const checkResponse = <A>(it: ValidResponse<A> | void): ValidResponse<A> => {
    if (it) {
      return it
    } else {
      throw new Error('No response')
    }
  }

  const all = (): Promise<ValidResponse<Out[]>> => {
    const opts = {
      method: 'GET',
    }
    return fetch(`${path}`, opts)
      .then(it => validateResponse<Out[]>(it))
      .then(checkResponse)
  }

  const get = (id: ID): Promise<ValidResponse<Out>> => {
    const opts = {
      method: 'GET',
    }
    return fetch(`${path}/${id}`, opts)
      .then(it => validateResponse<Out>(it))
      .then(checkResponse)
  }

  const post = (input: In): Promise<ValidResponse<Out>> => {
    const opts = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(input),
    }
    return fetch(path, opts)
      .then(it => validateResponse<Out>(it))
      .then(checkResponse)
  }

  const put = (id: ID, input: In): Promise<ValidResponse<Out>> => {
    const opts = {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(input),
    }
    return fetch(`${path}/${id}`, opts)
      .then(it => validateResponse<Out>(it))
      .then(checkResponse)
  }

  const del = (id: ID): Promise<void> => {
    const opts = {
      method: 'DELETE',
    }
    return fetch(`${path}/${id}`, opts)
      .then(it => validateResponse<void>(it))
      .then(() => {})
  }

  return {all, get, post, put, delete: del}
}
