import {ResourceClient} from '@flock-community/flock-eco-core/src/main/react/clients'
import {WorkspaceUser, WorkspaceUserInput} from '../graphql/workspace'

const path = (workspaceId: string) => `/api/workspaces/${workspaceId}/users`

export const WorkspaceUserClient = (workspaceId: string) => ({
  ...ResourceClient<WorkspaceUser, WorkspaceUserInput>(path(workspaceId)),
})
