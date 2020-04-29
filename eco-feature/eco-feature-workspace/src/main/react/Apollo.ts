/* tslint:disable */
/* eslint-disable */
// @generated
// This file was automatically generated and should not be edited.

// ====================================================
// GraphQL query operation: WorkspaceTableQuery
// ====================================================

export interface WorkspaceTableQuery_list {
  __typename: "Workspace";
  id: string;
  host: string | null;
  name: string;
}

export interface WorkspaceTableQuery {
  list: WorkspaceTableQuery_list[];
  count: number;
}

export interface WorkspaceTableQueryVariables {
  pageable?: Pageable | null;
}

/* tslint:disable */
/* eslint-disable */
// @generated
// This file was automatically generated and should not be edited.

// ====================================================
// GraphQL query operation: WorkspaceUserTableQuery
// ====================================================

export interface WorkspaceUserTableQuery_item_users {
  __typename: "WorkspaceUser";
  id: string;
  name: string;
  role: string;
}

export interface WorkspaceUserTableQuery_item {
  __typename: "Workspace";
  users: WorkspaceUserTableQuery_item_users[];
}

export interface WorkspaceUserTableQuery {
  item: WorkspaceUserTableQuery_item | null;
  roles: string[];
}

export interface WorkspaceUserTableQueryVariables {
  id?: string | null;
}

/* tslint:disable */
/* eslint-disable */
// @generated
// This file was automatically generated and should not be edited.

//==============================================================
// START Enums and Input Objects
//==============================================================

export enum Direction {
  ASC = "ASC",
  DESC = "DESC",
}

export interface Pageable {
  page?: number | null;
  size?: number | null;
  sort?: Sort | null;
}

export interface Sort {
  order?: string | null;
  direction?: Direction | null;
}

//==============================================================
// END Enums and Input Objects
//==============================================================
