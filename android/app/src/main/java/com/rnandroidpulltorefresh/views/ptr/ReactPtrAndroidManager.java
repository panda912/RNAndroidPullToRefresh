package com.rnandroidpulltorefresh.views.ptr;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.sgb.library.ptr.PtrClassicFrameLayout;
import com.sgb.library.ptr.PtrDefaultHandler;
import com.sgb.library.ptr.PtrFrameLayout;

import java.util.Map;

import javax.annotation.Nullable;


/**
 * Created by panda on 2016/12/7 下午4:50.
 */
public class ReactPtrAndroidManager extends ViewGroupManager<PtrClassicFrameLayout> {

    private static final int REFRESH_COMPLETE = 0;
    private static final int AUTO_REFRESH = 1;

    @Override
    public String getName() {
        return "RCTPtrAndroid";
    }

    @Override
    protected PtrClassicFrameLayout createViewInstance(final ThemedReactContext reactContext) {
        PtrClassicFrameLayout layout = new PtrClassicFrameLayout(reactContext);
        layout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return layout;
    }

    @ReactProp(name = "resistance", defaultFloat = 1.7f)
    public void setResistance(PtrClassicFrameLayout ptr, float resistance) {
        ptr.setResistance(resistance);
    }

    @ReactProp(name = "durationToCloseHeader", defaultInt = 200)
    public void setDurationToCloseHeader(PtrClassicFrameLayout ptr, int duration) {
        ptr.setDurationToCloseHeader(duration);
    }

    @ReactProp(name = "durationToClose", defaultInt = 300)
    public void setDurationToClose(PtrClassicFrameLayout ptr, int duration) {
        ptr.setDurationToClose(duration);
    }

    @ReactProp(name = "ratioOfHeaderHeightToRefresh", defaultFloat = 1.2f)
    public void setRatioOfHeaderHeightToRefresh(PtrClassicFrameLayout ptr, float ratio) {
        ptr.setRatioOfHeaderHeightToRefresh(ratio);
    }

    @ReactProp(name = "pullToRefresh", defaultBoolean = false)
    public void setPullToRefresh(PtrClassicFrameLayout ptr, boolean pullToRefresh) {
        ptr.setPullToRefresh(pullToRefresh);
    }

    @ReactProp(name = "keepHeaderWhenRefresh", defaultBoolean = false)
    public void setKeepHeaderWhenRefresh(PtrClassicFrameLayout ptr, boolean keep) {
        ptr.setKeepHeaderWhenRefresh(keep);
    }

    @ReactProp(name = "pinContent", defaultBoolean = false)
    public void setPinContent(PtrClassicFrameLayout ptr, boolean pinContent) {
        ptr.setPinContent(pinContent);
    }


    @Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of("autoRefresh", AUTO_REFRESH, "refreshComplete", REFRESH_COMPLETE);
    }

    @Override
    public void receiveCommand(PtrClassicFrameLayout root, int commandId, @Nullable ReadableArray args) {
        switch (commandId) {
            case AUTO_REFRESH:
                root.autoRefresh();
                break;
            case REFRESH_COMPLETE:
                root.refreshComplete();
                break;
            default:
                break;
        }
    }

    @Override
    protected void addEventEmitters(final ThemedReactContext reactContext, final PtrClassicFrameLayout view) {
        view.setLastUpdateTimeRelateObject(this);
        view.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                reactContext
                        .getNativeModule(UIManagerModule.class)
                        .getEventDispatcher()
                        .dispatchEvent(new PtrEvent(view.getId()));
            }
        });
    }

    @Nullable
    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>of(
                "ptrRefresh", MapBuilder.of("registrationName", "onPtrRefresh"));
    }

    @Override
    public void addView(PtrClassicFrameLayout parent, View child, int index) {
        super.addView(parent, child, 1);
        parent.finishInflateRN();
    }
}
