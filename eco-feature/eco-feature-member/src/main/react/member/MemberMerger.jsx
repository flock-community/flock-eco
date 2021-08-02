import React, {useEffect, useState} from 'react'
import Dialog from '@material-ui/core/Dialog'
import DialogActions from '@material-ui/core/DialogActions'
import DialogContent from '@material-ui/core/DialogContent'
import DialogTitle from '@material-ui/core/DialogTitle'
import Radio from '@material-ui/core/Radio'
import FormControlLabel from '@material-ui/core/FormControlLabel'
import FormControl from '@material-ui/core/FormControl'
import RadioGroup from '@material-ui/core/RadioGroup'
import FormLabel from '@material-ui/core/FormLabel'
import Button from '@material-ui/core/Button'
import * as Member from '../model/Member'

// TODO: refactor this so that it can be reused
function post(url, body) {
  return fetch(url, {
    method: 'POST',
    headers: {'Content-Type': 'application/json; charset=utf-8'},
    body: JSON.stringify(body),
  })
}

function get(id) {
  return fetch(`/api/members/${id}`).then(it => it.json())
}

function internalize(member) {
  return {
    ...member,
    groups: member.groups.map(it => it.code),
  }
}

export function MemberMerger({mergeMemberIds, onComplete, onCancel}) {
  const [state, setState] = useState({
    mergeMembers: [],
    newMember: null,
  })

  useEffect(() => {
    if (mergeMemberIds && mergeMemberIds.length > 1) {
      Promise.all((mergeMemberIds || []).map(id => get(id)))
        .then(it => it.map(internalize))
        .then(mergeMembers => {
          const {id, ...newMember} = mergeMembers[0]
          setState({mergeMembers, newMember})
        })
    }
  }, [mergeMemberIds])

  const merge = async () => {
    const res = await post('/api/members/merge', {
      mergeMemberIds: state.mergeMembers.map(it => it.id),
      newMember: state.newMember,
    })

    if (!res.ok) {
      const e = await res.json()
      setState({message: e.message || 'Cannot merge members'})
    }
    handleComplete()
  }

  const handleComplete = () => {
    onComplete && onComplete()
    setState({
      mergeMembers: [],
      newMember: null,
    })
  }

  const handleCancel = () => {
    onCancel && onCancel()
    setState({
      mergeMembers: [],
      newMember: null,
    })
  }

  const updateNewMember = (key, value) => {
    if (Object.keys(state.mergeMembers[0]).includes(key)) {
      // when key is not a field
      setState(prev => ({
        ...prev,
        newMember: {
          ...prev.newMember,
          [key]: value,
        },
      }))
    } else if (Object.keys(state.mergeMembers[0].fields).includes(key)) {
      // when key is a field
      setState(prev => ({
        ...prev,
        newMember: {
          ...prev.newMember,
          fields: [...prev.newMember.fields, {key, value}],
        },
      }))
    }
  }

  if (state.mergeMembers && state.mergeMembers.length < 2) return null

  const flattenedMembers = state.mergeMembers.map(it =>
    Member.flattenFields(it),
  )
  const flattenedNewMember = Member.flattenFields(state.newMember)

  return (
    <Dialog
      fullWidth
      maxWidth={'md'}
      open={state.mergeMembers.length >= 2}
      onClose={handleCancel}
      aria-labelledby="simple-dialog-title"
    >
      <DialogTitle id="simple-dialog-title">Merge members</DialogTitle>
      <DialogContent>
        <div style={{display: 'flex', flexDirection: 'column'}}>
          {Object.keys(flattenedMembers[0])
            .filter(key => {
              if (key === 'id') return false
              const labels = flattenedMembers.map(Member.getLabels)
              return labels.some(it => it[key] !== labels[0][key])
            })
            .map(key => {
              const values = flattenedMembers.map(it => it[key])
              const labels = flattenedMembers
                .map(Member.getLabels)
                .map(it => it[key])

              const valueWithLabels = values
                .map((value, i) => [value, labels[i]])
                .filter(([value, label], i) => labels.indexOf(label) === i)

              return (
                <FormControl
                  component="fieldset"
                  key={key}
                  style={{display: 'inline-block'}}
                >
                  <FormLabel component="legend">{key}</FormLabel>
                  <RadioGroup
                    style={{flexDirection: 'row'}}
                    aria-label={key}
                    name={key}
                    value={JSON.stringify(flattenedNewMember[key])}
                    onChange={e =>
                      updateNewMember(key, JSON.parse(e.target.value))
                    }
                  >
                    {valueWithLabels.map(([value, label], i) => (
                      <FormControlLabel
                        key={i}
                        value={JSON.stringify(value)}
                        control={<Radio />}
                        label={label}
                      />
                    ))}
                  </RadioGroup>
                </FormControl>
              )
            })}
        </div>
      </DialogContent>
      <DialogActions>
        <Button onClick={handleCancel} color="primary">
          Cancel
        </Button>
        <Button
          onClick={merge}
          type="submit"
          form="member-form"
          color="primary"
          autoFocus
        >
          Merge
        </Button>
      </DialogActions>
    </Dialog>
  )
}
