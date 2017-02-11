'use strict';
import React,{Component}from'react';
// import Text from './ShopText';
import {
  	Text,
  	View,
  	Image,
    TouchableWithoutFeedback,
  	TouchableHighlight,
    TouchableOpacity,
    PropTypes,
    StyleSheet,
    Platform,
    Animated,
    Dimensions,
    ToastAndroid,
    // PanResponder,
} from 'react-native';
import {createResponder} from 'react-native-gesture-responder';
var Config = require('../Config');
var Switcher = require('../natives/Switcher');
// var isClickable = true;
var screenHeight = Dimensions.get('window').height;
var screenWidth = Dimensions.get('window').width;
// var imgView = require('../ImgPage');

//请求参数
var Security = require('../natives/SecurityModule');
var myHeaders = new Headers();
myHeaders.append('User-Agent','Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0 HXMall Android');
var myInit = { method: 'GET',
               headers: myHeaders,
               mode: 'cors',
               cache: 'default' };
var myRequest;
var URL_SLIDER_AD;//ios  URL
// var midWidth = screenWidth*0.907407;
var midWidth = screenWidth;

var midHeight = screenWidth*0.3115942;

// var midPosition = (screenWidth-midWidth)/2;//中间view位置
var midPosition = 0;//中间view位置

// var sideHeight = midHeight*0.819047;
var sideHeight = midHeight;

// var sidewidth = sideHeight*2.333333;
var sidewidth = midWidth;

// var sidTop = (midHeight-sideHeight)/2;
var sidTop = 0;

// var leftSidePosition = -(sidewidth-midPosition);//左边view位置
var leftSidePosition = -sidewidth+10;//左边view位置

// var rightSidePosition = screenWidth-midPosition;//右边view位置
var rightSidePosition = screenWidth-10;//右边view位置

var singleViewHeight = midHeight;//单个view高度

var tempView0Left = midPosition;
var tempView1Left = leftSidePosition;
var tempView2Left = rightSidePosition;
var currentMidDataIndex=0;

var isCantMove = false;
var currentCenterView = 0;

var imgs = [
  {
    slide:'banner_3x',
    url:'',
    type:'d0',
  },
  {
    slide:'http://r2.ykimg.com//0510000057CE841467BC3D440606E723',
    url:'http://www.hxqc.com/zhuanti/wap/20151229/html/index.html',
    type:'1',
  },
  {
    slide:'http://r3.ykimg.com//051000005770910567BC3D38C904F14D',
    url:'http://www.hxqc.com/zhuanti/wap/20151229/html/index.html',
    type:'1',
  },
  {
    slide:'http://r4.ykimg.com//05100000577BAA1767BC3D3A1706E485',
    url:'http://www.hxqc.com/zhuanti/wap/20151229/html/index.html',
    type:'1',
  },
  {
    slide:'http://r1.ykimg.com//051000005762150067BC3D2A6E0154D2',
    url:'http://www.hxqc.com/zhuanti/wap/20151229/html/index.html',
    type:'1',
  }
];

var maxDatasSize = 1;//数据条目数
var isAutoplayActiving = true;//是否继续轮播
var delayForAnim = 600;//动画持续时长
var delayForAutoPlay = 3600;//轮播周期
var currentDownX = 0;//当前按下的x坐标
var moveDDistance = 35;//判断移动间隔左边移动距离
var moveLittleAsClick = 10;//小距离移动当点击 间隔
var that;
export default class ShopSwiper extends Component{

      _initRequestData(){
        // http://10.0.15.201:8089/Ver/Index/version?os=Android&deviceType=Android
        let params = "deviceType=Android";
        Security.getEncodeThings(params,
          (hostURL,requestParams)=>{
            //回调成功
            let requestURL =hostURL + Config.homeSliderURL + requestParams;
            console.log("apiClient", requestURL);
            myRequest = new Request(requestURL,myInit);
            that._fetchForADs();

          },(msg)=>{
            //操作失败
            console.log("Fetch failed!", msg);
          });

      }

    //获取轮播 列表
      _fetchForADs(){
          fetch(myRequest).then(function(res) {
              if (res.ok) {
                console.log("_fetchForADs   ok");
                that.timer_AD && clearTimeout(that.timer_AD);
                res.json().then(function(data) {
                    console.log("apiClient  1", data);
                    // that._initDatas(imgs);
                    that._initDatas(data);
                });
              } else {
                that.timer_AD = setTimeout(() => { that._fetchForADs();},10000);
                console.log("Looks like the response wasn't perfect, got status", res.status);
              }
            }, function(e) {
              that.timer_AD = setTimeout(() => { that._fetchForADs();},10000);
              console.log("Fetch failed!", e);
            });
      }

    constructor(props) {
        super(props);
        this.state = {

          //初始中间view
          animW: new Animated.Value(midWidth), // inits to zero
          animH: new Animated.Value(midHeight),
          animT: new Animated.Value(0),
          animL: new Animated.Value(midPosition),
          //初始左边view
          anim1W: new Animated.Value(sidewidth), // inits to zero
          anim1H: new Animated.Value(sideHeight),
          anim1T: new Animated.Value(sidTop),
          anim1L: new Animated.Value(leftSidePosition),
          //初始右边view
          anim2W: new Animated.Value(sidewidth), // inits to zero
          anim2H: new Animated.Value(sideHeight),
          anim2T: new Animated.Value(sidTop),
          anim2L: new Animated.Value(rightSidePosition),

          zIndex0:2,
          zIndex1:1,
          zIndex2:1,
          zIndexBar:3,

          view0URL:'banner_3x',
          view1URL:'banner_3x',
          view2URL:'banner_3x',

        };
    }

//  更新数据
    _initDatas(s_datas){
            console.log("apiClient _initDatas", s_datas);
            if (s_datas) {
              if (s_datas.length>0) {
                imgs = s_datas;
                maxDatasSize = s_datas.length;
              }
            }

            if (maxDatasSize>=3) {
              currentMidDataIndex = 1;
              this.setState({
                view1URL:imgs[0].slide,
                view0URL:imgs[1].slide,
                view2URL:imgs[2].slide,
              });
              this._autoPlay();
            }else if (maxDatasSize>1) {
              currentMidDataIndex = 1;
              this.setState({
                view1URL:imgs[0].slide,
                view0URL:imgs[1].slide,
                view2URL:imgs[0].slide,
              });
              this._autoPlay();
            }else if (maxDatasSize = 1) {
              //只有一个元素
              currentMidDataIndex = 0;
            }else {
              //没有元素
            }
    }

    componentWillMount(){
      that = this;
      this._initResponders();
      this._initRequestData();
    }

    componentDidMount(){
    }

     _autoPlay(){

       this.timer = setInterval(() => {
            if (isAutoplayActiving) {
              if (currentCenterView === 0) {
                this._onPress2View();
              }else if (currentCenterView ===1 ) {
                this._onPress0View();
              }else if (currentCenterView === 2) {
                this._onPress1View();
              }
            }
       },delayForAutoPlay);
     }

    _press2Mid(w,h,t,l){
      Animated.parallel([
          Animated.timing(w, {
            toValue: midWidth,
            duration: delayForAnim,  // return to start
          }),
          Animated.timing(h, {   // and twirl
            toValue: midHeight,
            duration: delayForAnim,
          }),
          Animated.timing(t, {   // and twirl
            toValue: 0,
            duration: delayForAnim,
          }),
          Animated.timing(l, {   // and twirl
            toValue: midPosition,
            duration: delayForAnim,
          }),
        ]).start(function(){
          isCantMove = false;
          this.timer1 = setTimeout(() => {isAutoplayActiving = true;},(delayForAutoPlay - delayForAnim));
        }.bind(this));
    }

    _press2Right(w,h,t,l){
      Animated.parallel([
          Animated.timing(w, {
            toValue: sidewidth,
            duration: delayForAnim,  // return to start
          }),
          Animated.timing(h, {   // and twirl
            toValue: sideHeight,
            duration: delayForAnim,
          }),
          Animated.timing(t, {   // and twirl
            toValue: sidTop,
            duration: delayForAnim,
          }),
          Animated.timing(l, {   // and twirl
            toValue: rightSidePosition,
            duration: delayForAnim,
          }),
        ]).start(function(){
          isCantMove = false;
          this.timer1 = setTimeout(() => {isAutoplayActiving = true;},(delayForAutoPlay - delayForAnim));
        }.bind(this));
    }

    _press2Left(w,h,t,l){
      Animated.parallel([
          Animated.timing(w, {
            toValue: sidewidth,
            duration: delayForAnim,  // return to start
          }),
          Animated.timing(h, {   // and twirl
            toValue: sideHeight,
            duration: delayForAnim,
          }),
          Animated.timing(t, {   // and twirl
            toValue: sidTop,
            duration: delayForAnim,
          }),
          Animated.timing(l, {   // and twirl
            toValue: leftSidePosition,
            duration: delayForAnim,
          }),
        ]).start(function(){
          isCantMove = false;
          this.timer1 = setTimeout(() => {isAutoplayActiving = true;},(delayForAutoPlay - delayForAnim));
        }.bind(this));
    }

    _onPress0View(){
      console.log('swiper_rn',tempView0Left);
      if (isCantMove) {
        return;
      }else {
        isCantMove = true;
      }
      currentCenterView = 0;

      if (tempView0Left === leftSidePosition) {
        console.log('swiper_rn',"view0 在左边");
        this.setState({
          zIndex0:3,
          zIndex1:1,
          zIndex2:2,
          zIndexBar:4,
        });
        currentMidDataIndex--;
        let leftIndex = 0;
        let rightIndex = 0;
        if (currentMidDataIndex<0) {
          currentMidDataIndex = maxDatasSize-1;
        }
        leftIndex = currentMidDataIndex-1;
        if (leftIndex <0) {
          leftIndex = maxDatasSize-1;
        }
        rightIndex = currentMidDataIndex+1;
        if (rightIndex > maxDatasSize-1) {
          rightIndex = 0;
        }
        this.setState({
          view0URL:imgs[currentMidDataIndex].slide,
          view1URL:imgs[leftIndex].slide,
          view2URL:imgs[rightIndex].slide,
        });
        this._ScrollViews2Right();
      }else if (tempView0Left === midPosition) {
        console.log('swiper_rn',"view0 在中间");
        this._HandleAllClickEventCallBack();
        isCantMove = false;
        this.timer1 = setTimeout(() => {isAutoplayActiving = true;},(delayForAutoPlay - delayForAnim));
      }else if (tempView0Left === rightSidePosition ) {
        console.log('swiper_rn',"view0 在右边");
        this.setState({
          zIndex0:3,
          zIndex1:2,
          zIndex2:1,
          zIndexBar:4,
        });
        currentMidDataIndex++;
        let leftIndex = 0;
        let rightIndex = 0;
        if (currentMidDataIndex>maxDatasSize-1) {
          currentMidDataIndex = 0;
        }
        leftIndex = currentMidDataIndex-1;
        if (leftIndex <0) {
          leftIndex = maxDatasSize-1;
        }
        rightIndex = currentMidDataIndex+1;
        if (rightIndex > maxDatasSize-1) {
          rightIndex = 0;
        }
        this.setState({
          view0URL:imgs[currentMidDataIndex].slide,
          view1URL:imgs[leftIndex].slide,
          view2URL:imgs[rightIndex].slide,
        });
        this._ScrollViews2Left();
      }
    }

    _onPress1View(){
      console.log('swiper_rn',tempView1Left);
      if (isCantMove) {
        return;
      }else {
        isCantMove = true;
      }
      currentCenterView = 1;

      if (tempView1Left === leftSidePosition) {
        console.log('swiper_rn',"view1 在左边");
        this.setState({
          zIndex0:2,
          zIndex1:3,
          zIndex2:1,
          zIndexBar:4,
        });
        currentMidDataIndex--;
        let leftIndex = 0;
        let rightIndex = 0;
        if (currentMidDataIndex<0) {
          currentMidDataIndex = maxDatasSize-1;
        }
        leftIndex = currentMidDataIndex-1;
        if (leftIndex <0) {
          leftIndex = maxDatasSize-1;
        }
        rightIndex = currentMidDataIndex+1;
        if (rightIndex > maxDatasSize-1) {
          rightIndex = 0;
        }
        this.setState({
          view1URL:imgs[currentMidDataIndex].slide,
          view2URL:imgs[leftIndex].slide,
          view0URL:imgs[rightIndex].slide,
        });
        this._ScrollViews2Right();
      }else if (tempView1Left === midPosition) {
        console.log('swiper_rn',"view1 在中间");
        this._HandleAllClickEventCallBack();
        isCantMove = false;
        this.timer1 = setTimeout(() => {isAutoplayActiving = true;},(delayForAutoPlay - delayForAnim));
      }else if (tempView1Left === rightSidePosition ) {
        console.log('swiper_rn',"view1 在右边");
        this.setState({
          zIndex0:1,
          zIndex1:3,
          zIndex2:2,
          zIndexBar:4,
        });
        currentMidDataIndex++;
        let leftIndex = 0;
        let rightIndex = 0;
        if (currentMidDataIndex>maxDatasSize-1) {
          currentMidDataIndex = 0;
        }
        leftIndex = currentMidDataIndex-1;
        if (leftIndex <0) {
          leftIndex = maxDatasSize-1;
        }
        rightIndex = currentMidDataIndex+1;
        if (rightIndex > maxDatasSize-1) {
          rightIndex = 0;
        }
        this.setState({
          view1URL:imgs[currentMidDataIndex].slide,
          view2URL:imgs[leftIndex].slide,
          view0URL:imgs[rightIndex].slide,
        });
        this._ScrollViews2Left();
      }
    }

    _onPress2View(){
      console.log('swiper_rn',tempView2Left);

      if (isCantMove) {
        return;
      }else {
        isCantMove = true;
      }
      currentCenterView = 2;


      if (tempView2Left === leftSidePosition) {
        console.log('swiper_rn',"view2 在左边");
        this.setState({
          zIndex0:1,
          zIndex1:2,
          zIndex2:3,
          zIndexBar:4,
        });
        currentMidDataIndex--;
        let leftIndex = 0;
        let rightIndex = 0;
        if (currentMidDataIndex<0) {
          currentMidDataIndex = maxDatasSize-1;
        }
        leftIndex = currentMidDataIndex-1;
        if (leftIndex <0) {
          leftIndex = maxDatasSize-1;
        }
        rightIndex = currentMidDataIndex+1;
        if (rightIndex > maxDatasSize-1) {
          rightIndex = 0;
        }
        this.setState({
          view2URL:imgs[currentMidDataIndex].slide,
          view0URL:imgs[leftIndex].slide,
          view1URL:imgs[rightIndex].slide,
        });
        this._ScrollViews2Right();
      }else if (tempView2Left === midPosition) {
        console.log('swiper_rn',"view2 在中间");
        this._HandleAllClickEventCallBack();
        isCantMove = false;
        this.timer1 = setTimeout(() => {isAutoplayActiving = true;},(delayForAutoPlay - delayForAnim));
      }else if (tempView2Left === rightSidePosition ) {
        console.log('swiper_rn',"view2 在右边");
        this.setState({
          zIndex0:2,
          zIndex1:1,
          zIndex2:3,
          zIndexBar:4,
        });
        currentMidDataIndex++;
        let leftIndex = 0;
        let rightIndex = 0;
        if (currentMidDataIndex>maxDatasSize-1) {
          currentMidDataIndex = 0;
        }
        leftIndex = currentMidDataIndex-1;
        if (leftIndex <0) {
          leftIndex = maxDatasSize-1;
        }
        rightIndex = currentMidDataIndex+1;
        if (rightIndex > maxDatasSize-1) {
          rightIndex = 0;
        }
        this.setState({
          view2URL:imgs[currentMidDataIndex].slide,
          view0URL:imgs[leftIndex].slide,
          view1URL:imgs[rightIndex].slide,
        });
        this._ScrollViews2Left();
      }
    }

//滚动向右
    _ScrollViews2Right(){
      console.log('_ScrollViews2Right...');
//view0
      if (tempView0Left === leftSidePosition) {
        console.log('swiper_rn',"view0 在左边");
        this._press2Mid(this.state.animW,this.state.animH,this.state.animT,this.state.animL);
        tempView0Left = midPosition;
      }else if (tempView0Left === midPosition) {
        console.log('swiper_rn',"view0 在中间");
        this._press2Right(this.state.animW,this.state.animH,this.state.animT,this.state.animL);
        tempView0Left = rightSidePosition;
      }else if (tempView0Left === rightSidePosition ) {
        console.log('swiper_rn',"view0 在右边");
        this._press2Left(this.state.animW,this.state.animH,this.state.animT,this.state.animL);
        tempView0Left = leftSidePosition;
      }
//view1
      if (tempView1Left === leftSidePosition) {
        console.log('swiper_rn',"view1 在左边");
        this._press2Mid(this.state.anim1W,this.state.anim1H,this.state.anim1T,this.state.anim1L);
        tempView1Left = midPosition;
      }else if (tempView1Left === midPosition) {
        console.log('swiper_rn',"view1 在中间");
        this._press2Right(this.state.anim1W,this.state.anim1H,this.state.anim1T,this.state.anim1L);
        tempView1Left = rightSidePosition;
      }else if (tempView1Left === rightSidePosition ) {
        console.log('swiper_rn',"view1 在右边");
        this._press2Left(this.state.anim1W,this.state.anim1H,this.state.anim1T,this.state.anim1L);
        tempView1Left = leftSidePosition;
      }
//view2
      if (tempView2Left === leftSidePosition) {
        console.log('swiper_rn',"view2 在左边");
        this._press2Mid(this.state.anim2W,this.state.anim2H,this.state.anim2T,this.state.anim2L);
        tempView2Left = midPosition;
      }else if (tempView2Left === midPosition) {
        console.log('swiper_rn',"view2 在中间");
        this._press2Right(this.state.anim2W,this.state.anim2H,this.state.anim2T,this.state.anim2L);
        tempView2Left = rightSidePosition;
      }else if (tempView2Left === rightSidePosition ) {
        console.log('swiper_rn',"view2 在右边");
        this._press2Left(this.state.anim2W,this.state.anim2H,this.state.anim2T,this.state.anim2L);
        tempView2Left = leftSidePosition;
      }
    }


    //滚动向左
        _ScrollViews2Left(){
          console.log('_ScrollViews2Left...');
    //view0
          if (tempView0Left === leftSidePosition) {
            console.log('swiper_rn',"view0 在左边");
            this._press2Right(this.state.animW,this.state.animH,this.state.animT,this.state.animL);
            tempView0Left = rightSidePosition;
          }else if (tempView0Left === midPosition) {
            console.log('swiper_rn',"view0 在中间");
            this._press2Left(this.state.animW,this.state.animH,this.state.animT,this.state.animL);
            tempView0Left = leftSidePosition;
          }else if (tempView0Left === rightSidePosition ) {
            console.log('swiper_rn',"view0 在右边");
            this._press2Mid(this.state.animW,this.state.animH,this.state.animT,this.state.animL);
            tempView0Left = midPosition;
          }
    //view1
          if (tempView1Left === leftSidePosition) {
            console.log('swiper_rn',"view1 在左边");
            this._press2Right(this.state.anim1W,this.state.anim1H,this.state.anim1T,this.state.anim1L);
            tempView1Left = rightSidePosition;
          }else if (tempView1Left === midPosition) {
            console.log('swiper_rn',"view1 在中间");
            this._press2Left(this.state.anim1W,this.state.anim1H,this.state.anim1T,this.state.anim1L);
            tempView1Left = leftSidePosition;
          }else if (tempView1Left === rightSidePosition ) {
            console.log('swiper_rn',"view1 在右边");
            this._press2Mid(this.state.anim1W,this.state.anim1H,this.state.anim1T,this.state.anim1L);
            tempView1Left = midPosition;
          }
    //view2
          if (tempView2Left === leftSidePosition) {
            console.log('swiper_rn',"view2 在左边");
            this._press2Right(this.state.anim2W,this.state.anim2H,this.state.anim2T,this.state.anim2L);
            tempView2Left = rightSidePosition;
          }else if (tempView2Left === midPosition) {
            console.log('swiper_rn',"view2 在中间");
            this._press2Left(this.state.anim2W,this.state.anim2H,this.state.anim2T,this.state.anim2L);
            tempView2Left = leftSidePosition;
          }else if (tempView2Left === rightSidePosition ) {
            console.log('swiper_rn',"view2 在右边");
            this._press2Mid(this.state.anim2W,this.state.anim2H,this.state.anim2T,this.state.anim2L);
            tempView2Left = midPosition;
          }
        }




        _initResponders(){
          //view0   手势
          console.log('_initResponders...');
          this.gestureResponderV0 = createResponder({
          onStartShouldSetResponder: (evt, gestureState) => true,
          onStartShouldSetResponderCapture: (evt, gestureState) => true,
          onMoveShouldSetResponder: (evt, gestureState) => true,
          onMoveShouldSetResponderCapture: (evt, gestureState) => true,
          onResponderGrant: (evt, gestureState) => {
            isAutoplayActiving = false;
            currentDownX = gestureState.x0;
          },
          onResponderMove: (evt, gestureState) => {},
          onResponderTerminationRequest: (evt, gestureState) => true,
          onResponderRelease: (evt, gestureState) => {

            let i = currentDownX- gestureState.previousMoveX;
            console.log('gestureResponderV0... i:'+i);
            if (gestureState.moveX === 0) {
              console.log('gestureResponderV0... 单机1:');
              this._onPress0View();
              return;
            }

            if (i>moveDDistance) {
              //左滑
              console.log('gestureResponderV0... 左滑:');
              this._GestureViews2Left();
            }else if (i<-moveDDistance) {
              //右滑
              console.log('gestureResponderV0... 右滑:');
              this._GestureViews2LRight();
            }else {

              if(i > -moveLittleAsClick && i < moveLittleAsClick){
                console.log('gestureResponderV0... 单机1:');
                this._onPress0View();
              }else {
                console.log('gestureResponderV0... 不算单机的 移动:');
              }

            }

          },
          onResponderTerminate: (evt, gestureState) => {},
          onResponderSingleTapConfirmed: (evt, gestureState) => {},
          moveThreshold: 8,
          debug: false
          });

          //view1   手势
          console.log('_initResponders...');
          this.gestureResponderV1 = createResponder({
          onStartShouldSetResponder: (evt, gestureState) => true,
          onStartShouldSetResponderCapture: (evt, gestureState) => true,
          onMoveShouldSetResponder: (evt, gestureState) => true,
          onMoveShouldSetResponderCapture: (evt, gestureState) => true,
          onResponderGrant: (evt, gestureState) => {
            isAutoplayActiving = false;
            currentDownX = gestureState.x0;
          },
          onResponderMove: (evt, gestureState) => {},
          onResponderTerminationRequest: (evt, gestureState) => true,
          onResponderRelease: (evt, gestureState) => {

            let i = currentDownX- gestureState.previousMoveX;
            console.log('gestureResponderV1... i:'+i);
            if (gestureState.moveX === 0) {
              console.log('gestureResponderV1... 单机1:');
              this._onPress1View();
              return;
            }

            if (i>moveDDistance) {
              //左滑
              console.log('gestureResponderV1... 左滑:');
              this._GestureViews2Left();
            }else if (i<-moveDDistance) {
              //右滑
              console.log('gestureResponderV1... 右滑:');
              this._GestureViews2LRight();
            }else {

              if(i > -moveLittleAsClick && i < moveLittleAsClick){
                console.log('gestureResponderV1... 单机1:');
                this._onPress1View();
              }else {
                console.log('gestureResponderV1... 不算单机的 移动:');
              }

            }

          },
          onResponderTerminate: (evt, gestureState) => {},
          onResponderSingleTapConfirmed: (evt, gestureState) => {},
          moveThreshold: 9,
          debug: false

          });

          //view2   手势
          console.log('_initResponders...');
          this.gestureResponderV2 = createResponder({
          onStartShouldSetResponder: (evt, gestureState) => true,
          onStartShouldSetResponderCapture: (evt, gestureState) => true,
          onMoveShouldSetResponder: (evt, gestureState) => true,
          onMoveShouldSetResponderCapture: (evt, gestureState) => true,
          onResponderGrant: (evt, gestureState) => {
            isAutoplayActiving = false;
            currentDownX = gestureState.x0;
          },
          onResponderMove: (evt, gestureState) => {},
          onResponderTerminationRequest: (evt, gestureState) => true,
          onResponderRelease: (evt, gestureState) => {

            let i = currentDownX- gestureState.previousMoveX;
            console.log('gestureResponderV2... i:'+i);
            if (gestureState.moveX === 0) {
              console.log('gestureResponderV2... 单机1:');
              this._onPress2View();
              return;
            }

           if (i>moveDDistance) {
              //左滑
              console.log('gestureResponderV2... 左滑:');
              this._GestureViews2Left();
            }else if (i<-moveDDistance) {
              //右滑
              console.log('gestureResponderV2... 右滑:');
              this._GestureViews2LRight();
            }else {
              //误差滑动单机

              if(i > -moveLittleAsClick && i < moveLittleAsClick){
                console.log('gestureResponderV2... 单机1:');
                this._onPress2View();
              }else {
                console.log('gestureResponderV2... 不算单机的 移动:');
              }

            }

          },
          onResponderTerminate: (evt, gestureState) => {},
          onResponderSingleTapConfirmed: (evt, gestureState) => {},
          moveThreshold: 10,
          debug: false
          });
    }

    _GestureViews2Left(){
      console.log('_GestureViews2Left... 左滑:');
      if (currentCenterView === 0) {
        this._onPress2View();
      }else if (currentCenterView ===1 ) {
        this._onPress0View();
      }else if (currentCenterView === 2) {
        this._onPress1View();
      }
    }

    _GestureViews2LRight(){
      console.log('_GestureViews2LRight... 右滑:');
      if (currentCenterView === 0) {
        this._onPress1View();
      }else if (currentCenterView ===1 ) {
        this._onPress2View();
      }else if (currentCenterView === 2) {
        this._onPress0View();
      }
    }

    _HandleAllClickEventCallBack(){
      // ToastAndroid.show(imgs[currentMidDataIndex].url, ToastAndroid.SHORT);
      if (this.props.onPressEvent instanceof Function) {
          this.props.onPressEvent(imgs[currentMidDataIndex]);
      }
    }

    render(){

      var ContentView = maxDatasSize>1?(<View><View style={styles.container}>
                                            <Animated.View    {...this.gestureResponderV1}
                                                              style={[styles.viewLeft,{
                                                                    zIndex:this.state.zIndex1,
                                                                    width:this.state.anim1W,
                                                                    height:this.state.anim1H,
                                                                    top:this.state.anim1T,
                                                                    left:this.state.anim1L,
                                                                  }]}
                                                                  >
                                                                  <Animated.Image style={[styles.sideImg,
                                                                      {width:this.state.anim1W,
                                                                       height:this.state.anim1H,
                                                                     }]} source={{uri: this.state.view1URL}} defaultSource={{uri: 'banner_3x'}} />
                                            </Animated.View>
                                            <Animated.View    {...this.gestureResponderV2}
                                                              style={[styles.viewRight,{
                                                                    zIndex:this.state.zIndex2,
                                                                    width:this.state.anim2W,
                                                                    height:this.state.anim2H,
                                                                    top:this.state.anim2T,
                                                                    left:this.state.anim2L,
                                                                  }]}
                                                                  >
                                                                  <Animated.Image style={[styles.sideImg,
                                                                      {width:this.state.anim2W,
                                                                       height:this.state.anim2H,
                                                                     }]} source={{uri: this.state.view2URL}} defaultSource={{uri:'banner_3x'}}/>
                                            </Animated.View>
                                            <Animated.View    {...this.gestureResponderV0}
                                                              style={[styles.viewMId,{
                                                                    zIndex:this.state.zIndex0,
                                                                    width:this.state.animW,
                                                                    height:this.state.animH,
                                                                    top:this.state.animT,
                                                                    left:this.state.animL,
                                                                  }]}
                                                                  >
                                                                  <Animated.Image style={[styles.midImg,
                                                                      {width:this.state.animW,
                                                                       height:this.state.animH,
                                                                     }]} source={{uri: this.state.view0URL}} defaultSource={{uri:'banner_3x'}}/>
                                            </Animated.View>
                                        </View>
                                        <Animated.View style={{
                                                        position:'absolute',
                                                        flexDirection:'row',
                                                        width:screenWidth,
                                                        paddingLeft:12,
                                                        height:20,
                                                        top:midHeight-24,
                                                        left:0,
                                                        zIndex:this.state.zIndexBar,
                                                        backgroundColor:'#00000000',
                                                      }}>
                                             {imgs.map((item) => (
                                               item.slide === imgs[currentMidDataIndex].slide?
                                               <View key={item.slide} style={[styles.tagStyle,{backgroundColor:'#C72229'}]}></View>:
                                               <View key={item.slide} style={styles.tagStyle}></View>
                                             ))}
                                         </Animated.View></View>):(<TouchableWithoutFeedback onPress={this._HandleAllClickEventCallBack.bind(this)}>
                                           <View style={[styles.viewMId,
                                                              {
                                                                width:screenWidth,
                                                                height:singleViewHeight,
                                                                top:0,
                                                                left:0,
                                                              }]}
                                                              >
                                                  <Image style={[styles.midImg,
                                                                {width:screenWidth,
                                                                 height:singleViewHeight,
                                                               }]} source={{uri: imgs[0].slide}} defaultSource={{uri:'banner_3x'}}/>
                                                           </View></TouchableWithoutFeedback>);

      return(
        <View style={styles.container}>
            {ContentView}
        </View>
      )
    }
}

const styles = StyleSheet.create({
  container:{
    backgroundColor:'#ffffff7e',
    width:screenWidth,
    height:midHeight,
    alignItems:'center',
  },
  viewLeft:{
    position:'absolute',
    top:(midHeight-sideHeight)/2,
    left:leftSidePosition,
    backgroundColor:'#00000000',
    width:sidewidth,
    height:sideHeight,
    // elevation:4,
    // borderColor:'#00000000',
    // borderWidth:0.1,
    // overflow:'visible',
  },
  viewMId:{
    position:'absolute',
    left:midPosition,
    backgroundColor:'#00000000',
    width:midWidth,
    height:midHeight,
    // elevation:4,
    // borderColor:'#00000000',
    // borderWidth:0.1,
    // overflow:'visible',
  },
  viewRight:{
    position:'absolute',
    top:(midHeight-sideHeight)/2,
    left:rightSidePosition,
    backgroundColor:'#00000000',
    width:sidewidth,
    height:sideHeight,
    // elevation:4,
    // borderColor:'#00000000',
    // borderWidth:0.1,
    // overflow:'visible',
  },
  sideImg:{
    width:sidewidth,
    height:sideHeight,
    resizeMode:'cover',
  },
  midImg:{
    width:midWidth,
    height:midHeight,
    resizeMode:'cover',
  },
  tagStyle:{
    backgroundColor: '#fff',
    borderRadius: 90,
    marginTop:8,
    marginBottom:8,
    marginLeft:4,
    marginRight:4,
    padding:4,
    alignItems:'center'
  }
});

module.exports = ShopSwiper;
