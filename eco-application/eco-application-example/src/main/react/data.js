import {UserTable} from '@flock-eco/feature-user/src/main/react/user/UserTable'
import {UserProfileFeature} from '@flock-eco/feature-user/src/main/react/user_profile/UserProfileFeature'

import {MemberFeature, MemberFilter, MemberForm, MemberTable} from '@flock-eco/feature-member/src/main/react/member'
import {MemberFieldFeature} from '@flock-eco/feature-member/src/main/react/member_field'
import {MemberGroupFeature} from '@flock-eco/feature-member/src/main/react/member_group'

import {WorkspaceFeature, WorkspaceForm, WorkspaceTable} from '@flock-eco/feature-workspace/src/main/react/workspace'

export const findComponentName = component => component.Naked ? component.Naked.name : component.name

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
    MemberGroupFeature,
  ],
  workspace: [
    WorkspaceFeature,
    WorkspaceTable,
    WorkspaceForm,
  ],
}
