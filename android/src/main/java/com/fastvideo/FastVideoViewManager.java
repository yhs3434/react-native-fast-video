package com.fastvideo;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import javax.annotation.Nullable;
import java.util.Map;

public class FastVideViewManager extends SimpleViewManager<FastVideoView> {
    public static final String REACT_CLASS = "RCTFastVideo";

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public FastVideoView createViewInstance(ThemedReactContext themedReactContext) {
        return new FastVideoView(themedReactContext);
    }
}