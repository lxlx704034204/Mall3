'use strict';
var React = require('react-native');
var {
  	Text,
  	View,
  	Image,
  	TouchableHighlight,
    TouchableOpacity,
    Component,
    Platform
} = React;
var BlockStyles = require('../styles/BlockStyles');
var Config = require('../Config');

var Switcher = require('../natives/Switcher');
var ButtonView = require('../Button');

export default class BlockView2 extends Component{

    _onPressButton(title,tag,jsonStr,iosType){

          if (Platform.OS === 'ios') {

          }else if (Platform.OS === 'android') {
              Switcher.turn(title,tag,jsonStr);
          }
    }

    render(){
      return(
        <View style={styles.container}>

          <View style={styles.viewLabel}>
            <Image style={styles.imgLabel} resizeMode='cover'
              defaultSource={{uri: Config.homeModuleBlue}}
              source={{uri: Config.homeModuleBlue}}>
              <Text style={styles.textLabel}>
                修车
              </Text>
            </Image>
          </View>

          <View style={styles.vertiaclWarp}>
            <View style={styles.hWarp1}>

                  <ButtonView
                    style={styles.smallView}
                    title="常规保养"
                    backgroundColor={Config.homeBlockBgColorBlue}
                    backgroundImage={{uri:Config.imageTaken}}
                    textStyle = {styles.buttonText}
                    onPress={this._onPressButton}
                    tagType="NORMAL_MAINTENANCE"
                    jsonStr={Config.fixCarMaintenance}
                     />

            </View>

            <View style={styles.hWarp2}>
            
                <ButtonView
                  style={styles.smallView}
                  title="修车预约"
                  backgroundColor={Config.homeBlockBgColorBlue}
                  backgroundImage={{uri:Config.imageTaken}}
                  textStyle = {styles.buttonText}
                  onPress={this._onPressButton}
                  tagType="USE_MODULE"
                  jsonStr={Config.fixCarBookRepair}
                    />

                <ButtonView
                  style={styles.smallView}
                  title="紧急救援"
                  backgroundColor={Config.homeBlockBgColorBlue}
                  backgroundImage={{uri:Config.imageTaken}}
                  textStyle = {styles.buttonText}
                  onPress={this._onPressButton}
                  tagType="USE_MODULE"
                  jsonStr={Config.fixCarEmergencyRescue}
                    />
                    
            </View>
          </View>
        </View>
      )
    }
}

var styles = BlockStyles;

module.exports = BlockView2;
