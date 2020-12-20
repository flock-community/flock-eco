/* tslint:disable */
/* eslint-disable */
// @generated
// This file was automatically generated and should not be edited.

// ====================================================
// GraphQL query operation: WorkspaceTableQuery
// ====================================================

export interface WorkspaceTableQuery_list {
  __typename: 'Workspace'
  id: string
  host: string | null
  name: string
}

export interface WorkspaceTableQuery {
  list: WorkspaceTableQuery_list[]
  count: number
}

export interface WorkspaceTableQueryVariables {
  pageable?: Pageable | null
}

/* tslint:disable */
/* eslint-disable */
// @generated
// This file was automatically generated and should not be edited.

//==============================================================
// START Enums and Input Objects
//==============================================================

export enum Direction {
  ASC = 'ASC',
  DESC = 'DESC',
}

export interface Pageable {
  page?: number | null
  size?: number | null
  sort?: Sort | null
}

export interface Sort {
  order?: string | null
  direction?: Direction | null
}

//==============================================================
// END Enums and Input Objects
//==============================================================
