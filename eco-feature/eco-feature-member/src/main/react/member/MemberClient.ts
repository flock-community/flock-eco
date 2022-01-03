import {
  PageableClient,
  ResourceClient,
} from '@flock-community/flock-eco-core/src/main/react/clients'
import {Member} from "../graphql/member";

const path = '/api/members'

const resourceClient = ResourceClient<Member, Partial<Member>>(path)
const pageableClient = PageableClient<Member>(path)

export const MemberClient = {
  ...resourceClient,
  ...pageableClient,
}
