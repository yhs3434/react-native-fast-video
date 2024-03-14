package com.fastvideo;

import android.view.View;

import androidx.annotation.Nullable;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ViewManagerDelegate;
import com.facebook.react.viewmanagers.FastVideoViewManagerDelegate;
import com.facebook.react.viewmanagers.FastVideoViewManagerInterface;

public abstract class FastVideoViewManagerSpec<T extends View> extends SimpleViewManager<T> implements FastVideoViewManagerInterface<T> {
  private final ViewManagerDelegate<T> mDelegate;

  public FastVideoViewManagerSpec() {
    mDelegate = new FastVideoViewManagerDelegate(this);
  }

  @Nullable
  @Override
  protected ViewManagerDelegate<T> getDelegate() {
    return mDelegate;
  }
}
