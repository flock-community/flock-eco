import {responseValidation} from './index'

interface Page<Output> {
  list: Output[]
  count: number
}

interface Pageable {
  page: number,
  size: number
  sort: string
}

export function PageableClient<Output>(path: string) {
  const findAllByPage = (pageable: Pageable): Promise<Page<Output>> => {
    const opts = {
      method: 'GET',
    }

    const query = Object.entries<any>(pageable)
      .filter(([_, value]) => (value == null))
      .map(([key, value]) => `${key}=${value}`)
      .join('&')

    return fetch(`${path}?${query}`, opts)
      .then(it => responseValidation<Output[]>(it))
      .then(it => {
        const total = it.headers.get('x-total')
        return ({
          list: it.body,
          count: total ? parseInt(total, 10) : 0,
        })
      })
  }
  return {findAllByPage}
}
