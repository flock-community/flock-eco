/**
 * Map the model using it's key and value.
 * The callback accepts a "tuple" [key, value] and returns the tuple [key, value].
 */
export function map(model, callback) {
  const res = Object.entries(model)
    .map(callback)
    .map(([key, value]) => ({[key]: value}))
  // @ts-ignore
  return Object.assign(...res)
}
