'use strict';
import React,{PropTypes,Component}from'react';
// import Text from '../components/ShopText';
import {
  	Text,
  	View,
  	Image,
  	TouchableHighlight,
    TouchableOpacity,
    StyleSheet,
    Platform,
    Dimensions,
    ToastAndroid
} from 'react-native';
var BlockStyles = require('../styles/BlockStyles');
var Config = require('../Config');
var Switcher = require('../natives/Switcher');
var that;
var screenWidth = Dimensions.get('window').width;
var isClickable = true;
import ThirdBlocksTitleView from './ThirdBlocksTitleView';
import ThirdBlocksItemView from './ThirdBlocksItemView';
import WithImgBlockItemView from './WithImgBlockItemView';

//修车
var repireCarDataL={
  textColor:'#C72229',
  imgURL:'http://r2.ykimg.com/0510000057E31E6867BC3D7272039304',
  itemLabel:'常规保养',
  itemSummary:'MAINTENANCE',
  tagType:"NORMAL_MAINTENANCE",
  jsonStr:Config.fixCarMaintenance
};

var repireCarDataR = [
  {
    imgURL:Config.homeRepaire,
    itemLabel:'修车预约',
    itemSummary:'线上快捷预约',
    tagType:"USE_MODULE",
    jsonStr:Config.fixCarBookRepair,
    isEnd:false,
  },
  {
    imgURL:Config.homeEmergencyRescue,
    itemLabel:'紧急救援',
    tagType:'USE_MODULE',
    jsonStr:Config.fixCarEmergencyRescue,
    itemSummary:'闪电直达',
    isEnd:true,
  }
];

//用车
var useCarL={
  textColor:'#F08F6F',
  imgURL:'http://u0.qiyipic.com/image/20160821/e4/f7/uv_2036603923_m_601_160_90.jpg',
  itemLabel:'违章查缴',
  itemSummary:'ILLEGAL QUERY',
  tagType:'BREAK_RULES',
  jsonStr:''
};

var useCarR=[
    {
      imgURL:Config.homeDriverLicense,
      itemLabel:'驾驶证换证',
      itemSummary:'方便快捷',
      tagType:"CHANGE_CERTIFICATE",
      jsonStr:'',
      isEnd:true,
    }
];

var useCarDatas1 = [
  {
    imgURL:Config.homeIllegallyPrepare,
    itemLabel:'违章查缴',
    itemSummary:'最快最准最优',
    tagType:'BREAK_RULES',
    jsonStr:''
  },
  {
    imgURL:Config.homeDriverLicense,
    itemLabel:'驾驶证换证',
    itemSummary:'不出门享换证',
    tagType:"CHANGE_CERTIFICATE",
    jsonStr:'',
  }
];

var useCarDatas2 = [
    {
      imgURL:Config.homeeCarInspection,
      itemLabel:'车辆年审',
      itemSummary:'年审轻松过',
      tagType:'ANNUAL_AUDIT',
      jsonStr:''
    },
    {
      imgURL:Config.homeRoadCondition,
      itemLabel:'实时路况',
      itemSummary:'哪不堵就走哪',
      tagType:"AROUND_MAP",
      jsonStr:Config.aroundMapTraffic
    },
];

var useCarDatas3 = [
  {
    imgURL:Config.homePark,
    itemLabel:'停车场',
    itemSummary:'快寻最近车位',
    tagType:'AROUND_MAP',
    jsonStr:Config.aroundMapPark
  },
  {
    imgURL:Config.homeDrive,
    itemLabel:'代驾',
    itemSummary:'安全护送到家',
    tagType:'DESIGNATED_DRIVER',
    jsonStr:''
  },
];

// var useCarDatas3 = [
//     {
//       imgURL:Config.homeInsurance,
//       itemLabel:'买保险',
//       itemSummary:'买险任君选',
//       tagType:"USE_MODULE",
//       jsonStr:Config.serviceInsurance
//     },
//     {
//       imgURL:Config.homeKaoJiaZhao,
//       itemLabel:'考驾照',
//       itemSummary:'随时学随时用',
//       tagType:"DRIVER_EXAM",
//       jsonStr:''
//     }
// ];

var useCarDatas4 = [
    {
      imgURL:Config.homeChargingStation,
      itemLabel:'充电站',
      tagType:'AROUND_MAP',
      itemSummary:'找的准充的快',
      jsonStr:Config.aroundMapCharger
    },
    {
      imgURL:Config.homeKaoJiaZhao,
      itemLabel:'考驾照',
      itemSummary:'驾照轻松拿',
      tagType:"DRIVER_EXAM",
      jsonStr:''
    }
];

var useCarDatas5 = [
    {
      imgURL:Config.homeGasStation,
      itemLabel:'加油站',
      tagType:'AROUND_MAP',
      itemSummary:'最优油价帮您找',
      jsonStr:Config.aroundMapGas
    },
];

//买车
// var buyCarL={
//       textColor:'#4FD2C2',
//       imgURL:'http://u0.qiyipic.com/image/20160821/e4/f7/uv_2036603923_m_601_160_90.jpg',
//       itemLabel:'4S店网上商城',
//       itemSummary:'ONLINE SHOPPING',
//       tagType:"NET_4S_SHOP",
//       jsonStr:Config.buyCar4sShop
// };
//
// var buyCarR=[
//     {
//       imgURL:Config.homeNewCarSale,
//       itemLabel:'新车销售',
//       itemSummary:'海量新车任君挑选',
//       tagType:"USE_MODULE",
//       jsonStr:Config.buyCarNewSale,
//       isEnd:true,
//     }
// ];

//卖车
var saleCarL={
  textColor:'#9794FE',
  imgURL:'http://r2.ykimg.com/0510000057E31E6867BC3D7272039304',
  itemLabel:'买二手车',
  itemSummary:'BUY USED CAR',
  tagType:"BUY_USED_CAR",
  jsonStr:Config.usedCarBuy
};

var saleCarR = [
  {
    imgURL:Config.homeSaleCar,
    itemLabel:'卖二手车',
    itemSummary:'免费全方位评估',
    tagType:"SALE_USED_CAR",
    jsonStr:Config.usedCarSale,
    isEnd:false,
  },
  {
    imgURL:Config.homeExchangeCar,
    itemLabel:'二手车置换',
    itemSummary:'7天可退换，杜绝事故车',
    tagType:"USED_CAR_Metathesis",
    jsonStr:'',
    isEnd:true,
  }
];


//买车
var buyCarData = [
  {
    imgURL:Config.home4SMall,
    itemLabel:'4S店网上商城',
    itemSummary:'掌上逛遍汽车城',
    tagType:"NET_4S_SHOP",
    jsonStr:Config.buyCar4sShop
  },
  {
    imgURL:Config.homeNewCarSale,
    itemLabel:'新车销售',
    itemSummary:'万款新车随心选',
    tagType:"USE_MODULE",
    jsonStr:Config.buyCarNewSale
  }
];
//
// var repireCarData = [
//   {
//     imgURL:Config.homeRepaire,
//     itemLabel:'预约维修',
//     itemSummary:'线上快捷预约',
//     tagType:"USE_MODULE",
//     jsonStr:Config.fixCarBookRepair
//   },
//   {
//     imgURL:Config.homeSuppliesSale,
//     itemLabel:'用品销售',
//     itemSummary:'一站式用品大卖场',
//     tagType:"Accessory_Sell",
//     jsonStr:Config.buyCar4sShop
//   }
// ];
//
// //用车
// var useCarData1 = [
//   {
//     imgURL:Config.homeRoadCondition,
//     itemLabel:'实时路况',
//     itemSummary:'随时掌握路况',
//     tagType:"AROUND_MAP",
//     jsonStr:Config.aroundMapTraffic
//   },
//   {
//     imgURL:Config.homeDriverLicense,
//     itemLabel:'驾驶证换证',
//     itemSummary:'方便快捷',
//     tagType:"CHANGE_CERTIFICATE",
//     jsonStr:''
//   }
// ];
//
// var useCarData2 = [
//   {
//     imgURL:Config.homeChargingStation,
//     itemLabel:'充电站',
//     itemSummary:'实时充电站位置',
//     tagType:"AROUND_MAP",
//     jsonStr:Config.aroundMapCharger
//   },
//   {
//     imgURL:Config.homeInsurance,
//     itemLabel:'买保险',
//     itemSummary:'买险任君选',
//     tagType:"USE_MODULE",
//     jsonStr:Config.serviceInsurance
//   }
// ];
//
// var useCarData3 = [
//   {
//     imgURL:Config.homeKaoJiaZhao,
//     itemLabel:'考驾照',
//     itemSummary:'随时学随时用',
//     tagType:"DRIVER_EXAM",
//     jsonStr:''
//   }
// ];
//
// //卖车
var sellCarData1 = [
  {
    imgURL:Config.homeBuyCar,
    itemLabel:'买二手车',
    itemSummary:'专业顾问帮卖',
    tagType:"BUY_USED_CAR",
    jsonStr:Config.usedCarBuy
  },
  {
    imgURL:Config.homeSaleCar,
    itemLabel:'卖二手车',
    itemSummary:'方便快捷卖车',
    tagType:"SALE_USED_CAR",
    jsonStr:Config.usedCarSale
  }
];

var sellCarData2 = [
  {
    imgURL:Config.homeExchangeCar,
    itemLabel:'二手车置换',
    itemSummary:'以旧换新补贴',
    tagType:"USED_CAR_Metathesis",
    jsonStr:''
  }
];
//
// //会员
// var vipData1 = [
//   {
//     imgURL:Config.homeWallet,
//     itemLabel:'我的钱包',
//     itemSummary:'优惠券余额信息',
//     tagType:"MY_WALLET",
//     jsonStr:Config.vipMyWalletActivity
//   },
//   {
//     imgURL:Config.homeCarInfo,
//     itemLabel:'车辆信息',
//     itemSummary:'您的车辆信息',
//     tagType:"MY_CARS",
//     jsonStr:Config.vipAutoInfoActivity
//   }
// ];
//
// var vipData2 = [
//   {
//     imgURL:Config.homeSaleRecord,
//     itemLabel:'消费记录',
//     itemSummary:'各类消费记录',
//     tagType:"Consumption_Record",
//     jsonStr:''
//   },
//   {
//     imgURL:Config.homeOnlineServices,
//     itemLabel:'在线客服',
//     itemSummary:'在线解答疑难',
//     tagType:"Online_Service",
//     jsonStr:''
//   }
// ];
//
// var vipData3 = [
//   {
//     imgURL:Config.homeFeedback,
//     itemLabel:'客户投诉',
//     itemSummary:'帮助我们做得更好',
//     tagType:"Customer_Complaint",
//     jsonStr:''
//   },
//   {
//     imgURL:Config.homeVipRights,
//     itemLabel:'会员权益',
//     itemSummary:'相关规章制度',
//     tagType:"VIP_RULE",
//     jsonStr:''
//   }
// ];
//
// var vipData4 = [
//   {
//     imgURL:Config.homeCollections,
//     itemLabel:'我的收藏',
//     itemSummary:'您收藏的产品',
//     tagType:"OTHER_S",
//     jsonStr:''
//   }
// ];
//
// var withImgDataL={
//   textColor:'#F08F6F',
//   imgURL:'http://r2.ykimg.com/0510000057E31E6867BC3D7272039304',
//   itemLabel:'带图模块左',
//   itemSummary:'带图模块左',
//   tagType:"OTHER_S",
//   jsonStr:''
// }
//
// var withImgDatasR = [
//   {
//     imgURL:Config.homeCollections,
//     itemLabel:'带图模块右1',
//     itemSummary:'带图模块右1',
//     tagType:"OTHER_S",
//     jsonStr:'',
//     isEnd:false,
//   },
//   // {
//   //   imgURL:Config.homeCollections,
//   //   itemLabel:'带图模块右2',
//   //   itemSummary:'带图模块右2',
//   //   tagType:"OTHER_S",
//   //   jsonStr:'',
//   //   isEnd:false,
//   // },
//   {
//     imgURL:Config.homeCollections,
//     itemLabel:'带图模块右3',
//     itemSummary:'带图模块右3',
//     tagType:"OTHER_S",
//     jsonStr:'',
//     isEnd:true,
//   }
// ];
var divideLineH = 10;
export default class ThirdBlocksView extends Component{

    render(){
      return(
        <View style={styles.container}>
          <ThirdBlocksTitleView mTitle='新车销售' mSummary='BUY CARS'/>
          <ThirdBlocksItemView mIsOne = 'no' itemData={buyCarData}/>

          <View style={{width:screenWidth,height:divideLineH}}>
          </View>
          <ThirdBlocksTitleView mTitle='二手车交易' mSummary='SELL CARS'/>
          <ThirdBlocksItemView mIsOne = 'no' itemData={sellCarData1}/>
          <ThirdBlocksItemView mIsOne = 'yes' itemData={sellCarData2}/>

          <View style={{width:screenWidth,height:divideLineH}}>
          </View>
          <ThirdBlocksTitleView mTitle='用车服务' mSummary='USE'/>

          <ThirdBlocksItemView mIsOne = 'no' itemData={useCarDatas1}/>
          <ThirdBlocksItemView mIsOne = 'no' itemData={useCarDatas2}/>
          <ThirdBlocksItemView mIsOne = 'no' itemData={useCarDatas3}/>
          <ThirdBlocksItemView mIsOne = 'no' itemData={useCarDatas4}/>
          <ThirdBlocksItemView mIsOne = 'yes' itemData={useCarDatas5}/>
          <View style={{width:screenWidth,height:divideLineH+8}}>
          </View>


        </View>
      )
    }
}

const styles = StyleSheet.create({
  container:{
    flex:1,
    alignItems:'center',
    // backgroundColor:'#302F37'
    // borderColor:'red',
    // borderWidth:1,
  },

});

module.exports = ThirdBlocksView;
