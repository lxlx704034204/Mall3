'use strict';
var React = require('react-native');
var {
  	StyleSheet,
    PixelRatio,
    Dimensions
} = React;


var screenHeight = Dimensions.get('window').height;
var screenWidth = Dimensions.get('window').width;

//子模块间间距
var spaceLength = 3;

var moduleNum = 2;

var buttonWidth = screenWidth*0.23125;
var buttonTextFont = (screenHeight > 568) ? 16 : 14;
var textLabelFont = (screenHeight > 568) ? 22 : 20;
var buttonHeight = buttonWidth;

//3个
moduleNum = 3;
var buttonheight3 = (buttonHeight/2.0 - spaceLength)*moduleNum+(moduleNum-1)*spaceLength;

//4个
moduleNum = 4;
var buttonheight4 = (buttonHeight/2.0 - spaceLength)*moduleNum+(moduleNum-1)*spaceLength;

//5个
moduleNum = 5;
var buttonheight5 = (buttonHeight/2.0 - spaceLength)*moduleNum+(moduleNum-1)*spaceLength;


var Config = require('../Config');
var BlockStyles = StyleSheet.create({

    vertiaclWarp3Label:{
      height:buttonheight3,
    },
    vertiaclWarp4Label:{
      height:buttonheight4,
    },
    vertiaclWarp5Label:{
      height:buttonheight5,
    },

  	container:{
      flex:1,
    	flexDirection:'row',
      marginTop:6,
      marginLeft:6,
      marginRight:6,
  	},
    viewLabel:{
      // flex:1,
      height:buttonHeight,
      width:buttonWidth,
      // borderWidth:1,
      // borderColor:'blue',
    },
    vertiaclWarp:{
      flex:1,
      height:buttonHeight
    },
    hWarp1:{
      flex:1,
      flexDirection:'row',
    },
    hWarp2:{
      flex:1,
      flexDirection:'row',
      marginTop:spaceLength,
    },
    hWarp3:{
      flex:1,
      flexDirection:'row',
      marginTop:spaceLength,
    },
    smallView:{
      flex:1,
      backgroundColor:'#CE4E45',
      marginLeft:spaceLength
    },
    textSon:{
      fontSize:buttonTextFont,
      color:"#fafafa",
    },
    textLabel:{
      flex:1,
      fontSize:textLabelFont,
      fontFamily:Config.homeLabelTextFamily,
      color:"#fafafa",
      textAlign:'center',
      textShadowColor:'#00000055',
      textShadowOffset:{width:2,height:2},
      backgroundColor:'#0000',
      // fontWeight:'bold',
    },
    buttonText:{
       fontSize:buttonTextFont,
       fontWeight:'bold',
       color:'#fafafa'
    },
    imgLabel:{
      flex:1,
      flexDirection:'row',
      alignItems:'center',
    }

});

module.exports = BlockStyles;
