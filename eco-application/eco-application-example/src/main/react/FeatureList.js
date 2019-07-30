import React from 'react'
import List from '@material-ui/core/List'
import ListItem from '@material-ui/core/ListItem'
import ListItemText from '@material-ui/core/ListItemText'
import {withStyles} from '@material-ui/core'

import {features} from './data'

const styles = theme => ({})

class FeatureList extends React.Component {

  state = {
    selected: null,
  }

  handleClickItem = (component) => (ev) => {
    this.props.onChange(component)
    this.setState({selected: component})
  }

  render() {
    return (<React.Fragment>
        <List component="nav">
          {
            features
              .map(component =>{
                return (
                  <ListItem
                    button
                    key={component.type.displayName}
                    onClick={this.handleClickItem(component)}>
                    <ListItemText primary={component.type.displayName}/>
                  </ListItem>)
              })
          }
        </List>
      </React.Fragment>
    )
  }
}

export default withStyles(styles)(FeatureList)