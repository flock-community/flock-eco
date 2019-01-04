import React, {Component} from 'react'
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

class MemberMerger extends Component {
  state = {
    mergeMembers: [],
    newMember: null,
  }

  async componentDidUpdate() {
    if (
      this.props.mergeMemberIds != null &&
      this.state.mergeMembers.length === 0
    ) {
      const [...mergeMembers] = await Promise.all(
        this.props.mergeMemberIds.map(id =>
          fetch(`/api/members/${id}`).then(it => it.json()),
        ),
      )
      // omit id
      const {id, ...newMember} = mergeMembers[0]
      this.setState({mergeMembers, newMember})
    }
  }

  merge = async () => {
    const res = await post('/api/members/merge', {
      mergeMemberIds: this.state.mergeMembers.map(it => it.id),
      newMember: this.state.newMember,
    })

    if (!res.ok) {
      const e = await res.json()
      this.setState({message: e.message || 'Cannot merge members'})
    }
    this.handleComplete()
  }

  handleComplete = () => {
    this.props.onComplete()
    this.setState({
      mergeMembers: [],
      newMember: null,
    })
  }

  handleCancel = () => {
    this.props.onCancel()
    this.setState({
      mergeMembers: [],
      newMember: null,
    })
  }

  updateNewMember = (key, value) => {
    if (Object.keys(this.state.mergeMembers[0]).includes(key)) {
      // when key is not a field
      this.setState(state => ({
        newMember: {
          ...state.newMember,
          [key]: value,
        },
      }))
    } else if (Object.keys(this.state.mergeMembers[0].fields).includes(key)) {
      // when key is a field
      this.setState(state => ({
        newMember: {
          ...state.newMember,
          fields: {
            ...state.newMember.fields,
            [key]: value,
          },
        },
      }))
    }
  }

  render() {
    const {mergeMembers, newMember} = this.state
    if (mergeMembers.length < 2) return null
    const flattenedMembers = mergeMembers.map(it => Member.flattenFields(it))
    const flattenedNewMember = Member.flattenFields(newMember)
    return (
      <React.Fragment>
        <Dialog
          fullWidth
          maxWidth={'md'}
          open={this.state.mergeMembers.length >= 2}
          onClose={this.handleCancel}
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
                          this.updateNewMember(key, JSON.parse(e.target.value))
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
            <Button onClick={this.handleCancel} color="primary">
              Cancel
            </Button>
            <Button
              onClick={this.merge}
              type="submit"
              form="member-form"
              color="primary"
              autoFocus
            >
              Merge
            </Button>
          </DialogActions>
        </Dialog>
      </React.Fragment>
    )
  }
}

export default MemberMerger
