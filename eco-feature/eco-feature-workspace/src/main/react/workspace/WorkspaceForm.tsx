import React from 'react'
import * as Yup from 'yup'
import {
  ArrayHelpers,
  Field,
  FieldArray,
  Form,
  Formik,
  FormikProps,
} from 'formik'
import {TextField} from 'formik-material-ui'
import {Grid, IconButton, Typography} from '@material-ui/core'
import {WorkspaceInput} from '../graphql/workspace'
import AddIcon from '@material-ui/icons/Add'
import DeleteIcon from '@material-ui/icons/Delete'

const schema = Yup.object()
  .shape({
    name: Yup.string()
      .required()
      .default('')
      .defined(),
    host: Yup.string()
      .required()
      .default('')
      .defined(),
    variables: Yup.array(
      Yup.object()
        .shape({
          key: Yup.string()
            .required()
            .default('')
            .defined(),
          value: Yup.string()
            .label('Value')
            .default('')
            .defined(),
        })
        .defined(),
    )
      .default([])
      .defined(),
  })
  .defined()

type SchemaType = Yup.InferType<typeof schema>

interface Props {
  value: WorkspaceInput | null

  onSubmit(input: WorkspaceInput): void
}

export const WORKSPACE_FORM_ID = 'WORKSPACE_FORM_ID'

export function WorkspaceForm({value, onSubmit}: Props) {
  const handleSubmit = (input: WorkspaceInput) => {
    onSubmit && onSubmit(input)
  }

  const renderVariables = (props: FormikProps<SchemaType>) => (
    helpers: ArrayHelpers,
  ) => {
    const addVariable = () =>
      helpers.insert(props.values.variables.length, {key: '', value: ''})
    const removeVariable = (index: number) => () => helpers.remove(index)
    return (
      <>
        <Grid container spacing={1} alignItems="center">
          <Grid item xs>
            <Typography variant="h6">Variables</Typography>
          </Grid>
          <Grid item>
            <IconButton color="primary" onClick={addVariable}>
              <AddIcon />
            </IconButton>
          </Grid>
        </Grid>

        {props.values?.variables?.map(
          (user: SchemaType['variables'], index: number) => (
            <Grid key={index} container spacing={1}>
              <Grid item xs>
                <Field
                  component={TextField}
                  fullWidth
                  name={`variables.${index}.key`}
                />
              </Grid>
              <Grid item xs>
                <Field
                  component={TextField}
                  fullWidth
                  name={`variables.${index}.value`}
                />
              </Grid>
              <Grid item>
                <IconButton color="primary" onClick={removeVariable(index)}>
                  <DeleteIcon />
                </IconButton>
              </Grid>
            </Grid>
          ),
        )}
      </>
    )
  }

  const renderForm = (props: FormikProps<SchemaType>) => (
    <Form id={WORKSPACE_FORM_ID}>
      <Grid container spacing={1}>
        <Grid item xs={12}>
          <Field component={TextField} fullWidth name="name" label="Name" />
        </Grid>
        <Grid item xs={12}>
          <Field component={TextField} fullWidth name="host" label="Host" />
        </Grid>
        <Grid item xs={12}>
          <FieldArray name="variables" render={renderVariables(props)} />
        </Grid>
      </Grid>
    </Form>
  )

  return (
    <Formik
      validationSchema={schema}
      initialValues={value || schema.default()}
      enableReinitialize
      onSubmit={handleSubmit}
      render={renderForm}
    />
  )
}
