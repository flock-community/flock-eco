import React from 'react'

import {TextField} from 'formik-material-ui'
import {Field, Form, Formik} from 'formik'
import * as Yup from 'yup'
import {Button} from '@material-ui/core'
import Grid from '@material-ui/core/Grid'

function UserRegisterForm({initialValues, onSubmit}) {

  initialValues = {
    email:'',
    password: '',
    confirmPassword: '',
    ...initialValues
  }

  const handleSubmit = (value) => {
    onSubmit && onSubmit(value)
  }

  return (
    <UserRegisterForm onSubmit={handleSubmit}/>)

}

export default UserRegisterForm
