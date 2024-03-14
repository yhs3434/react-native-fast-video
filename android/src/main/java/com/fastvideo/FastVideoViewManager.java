package com.fastvideo;

import android.graphics.Color;

import androidx.annotation.Nullable;

import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import com.fastvideo.FastVideoViewManagerSpec;

@ReactModule(name = FastVideoViewManager.NAME)
public class FastVideoViewManager extends FastVideoViewManagerSpec<FastVideoView> {

  public static final String NAME = "FastVideoView";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public FastVideoView createViewInstance(ThemedReactContext context) {
    return new FastVideoView(context);
  }

  @Override
  @ReactProp(name = "color")
  public void setColor(FastVideoView view, @Nullable String color) {
    view.setBackgroundColor(Color.parseColor(color));
  }
}
