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

          <View style={styles.viewLabel}>
            <Image style={styles.imgLabel} resizeMode='cover'
              defaultSource={{uri: Config.homeModuleYellow}}
              source={{uri: Config.homeModuleYellow}}>
              <Text style={styles.textLabel}>
                周边
              </Text>
            </Image>
          </View>

          <View style={styles.vertiaclWarp}>

            <View style={styles.hWarp1}>

                  <ButtonView
                    style={styles.smallView}
                    title="加油站"
                    backgroundColor={Config.homeBlockBgColorYellow}
                    backgroundImage={{uri:Config.imageTaken}}
                    textStyle = {styles.buttonText}
                    onPress={this._onPressButton}
                    tagType="AROUND_MAP"
                    jsonStr={Config.aroundMapGas}
                    />

                    <ButtonView
                      style={styles.smallView}
                      title="停车场"
                      backgroundColor={Config.homeBlockBgColorYellow}
                      backgroundImage={{uri:Config.imageTaken}}
                      textStyle = {styles.buttonText}
                      onPress={this._onPressButton}
                      tagType="AROUND_MAP"
                      jsonStr={Config.aroundMapPark}
                      />

            </View>

            <View style={styles.hWarp2}>

                  <ButtonView
                    style={styles.smallView}
                    title="充电站"
                    backgroundColor={Config.homeBlockBgColorYellow}
                    backgroundImage={{uri:Config.imageTaken}}
                    textStyle = {styles.buttonText}
                    onPress={this._onPressButton}
                    tagType="AROUND_MAP"
                    jsonStr={Config.aroundMapCharger}
                     />


                 <ButtonView
                   style={styles.smallView}
                   title="路况"
                   backgroundColor={Config.homeBlockBgColorYellow}
                   backgroundImage={{uri:Config.imageTaken}}
                   textStyle = {styles.buttonText}
                   onPress={this._onPressButton}
                   tagType="AROUND_MAP"
                   jsonStr={Config.aroundMapTraffic}
                    />
            </View>
          </View>
        </View>
      )
    }
}

var styles = BlockStyles;

module.exports = BlockView3;
