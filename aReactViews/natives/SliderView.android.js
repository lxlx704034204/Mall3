import { requireNativeComponent, PropTypes } from 'react-native';

var iface = {
  name: 'SliderView',
  propTypes: {
    adData: PropTypes.string,
  },
};

module.exports = requireNativeComponent('RCTSliderADView', iface,{nativeOnly:{
  'scaleX': true,
  'scaleY': true,
  'testID': true,
  'decomposedMatrix': true,
  'backgroundColor': true,
  'accessibilityComponentType': true,
  'renderToHardwareTextureAndroid': true,
  'translateY': true,
  'translateX': true,
  'accessibilityLabel': true,
  'accessibilityLiveRegion': true,
  'importantForAccessibility': true,
  'rotation': true,
  'opacity': true,
  'onLayout':true,
}});
