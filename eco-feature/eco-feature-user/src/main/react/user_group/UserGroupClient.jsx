import React from 'react'
import {ResourceClient} from '@flock-community/flock-eco-core'

const client = new ResourceClient('/api/user-groups')

const internalize = res => {
  if (res.ok) {
    return res.json()
  } else {
    return res.text().then(text => {
      throw new Error(text)
    })
  }
}

export function findByCode(code) {
  const opts = {
    method: 'GET',
  }
  return fetch(`/api/user-groups/${code}`, opts).then(internalize)
}

export function createUserGroup(item) {
  return client.create(item).then(internalize)
}

export function updateUserGroup(code, item) {
  return client.update(code, item).then(internalize)
}

export function deleteUserGroup(code) {
  return client.delete(code).then(internalize)
}

export default {
  findByCode,
  createUserGroup,
  updateUserGroup,
  deleteUserGroup,
}
