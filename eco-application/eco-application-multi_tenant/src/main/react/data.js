import React from 'react'

import {UserTable} from '@flock-eco/feature-user/src/main/react/user/UserTable'
import {UserProfileFeature} from '@flock-eco/feature-user/src/main/react/user_profile/UserProfileFeature'

import {MemberTable, MemberForm, MemberFeature, MemberFilter} from '@flock-eco/feature-member/src/main/react/member'
import {MemberFieldFeature} from '@flock-eco/feature-member/src/main/react/member_field'
import {MemberGroupFeature} from '@flock-eco/feature-member/src/main/react/member_group'

export const findComponentName = component => component.Naked ? component.Naked.name :component.name

export const features = {
  user: [
    UserTable,
    UserProfileFeature,
  ],
  member: [
    MemberTable,
    MemberForm,
    MemberFeature,
    MemberFilter,
    MemberFieldFeature,
    MemberGroupFeature
  ],
}

console.log(features)
