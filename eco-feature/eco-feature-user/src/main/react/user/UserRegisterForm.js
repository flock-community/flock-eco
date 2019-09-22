import React from 'react'

import {TextField} from 'formik-material-ui'
import {Field, Form, Formik} from 'formik'
import * as Yup from 'yup'
import {Button} from '@material-ui/core'
import Grid from '@material-ui/core/Grid'

function UserRegisterForm({initialValues, onSubmit}) {
  initialValues = {
    email: '',
    password: '',
    confirmPassword: '',
    ...initialValues,
  }

  const validation = Yup.object({
    email: Yup.string('Enter your email')
      .email('Enter a valid email')
      .required('Email is required'),
    password: Yup.string('')
      .min(8, 'Password must contain at least 8 characters')
      .required('Enter your password'),
    confirmPassword: Yup.string('Enter your password')
      .required('Confirm your password')
      .oneOf([Yup.ref('password')], 'Password does not match'),
  })

  const handleSubmit = value => {
    onSubmit && onSubmit(value)
  }

  return (
    <>
      <Formik
        initialValues={initialValues}
        validationSchema={validation}
        onSubmit={handleSubmit}
      >
        <Form>
          <Grid container spacing={1}>
            <Grid item xs={12}>
              <Field
                name="email"
                label="Email"
                fullWidth
                component={TextField}
              />
            </Grid>
            <Grid item xs={12}>
              <Field
                name="password"
                label="Password"
                fullWidth
                component={TextField}
              />
            </Grid>
            <Grid item xs={12}>
              <Field
                name="confirmPassword"
                label="Re-password"
                fullWidth
                component={TextField}
              />
            </Grid>
            <Grid item xs={12}>
              <Button fullWidth type="submit" color="primary">
                Submit
              </Button>
            </Grid>
          </Grid>
        </Form>
      </Formik>
    </>
  )
}

export default UserRegisterForm
