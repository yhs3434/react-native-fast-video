package com.fastvideo;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;

import com.fastvideo.FastVideoSpec;

public class FastVideoModule extends FastVideoSpec {
  public static final String NAME = "FastVideo";

  FastVideoModule(ReactApplicationContext context) {
    super(context);
  }

  @NonNull
  public String getName() {
    return NAME;
  }

  static {
    System.loadLibrary("react-native-fast-video");
  }

  public static native double nativeMultiply(double a, double b);

  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  public void multiply(double a, double b, Promise promise) {
    promise.resolve(nativeMultiply(a, b));
  }
}
