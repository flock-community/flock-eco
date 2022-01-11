import {PageableClient, ResourceClient} from '@flock-community/flock-eco-core'
import {UserGroup} from '../graphql/user'

const resourceClient = ResourceClient<UserGroup, Partial<UserGroup>>(
  '/api/user-groups',
)
const pageableClient = PageableClient<UserGroup>('/api/user-groups')

export default {
  ...resourceClient,
  ...pageableClient,
}
