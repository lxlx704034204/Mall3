
// // 测试 第一个 无用模块

// 'use strict';
// var React = require('react-native');


// var {
//   	StyleSheet,
//   	Text,
//   	View,
//   	Image,
//   	TouchableHighlight,
//     TouchableOpacity,
//     AsyncStorage,
// } = React;
// var Switcher = require('../Switcher');
// var STORAGE_KEY_ONE = '@AsyncStorageDemo:turn_data';
// var ButtonView = require('../ButtonView');


// var BlockView = React.createClass({


//   _onPressButton:function(e){

//       //跳转

//       AsyncStorage.setItem(STORAGE_KEY_ONE,'新车销售');
//       AsyncStorage.getItem(STORAGE_KEY_ONE, (error, result) => {
//                    if (error) {
//                         console.log(error);
//                      resolve(null);
//                    } else {
//                      console.log(result);
//                      Switcher.turn(result,"NEW_CAR");
//                    }
//                  });

//       console.log(e.nativeEvent);

//   },

//   render: function() {
//     return (

//       <View style={styles.container}>

//         <View style={styles.viewLabel}>
//           <Image style={styles.imgLabel} resizeMode='cover'
//             source={{uri: 'bg_red'}}>
//             <Text style={styles.textLabel}>
//               要买车
//             </Text>
//           </Image>
//         </View>

//         <View style={styles.vertiaclWarp}>
//           <View style={styles.hWarp1}>

//                 <ButtonView
//                   style={styles.smallView}
//                   title="新车销售"
//                   backgroundColor='#CE4E45'
//                   backgroundImage="./img/ic_taken.png"
//                   fontSize = {14}
//                   color='#fafafa'
//                   onPress={this._onPressButton}
//                    />

              

//           </View>

//           <View style={styles.hWarp2}>
          
//                 <ButtonView
//                       style={styles.smallView}
//                       title="网上4s店"
//                       backgroundColor='#CE4E45'
//                       backgroundImage="./img/ic_taken.png"
//                       fontSize = {14}
//                       color='#fafafa'
//                         />
  
//           </View>
//         </View>
//       </View>
//     );
//   }
// });

// var styles = StyleSheet.create({
//   	container:{
//       flex:1,
//     	backgroundColor:'#F2F2F2',
//     	flexDirection:'row',
//       marginTop:12,
//       marginLeft:14,
//       marginRight:14,
//   	},
//     viewLabel:{
//       flex:1,
//       height:85,
//       width:109,
//     },
//     vertiaclWarp:{
//       flex:2,
//       height:85
//     },
//     hWarp1:{
//       flex:1,
//       flexDirection:'row',
//       marginBottom:3,
//     },
//     hWarp2:{
//       flex:1,
//       flexDirection:'row',
//       marginTop:3,
//     },
//     smallView:{
//       flex:1,
//       backgroundColor:'#CE4E45',
//       marginLeft:5
//     },
//     textSon:{
//       fontSize:14,
//       color:"#fafafa",
//     },
//     textLabel:{
//       flex:1,
//       fontSize:18,
//       color:"#fafafa",
//       textAlign:'center',
//       textShadowColor:'#00000055',
//       textShadowOffset:{width:2,height:2},
//       fontWeight:'bold',
//     },
//     imgLabel:{
//       flex:1,
//       flexDirection:'row',
//       alignItems:'center',
//     }

// });



// module.exports = BlockView;
