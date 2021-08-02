export interface Workspace {
  id: string
  name: string
  variables: KeyValue[]
  users: WorkspaceUser[]
  host?: string
}

export interface WorkspaceUser {
  id: string
  name: string
  role: string
}

export interface WorkspaceRole {
  name: string
}

export interface WorkspaceInput {
  name: string
  image?: WorkspaceImageInput
  variables: KeyValueInput[]
  host?: string
}

export interface WorkspaceImageInput {
  file: string
  name: string
}

export interface WorkspaceUserInput {
  reference: string
  role: string
}

export interface KeyValue {
  key: string
  value?: string
}

export interface KeyValueInput {
  key: string
  value?: string
}
