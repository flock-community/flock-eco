import {UserTable} from '@flock-community/flock-eco-feature-user/src/main/react/user/UserTable'
import {UserProfileFeature} from '@flock-community/flock-eco-feature-user/src/main/react/user_profile/UserProfileFeature'

import {
  MemberFeature,
  MemberFilter,
  MemberForm,
  MemberTable,
  MemberFieldFeature,
  MemberGroupFeature,
} from '@flock-community/flock-eco-feature-member'
import {
  WorkspaceFeature,
  WorkspaceForm,
  WorkspaceTable,
} from '@flock-community/flock-eco-feature-workspace/src/main/react/workspace'

export const findComponentName = component =>
  component.Naked ? component.Naked.name : component.name

export const features = {
  user: [UserTable, UserProfileFeature],
  member: [
    MemberTable,
    MemberForm,
    MemberFeature,
    MemberFilter,
    MemberFieldFeature,
    MemberGroupFeature,
  ],
  workspace: [WorkspaceFeature, WorkspaceTable, WorkspaceForm],
}
