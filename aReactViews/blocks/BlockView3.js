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

export default class BlockView3 extends Component{


    _onPressButton(title,tag,jsonStr,iosType){
      if (Platform.OS === 'ios') {

      }else if (Platform.OS === 'android') {
          Switcher.turn(title,tag,jsonStr);
      }
    }

    render(){
      return(
        <View style={styles.container}>

          <View style={[styles.viewLabel,styles.vertiaclWarp5Label]}>
            <Image style={styles.imgLabel} resizeMode='cover'
              defaultSource={{uri: Config.homeModulePurple}}
              source={{uri: Config.homeModulePurple}}>
              <Text style={styles.textLabel}>
                用车
              </Text>
            </Image>
          </View>

          <View style={[styles.vertiaclWarp,styles.vertiaclWarp5Label]}>

            <View style={styles.hWarp1}>

                  <ButtonView
                    style={styles.smallView}
                    title="违章查缴"
                    backgroundColor={Config.homeBlockBgColorPurple}
                    backgroundImage={{uri:Config.imageTaken}}
                    textStyle = {styles.buttonText}
                    onPress={this._onPressButton}
                    tagType="BREAK_RULES"
                    />

                  <ButtonView
                    style={styles.smallView}
                    title="驾驶证换证"
                    backgroundColor={Config.homeBlockBgColorPurple}
                    backgroundImage={{uri:Config.imageTaken}}
                    textStyle = {styles.buttonText}
                    onPress={this._onPressButton}
                    tagType="CHANGE_CERTIFICATE"
                    />

            </View>

            <View style={styles.hWarp2}>

                  <ButtonView
                    style={styles.smallView}
                    title="车辆年审"
                    backgroundColor={Config.homeBlockBgColorPurple}
                    backgroundImage={{uri:Config.imageTaken}}
                    textStyle = {styles.buttonText}
                    onPress={this._onPressButton}
                    tagType="ANNUAL_AUDIT"
                     />

                  <ButtonView
                    style={styles.smallView}
                    title="实时路况"
                    backgroundColor={Config.homeBlockBgColorPurple}
                    backgroundImage={{uri:Config.imageTaken}}
                    textStyle = {styles.buttonText}
                    onPress={this._onPressButton}
                    tagType="AROUND_MAP"
                    jsonStr={Config.aroundMapTraffic}
                     />

            </View>

            <View style={styles.hWarp2}>
                  <ButtonView
                    style={styles.smallView}
                    title="洗车"
                    backgroundColor={Config.homeBlockBgColorPurple}
                    backgroundImage={{uri:Config.imageTaken}}
                    textStyle = {styles.buttonText}
                    onPress={this._onPressButton}
                    tagType="CAR_WASH"
                     />


                   <ButtonView
                     style={styles.smallView}
                     title="代驾"
                     backgroundColor={Config.homeBlockBgColorPurple}
                     backgroundImage={{uri:Config.imageTaken}}
                     textStyle = {styles.buttonText}
                     onPress={this._onPressButton}
                     tagType="DESIGNATED_DRIVER"
                      />

            </View>

            <View style={styles.hWarp2}>
                  <ButtonView
                    style={styles.smallView}
                    title="买保险"
                    backgroundColor={Config.homeBlockBgColorPurple}
                    backgroundImage={{uri:Config.imageTaken}}
                    textStyle = {styles.buttonText}
                    onPress={this._onPressButton}
                    tagType="USE_MODULE"
                    jsonStr={Config.serviceInsurance}
                     />


                   <ButtonView
                     style={styles.smallView}
                     title="加油站"
                     backgroundColor={Config.homeBlockBgColorPurple}
                     backgroundImage={{uri:Config.imageTaken}}
                     textStyle = {styles.buttonText}
                     onPress={this._onPressButton}
                     tagType="AROUND_MAP"
                     jsonStr={Config.aroundMapGas}
                     />

              </View>

              <View style={styles.hWarp2}>

                <ButtonView
                  style={styles.smallView}
                  title="停车场"
                  backgroundColor={Config.homeBlockBgColorPurple}
                  backgroundImage={{uri:Config.imageTaken}}
                  textStyle = {styles.buttonText}
                  onPress={this._onPressButton}
                  tagType="AROUND_MAP"
                  jsonStr={Config.aroundMapPark}
                  />

                <ButtonView
                  style={styles.smallView}
                  title="充电站"
                  backgroundColor={Config.homeBlockBgColorPurple}
                  backgroundImage={{uri:Config.imageTaken}}
                  textStyle = {styles.buttonText}
                  onPress={this._onPressButton}
                  tagType="AROUND_MAP"
                  jsonStr={Config.aroundMapCharger}
                   />

              </View>

          </View>
        </View>
      )
    }
}

var styles = BlockStyles;

module.exports = BlockView3;
