import {ResourceClient} from '@flock-community/flock-eco-core/src/main/react/clients'
import {Workspace, WorkspaceInput} from '../graphql/workspace'

const path = '/api/workspaces'

const resourceClient = ResourceClient<Workspace, WorkspaceInput>(path)

export const WorkspaceClient = {
  ...resourceClient,
}
