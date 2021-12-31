import {ResourceClient} from '@flock-community/flock-eco-core/src/main/react/clients'

const path = '/api/member_fields'

export type MemberField = {
  id?: string
  name: string
  label: string
  type: 'TEXT' | 'CHECKBOX' | 'SINGLE_SELECT' | 'MULTI_SELECT'
  required: boolean
  disabled: boolean
  options: string[]
  value?: string
}

const resourceClient = ResourceClient<Omit<MemberField, 'id'>, MemberField>(
  path,
)

export const MemberFieldClient = {
  ...resourceClient,
}
