'use strict';
var React = require('react-native');
var {
 Navigator,
} = React;

import HomeSlider from './aReactViews/HomeSlider';

class MallHome1 extends React.Component {
  render() {
    return (
        <HomeSlider />
    )
  }
}

React.AppRegistry.registerComponent('MallHome1', () => MallHome1);
