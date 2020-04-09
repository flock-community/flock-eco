import {PageableClient} from './PageableClient'
import {ResourceClient} from './ResourceClient'

export const responseValidation = res => {
  if (res.ok) {
    if (res.status === 204) {
      return null
    }
    return res.json()
      .then(body => ({
        headers: res.headers
          .entries(),
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
