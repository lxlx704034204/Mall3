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

export default class ImgPage extends React.Component {

    static propTypes = {
        slide: PropTypes.string.isRequired,  // 图片,加入.isRequired即为比填项
        url: PropTypes.string,  // 显示标题\文字
        type: PropTypes.string,  // Tag
        onClick: PropTypes.func,  // 回调函数
        height:PropTypes.number,
    };

    constructor(props) {
        super(props);
        this._onClick = this._onClick.bind(this);  // 需要在回调函数中使用this,必须使用bind(this)来绑定
    }

    _onClick() {
        if (this.props.onClick) {   // 在设置了回调函数的情况下
            this.props.onClick(this.props.url, this.props.type);  // 回调Title和Tag
        }
    }

    render() {
        return (
            <TouchableWithoutFeedback onPress={this._onClick}>

                    <Image style={[styles.page,{height:this.props.height}]} defaultSource={{uri: 'banner_3x'}} source={{uri:this.props.slide}}/>

            </TouchableWithoutFeedback>
        );
    }
}

const styles = StyleSheet.create({
  page: {
      flex: 1,
      resizeMode: 'stretch'
  },
    showText: {
        fontSize: 12
    }
});
