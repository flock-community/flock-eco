import React, {useEffect, useState} from 'react'

import Grid from '@material-ui/core/Grid'

import FormControl from '@material-ui/core/FormControl'
import UserClient from './UserClient'
import {Field, Form, Formik} from 'formik'
import {Checkbox, TextField} from 'formik-material-ui'
import FormControlLabel from '@material-ui/core/FormControlLabel'
import FormGroup from '@material-ui/core/FormGroup'
import FormLabel from '@material-ui/core/FormLabel'
import * as Yup from 'yup'

export const USER_FORM_ID = 'user-form-id'

export function UserForm({value, onSummit, ...props}) {

  const [formRef, setFormRef] = useState(null)
  const [authorities, setAuthorities] = useState(null)

  const init = {
    name: '',
    email: '',
    authorities: [],
  }

  const [state, setState] = useState(init)

  useEffect(() => {
    if(!props.authorities){
      UserClient.findAllAuthorities()
        .then(setAuthorities)
    }else {
      setAuthorities(props.authorities)
    }

  }, [])

  useEffect(() => {
    if(value &&  authorities){
      setState({
        ...init,
        ...value,
        authorities: authorities.map(it => true),
      })
    }
  }, [value, authorities])

  useEffect(() => {
    formRef && formRef.resetForm()
  }, [state])

  const handleSubmit = value => {
    onSummit && onSummit(value)
  }

  const validation = Yup.object({
    name: Yup.string('Enter name')
      .required('Name is required'),
    email: Yup.string('Enter email')
      .email('Enter a valid email'),
    reference: Yup.string('Enter reference')
      .required('Reference is required'),
  })

  return authorities && (
     <Formik
      ref={setFormRef}
      onSubmit={handleSubmit}
      initialValues={state}
      validationSchema={validation}
    >
      <Form id={USER_FORM_ID}>
        <Grid container spacing={1}>
          <Grid item xs={12}>
            <Field fullWidth name="name" label="Name" component={TextField}/>
          </Grid>

          <Grid item xs={12}>
            <Field fullWidth name="email" label="Email" component={TextField}/>
          </Grid>

          <Grid item xs={12}>
            <FormControl component="fieldset">
              <FormLabel component="legend">Authorities</FormLabel>
              <FormGroup>
                {authorities.map((value, i) => (
                  <FormControlLabel
                    key={`user-form-authorities-${i}`}
                    control={
                      <Field name={`authorities[${i}]`} component={Checkbox}/>
                    }
                    label={value}
                  />
                ))}
              </FormGroup>
            </FormControl>
          </Grid>
        </Grid>
      </Form>
    </Formik>
  )
}
