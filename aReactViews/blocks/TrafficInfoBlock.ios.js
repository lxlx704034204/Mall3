'use strict';
import React,{PropTypes,Component}from'react';
// import Text from '../components/ShopText';
import {
  	Text,
  	View,
  	Image,
  	TouchableHighlight,
    TouchableOpacity,

    StyleSheet,
    Platform,
    Dimensions,
    ToastAndroid
} from 'react-native';
var BlockStyles = require('../styles/BlockStyles');
var Config = require('../Config');
var Switcher = require('../natives/Switcher');
var that;
var isClickable = true;

export default class BlockView3 extends Component{

    // static propTypes = {
    //     trafficIcon: PropTypes.string,
    //     trafficInfo: PropTypes.string,
    //     paddings: PropTypes.number,
    // }

    constructor(props) {
        super(props);
    }

    componentWillMount(){
      that = this;
    }

    _onPressButton(){

      if (isClickable) {
            isClickable = false;
            that.timer = setTimeout(() => {isClickable = true;},500);
            if (Platform.OS === 'ios') {
            }else if (Platform.OS === 'android') {
                Switcher.turn("限行","USE_MODULE",Config.aroundTrafficControls);
            }
      }

    }

    render(){
      return(
        <TouchableOpacity   activeOpacity={1} onPress={this._onPressButton}>
          <View style={[styles.text,
              {
                  marginTop  :   this.props.paddings,
                  paddingTop    :   this.props.paddings,
                  paddingBottom       :   this.props.paddings,
              }, styles.container]}>

            <Image source={{uri: this.props.trafficIcon}} defaultSource={{uri: Config.imageTaken}} style={styles.trafficIcon}/>

            <View style={styles.textContainer}>
              <Text style={styles.textContent}>
                  {this.props.trafficInfo}
              </Text>
            </View>

          </View>
        </TouchableOpacity>
      )
    }
}

const styles = StyleSheet.create({
  container:{
    flex:1,
    flexDirection:'row',
    marginLeft:16,
    marginRight:16,
    alignItems:'center',
    // borderColor:'red',
    // borderWidth:1,
  },
  textContainer:{
    marginLeft:4,
    flex:1,
  },
  textContent:{
    fontSize:14,
    color:'#000',
  },
  trafficIcon:{
    // borderColor:'red',
    // borderWidth:1,
    width:24,
    height:12,
    resizeMode:'contain',
  }

});

module.exports = BlockView3;
