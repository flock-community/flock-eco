import React, {useEffect, useState} from 'react'
import {features, findComponentName} from './data'
import ListItem from '@material-ui/core/ListItem'
import ListItemText from '@material-ui/core/ListItemText'
import Typography from '@material-ui/core/Typography'
import Drawer from '@material-ui/core/Drawer'
import List from '@material-ui/core/List'

export function AppDrawer({open, onClick}) {

  const [state, setState] = useState({
    open,
  })

  useEffect(() => {
    setState({
      open,
    })
  }, [open])

  const toggleDrawer = () => {
    setState({
      ...state,
      open: !open,
    })
  }

  const handleClickItem = (component) => (ev) => {
    onClick && onClick(component)
  }


  const renderList = (name) => (<>
      <ListItem><Typography><b>{name}</b></Typography></ListItem>
      {features[name]
        .map(component => (<ListItem
            button
            key={findComponentName(component)}
            onClick={handleClickItem(component)}>
            <ListItemText primary={findComponentName(component)}/>
          </ListItem>
        ))}
    </>
  )

  return (<Drawer open={state.open} onClose={toggleDrawer}>
    <List>
      {renderList('user')}
      {renderList('member')}
    </List>
  </Drawer>)

}
