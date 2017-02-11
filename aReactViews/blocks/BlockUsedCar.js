'use strict';
var React = require('react-native');
var {
  	Text,
  	View,
  	Image,
  	TouchableHighlight,
    TouchableOpacity,
    Component,
    Platform,
} = React;


var BlockStyles = require('../styles/BlockStyles');
var Config = require('../Config');

var Switcher = require('../natives/Switcher');
var ButtonView = require('../Button');

export default class BlockUsedCar extends Component{


  _onPressButton(title,tag,jsonStr,iosType){

    if (Platform.OS === 'ios') {
      Switcher.turn(title, tag, jsonStr, "");
    }else if (Platform.OS === 'android') {
      console.log(tag);
        Switcher.turn(title,tag,jsonStr);
    }
  }

    render(){
      return(
        <View style={styles.container}>

          <View style={styles.viewLabel}>
            <Image style={styles.imgLabel} resizeMode='cover'
              defaultSource={{uri: Config.homeModuleOrange}}
              source={{uri: Config.homeModuleOrange}}>
              <Text style={styles.textLabel}>
                卖车
              </Text>
            </Image>
          </View>

          <View style={styles.vertiaclWarp}>
            <View style={styles.hWarp1}>

                   <ButtonView
                    style={styles.smallView}
                    title="二手车估价"
                    backgroundColor={Config.homeBlockBgColorOrange}
                    backgroundImage={{uri:Config.imageTaken}}
                    textStyle = {styles.buttonText}
                    onPress={this._onPressButton}
                    tagType="USED_CAR_EVALUATE"
                     />

            </View>

            <View style={styles.hWarp2}>
            
                  <ButtonView
                    style={styles.smallView}
                    title="买二手车"
                    backgroundColor={Config.homeBlockBgColorOrange}
                    backgroundImage={{uri:Config.imageTaken}}
                    textStyle = {styles.buttonText}
                    onPress={this._onPressButton}
                    tagType="BUY_USED_CAR"
                    jsonStr={Config.usedCarBuy}
                     />

                   <ButtonView
                     style={styles.smallView}
                     title="卖二手车"
                     backgroundColor={Config.homeBlockBgColorOrange}
                     backgroundImage={{uri:Config.imageTaken}}
                     textStyle = {styles.buttonText}
                     onPress={this._onPressButton}
                     tagType="SALE_USED_CAR"
                     jsonStr={Config.usedCarSale}
                      />

            </View>
          </View>
        </View>
      )
    }
}

var styles = BlockStyles;

module.exports = BlockUsedCar;
