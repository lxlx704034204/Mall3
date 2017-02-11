'use strict';
var React = require('react-native');
import Text from '../components/ShopText';
var {
  	View,
  	Image,
    TouchableWithoutFeedback,
  	TouchableHighlight,
    TouchableOpacity,
    Component,
    PropTypes,
    StyleSheet,
    Platform,
    Dimensions,
    Animated,
    PanResponder
} = React;

var Config = require('../Config');
var Switcher = require('../natives/Switcher');
var isClickable = true;
var screenHeight = Dimensions.get('window').height;
var screenWidth = Dimensions.get('window').width;
var imgView = require('../ImgPage');

var sideHeight = screenWidth*0.36;
var sidewidth = screenWidth-160;
var midHeight = screenWidth*0.410746;
var midWidth = screenWidth-80;
var sidTop = (midHeight-sideHeight)/2;

var midPosition = 40;
var leftSidePosition = -(sidewidth-midPosition);
var rightSidePosition = screenWidth-midPosition;

var tempView0Left = midPosition;
var tempView1Left = leftSidePosition;
var tempView2Left = rightSidePosition;

var currentMidDataIndex=1;

var imgs = [
  'http://r3.ykimg.com//051000005770910567BC3D38C904F14D',
  'http://r4.ykimg.com//05100000578A538A67BC3D29FE00579B',
  'http://r1.ykimg.com//0510000057ABC23C67BC3D7A5003A7C8',
  'http://r1.ykimg.com//051000005762150067BC3D2A6E0154D2',
  'http://r2.ykimg.com//05100000577E2D7C67BC3D6C0B02B6CC'];


export default class ShopSwiper extends Component{

    constructor(props) {
        super(props);
        this.state = {
          //初始中间view
          animW: new Animated.Value(midWidth), // inits to zero
          animH: new Animated.Value(midHeight),
          animT: new Animated.Value(0),
          animL: new Animated.Value(40),
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

        };
    }

    componentWillMount(){

    }

    _press2Mid(w,h,t,l){
      Animated.parallel([
          Animated.timing(w, {
            toValue: midWidth,
            duration: 600,  // return to start
          }),
          Animated.timing(h, {   // and twirl
            toValue: midHeight,
            duration: 600,
          }),
          Animated.timing(t, {   // and twirl
            toValue: 0,
            duration: 600,
          }),
          Animated.timing(l, {   // and twirl
            toValue: midPosition,
            duration: 600,
          }),
        ]).start();
    }

    _press2Right(w,h,t,l){
      Animated.parallel([
          Animated.timing(w, {
            toValue: sidewidth,
            duration: 600,  // return to start
          }),
          Animated.timing(h, {   // and twirl
            toValue: sideHeight,
            duration: 600,
          }),
          Animated.timing(t, {   // and twirl
            toValue: sidTop,
            duration: 600,
          }),
          Animated.timing(l, {   // and twirl
            toValue: rightSidePosition,
            duration: 600,
          }),
        ]).start();
    }

    _press2Left(w,h,t,l){
      Animated.parallel([
          Animated.timing(w, {
            toValue: sidewidth,
            duration: 600,  // return to start
          }),
          Animated.timing(h, {   // and twirl
            toValue: sideHeight,
            duration: 600,
          }),
          Animated.timing(t, {   // and twirl
            toValue: sidTop,
            duration: 600,
          }),
          Animated.timing(l, {   // and twirl
            toValue: leftSidePosition,
            duration: 600,
          }),
        ]).start();
    }

    _onPress0View(){
      console.log('swiper_rn',tempView0Left);
      if (tempView0Left === leftSidePosition) {
        console.log('swiper_rn',"view0 在左边");
        this.setState({
          zIndex0:2,
          zIndex1:1,
          zIndex2:1,
        });
        this._ScrollViews2Right();
      }else if (tempView0Left === midPosition) {
        console.log('swiper_rn',"view0 在中间");
      }else if (tempView0Left === rightSidePosition ) {
        console.log('swiper_rn',"view0 在右边");
      }
    }

    _onPress1View(){
      console.log('swiper_rn',tempView1Left);
      if (tempView1Left === leftSidePosition) {
        console.log('swiper_rn',"view1 在左边");
        this.setState({
          zIndex0:1,
          zIndex1:2,
          zIndex2:1,
        });
        this._ScrollViews2Right();
      }else if (tempView1Left === midPosition) {
        console.log('swiper_rn',"view1 在中间");
      }else if (tempView1Left === rightSidePosition ) {
        console.log('swiper_rn',"view1 在右边");
      }
    }

    _onPress2View(){
      console.log('swiper_rn',tempView2Left);
      if (tempView2Left === leftSidePosition) {
        console.log('swiper_rn',"view2 在左边");
        this.setState({
          zIndex0:1,
          zIndex1:1,
          zIndex2:2,
        });
        this._ScrollViews2Right();
      }else if (tempView2Left === midPosition) {
        console.log('swiper_rn',"view2 在中间");
      }else if (tempView2Left === rightSidePosition ) {
        console.log('swiper_rn',"view2 在右边");
      }
    }

//滚动向右
    _ScrollViews2Right(){
//view0
      if (tempView0Left === leftSidePosition) {
        console.log('swiper_rn',"view0 在左边");
        this._press2Mid(this.state.animW,this.state.animH,this.state.animT,this.state.animL);
        tempView0Left = midPosition;
      }else if (tempView0Left === midPosition) {
        console.log('swiper_rn',"view1 在中间");
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




    render(){
      return(
        <View style={styles.container}>

          <TouchableWithoutFeedback onPress={this._onPress1View.bind(this)}>
            <Animated.View style={[styles.viewLeft,{
                                    zIndex:this.state.zIndex1,
                                    width:this.state.anim1W,
                                    height:this.state.anim1H,
                                    top:this.state.anim1T,
                                    left:this.state.anim1L
                                  }]}>
              <Animated.Image style={[styles.sideImg,{
                                    width:this.state.anim1W,
                                    height:this.state.anim1H
                                  }]} source={{uri: imgs[0]}} defaultSource={{uri: 'banner_3x'}} />
            </Animated.View>
          </TouchableWithoutFeedback>

          <TouchableWithoutFeedback onPress={this._onPress2View.bind(this)}>
            <Animated.View style={[styles.viewRight,{
                                    zIndex:this.state.zIndex2,
                                    width:this.state.anim2W,
                                    height:this.state.anim2H,
                                    top:this.state.anim2T,
                                    left:this.state.anim2L
                                  }]}>
              <Animated.Image style={[styles.sideImg,{
                                    width:this.state.anim2W,
                                    height:this.state.anim2H
                                  }]} source={{uri: imgs[1]}} defaultSource={{uri:'banner_3x'}}/>
            </Animated.View>
          </TouchableWithoutFeedback>

          <TouchableWithoutFeedback onPress={this._onPress0View.bind(this)}>
            <Animated.View ref='view0' style={[styles.viewMId,{
                                    zIndex:this.state.zIndex0,
                                    width:this.state.animW,
                                    height:this.state.animH,
                                    top:this.state.animT,
                                    left:this.state.animL
                                  }]}>
              <Animated.Image style={[styles.sideImg,{
                                    width:this.state.animW,
                                    height:this.state.animH
                                  }]} source={{uri: imgs[2]}} defaultSource={{uri:'banner_3x'}}/>
            </Animated.View>
          </TouchableWithoutFeedback>
        </View>
      )
    }
}

const styles = StyleSheet.create({
  container:{
    backgroundColor:'#fff',
    width:screenWidth,
    height:screenWidth*0.410746+4,
    paddingLeft:12,
    paddingRight:12,
    alignItems:'center',
    borderColor:'#fff',
    borderBottomWidth:1
  },
  viewLeft:{
    position:'absolute',
    top:(midHeight-sideHeight)/2,
    left:leftSidePosition,
    backgroundColor:'#303f9f',
    width:sidewidth,
    height:sideHeight,
    elevation:4,
  },
  viewMId:{
    position:'absolute',
    left:40,
    backgroundColor:'#FFC107',
    width:midWidth,
    height:midHeight,
    elevation:4,
  },
  viewRight:{
    position:'absolute',
    top:(midHeight-sideHeight)/2,
    left:rightSidePosition,
    backgroundColor:'#607d8b',
    width:sidewidth,
    height:sideHeight,
    elevation:4,
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
  }
});

module.exports = ShopSwiper;
