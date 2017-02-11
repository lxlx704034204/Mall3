'use strict';
import React,{    PropTypes,Component}from'react';
// import Text from './components/ShopText';
import {
  	Text,
  	View,
  	Image,
    TextInput,
    LayoutAnimation,
    TouchableWithoutFeedback,
  	TouchableHighlight,
    TouchableOpacity,

    StyleSheet,
    Platform,
    Animated,
    Dimensions,
    ToastAndroid
} from 'react-native';

var screenWidth = require('Dimensions').get('window').width;
var width;
var height;

var isClickable = true;

var Button = React.createClass({
    getInitialState: function() {
       return {scale: new Animated.Value(1)};
    },
    getDefaultProps:function(){
        return {
            title:null,
            backgroundImage:null,
            backgroundColor:'#00000000',
            resizeMode:'stretch',
            cornerRadius: 0,
            tagType: null,
            jsonStr: null,
            iosType: null,
        };
    },
    propTypes:{
        title:              React.PropTypes.string,
        backgroundImage:    React.PropTypes.oneOfType([React.PropTypes.string,React.PropTypes.object]),
        backgroundColor:    React.PropTypes.string,
        borderColor:        React.PropTypes.string,
        resizeMode:         React.PropTypes.string,
        // style:              React.PropTypes.object,
        width:              React.PropTypes.number,
        height:             React.PropTypes.number,
        cornerRadius:       React.PropTypes.number,
        fontSize:           React.PropTypes.number,
        fontWeight:         React.PropTypes.string,
        fontFamily:         React.PropTypes.string,
        color:              React.PropTypes.string,
        onPress:            React.PropTypes.func,
        // textStyle:          React.PropTypes.object
        tagType:            React.PropTypes.string,
        jsonStr:            React.PropTypes.string,
        iosType:            React.PropTypes.string,
    },

    render: function() {
        var backgroundImage = this.props.backgroundImage;
        if (typeof backgroundImage == "string") {
            backgroundImage = {uri:backgroundImage}
        }

        return (
            <TouchableOpacity
                ref='buttonWrapper'
                activeOpacity={1}
                style={[
                    styles.container,
                    this.props.style,
                    {
                        width:this.props.width,
                        height:this.props.height,
                        backgroundColor:this.props.backgroundColor,
                        transform:[{scale:this.state.scale}]
                    }
                ]}
                onPressIn={this._buttonDown}
                onPressOut={this._buttonUp}
                onPress={this._buttonPress}
            >
                <Image
                    style={[styles.backgroundImage,
                        // {transform:[{scale:this.state.scale}]}
                    ]}
                    source={backgroundImage}
                    resizeMode={this.props.resizeMode}
                    //backgroundColor={this.props.backgroundColor}
                >
                    <Text
                        ref='buttonText'
                        style={[styles.text,
                            {
                                fontFamily  :   this.props.fontFamily,
                                fontSize    :   this.props.fontSize,
                                color       :   this.props.color,
                                fontWeight  :   this.props.fontWeight,
                            }, this.props.textStyle]}>
                        {this.props.title}
                    </Text>
                </Image>
            </TouchableOpacity>
        );
    },

    _jump:function(){
      if (this.props.onPress instanceof Function) {
          this.props.onPress(this.props.title,this.props.tagType,this.props.jsonStr,this.props.iosType);
      }
    },

    _buttonDown: function(event){
        console.log('buttonDown');

        Animated.timing(                          // Base: spring, decay, timing
          this.state.scale,                 // Animate `bounceValue`
          {
            toValue: 0.93,                         // Animate to smaller size
            duration: 100
          }
        ).start();
    },
    _buttonUp: function(event){
        console.log('buttonUp');

        Animated.timing(                          // Base: spring, decay, timing
          this.state.scale,                 // Animate `bounceValue`
          {
            toValue: 1,                         // Animate to smaller size
            duration: 100
          }
        ).start();
    },
    _buttonPress: function(event){


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
                  this._jump();
                }

                  Animated.timing(                          // Base: spring, decay, timing
                                this.state.scale,                 // Animate `bounceValue`
                                {
                                  toValue: 1,                         // Animate to smaller size
                                  duration: 100
                                }
                              ).start();
              }.bind(this));

    }
});


var styles = StyleSheet.create({
    container:{
        overflow:'hidden',
    },
    backgroundImage:{
        borderColor:'#000',
        flex:1,
        justifyContent:'center',
    },
    text:{
        backgroundColor:'#00000000',
        // borderColor:'#00f',
        // borderWidth:2,
        textAlign:'center',
    }
});

module.exports = Button;
