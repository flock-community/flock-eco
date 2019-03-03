import {User} from '../models/user.model.json'
import {Page, Pageable} from '../utils/Pagination'

const internalize = (it: any): User => {
  return {
    name: it.name,
    email: it.email,
    authorities: it.authorities,
  }
}

export class AuthoritiesClient {

  static cache = {}

  static findAuthorities = async (): Promise<Page<User>> => {
    return fetch(`/api/authorities`)
      .then(async res => {
        return {
          page: parseInt(res.headers.get('x-page')),
          total: parseInt(res.headers.get('x-total')),
          data: await res.json()
            .then(json => json.map(internalize)),
        }
      })
  }


}