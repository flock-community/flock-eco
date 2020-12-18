import React, {useEffect} from 'react'
import {emphasize} from '@material-ui/core/styles'
import Typography from '@material-ui/core/Typography'
import TextField from '@material-ui/core/TextField'
import Paper from '@material-ui/core/Paper'
import Chip from '@material-ui/core/Chip'
import MenuItem from '@material-ui/core/MenuItem'
import CancelIcon from '@material-ui/icons/Cancel'
import makeStyles from '@material-ui/core/styles/makeStyles'
import useTheme from '@material-ui/core/styles/useTheme'
import NoSsr from '@material-ui/core/NoSsr'
import AsyncSelect from 'react-select/async'
import clsx from 'clsx'
import UserClient from './UserClient'

const useStyles = makeStyles(theme => ({
  root: {
    flexGrow: 1,
  },
  input: {
    display: 'flex',
    padding: 0,
    height: 'auto',
  },
  valueContainer: {
    display: 'flex',
    flexWrap: 'wrap',
    flex: 1,
    alignItems: 'center',
    overflow: 'hidden',
  },
  chip: {
    margin: theme.spacing(0.5, 0.25),
  },
  chipFocused: {
    backgroundColor: emphasize(
      theme.palette.type === 'light'
        ? theme.palette.grey[300]
        : theme.palette.grey[700],
      0.08,
    ),
  },
  noOptionsMessage: {
    padding: theme.spacing(1, 2),
  },
  placeholder: {
    position: 'absolute',
    left: 2,
    bottom: 6,
    fontSize: 16,
  },
  paper: {
    position: 'absolute',
    zIndex: 1,
    overflow: 'visible',
    marginTop: theme.spacing(1),
    left: 0,
    right: 0,
  },
  divider: {
    height: theme.spacing(2),
  },
}))

function NoOptionsMessage(props) {
  return (
    <Typography
      color="textSecondary"
      className={props.selectProps.classes.noOptionsMessage}
      {...props.innerProps}
    >
      {props.children}
    </Typography>
  )
}

function inputComponent({inputRef, ...props}) {
  return <div ref={inputRef} {...props} />
}

function Control(props) {
  const {
    children,
    innerProps,
    innerRef,
    selectProps: {classes, TextFieldProps},
  } = props

  return (
    <TextField
      fullWidth
      InputProps={{
        inputComponent,
        inputProps: {
          className: classes.input,
          ref: innerRef,
          children,
          ...innerProps,
        },
      }}
      {...TextFieldProps}
    />
  )
}

function Option(props) {
  return (
    <MenuItem
      ref={props.innerRef}
      selected={props.isFocused}
      component="div"
      style={{
        fontWeight: props.isSelected ? 500 : 400,
      }}
      {...props.innerProps}
    >
      {props.children}
    </MenuItem>
  )
}

function Placeholder(props) {
  const {selectProps, innerProps = {}, children} = props
  return (
    <Typography
      color="textSecondary"
      className={selectProps.classes.placeholder}
      {...innerProps}
    >
      {children}
    </Typography>
  )
}

function ValueContainer(props) {
  return (
    <div className={props.selectProps.classes.valueContainer}>
      {props.children}
    </div>
  )
}

function MultiValue(props) {
  return (
    <Chip
      tabIndex={-1}
      label={props.children}
      className={clsx(props.selectProps.classes.chip, {
        [props.selectProps.classes.chipFocused]: props.isFocused,
      })}
      onDelete={props.removeProps.onClick}
      deleteIcon={<CancelIcon {...props.removeProps} />}
    />
  )
}

function Menu(props) {
  return (
    <Paper
      square
      className={props.selectProps.classes.paper}
      {...props.innerProps}
    >
      {props.children}
    </Paper>
  )
}

const components = {
  Control,
  Menu,
  MultiValue,
  NoOptionsMessage,
  Option,
  Placeholder,
  ValueContainer,
}

export function UserAutocomplete({value, onChange}) {
  const classes = useStyles()
  const theme = useTheme()

  const [state, setState] = React.useState(null)

  useEffect(() => {
    const acc = value && JSON.stringify([...value].sort())
    const cur = state && JSON.stringify([...state].map(it => it.value).sort())
    if (acc !== cur) {
      UserClient.findAllUserByCodes(value).then(res => {
        setState(res.map(it => ({label: it.name, value: it.code})))
      })
    }
  }, [value])

  const handleChange = ev => {
    setState(ev)
    onChange && onChange(ev.map(it => it.value))
  }

  const selectStyles = {
    input: base => ({
      ...base,
      color: theme.palette.text.primary,
      '& input': {
        font: 'inherit',
      },
    }),
  }

  const loadOptions = inputValue => {
    return UserClient.findAllUsers(inputValue, 0, 25).then(res => {
      return res.list.map(it => ({label: it.name, value: it.code})).slice(0, 10)
    })
  }

  return (
    <div className={classes.root}>
      <NoSsr>
        <AsyncSelect
          classes={classes}
          styles={selectStyles}
          inputId="autocomplete-users"
          TextFieldProps={{
            label: 'Users',
            InputLabelProps: {
              htmlFor: 'autocomplete-users',
              shrink: true,
            },
          }}
          placeholder="Select multiple users"
          components={components}
          value={state}
          onChange={handleChange}
          isMulti
          cacheOptions
          defaultOptions
          loadOptions={loadOptions}
        />
      </NoSsr>
    </div>
  )
}
