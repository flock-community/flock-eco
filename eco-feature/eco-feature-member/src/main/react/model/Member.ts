import * as Model from './Model'

/** Flatten fields into the member object */
export function flattenFields(member) {
  const {fields, ...rest} = member
  return {
    ...rest,
    ...fields.reduce((acc, cur) => ({...acc, [cur.key]: cur.value}), {}),
  }
}

/** Get a string representation of all properties */
export function getLabels(member) {
  return Model.map(member, ([key, value]) => {
    if (key === 'groups') {
      return [key, value.join(', ')]
    } else if (value == null) {
      return [key, '[EMPTY]']
    } else {
      return [key, value]
    }
  })
}

export function toName(member) {
  if (member.infix) {
    return `${member.firstName} ${member.infix} ${member.surName}`
  }
  return `${member.firstName} ${member.surName}`
}
