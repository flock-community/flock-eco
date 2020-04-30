import {ResourceClient} from '@flock-eco/core/src/main/react/clients'
import {Workspace, WorkspaceInput} from '../Simple'

const path = '/api/workspaces'

const resourceClient = ResourceClient<Workspace, WorkspaceInput>(path)

export const WorkspaceClient = {
  ...resourceClient,
}
