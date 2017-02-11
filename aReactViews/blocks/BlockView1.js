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

export default class BlockView1 extends Component{


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
              defaultSource={{uri: Config.homeModuleRed}}
              source={{uri: Config.homeModuleRed}}>
              <Text style={styles.textLabel}>
                买车
              </Text>
            </Image>
          </View>

          <View style={styles.vertiaclWarp}>
              <View style={styles.hWarp1}>

                  <ButtonView
                      style={styles.smallView}
                      title="4S店网上商城"
                      backgroundColor={Config.homeBlockBgColorRed}
                      backgroundImage={{uri:Config.imageTaken}}
                      textStyle = {styles.buttonText}
                      onPress={this._onPressButton}
                      tagType="NET_4S_SHOP"
                      jsonStr={Config.buyCar4sShop}
                      />

              </View>

              <View style={styles.hWarp3}>
              
                  <ButtonView
                      style={styles.smallView}
                      title="新车销售"
                      backgroundColor={Config.homeBlockBgColorRed}
                      backgroundImage={{uri:Config.imageTaken}}
                      textStyle = {styles.buttonText}
                      onPress={this._onPressButton}
                      tagType="USE_MODULE"
                      jsonStr={Config.buyCarNewSale}
                      />

                  <ButtonView
                      style={styles.smallView}
                      title="用品销售"
                      backgroundColor={Config.homeBlockBgColorRed}
                      backgroundImage={{uri:Config.imageTaken}}
                      textStyle = {styles.buttonText}
                      onPress={this._onPressButton}
                      tagType="Accessory_Sell"
                      jsonStr={Config.buyCar4sShop}
                    />
                      
              </View>
          </View>
        </View>
      )
    }
}

var styles = BlockStyles;

module.exports = BlockView1;
