// 'use strict';
// var React = require('react-native');
// var {
//   StyleSheet,
//   Text,
//   View,
//   TextInput,
//   Image,
//   TouchableHighlight,
//   TouchableOpacity,
//   Dimensions,
//   LayoutAnimation,
//   Animated
// } = React;
//
// var screenWidth = require('Dimensions').get('window').width;
//
// var width;
// var height;
// var Button = React.createClass({
//     getInitialState: function() {
//         return {scale: 1};
//     },
//
//     getDefaultProps:function(){
//         return {
//             title:null,
//             backgroundImage:null,
//             backgroundColor:'#00000000',
//             resizeMode:'stretch',
//             cornerRadius: 0,
//             tagType: null,
//             iosType: null,
//             jsonStr: null,
//         };
//     },
//     propTypes:{
//         title:              React.PropTypes.string,
//         backgroundImage:    React.PropTypes.oneOfType([React.PropTypes.string,React.PropTypes.object]),
//         backgroundColor:    React.PropTypes.string,
//         borderColor:        React.PropTypes.string,
//         resizeMode:         React.PropTypes.string,
//         width:              React.PropTypes.number,
//         height:             React.PropTypes.number,
//         cornerRadius:       React.PropTypes.number,
//         fontSize:           React.PropTypes.number,
//         fontWeight:         React.PropTypes.string,
//         color:              React.PropTypes.string,
//         onPress:            React.PropTypes.func,
//         tagType:            React.PropTypes.string,
//         iosType:            React.PropTypes.string,
//         jsonStr:            React.PropTypes.string,
//     },
//     componentWillMount: function() {
//       console.log(this.props.title);
//         width = this.props.width;
//         height = this.props.height;
//     },
//     componentDidMount: function() {
//     },
//     render: function() {
//         var backgroundImage = this.props.backgroundImage;
//         if (typeof backgroundImage == "string") {
//             backgroundImage = {uri:backgroundImage}
//         }
//
//         return (
//             <TouchableOpacity
//                 ref='buttonWrapper'
//                 activeOpacity={1}
//                 style={[
//                     styles.container,
//                     this.props.style,
//                     {
//                         width:this.props.width,
//                         height:this.props.height,
//                         backgroundColor:this.props.backgroundColor,
//                         transform:[{scale:this.state.scale}]
//                     }
//                 ]}
//
//                 onPressIn={this._buttonDown}
//                 onPressOut={this._buttonUp}
//             >
//                 <Image
//                     style={[styles.backgroundImage]}
//                     source={backgroundImage}
//                     resizeMode={this.props.resizeMode}
//                     backgroundColor='#fafafa00'
//                 >
//                     <Text
//                         ref='buttonText'
//                         style={[styles.text,{fontSize:this.props.fontSize,color:this.props.color,fontWeight:this.props.fontWeight}, this.props.textStyle]}>
//                         {this.props.title}
//                     </Text>
//                 </Image>
//             </TouchableOpacity>
//         );
//     },
//     _buttonDown: function(event){
//         // this.props.onPress(event);
//         LayoutAnimation.spring();
//         this.setState({scale:0.8});
//     },
//     _buttonUp: function(event){
//         LayoutAnimation.spring();
//         this.setState({scale:1});
//
//         if (this.props.onPress) {   // 在设置了回调函数的情况下
//  // 回调Title和Tag和jsonStr
//             this.props.onPress(this.props.title,this.props.tagType,this.props.jsonStr,this.props.iosType);
//         }
//     }
// });
//
// var styles = StyleSheet.create({
//     container:{
//         overflow:'hidden',
//     },
//     backgroundImage:{
//         borderColor:'#000',
//         flex:1,
//         justifyContent:'center',
//
//     },
//     text:{
//         backgroundColor:'#00000000',
//         textAlign:'center',
//         borderColor:'#000',
//         borderWidth:1
//     }
// });
//
// module.exports = Button;
