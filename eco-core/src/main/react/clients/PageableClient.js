import {responseValidation} from './index'

export function PageableClient(path) {
  const findAllByPage = ({page, size, sort}) => {
    const opts = {
      method: "GET",
    }
    const params = {page, size, sort}
    const query = Object.keys(params)
      .filter(key => params[key])
      .map(key => `${key}=${params[key]}`)
      .join("&")
    return fetch(`${path}?${query}`, opts)
      .then(responseValidation)
  }

  return {findAllByPage}
}
