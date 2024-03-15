package com.fastvideo;

import android.view.View;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.SimpleViewManager;

public abstract class FastVideoViewManagerSpec<T extends View> extends SimpleViewManager<T> {
  public abstract void setSrc(T view, @Nullable String src);
}
