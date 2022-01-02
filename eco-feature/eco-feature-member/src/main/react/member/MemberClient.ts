import {
  PageableClient,
  ResourceClient,
} from '@flock-community/flock-eco-core/src/main/react/clients'

const path = '/api/members'

const resourceClient = ResourceClient<any, any>(path)
const pageableClient = PageableClient<any>(path)

export const MemberClient = {
  ...resourceClient,
  ...pageableClient,
}
