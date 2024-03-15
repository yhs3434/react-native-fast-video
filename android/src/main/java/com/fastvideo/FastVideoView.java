package com.fastvideo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;

import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.view.ReactViewGroup;

import com.yqritc.scalablevideoview.ScalableType;
import com.yqritc.scalablevideoview.ScalableVideoView;

public class FastVideoView extends ScalableVideoView implements LifecycleEventListener {
  public enum Events {
    EVENT_LOAD_START("onVideoLoadStart"),
    EVENT_LOAD("onVideoLoad"),
    EVENT_ERROR("onVideoError"),
    EVENT_PROGRESS("onVideoProgress"),
    EVENT_TIMED_METADATA("onTimedMetadata"),
    EVENT_SEEK("onVideoSeek"),
    EVENT_END("onVideoEnd"),
    EVENT_STALLED("onPlaybackStalled"),
    EVENT_RESUME("onPlaybackResume"),
    EVENT_READY_FOR_DISPLAY("onReadyForDisplay"),
    EVENT_FULLSCREEN_WILL_PRESENT("onVideoFullscreenPlayerWillPresent"),
    EVENT_FULLSCREEN_DID_PRESENT("onVideoFullscreenPlayerDidPresent"),
    EVENT_FULLSCREEN_WILL_DISMISS("onVideoFullscreenPlayerWillDismiss"),
    EVENT_FULLSCREEN_DID_DISMISS("onVideoFullscreenPlayerDidDismiss");

    private final String mName;

    Events(final String name) {
      mName = name;
    }

    @Override
    public String toString() {
      return mName;
    }
  }

  public static final String EVENT_PROP_FAST_FORWARD = "canPlayFastForward";
  public static final String EVENT_PROP_SLOW_FORWARD = "canPlaySlowForward";
  public static final String EVENT_PROP_SLOW_REVERSE = "canPlaySlowReverse";
  public static final String EVENT_PROP_REVERSE = "canPlayReverse";
  public static final String EVENT_PROP_STEP_FORWARD = "canStepForward";
  public static final String EVENT_PROP_STEP_BACKWARD = "canStepBackward";

  public static final String EVENT_PROP_DURATION = "duration";
  public static final String EVENT_PROP_PLAYABLE_DURATION = "playableDuration";
  public static final String EVENT_PROP_SEEKABLE_DURATION = "seekableDuration";
  public static final String EVENT_PROP_CURRENT_TIME = "currentTime";
  public static final String EVENT_PROP_SEEK_TIME = "seekTime";
  public static final String EVENT_PROP_NATURALSIZE = "naturalSize";
  public static final String EVENT_PROP_WIDTH = "width";
  public static final String EVENT_PROP_HEIGHT = "height";
  public static final String EVENT_PROP_ORIENTATION = "orientation";
  public static final String EVENT_PROP_METADATA = "metadata";
  public static final String EVENT_PROP_TARGET = "target";
  public static final String EVENT_PROP_METADATA_IDENTIFIER = "identifier";
  public static final String EVENT_PROP_METADATA_VALUE = "value";

  public static final String EVENT_PROP_ERROR = "error";
  public static final String EVENT_PROP_WHAT = "what";
  public static final String EVENT_PROP_EXTRA = "extra";

  private ThemedReactContext mThemedReactContext;
  private RCTEventEmitter mEventEmitter;

  private Handler mProgressUpdateHandler = new Handler();
  private Runnable mProgressUpdateRunnable = null;
  private Handler videoControlHandler = new Handler();
  private MediaController mediaController;

  private String mSrcUriString = null;
  private String mSrcType = "mp4";
  private ReadableMap mRequestHeaders = null;
  private boolean mSrcIsNetwork = false;
  private boolean mSrcIsAsset = false;
  private ScalableType mResizeMode = ScalableType.LEFT_TOP;
  private boolean mRepeat = false;
  private boolean mPaused = false;
  private boolean mMuted = false;
  private boolean mPreventsDisplaySleepDuringVideoPlayback = true;
  private float mVolume = 1.0f;
  private float mStereoPan = 0.0f;
  private float mProgressUpdateInterval = 250.0f;
  private float mRate = 1.0f;
  private float mActiveRate = 1.0f;
  private long mSeekTime = 0;
  private boolean mPlayInBackground = false;
  private boolean mBackgroundPaused = false;
  private boolean mIsFullscreen = false;

  private int mMainVer = 0;
  private int mPatchVer = 0;

  private boolean mMediaPlayerValid = false; // True if mMediaPlayer is in prepared, started, paused or completed state.

  private int mVideoDuration = 0;
  private int mVideoBufferedDuration = 0;
  private boolean isCompleted = false;
  private boolean mUseNativeControls = false;



  public FastVideoView(ThemedReactContext themedReactContext) {
    super(themedReactContext);
    mThemedReactContext = themedReactContext;
    mEventEmitter = themedReactContext.getJSModule(RCTEventEmitter.class);
    themedReactContext.addLifecycleEventListener(this);
    Log.d("FastVideoView", "rendered");

    initializeMediaPlayerIfNeeded();
    setSurfaceTextureListener(this);

    mProgressUpdateRunnable = new Runnable() {
      @Override
      public void run() {

        if (mMediaPlayerValid && !isCompleted && !mPaused && !mBackgroundPaused) {
          WritableMap event = Arguments.createMap();
          event.putDouble(EVENT_PROP_CURRENT_TIME, mMediaPlayer.getCurrentPosition() / 1000.0);
          event.putDouble(EVENT_PROP_PLAYABLE_DURATION, mVideoBufferedDuration / 1000.0); //TODO:mBufferUpdateRunnable
          event.putDouble(EVENT_PROP_SEEKABLE_DURATION, mVideoDuration / 1000.0);
          mEventEmitter.receiveEvent(getId(), Events.EVENT_PROGRESS.toString(), event);

          mProgressUpdateHandler.postDelayed(mProgressUpdateRunnable, Math.round(mProgressUpdateInterval));
        }
      }
    };
  }

  private void initializeMediaPlayerIfNeeded() {
    if (mMediaPlayer == null) {
      mMediaPlayerValid = false;
      mMediaPlayer = new MediaPlayer();
      mMediaPlayer.setOnVideoSizeChangedListener(this);
//      mMediaPlayer.setOnErrorListener(this);
//      mMediaPlayer.setOnPreparedListener(this);
//      mMediaPlayer.setOnBufferingUpdateListener(this);
//      mMediaPlayer.setOnSeekCompleteListener(this);
//      mMediaPlayer.setOnCompletionListener(this);
//      mMediaPlayer.setOnInfoListener(this);
//      if (Build.VERSION.SDK_INT >= 23) {
//        mMediaPlayer.setOnTimedMetaDataAvailableListener(new TimedMetaDataAvailableListener());
//      }
    }
  }

  public void setSrc(String src) {
    initializeMediaPlayerIfNeeded();
    mMediaPlayer.reset();

    try {
      setDataSource(src);
      prepareAsync();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Override
  public void onHostPause() {
    if (mMediaPlayerValid && !mPaused && !mPlayInBackground) {
      /* Pause the video in background
       * Don't update the paused prop, developers should be able to update it on background
       *  so that when you return to the app the video is paused
       */
      mBackgroundPaused = true;
      mMediaPlayer.pause();
    }
  }

  @Override
  public void onHostResume() {
    mBackgroundPaused = false;
    if (mMediaPlayerValid && !mPlayInBackground && !mPaused) {
      new Handler().post(new Runnable() {
        @Override
        public void run() {
          // Restore original state
//          setPausedModifier(false);
        }
      });
    }
  }

  @Override
  public void onHostDestroy() {
  }
}
