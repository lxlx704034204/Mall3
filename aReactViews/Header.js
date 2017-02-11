'use strict';
import React, {
  Component,
  Image,
  // TextInput,
  Text,
  View,
  StyleSheet,
  Platform,
  Dimensions,
  TouchableWithoutFeedback,
  ToastAndroid,
  TouchableOpacity
} from 'react-native';

var Security = require('./natives/SecurityModule');
var myHeaders = new Headers();
myHeaders.append('User-Agent','Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0 HXMall Android');
var myInit = { method: 'GET',
               headers: myHeaders,
               mode: 'cors',
               cache: 'default' };
var myRequest;

var screenWidth = Dimensions.get('window').width;
var Switcher = require('./natives/Switcher');
var Config = require('./Config');
var that;
export default class Header extends Component{

  constructor(props) {
      super(props);
      // 实际的DataSources存放在state中
      this.state = {
          keyword:'搜索'
      };
  }

  _onPressSearch(){
    if (Platform.OS === 'ios') {
      Switcher.jumpToSearch();
    }else if (Platform.OS === 'android') {
      Switcher.turn("搜索框","SEARCH_BOX","");
    }
  }

  _onPressMessage(){
    if (Platform.OS === 'ios') {
      Switcher.jumpToMessage();
    }else if (Platform.OS === 'android') {
      Switcher.turn("消息","MESSAGE","");
    }
  }

  _initRequestData(){
    // http://10.0.15.201:8089/Ver/Index/version?os=Android&deviceType=Android
    // deviceType=Android
    let params = "deviceType=Android";
    Security.getEncodeThings(params,
      (hostURL,requestParams)=>{
        //回调成功
        let requestURL =hostURL + Config.homeHeadKeywordURL + requestParams;
        // testURL = requestURL;
        myRequest = new Request(requestURL,myInit);
        // that._fetchForADs();
        that._fetchForHotKeyWord()

      },(msg)=>{
        //操作失败
        console.log("Fetch failed!", msg);
      });

  }

  _fetchForHotKeyWord(){

      fetch(myRequest).then(function(res) {
          if (res.ok) {
            that.timer && clearTimeout(that.timer);
            res.json().then(function(data) {
              if (data.recommand) {
                that.setState({
                  keyword:data.recommand
                });
              }
            });

          } else {
            that.timer = setTimeout(() => { that._fetchForHotKeyWord();},5000);
            console.log("数据解析问题", res.status);
          }
        }, function(e) {
          that.timer = setTimeout(() => { that._fetchForHotKeyWord();},5000);
          console.log("Fetch failed!", e);
        });

  }

  componentWillMount(){
    that = this;
    // this._initRequestData();
  }

  render(){
    return(
      <View style={styles.Header_}>
        <Image source={{uri: Config.homeHeadLogo}} defaultSource ={{uri: Config.homeHeadLogo}} style={styles.logo}/>
        <View style={styles.searchBox}>
          <TouchableWithoutFeedback onPress={this._onPressSearch}>
             <Text style={styles.searchText}>
                {this.state.keyword}
             </Text>
          </TouchableWithoutFeedback>
          <Image source={{uri: Config.homeHeadSearch}} defaultSource={{uri: Config.homeHeadSearch}} style={styles.searchIcon}/>
        </View>
        <TouchableOpacity onPress={this._onPressMessage}>
          <Image
            source={{uri:Config.homeHeadMessage}}
            defaultSource={{uri:Config.homeHeadMessage}}
            style={styles.messageImg}
            />
        </TouchableOpacity>
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
    paddingLeft:10,
    paddingRight:10,
    paddingTop:Platform.OS === 'ios'?20:Config.homeHeadPaddingTop,
    height:Platform.OS === 'ios'?68:Config.homeHeadHeight,
    backgroundColor:'#fffffff1',
    alignItems:'center',
  },
  logo:{
    marginLeft:20,
    height:24,
    width:64,
    resizeMode:'stretch'
  },
  searchBox:{
    height:26,
    flexDirection:'row',
    flex:1,
    borderRadius:5,
    borderColor:'#999999',
    borderWidth:1,
    alignItems:'center',
    marginLeft:20,
    marginRight:12,
  },
  scanIcon:{
    height:17,
    width:32,
    resizeMode:'stretch',
  },
  searchIcon:{
    marginRight:6,
    marginLeft:6,
    width:16.7,
    height:16.7,
    resizeMode:'stretch',
  },
  voiceIcon:{
    marginLeft:5,
    marginRight:8,
    width:15,
    height:20,
    resizeMode:'stretch',
  },
  searchText:{
    flex:1,
    backgroundColor:'transparent',
    fontSize:12,
    color:"#000",
    marginLeft:12,
  },
  messageImg:{
    height:24,
    width:20,
    marginRight:8,
    marginTop:2
  }
});
// AppRegistry.registerComponent('Header', () => Header);
