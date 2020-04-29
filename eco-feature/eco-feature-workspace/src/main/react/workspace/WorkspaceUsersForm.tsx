import React, {useEffect, useState} from 'react'
// @ts-ignore
import {FormControl, Grid, IconButton, InputLabel, MenuItem} from '@material-ui/core'
import {Select, TextField} from 'formik-material-ui'
import {Field, Form, Formik} from 'formik'
import AddIcon from '@material-ui/icons/Add'
import * as yup from 'yup'
import {WorkspaceUserClient} from './WorkspaceUserClient'
import {WorkspaceRoleClient} from './WorkspaceRoleClient'
import {FormikActions} from 'formik/dist/types'

const schema = yup.object({
  reference: yup.string()
    .required()
    .default(''),
  role: yup.string()
    .required()
    .default(''),
})

interface Props {
  id: string

  onComplete?(): void
}

type Values = yup.InferType<typeof schema>

export function WorkspaceUsersForm({id, onComplete}: Props) {

  const client = WorkspaceUserClient(id)

  const [roles, setRoles] = useState<string[]>([])

  useEffect(() => {
    WorkspaceRoleClient.all()
      .then(res => setRoles(res))
  },[])

  const handleSubmit = (values: Values, actions:FormikActions<Values>) => {
    client.post({...values})
      .then(() => {
        actions.setSubmitting(false)
        onComplete && onComplete()
      })
  }

  const renderForm = () => (<Form>
    <Grid container spacing={1}>
      <Grid item xs>
        <Field
          component={TextField}
          fullWidth
          name="reference"
          label="Reference"
        />
      </Grid>
      <Grid item xs={3}>
        <FormControl fullWidth>
          <InputLabel>Role</InputLabel>
          <Field
            component={Select}
            name="role"
          >
            {roles.map(role => (<MenuItem value={role}>
              {role}
            </MenuItem>))}
          </Field>
        </FormControl>
      </Grid>
      <Grid item>
        <IconButton type="submit">
          <AddIcon/>
        </IconButton>
      </Grid>
    </Grid>
  </Form>)

  return (<Formik
    validationSchema={schema}
    initialValues={schema.cast()}
    render={renderForm}
    onSubmit={handleSubmit}
  />)


}
