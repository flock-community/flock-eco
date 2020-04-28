import React, {useState} from 'react'
import * as Yup from 'yup'
import {Field, Form, Formik} from 'formik'
import {TextField} from 'formik-material-ui'
import {Grid} from '@material-ui/core'
import {WorkspaceInput} from './WorkspaceClient'

const schema = Yup.object({
  name: Yup.string()
    .required()
    .default(''),
  host: Yup.string()
    .required()
    .default(''),
  variables: Yup.array()
    .of(Yup.string())
    .required()
    .default([]),
})

interface Props {
  value: WorkspaceInput | null,
  onSubmit(input:WorkspaceInput): void
}

export const WORKSPACE_FORM_ID = 'WORKSPACE_FORM_ID'

export function WorkspaceForm({value, onSubmit}:Props) {

  const handleSubmit = (input:WorkspaceInput) => {
    onSubmit && onSubmit(input)
  }

  const renderForm = () => (<Form id={WORKSPACE_FORM_ID}>
    <Grid container spacing={1}>
      <Grid item xs={12}>
        <Field
          component={TextField}
          fullWidth
          name="name"
          label="Name"
        />
      </Grid>
      <Grid item xs={12}>
        <Field
          component={TextField}
          fullWidth
          name="host"
          label="Host"
        />
      </Grid>
    </Grid>
  </Form>)

  return (<Formik
    initialValues={value || schema.cast()}
    enableReinitialize
    onSubmit={handleSubmit}
    component={renderForm}
  />)
}
