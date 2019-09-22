import React from 'react'
import classNames from 'classnames'
import AsyncSelect from 'react-select/lib/Async'
import {withStyles} from '@material-ui/core/styles'
import Typography from '@material-ui/core/Typography'
import TextField from '@material-ui/core/TextField'
import Paper from '@material-ui/core/Paper'
import Chip from '@material-ui/core/Chip'
import MenuItem from '@material-ui/core/MenuItem'
import CancelIcon from '@material-ui/icons/Cancel'
import {PageableClient} from '@flock-eco/core'

const styles = theme => ({
  root: {
    flexGrow: 1,
    height: 250,
  },
  input: {
    display: 'flex',
    padding: 0,
  },
  valueContainer: {
    display: 'flex',
    flexWrap: 'wrap',
    flex: 1,
    alignItems: 'center',
    overflow: 'hidden',
  },
  chip: {
    margin: `${theme.spacing(2)}px ${theme.spacing(4)}px`,
  },

  noOptionsMessage: {
    padding: `${theme.spacing(1)}px ${theme.spacing(2)}px`,
  },
  singleValue: {
    fontSize: 16,
  },
  placeholder: {
    position: 'absolute',
    left: 2,
    fontSize: 16,
  },
  paper: {
    position: 'absolute',
    zIndex: 1,
    marginTop: theme.spacing(1),
    left: 0,
    right: 0,
  },
  divider: {
    height: theme.spacing(2),
  },
})

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
  return (
    <TextField
      fullWidth
      InputProps={{
        inputComponent,
        inputProps: {
          className: props.selectProps.classes.input,
          inputRef: props.innerRef,
          children: props.children,
          ...props.innerProps,
        },
      }}
      {...props.selectProps.textFieldProps}
    />
  )
}

function Option(props) {
  return (
    <MenuItem
      buttonRef={props.innerRef}
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
  return (
    <Typography
      color="textSecondary"
      className={props.selectProps.classes.placeholder}
      {...props.innerProps}
    >
      {props.children}
    </Typography>
  )
}

function SingleValue(props) {
  return (
    <Typography
      className={props.selectProps.classes.singleValue}
      {...props.innerProps}
    >
      {props.children}
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
      className={classNames(props.selectProps.classes.chip, {
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
  SingleValue,
  ValueContainer,
}

class UserAutocomplete extends React.Component {
  state = {
    value: null,
  }

  client = new PageableClient('/api/users', {size: 7})

  componentDidUpdate(prevProps, prevState, snapshot) {
    const {value} = this.props
    if (prevProps.value !== value) {
      this.setState({value: value.map(this.mapUser)})
    }
  }

  handleChange = value => {
    const {onChange} = this.props
    this.setState({value}, () => onChange && onChange(value))
  }

  promiseOptions = input =>
    this.client.findAll(0, {search: input}).then(data => {
      return data.list.map(this.mapUser)
    })

  mapUser = user => {
    return {
      value: user.code,
      label: `${user.name} <${user.reference}>`,
      user: user,
    }
  }

  render() {
    const {classes, theme} = this.props

    const selectStyles = {
      input: base => ({
        ...base,
        '& input': {
          font: 'inherit',
        },
      }),
    }

    return (
      <AsyncSelect
        classes={classes}
        styles={selectStyles}
        textFieldProps={{
          label: 'Label',
          InputLabelProps: {
            shrink: true,
          },
        }}
        cacheOptions
        defaultOptions
        loadOptions={this.promiseOptions}
        components={components}
        value={this.state.value}
        onChange={this.handleChange}
        placeholder="Select users"
        isMulti
      />
    )
  }
}

export default withStyles(styles)(UserAutocomplete)
