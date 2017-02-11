'use strict';
import React, {
    Component,
    View,
    StyleSheet,
    ToastAndroid,
    Animated,
    Platform,
    Dimensions
} from 'react-native';

var Config = require('./Config');
import ViewPager from 'react-native-viewpager';
import ImgPage from './ImgPage';
var Switcher = require('./natives/Switcher');

//请求参数
var Security = require('./natives/SecurityModule');
var myHeaders = new Headers();
myHeaders.append('User-Agent','Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0 HXMall Android');
var myInit = { method: 'GET',
               headers: myHeaders,
               mode: 'cors',
               cache: 'default' };
var myRequest;

// var JSON=require('json5');

const BANNER_IMGS = [
  {
    url:'default',
    type:'d0',
    slide:'banner_3x',
  },
];

var currentPage = 0;
var that;
var dataSource = new ViewPager.DataSource({
      pageHasChanged: (p1, p2) => p1 !== p2,
  });
var slideData = [];

var screenWidth = Dimensions.get('window').width;
var pageHeight = screenWidth*0.450746;

var isLoadSuccess = false;
var isClickable = true;
export default class ADSliderView extends Component {

    constructor(props) {
        super(props);
        // 实际的DataSources存放在state中
        this.state = {
            dataSource: dataSource.cloneWithPages(BANNER_IMGS),
            isViewPagerLoop: false,
            isAutoPlay: false,
        };
    }

    _initRequestData(){
      // http://10.0.15.201:8089/Ver/Index/version?os=Android&deviceType=Android
      let params = "deviceType=Android";
      Security.getEncodeThings(params,
        (hostURL,requestParams)=>{
          //回调成功
          let requestURL =hostURL + Config.homeSliderURL + requestParams;
          myRequest = new Request(requestURL,myInit);
          that._fetchForADs();

        },(msg)=>{
          //操作失败
          console.log("Fetch failed!", msg);
        });

    }

    _fetchForADs(){
        fetch(myRequest).then(function(res) {
            // res instanceof Response == true.
            // ToastAndroid.show("fetch 1", ToastAndroid.SHORT)
            if (res.ok) {
              console.log("2: ");
              that.timer && clearTimeout(that.timer);
              isLoadSuccess = true;

              res.json().then(function(data) {

                    slideData = data;
                    if (slideData.length > 1) {
                      that.setState({
                        isViewPagerLoop: true,
                        isAutoPlay: true,
                      });
                    }
                    if (slideData.length >= 1) {
                      that.setState({
                                dataSource: dataSource.cloneWithPages(data)
                            });
                    }

              });

            } else {
              // ToastAndroid.show("fetch 2", ToastAndroid.SHORT)
              that.timer = setTimeout(() => { that._fetchForADs();},5000);
              console.log("Looks like the response wasn't perfect, got status", res.status);
            }
          }, function(e) {
            that.timer = setTimeout(() => { that._fetchForADs();},5000);
            console.log("Fetch failed!", e);
          });
    }

    componentWillMount(){
      this._initRequestData();
      that = this;
    }

    _renderPage(data, pageID) {
        return (
          <ImgPage
            slide={data.slide}
            onClick={this.showMessage}
            height={pageHeight}
             />
        );
    }

    _onChangePage(page){
        currentPage = page;
    }

    showMessage(){
        if (isLoadSuccess) {

          if (isClickable) {
                isClickable = false;
                that.timer = setTimeout(() => {isClickable = true;},500);
                if (Platform.OS === 'ios') {
                  Switcher.jumpToPromotionViewWithUrl(slideData[currentPage].url, slideData[currentPage].type);
                }else if (Platform.OS === 'android') {
                  let str = JSON.stringify(slideData[currentPage]);
                  Switcher.turn("slider","AD_SLIDER",str);
                }
            }

        }
    }

    render() {
        return (
          <View>
              <ViewPager
                  style={{height:pageHeight}}
                  dataSource={this.state.dataSource}
                  renderPage={this._renderPage.bind(this)}
                  onChangePage={this._onChangePage}
                  isLoop={this.state.isViewPagerLoop}
                  autoPlay={this.state.isAutoPlay}
                  durationAutoPlay={6500}
                />
          </View>
        )
    }
}

const styles = StyleSheet.create({

});

module.exports = ADSliderView;
