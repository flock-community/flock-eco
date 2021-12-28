import React from 'react'
import {Form, Formik, Field} from 'formik'

import {TextField} from 'formik-material-ui'
import {Button, Grid} from '@material-ui/core'

export function CreateTenantForm() {
  const init = {
    tenantName: '',
  }

  const handleSubmit: (values: typeof init) => void = async ({tenantName}) => {
    await fetch('/api/tenants/create', {
      method: 'POST',
      headers: {
        'content-type': 'application/json',
      },
      body: JSON.stringify({tenantName}),
    })
  }

  return (
    <Formik initialValues={init} onSubmit={handleSubmit}>
      <Form>
        <Grid container>
          <Grid item xs={12}>
            <Field
              fullWidth
              component={TextField}
              name="tenantName"
              type="text"
              label="Tenant name"
            />
          </Grid>
          <Grid item xs={12}>
            <Button type="submit">Submit</Button>
          </Grid>
        </Grid>
      </Form>
    </Formik>
  )
}
