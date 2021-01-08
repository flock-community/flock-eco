import {
  UserProfileFeature,
  UserTable,
  UserFeature,
  UserLoginSignInForm,
  UserLoginResetForm,
} from '@flock-community/flock-eco-feature-user'
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
  user: [
    UserFeature,
    UserTable,
    UserProfileFeature,
    UserLoginSignInForm,
    UserLoginResetForm,
  ],
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
