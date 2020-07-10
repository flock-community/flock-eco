import {PageableClient} from './PageableClient'
import {ResourceClient} from './ResourceClient'

interface ValidResponse<T> {
  headers:Headers
  body:T
}

fetch("")
  .then()

export const responseValidation = <T>(res:Response):Promise<ValidResponse<T> | void> => {
  if (res.ok) {
    if (res.status === 204) {
      return Promise.resolve()
    }
    return res.json()
      .then(body => ({
        headers: res.headers,
        body,
      }))
  }
  return res.text().then(text => {
    throw new Error(text)
  })
}

export {
  PageableClient,
  ResourceClient,
}
