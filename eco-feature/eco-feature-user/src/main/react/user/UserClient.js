const internalize = res => {
  if (res.ok) {
    if (res.status === 204) {
      return null
    } else {
      return res.json()
    }
  } else {
    return res.text().then(text => {
      throw new Error(text)
    })
  }
}

export function findAllAuthorities() {
  return fetch('/api/authorities').then(internalize)
}

export function findUsersMe(page, size) {
  return fetch(`/api/users/me`).then(internalize)
}

export function findAllUsers(search, page, size) {
  return fetch(`/api/users?search=${search}&page=${page}&size=${size}`).then(
    res =>
      internalize(res).then(json => ({
        list: json,
        count: parseInt(res.headers.get('x-total')),
      })),
  )
}

export function findAllUserByCodes(codes) {
  const opts = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json; charset=utf-8',
    },
    body: JSON.stringify(codes),
  }
  return fetch(`/api/users/search`, opts).then(internalize)
}

export function findUserByCode(code) {
  const opts = {
    method: 'GET',
  }
  return fetch(`/api/users/${code}`, opts).then(internalize)
}

export function createUser(item) {
  const opts = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json; charset=utf-8',
    },
    body: JSON.stringify(item),
  }
  return fetch('/api/users', opts).then(internalize)
}

export function updateUser(code, item) {
  const opts = {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json; charset=utf-8',
    },
    body: JSON.stringify(item),
  }
  return fetch(`/api/users/${code}`, opts).then(internalize)
}

export function deleteUser(code) {
  const opts = {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json; charset=utf-8',
    },
  }
  return fetch(`/api/users/${code}`, opts).then(internalize)
}

export function resetUserPassword(code) {
  const opts = {
    method: 'PUT',
  }
  return fetch(`/api/users/${code}/reset-password`, opts).then(internalize)
}

export default {
  findUsersMe,
  findAllAuthorities,
  findAllUsers,
  findUserByCode,
  findAllUserByCodes,
  createUser,
  updateUser,
  deleteUser,
  resetUserPassword,
}
