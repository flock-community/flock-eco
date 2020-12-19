export type ValidResponse<T> = {
  status: number
  headers: Headers
  body: T
}
export const validateResponse = <T>(
  res: Response,
): Promise<ValidResponse<T> | undefined> => {
  if (res.ok) {
    if (res.status === 204) {
      return Promise.resolve(undefined)
    }
    return res.json().then(body => ({
      status: res.status,
      headers: res.headers,
      body,
    }))
  }
  return res.text().then(text => {
    throw new Error(text)
  })
}
