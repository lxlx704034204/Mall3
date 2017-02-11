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
              <View style={{flexDirection: 'column', flex: 1}}>
               <Text style={[styles.textLabel,{marginBottom:3,}]}>
                 会员
               </Text>
               <Text style={styles.textLabel}>
                 中心
               </Text>
             </View>
            </Image>
          </View>

          <View style={styles.vertiaclWarp}>

            <View style={styles.hWarp1}>

                  <ButtonView
                    style={styles.smallView}
                    title="我的钱包"
                    backgroundColor={Config.homeBlockBgColorYellow}
                    backgroundImage={{uri:Config.imageTaken}}
                    textStyle = {styles.buttonText}
                    onPress={this._onPressButton}
                    tagType="MY_WALLET"
                    jsonStr={Config.vipMyWalletActivity}
                    />

            </View>

            <View style={styles.hWarp2}>

                  <ButtonView
                    style={styles.smallView}
                    title="车辆信息"
                    backgroundColor={Config.homeBlockBgColorYellow}
                    backgroundImage={{uri:Config.imageTaken}}
                    textStyle = {styles.buttonText}
                    onPress={this._onPressButton}
                    tagType="MY_CARS"
                    jsonStr={Config.vipAutoInfoActivity}
                     />


                 <ButtonView
                   style={styles.smallView}
                   title="会员规则"
                   backgroundColor={Config.homeBlockBgColorYellow}
                   backgroundImage={{uri:Config.imageTaken}}
                   textStyle = {styles.buttonText}
                   onPress={this._onPressButton}
                   tagType="VIP_RULE"
                    />
            </View>
          </View>
        </View>
      )
    }
}

var styles = BlockStyles;

module.exports = BlockView3;
