import {ResourceClient} from '@flock-eco/core/src/main/react/clients'

const path = '/api/workspaces'

export interface Workspace {
  id: string,
  name: string,
  host: string,
  variables: string[],
}

export interface WorkspaceInput {
  name: string,
  host: string,
  variables: string[],
}

const resourceClient = ResourceClient<Workspace, WorkspaceInput>(path)

export const WorkspaceClient = {
  ...resourceClient,
}
