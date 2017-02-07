import React, {
    Component,
    PropTypes,
} from 'react';
import {
    requireNativeComponent,
    View,
} from 'react-native';
const UIManager = require('UIManager');
const ReactNative = require('ReactNative');
const REF_PTR = "ptr_ref";

export default class PtrComponent extends Component {
    constructor(props) {
        super(props);
        this._onRefresh = this._onRefresh.bind(this);
    }

    _onRefresh() {
        if (!this.props.handleRefresh) {
            return;
        }
        this.props.handleRefresh();
    };

    /**
     * 自动刷新
     */
    autoRefresh() {
        let self = this;
        UIManager.dispatchViewManagerCommand(
            ReactNative.findNodeHandle(self.refs[REF_PTR]),
            1,
            null
        );
    }

    /**
     * 刷新完成
     */
    refreshComplete() {
        UIManager.dispatchViewManagerCommand(
            ReactNative.findNodeHandle(this.refs[REF_PTR]),
            0,
            null
        );
    }

    render() {
        // onPtrRefresh 事件对应原生的ptrRefresh事件
        return (
            <RCTPtrAndroid
                ref={REF_PTR}
                {...this.props}
                onPtrRefresh={() => this._onRefresh()}/>
        );
    }
}

PtrComponent.name = "RCTPtrAndroid"; //便于调试时显示(可以设置为任意字符串)
PtrComponent.propTypes = {
    handleRefresh: PropTypes.func,
    resistance: PropTypes.number,
    durationToCloseHeader: PropTypes.number,
    durationToClose: PropTypes.number,
    ratioOfHeaderHeightToRefresh: PropTypes.number,
    pullToRefresh: PropTypes.bool,
    keepHeaderWhenRefresh: PropTypes.bool,
    pinContent: PropTypes.bool,
    ...View.propTypes,
};

const RCTPtrAndroid = requireNativeComponent('RCTPtrAndroid', PtrComponent, {nativeOnly: {onPtrRefresh: true}});