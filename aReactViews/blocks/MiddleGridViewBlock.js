'use strict';
import React,{PropTypes,Component}from'react';
// import Text from '../components/ShopText';
import {
  	Text,
  	View,
  	Image,
  	TouchableHighlight,
    TouchableWithoutFeedback,
    TouchableOpacity,
    StyleSheet,
    Platform,
    Dimensions,
    ToastAndroid
} from 'react-native';
var BlockStyles = require('../styles/BlockStyles');
var Config = require('../Config');
var Switcher = require('../natives/Switcher');
var GridView = require('react-native-grid-view');

// var isClickable = true;

var screenHeight = Dimensions.get('window').height;
var screenWidth = Dimensions.get('window').width;

var buttonDatas = [
  {
    imgURL:Config.homeCarInfo,
    btnLabel:'车辆信息',
    tagType:"MY_CARS",
    jsonStr:Config.vipAutoInfoActivity
  },
  {
    imgURL:Config.homeSaleRecord,
    btnLabel:'消费记录',
    tagType:"Consumption_Record",
    jsonStr:''
  },
  {
    imgURL:Config.homeWallet,
    btnLabel:'我的钱包',
    tagType:"MY_WALLET",
    jsonStr:Config.vipMyWalletActivity
  },
  {
    imgURL:Config.homeOnlineServices,
    btnLabel:'在线客服',
    tagType:"Online_Service",
    jsonStr:''
  },
  {
    imgURL:Config.homeVipRights,
    btnLabel:'会员权益',
    tagType:"VIP_RULE",
    jsonStr:''
  },
  {
    imgURL:Config.homeEmergencyRescue,
    btnLabel:'一键救援',
    tagType:"USE_MODULE",
    jsonStr:Config.fixCarEmergencyRescue
  },
  {
    imgURL:Config.homeMaintenance,
    btnLabel:'常规保养',
    tagType:'NORMAL_MAINTENANCE',
    jsonStr:Config.fixCarMaintenance
  },
  {
    imgURL:Config.homeRepaire,
    btnLabel:'修车预约',
    tagType:"USE_MODULE",
    jsonStr:Config.fixCarBookRepair
  }
];

  // {
  //   imgURL:Config.homeeCarInspection,
  //   btnLabel:'车辆年审',
  //   tagType:'ANNUAL_AUDIT',
  //   jsonStr:''
  // }
// var buttonDatas = [
//   {
//     imgURL:Config.homeMaintenance,
//     btnLabel:'常规保养',
//     tagType:'NORMAL_MAINTENANCE',
//     jsonStr:Config.fixCarMaintenance
//   },
//   {
//     imgURL:Config.homeIllegallyPrepare,
//     btnLabel:'违章查缴',
//     tagType:'BREAK_RULES',
//     jsonStr:''
//   },
//   {
//     imgURL:Config.homeEmergencyRescue,
//     btnLabel:'紧急救援',
//     tagType:'USE_MODULE',
//     jsonStr:Config.fixCarEmergencyRescue
//   },
//   {
//     imgURL:Config.homeeCarInspection,
//     btnLabel:'车辆年审',
//     tagType:'ANNUAL_AUDIT',
//     jsonStr:''
//   },
//   {
//     imgURL:Config.homeWashCar,
//     btnLabel:'洗车',
//     tagType:'CAR_WASH',
//     jsonStr:''
//   },
//   {
//     imgURL:Config.homeDrive,
//     btnLabel:'代驾',
//     tagType:'DESIGNATED_DRIVER',
//     jsonStr:''
//   },
//   {
//     imgURL:Config.homePark,
//     btnLabel:'停车场',
//     tagType:'AROUND_MAP',
//     jsonStr:Config.aroundMapPark
//   },
//   {
//     imgURL:Config.homeGasStation,
//     btnLabel:'加油站',
//     tagType:'AROUND_MAP',
//     jsonStr:Config.aroundMapGas
//   },
//
// ]


class ItemC extends Component{

  constructor(props) {
      super(props);
      this.state = {
          textColor:'#434544',
          tDisable:false
      };

  }

  pressImg(){
    // if (isClickable) {
    //       isClickable = false;
    //       this.timer = setTimeout(() => {isClickable = true;},1800);
          Switcher.turn(this.props.resData.btnLabel,this.props.resData.tagType,this.props.resData.jsonStr);
    // }
  }

  render() {
      return(
        <View style={styles.thumbnail}>
          <TouchableWithoutFeedback
            disabled={this.state.tDisable}
            onPress={this.pressImg.bind(this)}
            onPressIn={()=>{
              this.setState({
                  textColor:'#434544',
                  tDisable:true
              });
            }}
            onPressOut={()=>{
              this.setState({
                  textColor:'#434544',
                  tDisable:false
              });
            }}>

            <View>
              <Image
                source={{uri: this.props.resData.imgURL}}
                defaultSource={{uri: Config.imageTaken}}
                style={styles.itemImg}
              />
              <Text style={[styles.itemLabel,{color:this.state.textColor}]}>
                {this.props.resData.btnLabel}
              </Text>
            </View>

          </TouchableWithoutFeedback>

      </View>
    );
  };
};

export default class MiddleGridViewBlock extends Component{

    constructor(props) {
        super(props);
    }

    //图片 renderItem
    renderItem(item, rowData) {
        return <ItemC key={item.imgURL} resData={item} btnData={rowData}/>
    }

    render(){
      return(
        <View style={styles.container}>
          <GridView
                items={buttonDatas}
                itemsPerRow={4}
                renderItem={this.renderItem}
                style={styles.listView}
                enableEmptySections={true}
          />
        </View>
      )
    }
}

const styles = StyleSheet.create({
  container:{
    // margin:18,
    // marginTop:6,
    paddingBottom:8,
    alignItems:'center',
    backgroundColor:Config.itemBackgroundColor,
    // borderColor:'red',
    // borderWidth:1,
  },
  titleS:{
    fontWeight:'bold',
    fontSize:16,
    color:'#434544'
  },
  listView: {
    alignSelf:'flex-start',
    marginLeft:0,
    marginRight:0
  },
  thumbnail: {
    alignItems:'center',
    justifyContent:'center',
    // borderColor:'#fff',
    // borderWidth: 1,
    marginLeft:4,
    marginRight:4,
    marginTop:8,
    width: ((screenWidth-4*2-8*3) / 4),
    height: ((screenWidth-4*2-8*3) / 4)-20,
  },
  itemImg:{
    //borderRadius:50,
    flex:1,
    alignSelf:'center',
    // padding:20,
    width: ((screenWidth - 22*2 - 8*3) / 4)-30,
    height: ((screenWidth - 22*2 - 8*3) / 4)-30,
    resizeMode:'contain',
  },
  itemLabel:{
    marginTop:4,
    alignSelf:'center',
    fontSize:14,
    color:'#434544',
  }

});

module.exports = MiddleGridViewBlock;
