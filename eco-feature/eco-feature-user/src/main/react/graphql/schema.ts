export interface Query {}

export interface Pageable {
  page?: number
  size?: number
  sort?: Sort
}

export interface Sort {
  order?: string
  direction?: Direction
}

export type Direction = 'ASC' | 'DESC'
