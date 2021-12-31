import {ResourceClient} from '@flock-community/flock-eco-core/src/main/react/clients'

const path = '/api/member_groups'

export type MemberGroup = {
  id?: string
  name: string
  code: string
}

const resourceClient = ResourceClient<Omit<MemberGroup, 'id'>, MemberGroup>(
  path,
)

export const MemberGroupClient = {
  ...resourceClient,
}
