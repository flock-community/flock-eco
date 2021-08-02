import React, {useEffect, useState} from 'react'

import Button from '@material-ui/core/Button'

import Dialog from '@material-ui/core/Dialog'
import DialogActions from '@material-ui/core/DialogActions'
import DialogContent from '@material-ui/core/DialogContent'
import DialogTitle from '@material-ui/core/DialogTitle'

import Snackbar from '@material-ui/core/Snackbar'

import {MemberForm} from './MemberForm'
import {ConfirmDialog} from '@flock-community/flock-eco-core/src/main/react/components/ConfirmDialog'
import {Typography} from '@material-ui/core'
import {MemberClient} from './MemberClient'

export function MemberDialog({id, open, onComplete}) {
  const [countries, setCountries] = useState(null)
  const [languages, setLanguages] = useState(null)
  const [state, setState] = useState({
    item: null,
    message: null,
    deleteOpen: false,
  })

  useEffect(() => {
    if (id) {
      MemberClient.get(id)
        .then(res =>
          setState(prev => ({
            ...prev,
            item: {
              ...res.body,
              groups: res.body.groups.map(it => it.code),
            },
            message: null,
          })),
        )
        .catch(() => {
          setState(prev => ({
            ...prev,
            item: null,
            message: 'Cannot load member',
          }))
        })
    }
  }, [id])

  useEffect(() => {
    fetch('/api/countries')
      .then(res => res.json())
      .then(json => setCountries(json))
  }, [])

  useEffect(() => {
    fetch('/api/languages')
      .then(res => res.json())
      .then(json => setLanguages(json))
  }, [])

  useEffect(() => {
    fetch(`/api/member_groups`)
      .then(res => {
        return res.json()
      })
      .then(json => {
        setState(prev => ({
          ...prev,
          groups: json,
        }))
      })
      .catch(() => {
        setState(prev => ({
          ...prev,
          message: 'Cannot load groups',
        }))
      })
  }, [])

  useEffect(() => {
    fetch(`/api/member_fields`)
      .then(res => {
        return res.json()
      })
      .then(json => {
        setState(prev => ({
          ...prev,
          fields: json,
        }))
      })
      .catch(() => {
        setState(prev => ({
          ...prev,
          message: 'Cannot load fields',
        }))
      })
  }, [])

  const handleClose = () => {
    setState(prev => ({
      ...prev,
      item: null,
    }))
    onComplete && onComplete()
  }

  const handleDelete = () => {
    MemberClient.delete(id).then(() => {
      onComplete && onComplete()
      setState(prevState => ({
        ...prevState,
        deleteOpen: false,
        item: null,
      }))
    })
  }

  const handleDeleteOpen = () => {
    setState(prevState => ({
      ...prevState,
      deleteOpen: true,
    }))
  }

  const handleDeleteClose = () => {
    setState(prevState => ({
      ...prevState,
      deleteOpen: false,
    }))
  }

  const handleFormUpdate = value => {
    setState(prevState => ({
      ...prevState,
      item: value,
    }))
  }

  const handleSubmit = () => {
    if (id) {
      MemberClient.put(id, state.item)
        .then(() => {
          onComplete && onComplete()
          setState(prevState => ({
            ...prevState,
            item: null,
          }))
        })
        .catch(e => {
          setState(prevState => ({
            ...prevState,
            item: null,
            message: 'Cannot load fields',
          }))
        })
    } else {
      MemberClient.post(state.item)
        .then(() => {
          onComplete && onComplete()
          setState(prevState => ({
            ...prevState,
            item: null,
          }))
        })
        .catch(e => {
          setState(prevState => ({
            ...prevState,
            item: null,
            message: 'Cannot load fields',
          }))
        })
    }
  }

  const handleCloseSnackbar = () => {
    setState(prevState => ({
      ...prevState,
      message: null,
    }))
  }

  const isUpdatable = state.item
    ? !['DELETED', 'MERGED'].includes(state.item.status)
    : !id

  const dialogBody = (
    <>
      <DialogContent>
        <MemberForm
          value={state.item}
          groups={state.groups}
          fields={state.fields}
          languages={languages}
          countries={countries}
          onChange={handleFormUpdate}
          onSubmit={handleSubmit}
          disabled={!isUpdatable}
        />
      </DialogContent>
      <DialogActions>
        {id && isUpdatable && (
          <Button onClick={handleDeleteOpen} color="secondary">
            Delete
          </Button>
        )}
        <Button onClick={handleClose} color="primary">
          Cancel
        </Button>
        {isUpdatable && (
          <Button type="submit" form="member-form" color="primary" autoFocus>
            Save
          </Button>
        )}
      </DialogActions>
    </>
  )

  return (
    <>
      <Dialog fullWidth maxWidth={'md'} open={open} onClose={handleClose}>
        <DialogTitle>Member</DialogTitle>
        {dialogBody}
      </Dialog>

      <Snackbar
        open={state.message !== null}
        autoHideDuration={5000}
        onClose={handleCloseSnackbar}
        message={state.message}
      />

      <ConfirmDialog
        open={state.deleteOpen}
        onClose={handleDeleteClose}
        onConfirm={handleDelete}
      >
        <Typography>
          Delete member:{' '}
          {state.item && `${state.item.firstName} ${state.item.surName}`}
        </Typography>
      </ConfirmDialog>
    </>
  )
}
