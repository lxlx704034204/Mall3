'use strict';
import React,{PropTypes,Component}from'react';
// import Text from './components/ShopText';
import {
  	Text,
  	View,
    Platform,
    ToastAndroid
} from 'react-native';
var ShopSwiper = require('./components/ShopSwiper');
var Switcher = require('./natives/Switcher');

export default class ADSliderView extends Component {

    constructor(props) {
        super(props);
    }

    componentWillMount(){
    }

    showMessage(obj){
         if (Platform.OS === 'ios') {
           Switcher.turn("slider", "AD_SLIDER", obj.url, obj.type);
         }else if (Platform.OS === 'android') {
           let str = JSON.stringify(obj);
           console.log("showMessage : "+str);
           Switcher.turn("slider","AD_SLIDER",str);
         }
     }

    render() {
        return (
          <View>
              <ShopSwiper onPressEvent={this.showMessage}/>
          </View>
        )
    }
}

module.exports = ADSliderView;
