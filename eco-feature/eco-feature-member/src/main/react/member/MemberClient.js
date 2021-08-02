import {
  PageableClient,
  ResourceClient,
} from '@flock-community/flock-eco-core/src/main/react/clients'

const path = '/api/members'

const resourceClient = ResourceClient(path)
const pageableClient = PageableClient(path)

export const MemberClient = {
  ...resourceClient,
  ...pageableClient,
}
