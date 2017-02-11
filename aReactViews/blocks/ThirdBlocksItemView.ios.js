'use strict';
import React,{  PropTypes,Component}from'react';
// import Text from '../components/ShopText';
import {
  	Text,
  	View,
  	Image,
  	TouchableHighlight,
    TouchableOpacity,
    TouchableWithoutFeedback,
    Animated,
    StyleSheet,
    Platform,
    Dimensions,
    ToastAndroid
} from 'react-native';
var BlockStyles = require('../styles/BlockStyles');
var Config = require('../Config');
var Switcher = require('../natives/Switcher');
var isClickable = true;
var screenHeight = Dimensions.get('window').height;
var screenWidth = Dimensions.get('window').width;
var itemH = screenWidth*0.236065;
var marginH = 6;
var itemW = screenWidth;
var subItemW = (itemW-2)/2;

class ItemBlock extends Component{

  constructor(props) {
      super(props);
      this.state = {
        scale: new Animated.Value(1),
      };
  }

  _switch(title,tag,jsonStr){
    if (Platform.OS === 'ios') {

    }else if (Platform.OS === 'android') {
      Switcher.turn(title,tag,jsonStr);
    }
  }

  _press(){

      Animated.timing(                          // Base: spring, decay, timing
        this.state.scale,                 // Animate `bounceValue`
        {
          toValue: 0.93,                         // Animate to smaller size
          duration: 100
        }
      ).start(function(){

        if (isClickable) {
            isClickable = false;
            this.timer = setTimeout(() => {isClickable = true;},1800);
            this._switch(this.props.itemD.itemLabel,this.props.itemD.tagType,this.props.itemD.jsonStr);
        }
        Animated.timing(this.state.scale,                 // Animate `bounceValue`
                        {
                          toValue: 1,                         // Animate to smaller size
                          duration: 100
                        }
                       ).start();
      }.bind(this));
  }

  _pressIn(){
    Animated.timing(                          // Base: spring, decay, timing
      this.state.scale,                 // Animate `bounceValue`
      {
        toValue: 0.93,                         // Animate to smaller size
        duration: 100
      }
    ).start();
  }

  _pressOut(){
    Animated.timing(                          // Base: spring, decay, timing
      this.state.scale,                 // Animate `bounceValue`
      {
        toValue: 1,                         // Animate to smaller size
        duration: 100
      }
    ).start();
  }

  render() {
      return(

        <TouchableOpacity
          activeOpacity={1}
          style={[this.props.itemStyle,{transform:[{scale:this.state.scale}]}]}
          onPress={this._press.bind(this)}
          onPressIn={this._pressIn.bind(this)}
          onPressOut={this._pressOut.bind(this)}
          >

            <Image
              source={{uri: this.props.itemD.imgURL}}
              defaultSource={{uri: Config.imageTaken}}
              style={[styles.itemImg]}
            />

            <View style={styles.textC}>
              <Text style={styles.titleS}>
                {this.props.itemD.itemLabel}
              </Text>

              <Text style={styles.summaryS}>
                {this.props.itemD.itemSummary}
              </Text>
            </View>



        </TouchableOpacity>

      );
  };
};


export default class ThirdBlocksItemView extends Component{

    constructor(props) {
        super(props);
    }

    _onPressLeft(){
      this._switch(this.props.itemData[0].itemLabel,this.props.itemData[0].tagType,this.props.itemData[0].jsonStr);
    }

    _onPressRight(){
      this._switch(this.props.itemData[1].itemLabel,this.props.itemData[1].tagType,this.props.itemData[1].jsonStr);
    }


    render(){
      var itemView = this.props.mIsOne === 'yes'?
      (<ItemBlock itemD={this.props.itemData[0]} itemStyle={styles.cOne}/>):
      (<View style={styles.container}>
        <ItemBlock itemD={this.props.itemData[0]} itemStyle={styles.cLeft}/>
        <ItemBlock itemD={this.props.itemData[1]} itemStyle={styles.cRight}/>
       </View>);
      return(
        <View>
          {itemView}
        </View>
      )
    }
}

const styles = StyleSheet.create({
  container:{
    width:itemW,
    // marginLeft:marginH,
    // marginRight:marginH-2,
    marginBottom:1,
    flexDirection:'row',
    alignItems:'center',
    paddingBottom:1,
    // backgroundColor:'#FAEFED'
    // borderColor:'blue',
    // borderWidth:1,
  },
  cOne:{
    width:itemW,
    height:itemH,
    backgroundColor:Config.itemBackgroundColor,
    flexDirection:'row',
    paddingLeft:12,
    paddingRight:subItemW+2*6+2,
    alignItems:'center',
  },
  cLeft:{
    backgroundColor:Config.itemBackgroundColor,
    flex:1,
    height:itemH,
    flexDirection:'row',
    // borderWidth:1,
    // borderColor:'red',
    paddingLeft:12,
    paddingRight:12,
    alignItems:'center',
    marginRight:1,
    // justifyContent:'center',
  },
  cRight:{
  backgroundColor:Config.itemBackgroundColor,
    flex:1,
    height:itemH,
    flexDirection:'row',
    // borderWidth:1,
    // borderColor:'red',
    alignItems:'center',
    justifyContent:'flex-end',
    paddingLeft:12,
    paddingRight:12,
    marginLeft:1,
    // justifyContent:'center',
    // borderWidth:1,
    // borderColor:'red',
  },
  itemImg:{
  //  borderRadius:50,
    // flex:1,
    alignSelf:'center',
    width: ((screenWidth - 22*2 - 8*3) / 4)-30,
    height: ((screenWidth - 22*2 - 8*3) / 4)-30,
    resizeMode:'contain',
    marginRight:12
  },
  titleS:{
    fontWeight:'bold',
    fontSize:16,
    color:'#444444',
    marginBottom:4,
  },
  summaryS:{
    // marginLeft:8,
    fontSize:11,
    color:'#999999'
  },
  textC:{
    flex:1,
    // paddingLeft:6,
    // borderWidth:1,
    // borderColor:'red',
  }


});

module.exports = ThirdBlocksItemView;
