import React, {useEffect, useState} from 'react'

import Grid from '@material-ui/core/Grid'
import {Field, Form, Formik} from 'formik'
import {TextField} from 'formik-material-ui'
import * as Yup from 'yup'
import {MultipleSelect} from 'react-select-material-ui'

export const USER_GROUP_FORM_ID = 'user-form-id'

const options = ["Africa", "America", "Asia", "Europe", "London", "Vienna"];

const init = {
  name: '',
}

/**
 * @return {null}
 */
export function UserGroupForm({value, onSummit, ...props}) {
  const [state, setState] = useState(init)

  useEffect(() => {
    if (value) {
      setState({
        ...init,
        ...value,
      })
    } else {
      setState({
        ...init,
      })
    }
  }, [value])

  const validation = Yup.object({
    name: Yup.string().required('Name is required'),
  })

  const handleSubmit = value => {
    onSummit &&
      onSummit({
        ...value,
        users:[]
      })
  }

  return (
    <Formik
      onSubmit={handleSubmit}
      enableReinitialize
      initialValues={state}
      validationSchema={validation}
    >
      <Form id={USER_GROUP_FORM_ID}>
        <Grid container spacing={1}>
          <Grid item xs={12}>
            <Field fullWidth name="name" label="Name" component={TextField} />
          </Grid>
          <Grid item xs={12}>
            <MultipleSelect
              label="Choose some cities"
              values={["London", "Vienna"]}
              options={options}
              helperText="You can add a new city by writing its name and pressing enter"
              onChange={console.log}
              SelectProps={{
                isCreatable: true,
                msgNoOptionsAvailable: "All cities are selected",
                msgNoOptionsMatchFilter: "No city name matches the filter"
              }}
            />
          </Grid>
        </Grid>
      </Form>
    </Formik>
  )
}
