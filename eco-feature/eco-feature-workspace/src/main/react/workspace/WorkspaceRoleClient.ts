import {ResourceClient} from '@flock-community/flock-eco-core/src/main/react/clients'

const path = `/api/workspace-roles`

export const WorkspaceRoleClient = {
  ...ResourceClient<string, string>(path),
}
