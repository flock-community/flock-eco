import React from 'react'

import {UserTable} from '@flock-eco/feature-user/src/main/react/user/UserTable'
import {UserProfileFeature} from '@flock-eco/feature-user/src/main/react/user_profile/UserProfileFeature'

import {MemberTable, MemberForm, MemberFeature} from '@flock-eco/feature-member/src/main/react/member'

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
  ],
}

console.log(features)
