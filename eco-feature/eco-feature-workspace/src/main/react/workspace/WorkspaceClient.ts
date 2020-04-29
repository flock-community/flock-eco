import {ResourceClient} from '@flock-eco/core/src/main/react/clients'

const path = '/api/workspaces'

interface KeyValue {
  key: string,
  value: string
}

export interface WorkspaceUser {
  id: string,
  name: string,
  role: string,
}

export interface Workspace {
  id: string,
  name: string,
  host: string,
  variables: KeyValue[]
  users: WorkspaceUser[]
}

export interface WorkspaceInput {
  name: string,
  host: string,
  variables: KeyValue[]
}

const resourceClient = ResourceClient<Workspace, WorkspaceInput>(path)

export const WorkspaceClient = {
  ...resourceClient,
}
