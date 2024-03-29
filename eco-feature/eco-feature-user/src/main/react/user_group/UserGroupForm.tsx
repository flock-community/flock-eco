import React, {useEffect, useState} from 'react'

import Grid from '@material-ui/core/Grid'
import {Field, Form, Formik} from 'formik'
import {TextField} from 'formik-material-ui'
import * as Yup from 'yup'
import {UserAutocomplete} from '../user/UserAutocomplete'
import {User, UserGroup} from '../graphql/user'

export const USER_GROUP_FORM_ID = 'user-form-id'

const init: {name: string; users: string[]} = {
  name: '',
  users: [],
}

type UserGroupFormProps = {
  value: UserGroup
  onSummit: (userGroup: UserGroup) => void
}
export function UserGroupForm({value, onSummit}: UserGroupFormProps) {
  const [state, setState] = useState(init)
  const [users, setUsers] = useState<string[]>(value && value.users)

  useEffect(() => {
    if (value) {
      setState({...init, ...value})
      setUsers(value.users)
    } else {
      setState({...init})
      setUsers([])
    }
  }, [value])

  const validation = Yup.object({
    name: Yup.string().required('Name is required'),
  })

  const handleSubmit = (ev) => {
    onSummit &&
      onSummit({
        ...ev,
        users,
      })
  }

  const handleUserChange = (ev) => {
    setUsers(ev)
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
            <UserAutocomplete value={users} onChange={handleUserChange} />
          </Grid>
        </Grid>
      </Form>
    </Formik>
  )
}
