package com.fastvideo;

import androidx.annotation.Nullable;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import android.util.Log;
import android.view.View;

public class FastVideoView extends View {

  public FastVideoView(Context context) {
    super(context);
    Log.d("FastVideoView", "rendered");
  }

  public FastVideoView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    Log.d("FastVideoView", "rendered");
  }

  public FastVideoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    Log.d("FastVideoView", "rendered");
  }

}
