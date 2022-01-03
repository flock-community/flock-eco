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

export function findByid(id) {
  const opts = {
    method: 'GET',
  }
  return fetch(`/api/user-groups/${id}`, opts).then(internalize)
}

export function createUserGroup(item) {
  return client.create(item).then(internalize)
}

export function updateUserGroup(id, item) {
  return client.update(id, item).then(internalize)
}

export function deleteUserGroup(id) {
  return client.delete(id).then(internalize)
}

export default {
  findByid,
  createUserGroup,
  updateUserGroup,
  deleteUserGroup,
}
