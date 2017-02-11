'use strict';
import React,{  PropTypes,Component}from'react';
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

var screenHeight = Dimensions.get('window').height;
var screenWidth = Dimensions.get('window').width;
var itemH = screenWidth*0.11;

export default class ThirdBlocksTitleView extends Component{

    // static propTypes = {
    //     mTitle: PropTypes.string,
    //     mSummary: PropTypes.string,
    // }

    constructor(props) {
        super(props);
    }

    componentWillMount(){
      that = this;
    }

    _onPressButton(){

    }

    render(){
      return(
        <View style={styles.container}>
          <Text style={styles.titleS}>
                {this.props.mTitle}
          </Text>
        </View>
      )
    }
}

// <View style={styles.container}>
//   <View style={{alignSelf:'center',height:itemH,alignItems:'center',justifyContent: 'center',borderColor:'red',borderBottomWidth:0}}>
//     <Text style={styles.titleS}>
//       {this.props.mTitle}
//     </Text>
//   </View>
//   <View>
//     <Text style={styles.summaryS}>
//       {this.props.mSummary}
//     </Text>
//   </View>
// </View>

const styles = StyleSheet.create({
  container:{
    backgroundColor:Config.itemBackgroundColor,
    width:screenWidth,
    height:itemH,
    flexDirection:'row',
    marginBottom:2,
    paddingLeft:12,
    paddingRight:12,
    alignItems:'center',
    // justifyContent: 'center',
    // marginTop:5,
  },
  titleS:{
    fontWeight:'bold',
    fontSize:18,
    color:Config.titleColor
  },
  summaryS:{
    marginLeft:8,
    fontSize:14,
    color:'#D50D18'
  }

});

module.exports = ThirdBlocksTitleView;
