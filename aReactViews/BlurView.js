const React = require('react-native');
const {
  requireNativeComponent,
  Component
} = React;

class BlurView extends Component {
  render() {
    return (
      <NativeBlurView
        {...this.props}
        style={[{
          backgroundColor: 'transparent',
        }, this.props.style
        ]}
      />
    );
  }
}

BlurView.propTypes = {};

const NativeBlurView = requireNativeComponent('BlurView', BlurView);

module.exports = BlurView;
