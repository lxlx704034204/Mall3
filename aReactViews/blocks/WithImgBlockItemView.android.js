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
    Animated,
    Dimensions,
    ToastAndroid
} from 'react-native';
var BlockStyles = require('../styles/BlockStyles');
var Config = require('../Config');
var Switcher = require('../natives/Switcher');
var isClickable = true;
var screenHeight = Dimensions.get('window').height;
var screenWidth = Dimensions.get('window').width;
var itemH = screenWidth*0.236065;//单位高度
var marginH = 6;//两边margin
var marginCell = 2;//中间间隙
var itemW = screenWidth - 2*marginH;
var subItemW = (itemW-marginCell)/2 -1;//单位宽度



class ItemR extends Component{

  constructor(props) {
      super(props);
      this.state = {
        scale: new Animated.Value(1),
      };
  }

  pressImg(){

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
            Switcher.turn(this.props.item.itemLabel,this.props.item.tagType,this.props.item.jsonStr);
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
          key={this.props.item.itemLabel}
          style={[styles.cRight,{transform:[{scale:this.state.scale}]}]}
          onPress={this.pressImg.bind(this)}
          onPressIn={this._pressIn.bind(this)}
          onPressOut={this._pressOut.bind(this)}>

          <View style={styles.textC}>
            <Text style={styles.titleS}>
              {this.props.item.itemLabel}
            </Text>

            <Text style={styles.summaryS}>
              {this.props.item.itemSummary}
            </Text>
          </View>

          <Image
            source={{uri: this.props.item.imgURL}}
            defaultSource={{uri: Config.imageTaken}}
            style={[styles.itemImg,{marginLeft:6}]}
          />

        </TouchableOpacity>

      );
  };
};

class ItemREnd extends Component{

  constructor(props) {
      super(props);
      this.state = {
        scale: new Animated.Value(1),
      };
  }

  pressImg(){

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
            Switcher.turn(this.props.item.itemLabel,this.props.item.tagType,this.props.item.jsonStr);
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
          key={this.props.item.itemLabel}
          style={[styles.cRightEnd,{transform:[{scale:this.state.scale}]}]}
          onPress={this.pressImg.bind(this)}
          onPressIn={this._pressIn.bind(this)}
          onPressOut={this._pressOut.bind(this)}

          >

          <View style={styles.textC}>
            <Text style={styles.titleS}>
              {this.props.item.itemLabel}
            </Text>

            <Text style={styles.summaryS}>
              {this.props.item.itemSummary}
            </Text>
          </View>

          <Image
            source={{uri: this.props.item.imgURL}}
            defaultSource={{uri: Config.imageTaken}}
            style={[styles.itemImg,{marginLeft:6}]}
          />

        </TouchableOpacity>

      );
  };
};



export default class WithImgBlockItemView extends Component{

    constructor(props) {
        super(props);
        this.state = {
          leftImgBlockHeight:itemH,
          leftImgURL:Config.imageTaken,
          leftTextColor:'#C62328',
          scale: new Animated.Value(1),
        };
    }

    componentWillMount(){
      this.setState({
        leftImgBlockHeight:(this.props.rightNum*itemH+marginCell*(this.props.rightNum-1)),
        leftImgURL:this.props.withImgDataL.imgURL,
        leftTextColor:this.props.withImgDataL.textColor,
      });
    }

    _onPressLeft(){

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
              this._switch(this.props.withImgDataL.itemLabel,this.props.withImgDataL.tagType,this.props.withImgDataL.jsonStr);
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

    _switch(title,tag,jsonStr){
      if (Platform.OS === 'ios') {

      }else if (Platform.OS === 'android') {
        Switcher.turn(title,tag,jsonStr);
      }
    }

    render(){
      return(
        <View style={styles.container}>

          <TouchableOpacity
            activeOpacity={1}
            onPressIn={this._pressIn.bind(this)}
            onPressOut={this._pressOut.bind(this)}
            onPress={this._onPressLeft.bind(this)}
            style={[{width:subItemW,height:this.state.leftImgBlockHeight,transform:[{scale:this.state.scale}]}]}>
            <Image
                source={{uri: this.state.leftImgURL}}
                defaultSource={{uri: this.state.leftImgURL}}
                style={{width:subItemW,height:this.state.leftImgBlockHeight}}>

                <View style={[{ width:subItemW,height:itemH,paddingLeft:6,
                                paddingRight:6,
                                justifyContent:'center'
                              }]}>
                  <Text style={[styles.titleS,{color:this.state.leftTextColor}]}>
                    {this.props.withImgDataL.itemLabel}
                  </Text>

                  <Text style={[styles.summaryS,{color:this.state.leftTextColor}]}>
                    {this.props.withImgDataL.itemSummary}
                  </Text>
                </View>

            </Image>

          </TouchableOpacity>

          <View style={{width:subItemW,height:this.state.leftImgBlockHeight,marginLeft:2}}>
            {this.props.itemData.map((item) => (
              item.isEnd?<ItemREnd key={item.itemLabel} item={item} />:<ItemR key={item.itemLabel} item={item}/>
            ))}
          </View>

        </View>
      )
    }
}

const styles = StyleSheet.create({
  container:{
    width:itemW,
    marginLeft:marginH,
    marginRight:marginH-2,
    marginBottom:1,
    flexDirection:'row',
    alignItems:'center',
    paddingBottom:1,
    // borderColor:'blue',
    // borderWidth:1,
  },
  cRight:{
    backgroundColor:'#000',
    flex:1,
    height:itemH,
    flexDirection:'row',
    // borderWidth:1,
    // borderColor:'red',
    paddingLeft:6,
    paddingRight:6,
    alignItems:'center',
    // marginRight:2,
    marginBottom:2,
    // justifyContent:'center',
  },
  cRightEnd:{
    backgroundColor:'#000',
    flex:1,
    height:itemH,
    flexDirection:'row',
    // borderWidth:1,
    // borderColor:'red',
    paddingLeft:6,
    paddingRight:6,
    alignItems:'center',
    // marginRight:2,
    // marginBottom:1,
    // justifyContent:'center',
  },
  itemImg:{
  //  borderRadius:50,
    // flex:1,
    alignSelf:'center',
    width: ((screenWidth - 22*2 - 8*3) / 4)-30,
    height: ((screenWidth - 22*2 - 8*3) / 4)-30,
    resizeMode:'contain',
    marginRight:4
  },
  titleS:{
    fontWeight:'bold',
    fontSize:16,
    color:'#fff',
    marginBottom:4,
  },
  summaryS:{
    // marginLeft:8,
    fontSize:11,
    color:'#fff'
  },
  textC:{
    flex:1,
    // paddingLeft:6,
    // borderWidth:1,
    // borderColor:'red',
  }


});

module.exports = WithImgBlockItemView;
