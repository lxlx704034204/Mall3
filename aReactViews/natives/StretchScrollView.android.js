'use strict';

var NativeMethodsMixin = require('react/lib/NativeMethodsMixin');
var React = require('React');
var View = require('View');
var StyleSheet = require('StyleSheet');

var requireNativeComponent = require('requireNativeComponent');

var INNERVIEW = 'InnerView';

var StretchScrollView = React.createClass({

  propTypes: {
    ...View.propTypes,
    isScrollNever: React.PropTypes.bool,
    showsVerticalScrollIndicator:React.PropTypes.bool,
  },

  render: function() {
    var props = {
      ...this.props,
      style: ([{flex: 1}, this.props.style]: ?Array<any>),
      ref: INNERVIEW,
    };

    if (this.props.onContentSizeChange) {
      props.onContentSizeChange = this._handleContentSizeChange;
    }

    var wrappedChildren = React.Children.map(this.props.children, (child) => {
      if (!child) {
        return null;
      }
      return (
        <View
          collapsable={false}
          style={styles.absolute}>
          {child}
        </View>
      );
    });

    return (
      <NativeStretchScrollView {...props}>
        {wrappedChildren}
      </NativeStretchScrollView>
    );
  },
});

var styles = StyleSheet.create({
  absolute: {
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
  },
});

var NativeStretchScrollView = requireNativeComponent(
  'RCTStretchScrollView',
  StretchScrollView,
  {nativeOnly:{
  // 'scaleX': true,
  // 'scaleY': true,
  'testID': true,
  // 'decomposedMatrix': true,
  // 'backgroundColor': true,
  // 'accessibilityComponentType': true,
  // 'renderToHardwareTextureAndroid': true,
  // 'translateY': true,
  // 'translateX': true,
  // 'accessibilityLabel': true,
  // 'accessibilityLiveRegion': true,
  // 'importantForAccessibility': true,
  // 'rotation': true,
  // 'opacity': true,
}}
);

module.exports = StretchScrollView;
