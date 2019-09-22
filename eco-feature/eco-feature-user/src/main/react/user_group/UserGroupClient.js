import React from 'react'


export function findByCode(code) {
  const opts = {
    method: 'GET',
  }
  return fetch(`/api/user-groups/${code}`, opts)
    .then(res => res.json())
}

export function createUserGroup(item) {
  const opts = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json; charset=utf-8',
    },
    body: JSON.stringify(item),
  }
  return fetch('/api/user-groups', opts)
}

export function updateUserGroup(code, item) {
  const opts = {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json; charset=utf-8',
    },
    body: JSON.stringify(item),
  }
  return fetch(`/api/user-groups/${code}`, opts)
}

export function deleteUserGroup(code) {
  const opts = {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json; charset=utf-8',
    },
  }
  return fetch(`/api/user-groups/${code}`, opts)
}

export default {
  findByCode,
  createUserGroup,
  updateUserGroup,
  deleteUserGroup,
}
