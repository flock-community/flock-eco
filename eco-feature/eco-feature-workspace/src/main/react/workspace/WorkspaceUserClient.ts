import {ResourceClient} from '@flock-eco/core/src/main/react/clients'

const path = (workspaceId:string) => `/api/workspaces/${workspaceId}/users`

export interface WorkspaceUser {
  id: string,
  name: string,
  role: string,
}

export interface WorkspaceUserInput {
  reference: string,
  role?: string,
}

export const WorkspaceUserClient = (workspaceId:string) => ({
  ...ResourceClient<WorkspaceUser, WorkspaceUserInput>(path(workspaceId))
})
