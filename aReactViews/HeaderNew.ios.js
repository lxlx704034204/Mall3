'use strict';
import React,{PropTypes,Component}from'react';
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
    PixelRatio,
    ToastAndroid
} from 'react-native';
var HomeInfosShow = require('./natives/HomeInfosShow');
var screenWidth = Dimensions.get('window').width;
var Switcher = require('./natives/Switcher');
var Config = require('./Config');
var that;
var isClickable = true;
export default class Header extends Component{

  static propTypes = {
      weatherIcon: PropTypes.string,
      weatherInfo: PropTypes.string,
      weatherTemp: PropTypes.string,
      cityName:  PropTypes.string,
      mMonth: PropTypes.string,
      mDay: PropTypes.string,
  }

  constructor(props) {
      super(props);
  }

  componentWillMount(){
    that = this;
  }

  _onPressCity(){
    // var screenHeight = Dimensions.get('window').height;
    // ToastAndroid.show('screenHeight: '+screenHeight+" PixelRatio: "+PixelRatio.get(), ToastAndroid.SHORT)

    if (isClickable) {
          isClickable = false;
          that.timer = setTimeout(() => {isClickable = true;},500);
          HomeInfosShow.openCityList();
      }

  }

//带日期版本
  // <View style={styles.containerLeft}>
  //
  //         <View style={{flexDirection:'row',marginRight:4,}}>
  //
  //             <View style={{padding:0,alignItems:'center',justifyContent:'center',}}>
  //               <View style={{height:28,width:32,alignItems:'center',justifyContent:'center'}}>
  //                 <Text style={[styles.weatherContainer,{fontSize:22,textAlign:'center',textAlignVertical:'top'  }]}>
  //                   {this.props.weatherTemp}
  //                 </Text>
  //               </View>
  //
  //               <View  style={{height:14,width:32,alignItems:'center',justifyContent:'center'}}>
  //                 <Text style={[styles.weatherContainer],{fontWeight:'bold', fontSize:11,textAlign:'center',textAlignVertical:'top'}}>
  //                   {this.props.mMonth?  this.props.mMonth+'/'+this.props.mDay:''}
  //                 </Text>
  //               </View>
  //
  //             </View>
  //
  //             <Text style={[styles.weatherContainer,{fontWeight:'bold', fontSize:22}]}>
  //               °
  //             </Text>
  //
  //
  //         </View>
  //
  //         <View style={{width:1,height:41,backgroundColor:'#D5D5D5'}}>
  //         </View>
  //
  //         <View style={{alignItems:'center',justifyContent:'center',marginLeft:4}}>
  //
  //                       <View  style={{height:28,alignItems:'center',justifyContent:'center',width:32}}>
  //                         <Image style={[styles.weatherIcon]}
  //                         source={{uri: this.props.weatherIcon}}
  //                         defaultSource={{uri: Config.imageTaken}}/>
  //                       </View>
  //
  //                       <View  style={{height:14,width:32,alignItems:'center',justifyContent:'center'}}>
  //                         <Text style={[styles.weatherContainer,{fontSize:11,textAlign:'center',textAlignVertical:'top'}]}>
  //                           {this.props.weatherInfo}
  //                         </Text>
  //                       </View>
  //
  //         </View>
  //
  //
  //
  //
  // </View>


  render(){

    var WeatherView = this.props.weatherInfo?(



                                <View style={styles.containerLeft}>

                                        <View style={{alignItems:'center',justifyContent:'center'}}>

                                            <Image style={[styles.weatherIcon]}
                                            source={{uri: this.props.weatherIcon}}
                                            defaultSource={{uri: Config.imageTaken}}/>

                                        </View>

                                        <View style={{alignItems:'center',justifyContent:'center',marginLeft:2}}>

                                                      <View  style={{alignItems:'center',justifyContent:'center',width:32}}>
                                                        <Text style={[styles.weatherContainer,{textAlign:'center'}]}>
                                                          {this.props.weatherTemp+'°'}
                                                        </Text>
                                                      </View>

                                                      <View  style={{height:14,width:32,alignItems:'center',justifyContent:'center'}}>
                                                        <Text style={[styles.weatherContainer,{fontSize:11,textAlign:'center',textAlignVertical:'top'}]}>
                                                          {this.props.weatherInfo}
                                                        </Text>
                                                      </View>

                                        </View>
                                </View>
                                            ):(<View style={styles.containerLeft}>
                                                          <Text style={styles.weatherContainer}>
                                                            获取中...
                                                          </Text>
                                                      </View>);

    return(
      <View style={styles.Header_}>

        {WeatherView}

        <Image source={{uri: Config.homeHeadLogo}} defaultSource ={{uri: Config.homeHeadLogo}} style={styles.logo}/>

        <TouchableWithoutFeedback onPress={this._onPressCity}>
          <View style={styles.containerRight}>
            <Text style={styles.cityContainer}>
              {this.props.cityName}
            </Text>
            <Image source={{uri: Config.homeCityDownArrow}} defaultSource={{uri: Config.homeCityDownArrow}} style={styles.arrow}/>
          </View>
        </TouchableWithoutFeedback>

      </View>
    )
  }
}

const styles = StyleSheet.create({
  Header_:{
    position:'absolute',
    top:0,
    width:screenWidth,
    flexDirection:'row',
    paddingLeft:6,
    paddingRight:6,
    paddingTop:Platform.OS === 'ios'?20:Config.homeHeadPaddingTop,
    height:Platform.OS === 'ios'?68:Config.homeHeadHeight,
    backgroundColor:'#F4F4F6',
    alignItems:'center',
    // borderColor:'blue',
    // borderWidth:1,
    // justifyContent: 'center'
  },
  logo:{
    flex:1,
    height:32,
    width:68,
    resizeMode:'contain',
    alignSelf:'center',
  },
  weatherContainer:{
    // flex:1,
    // marginLeft:6,
    fontSize:12,
    color:'#444444',
    padding:0,

    // borderColor:'red',
    // borderWidth:1,
    justifyContent:"flex-end",
  },
  cityContainer:{
    flex:1,
    fontSize:14,
    color:'#444444',
    textAlign:'right',
    marginRight:6,
  },
  containerLeft:{
    // borderColor:'red',
    // borderWidth:1,
    flex:1,
    flexDirection:'row',
  },
  containerRight:{
    flex:1,
    flexDirection:'row',
    alignItems:'center',
    justifyContent:"flex-end",
  },
  arrow:{
    width:8,
    height:5 ,
    resizeMode:'contain',
  },
  weatherIcon:{
    width:24,
    height:24,
    resizeMode:'contain',
  }

});
