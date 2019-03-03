export interface Page<T> {
  page:number,
  total:number,
  data:T[]
}

export class Pageable  {

  private page: number
  private size: number

  constructor(page:number, size:number){
    this.page = page
    this.size = size
  }


  getQueryString(){
   return `page=${this.page}&size=${this.size}`
  }
}