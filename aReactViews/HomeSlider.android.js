
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

    ScrollView,
    StyleSheet,
    Platform,
    Animated,
    Dimensions,
    PixelRatio,
    ToastAndroid,
    DeviceEventEmitter
} from 'react-native';

var StretchScrollView = require('./natives/StretchScrollView');

var screenHeight = Dimensions.get('window').height;
var screenWidth = Dimensions.get('window').width;
var ButtonView = require('./Button');
var Switcher = require('./natives/Switcher');
var HomeInfosShow = require('./natives/HomeInfosShow');
var PhoneInfoModule = require('./natives/PhoneInfoModule');


var Security = require('./natives/SecurityModule');
var myHeaders = new Headers();
myHeaders.append('User-Agent','Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0 HXMall Android');
var myInit = { method: 'GET',
               headers: myHeaders,
               mode: 'cors',
               cache: 'default' };
var myRequest;


var Config = require('./Config');

// import Header from './Header';
import NewHeader from './HeaderNew';
import TrafficInfo from './blocks/TrafficInfoBlock'

import ADSliderView from './ADSliderView';
import MiddleGridViewBlock from './blocks/MiddleGridViewBlock';
import ThirdBlocksView from './blocks/ThirdBlocksView';
import ThirdBlocksTitleView from './blocks/ThirdBlocksTitleView';
import InformationBlocksView from './blocks/InformationBlocksView';

// import BlockAround from './blocks/BlockAround';

var that;
export default class HomePage extends Component {

    constructor(props) {
        super(props);
        this.state = {
            viewTotalHeight:screenHeight - 75,
            weatherIcon: Config.imageTaken,
            weather: "",
            weatherTemp:"",
            traffic: "",
            cityName: "定位中...",
            trafficIcon: Config.imageTaken,
            trafficPadding:-2,
            mMonth:"",
            mDay:"",
        };
    }

    _getPhoneInfo(){
      var statusBar =  0;
      var menuBar = 0;
      PhoneInfoModule.getPhoneHeadHeight(function(h:number){
          statusBar = h;
          PhoneInfoModule.getPhoneMenuKeyboardHeight(function(h:number){
            menuBar = h;
            that.setState({
                viewTotalHeight:screenHeight - 49 - statusBar - menuBar,
            });
          });
      });
    }

    _initRequestData(){

      let params = "deviceType=Android&cityname="+this.state.cityName;
      Security.getEncodeThingsService(params,
        (hostURL,requestParams)=>{
          //回调成功
          let requestURL =hostURL + Config.homeWeatherURL + requestParams;
          // testURL = requestURL;
          console.log("apiClient", requestURL);
          myRequest = new Request(requestURL,myInit);
          that.timer_Home && clearTimeout(that.timer_Home);
          that._fetchForWeather()

        },(msg)=>{
          //操作失败
          console.log("222Fetch failed!", msg);
        });

    }

    _fetchForWeather(){

      console.log("myRequest!", myRequest);

      fetch(myRequest).then(function(res) {
          if (res.ok) {
            that.timer_Home && clearTimeout(that.timer_Home);
            res.json().then(function(data) {
              console.log("data!", data);
              if (data) {
                if (data.weather) {
                  // ToastAndroid.show(data.message.temp+"°"+data.message.weather, ToastAndroid.SHORT)
                  that.setState({
                    weatherTemp:data.temp,
                    weather:data.weather
                  });
                }

                var w = data.weather;

                var currentWeatherIcon = Config.imageTaken;

                that.setState({
                 weatherIcon: "weather_sunny"
                });

            if (w.contains("雨")) {
                 if (w.contains("大") || w.contains("暴")) {
                     currentWeatherIcon = "weather_dayu";
                 } else if (w.contains("中")) {
                     currentWeatherIcon = "weather_zhongyu";
                 } else if (w.contains("雪")) {
                     currentWeatherIcon = "weather_snow";
                 } else {
                     currentWeatherIcon = "weather_xiaoyu";
                 }
                 that.setState({
                  weatherIcon: currentWeatherIcon
                 });
                 return;
             }

             if (w.contains("雪")) {
                 currentWeatherIcon = "weather_snow";
                 that.setState({
                  weatherIcon: currentWeatherIcon
                 });
                 return;
             }

             if (w.contains("晴")) {
                 currentWeatherIcon = "weather_sunny";
                 that.setState({
                  weatherIcon: currentWeatherIcon
                 });
                 return;
             }

             if (w.contains("云")) {
                 currentWeatherIcon = "weather_yintian";
                 that.setState({
                  weatherIcon: currentWeatherIcon
                 });
                 return;
             }

             if (w.contains("风")) {
                 currentWeatherIcon = "weather_feng";
                 that.setState({
                  weatherIcon: currentWeatherIcon
                 });
                 return;
             }


             if (w.contains("雾")) {
                 currentWeatherIcon = "weather_feng";
                 that.setState({
                  weatherIcon: currentWeatherIcon
                 });
             } else {
                 currentWeatherIcon = "weather_yintian";
                 that.setState({
                  weatherIcon: currentWeatherIcon
                 });
             }

              }
            });

          } else {
            that.timer_Home = setTimeout(() => { that._fetchForWeather();},10000);
            console.log("数据解析问题", res.status);
          }
        }, function(e) {
          that.timer_Home = setTimeout(() => { that._fetchForWeather();},10000);
          console.log("111Fetch failed!", e);
        });
    }

    componentWillMount(){
      that = this;

      this._getPhoneInfo();

      DeviceEventEmitter.addListener('homeCityName', function(e: Event) {

          that.setState({
              cityName: e.city_name,
              mMonth:e.current_month,
              mDay:e.current_day,
          });

          // ToastAndroid.show(that.state.cityName, ToastAndroid.SHORT)
          console.log("apiClient", 'ss: '+e.city_name + ' mMonth: '+e.current_month+' mDay: '+e.current_day);
          that._initRequestData();
      });


      DeviceEventEmitter.addListener('homeTrafficShow', function(e: Event) {
        if (e.traffic_message === 'null' ) {
            that.setState({
                trafficIcon:Config.imageTaken,
                traffic: "",
                trafficPadding: -2,
            });
        }else {
            that.setState({
              trafficIcon: Config.homeTrafficIcon,
              traffic: e.traffic_message,
              trafficPadding: 4,
            });
        }
      });

      // HomeInfosShow.firstInitData();
    }

    componentDidMount(){
      HomeInfosShow.firstInitData();
    }

    _onPressButton(title,tag,jsonStr){
        Switcher.turn(title,tag,jsonStr);
    }

    render() {
        return (
          <View>
            <Image style={{
                width:screenWidth,
                height:this.state.viewTotalHeight,
                resizeMode:'cover',
              }} source={{uri: Config.homeBigBG}} defaultSource={{uri: Config.homeBigBG}}>
                <StretchScrollView
                    showsVerticalScrollIndicator={false}
                    style={[styles.rvbsv,{height:this.state.viewTotalHeight}]}
                    isScrollNever = {true}
                    >
                        <View>
                          <View style={styles.sloganC}>
                            <Image
                              style={styles.slogan}
                              source={{uri: Config.homeSlogan}}
                              />
                          </View>

                            <ADSliderView />
                            <ThirdBlocksTitleView mTitle='恒信车管家' mSummary='REPAIR'/>
                            <MiddleGridViewBlock />
                            <View style={{height:12,width:screenWidth}}>
                            </View>
                            <InformationBlocksView mTitle='汽车资讯'/>
                            <ThirdBlocksView />
                        </View>
                </StretchScrollView>
              </Image>

              <NewHeader mMonth={this.state.mMonth} mDay={this.state.mDay} weatherTemp={this.state.weatherTemp} weatherIcon={this.state.weatherIcon} weatherInfo={this.state.weather} cityName={this.state.cityName}/>
          </View>
        )
    }
}

const styles = StyleSheet.create({
      sloganC:{
        height:44,
        width:screenWidth,
        justifyContent:'center',
        // borderWidth:1,
        // borderColor:'red'
      },
      slogan:{
        height:36,
        margin:2,
        width:screenWidth-4,
        resizeMode:'contain',
        // marginTop:10,
      },
      rvbsv:{
        // marginTop:Platform.OS === 'ios'?68:Config.homeHeadHeight,
        height:screenHeight - 69,
        width:screenWidth,
        paddingBottom:20,
        // backgroundColor:'red',
      },
});

module.exports = HomePage;
