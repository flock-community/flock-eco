import React from 'react'

function UserRegisterForm({initialValues, onSubmit}) {
  initialValues = {
    email: '',
    password: '',
    confirmPassword: '',
    ...initialValues,
  }

  const handleSubmit = value => {
    onSubmit && onSubmit(value)
  }

  return <UserRegisterForm onSubmit={handleSubmit} />
}

export default UserRegisterForm
