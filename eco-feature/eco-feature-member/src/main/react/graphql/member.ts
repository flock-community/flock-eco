export interface MemberFilter {
  search?: string
  statuses?: MemberStatus[]
  groups?: string[]
}

export interface Member {
  id: string
  firstName: string
  infix?: string
  surName: string
  email?: string
  phoneNumber?: string
  street?: string
  houseNumber?: string
  houseNumberExtension?: string
  postalCode?: string
  city?: string
  country?: string
  language?: string
  gender?: MemberGender
  birthDate?: Date
  groups?: MemberGroup[]
  fields?: MemberField[]
  status?: MemberStatus
  created?: Date
}

export type MemberGender = 'UNKNOWN' | 'MALE' | 'FEMALE' | 'OTHER'
export type MemberStatus = 'NEW' | 'ACTIVE' | 'DISABLED' | 'DELETED' | 'MERGED'

export interface MemberGroup {
  code?: string
  name?: string
}

export interface MemberField {
  key?: string
  value?: string
}

export interface MemberInput {
  firstName: string
  infix?: string
  surName: string
  email?: string
  phoneNumber?: string
  street?: string
  houseNumber?: string
  houseNumberExtension?: string
  postalCode?: string
  city?: string
  country?: string
  language?: string
  gender?: MemberGender
  birthDate?: Date
  groups: string[]
  fields: MemberFieldInput[]
  status?: MemberStatus
}

export interface MemberFieldInput {
  key: string
  value: string
}
