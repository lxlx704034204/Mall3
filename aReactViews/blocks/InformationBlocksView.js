'use strict';
//汽车资讯   板块
import React,{  PropTypes,Component}from'react';
// import Text from '../components/ShopText';
import {
  	Text,
  	View,
  	Image,
  	TouchableHighlight,
    TouchableOpacity,
    ScrollView,
    StyleSheet,
    Platform,
    Dimensions,
    ToastAndroid
} from 'react-native';
var BlockStyles = require('../styles/BlockStyles');
var Config = require('../Config');
var Switcher = require('../natives/Switcher');
var Security = require('../natives/SecurityModule');
var that;
var isClickable = true;

var screenHeight = Dimensions.get('window').height;
var screenWidth = Dimensions.get('window').width;

var buttonCViewH = screenWidth*0.12179487;
var buttonW = screenWidth*0.20192308;//按钮宽度
var buttonH = buttonW*0.41904762;//按钮高度
var itemH = screenWidth*0.21929825;//列表单个条目高度
var imageH = itemH*0.8;//图片高度
var imageW = imageH*1.5875;//图片宽度

var titleH = screenWidth*0.11;

//请求参数------------------------------------------------
var myHeaders = new Headers();
myHeaders.append('User-Agent','Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0 HXMall Android');
var myInit = { method: 'GET',
               headers: myHeaders,
               mode: 'cors',
               cache: 'default' };
var myRequest_Btns;//上部按钮
var myRequest_List;//推荐列表
//请求参数------------------------------end----------------


var testData = [
  {
    slide:'http://r2.ykimg.com//0510000057CE841467BC3D440606E723',
    type:'1',
    title:'一往无前 全新PANAMERA',
    summary:'脱胎换骨的新一代，在这期网络杂志中，您将听到非同寻常的故事勇气'
  },
  {
    slide:'http://r3.ykimg.com//051000005770910567BC3D38C904F14D',
    type:'1',
    title:'纵 “擎” 发现新境',
    summary:'这个金秋，就让保时捷服务为您的悠然假期保驾护航毋庸置疑保时捷中心将作为您的旅行首站。'
  },
  {
    slide:'http://r4.ykimg.com//05100000577BAA1767BC3D3A1706E485',
    type:'1',
    title:'坐拥保时捷，更待何时',
    summary:'全新718车型有何与众不同？车辆性能、操控、设计、双行李箱带来的宽敞空间'
  },
  {
    slide:'http://r1.ykimg.com//051000005762150067BC3D2A6E0154D2',
    type:'1',
    title:'保时捷高尔夫巡回赛',
    summary:'是否还记得体验完美的感觉？当你所需的食物一应俱全，您就能拥有这种感觉'
  }
];

var testBtnData = [
  {
    guideName:'新车上市',
    guideCode:'1',
    dataStr:'',
  },
  {
    guideName:'导购对比',
    guideCode:'2',
    dataStr:'',
  },
  {
    guideName:'新车到店',
    guideCode:'3',
    dataStr:'',
  },
  {
    guideName:'优惠汇总',
    guideCode:'4',
    dataStr:'',
  },
  {
    guideName:'测试',
    guideCode:'5',
    dataStr:'',
  },
];

var isClickable = true;

class TopButtonView extends Component{

  constructor(props) {
      super(props);
  }

  _press(){
    if (isClickable) {
        isClickable = false;
        this.timer = setTimeout(() => {isClickable = true;},1200);
        var str = JSON.stringify(this.props.btnData)
        this._switch(this.props.buttonText,'zixun_btn',str);
    }
  }

  _pressIn(){
  }

  _pressOut(){
  }

  _switch(title,tag,jsonStr){
    if (Platform.OS === 'ios') {

    }else if (Platform.OS === 'android') {
      Switcher.turn(title,tag,jsonStr);
    }
  }

  render() {
      return(
      <View style={{
          width:screenWidth/4,
          justifyContent:'center',
          alignItems:'center'
        }}>
        <TouchableOpacity
          style={{width:screenWidth/4,alignItems:'center'}}
          onPress={this._press.bind(this)}
          onPressIn={this._pressIn.bind(this)}
          onPressOut={this._pressOut.bind(this)}
          >
            <View style={styles.buttonStyle}>
              <Text style={styles.buttonText}>
                {this.props.buttonText}
              </Text>
            </View>
        </TouchableOpacity>

      </View>

      );
  };
};

class ListItemView extends Component{

  constructor(props) {
      super(props);
  }

  _press(){
    if (isClickable) {
        isClickable = false;
        this.timer = setTimeout(() => {isClickable = true;},1200);
        var str = JSON.stringify(this.props.itemD)
        this._switch(this.props.itemD.title,'zixun_list_item',str);
    }
  }

  _pressIn(){
  }

  _pressOut(){
  }

  _switch(title,tag,jsonStr){
    if (Platform.OS === 'ios') {

    }else if (Platform.OS === 'android') {
      Switcher.turn(title,tag,jsonStr);
    }
  }

  render() {
      return(

        <TouchableOpacity
          style={[this.props.cOne]}
          onPress={this._press.bind(this)}
          onPressIn={this._pressIn.bind(this)}
          onPressOut={this._pressOut.bind(this)}
          >

          <View   style={[styles.cOne]}>
            <Image
              source={{uri: this.props.itemD.thumbImage[0]}}
              defaultSource={{uri: Config.pic_d}}
              style={styles.itemImg}
            />
            <View style={styles.textC}>
              <Text style={styles.itemTextM} numberOfLines ={2}>
                {this.props.itemD.title}
              </Text>

              <Text style={styles.itemTextS} numberOfLines ={2}>
                {this.props.itemD.introduction}
              </Text>
            </View>
          </View>

        </TouchableOpacity>

      );
  };
};

export default class InformationBlocksView extends Component{

    constructor(props) {
        super(props);
        this.state = {

          BtnDatas:testBtnData,
          ListDatas:[],
          btnViewH:buttonCViewH,
        };
    }

    componentWillMount(){
      that = this;
      if (Platform.OS === 'ios') {

      }else if (Platform.OS === 'android') {
        that._initRequestData();
      }
    }

    _initRequestData(){
      // http://10.0.15.201:8089/Ver/Index/version?os=Android&deviceType=Android
      let params = "deviceType=Android";
      Security.getEncodeThingsAccount(params,
        (hostURL,requestParams)=>{
          //回调成功
          let requestURL1 =hostURL + Config.homeInfoBtnURL + requestParams;
          let requestURL2 =hostURL + Config.homeInfoListURL + requestParams;
          console.log("apiClient", requestURL1);
          console.log("apiClient", requestURL2);
          myRequest_Btns = new Request(requestURL1,myInit);
          myRequest_List = new Request(requestURL2,myInit);
          // that._fetchForBtns();
          that._fetchForList();

        },(msg)=>{
          //操作失败
          console.log("Fetch failed!", msg);
        });

    }

  //获取按钮
    _fetchForBtns(){
        fetch(myRequest_Btns).then(function(res) {
            if (res.ok) {
              console.log("_fetchForADs   ok");
              that.timer_Btn&& clearTimeout(that.timer_Btn);
              res.json().then(function(data) {
                  console.log("apiClient  _fetchForBtns", data);
                  that.setState({
                    BtnDatas:data,
                    btnViewH:buttonCViewH
                  });
              });
            } else {
              that.timer_Btn = setTimeout(() => { that._fetchForBtns();},10000);
              console.log("Looks like the response wasn't perfect, got status", res.status);
            }
          }, function(e) {
            that.timer_Btn = setTimeout(() => { that._fetchForBtns();},10000);
            console.log("Fetch failed!", e);
          });
    }

    //获取列表
      _fetchForList(){
          fetch(myRequest_List).then(function(res) {
              if (res.ok) {
                console.log("_fetchForADs   ok");
                that.timer_list && clearTimeout(that.timer_list);
                res.json().then(function(data) {
                    console.log("apiClient  _fetchForList", data);

                    let _listDatas = [];
                    if (data.length>4) {
                      _listDatas[0] = data[0];
                      _listDatas[1] = data[1];
                      _listDatas[2] = data[2];
                      _listDatas[3] = data[3];
                    }else {
                      _listDatas = data;
                    }

                    that.setState({
                      ListDatas:_listDatas
                    });
                });
              } else {
                that.timer_list = setTimeout(() => { that._fetchForList();},10000);
                console.log("Looks like the response wasn't perfect, got status", res.status);
              }
            }, function(e) {
              that.timer_list = setTimeout(() => { that._fetchForList();},10000);
              console.log("Fetch failed!", e);
            });
      }

      _pressMore(){
        if (isClickable) {
            isClickable = false;
            this.timer = setTimeout(() => {isClickable = true;},1200);
            this._switch('更多','MAIN_TAB','1');
        }
      }

      _switch(title,tag,jsonStr){
        if (Platform.OS === 'ios') {

        }else if (Platform.OS === 'android') {
          Switcher.turn(title,tag,jsonStr);
        }
      }

    render(){
      return(
        <View style={styles.container}>
          <View style={{
              height:titleH,
              width:screenWidth,
              paddingLeft:12,
              paddingRight:12,
              flexDirection:'row',
              alignItems:'center',
              backgroundColor:Config.itemBackgroundColor
            }}>
            <Text style={styles.titleM}>
                  汽车资讯
            </Text>

            <TouchableOpacity style={{
                flexDirection:'row',
                flex:1,
                height:titleH,
                alignItems:'center',
              }} onPress={this._pressMore.bind(this)}>

              <Text style={styles.titleS}>
                    更多
              </Text>

              <Image
                source={{uri:Config.homeMoreRightArrow}}
                defaultSource={{uri:Config.homeMoreRightArrow}}
                style={{
                  width:7,
                  height:13,
                  resizeMode:'contain'
                }}
              />
            </TouchableOpacity>

          </View>


          <ScrollView
            style={[styles.ScrollViewS,{height:this.state.btnViewH}]}
            horizontal={true}
            showsHorizontalScrollIndicator ={false}
            >
            {this.state.BtnDatas.map((item) => (
              <TopButtonView key={item.guideCode} buttonText={item.guideName} btnData = {item} />
            ))}
          </ScrollView>

          <View style={{
              marginTop:2,
            }}>
            {this.state.ListDatas.map((item) => (
              <ListItemView key={item.infoID} itemD={item}/>
            ))}
          </View>

        </View>
      )
    }
}

const styles = StyleSheet.create({
  container:{
    // backgroundColor:'#000',
    marginBottom:10
  },
  ScrollViewS:{
    width:screenWidth,
    marginTop:2,
    backgroundColor:Config.itemBackgroundColor,
  },
  ButtonContainerS:{
    marginTop:2,
    width:screenWidth,
    flexDirection:'row',
    alignItems:'center',
    paddingTop:6,
    paddingBottom:6,
    backgroundColor:Config.itemBackgroundColor
  },
  titleM:{
    flex:1,
    textAlign:'left',
    fontWeight:'bold',
    fontSize:18,
    color:Config.titleColor
  },
  titleS:{
    flex:1,
    marginRight:4,
    textAlign:'right',
    fontSize:16,
    color:'#999999'
  },
  buttonText:{
    fontSize:15,
    color:'#252525',
    alignSelf:'center',
    paddingTop:6,
    paddingBottom:6
  },
  itemTextM:{
    fontWeight:'bold',
    fontSize:16,
    color:'#2A2A2A'
  },
  itemTextS:{
    // marginLeft:8,
    marginTop:6,
    fontSize:14,
    color:'#6B6B6B',
  },
  buttonStyle:{
    backgroundColor:'#fafafa',
    borderColor:'#ddd',
    borderWidth:0.5,
    borderRadius:3,
    width:buttonW,
    alignItems:'center',
    justifyContent:'center',
  },
  itemImg:{
    alignSelf:'center',
    width: imageW,
    height: imageH,
    resizeMode:'cover',
    marginRight:6,
    // borderWidth:1,
    // borderColor:'#ccc',
    // padding:1,
    backgroundColor:'#d1d1d1'
  },
  cOne:{
    width:screenWidth,
    // height:itemH,
    backgroundColor:Config.itemBackgroundColor,
    flexDirection:'row',
    paddingLeft:10,
    paddingRight:10,
    paddingTop:6,
    paddingBottom:6,
    marginBottom:2,
    alignItems:'center',
  },
  textC:{
    flex:1
  }

});

module.exports = InformationBlocksView;
