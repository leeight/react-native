/**
 * Copyright (c) 2015-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

package com.facebook.react.modules.fresco;

import android.content.Context;
import android.support.annotation.Nullable;

import com.facebook.common.logging.FLog;
import com.facebook.common.soloader.SoLoaderShim;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.common.ReactConstants;
import com.facebook.react.modules.network.OkHttpClientProvider;
import com.facebook.soloader.SoLoader;

import java.util.HashSet;

public class FrescoHelpers {
  private static boolean sHasBeenInitialized = false;

  private static ImagePipelineConfig getDefaultConfig(Context context) {
    HashSet<RequestListener> requestListeners = new HashSet<>();
    requestListeners.add(new SystraceRequestListener());

    return OkHttpImagePipelineConfigFactory
      .newBuilder(context.getApplicationContext(), OkHttpClientProvider.getOkHttpClient())
      .setDownsampleEnabled(false)
      .setRequestListeners(requestListeners)
      .build();
  }

  public static void initialize(ReactApplicationContext reactContext) {
    initialize(reactContext, null);
  }

  public static void initialize(ReactApplicationContext reactContext, @Nullable ImagePipelineConfig config) {
    if (!hasBeenInitialized()) {
      if (config == null) {
        config = getDefaultConfig(reactContext);
      }

      // Make sure the SoLoaderShim is configured to use our loader for native libraries.
      // This code can be removed if using Fresco from Maven rather than from source
      SoLoaderShim.setHandler(new FrescoHandler());
      Fresco.initialize(reactContext.getApplicationContext(), config);
      sHasBeenInitialized = true;
    }
    else if (config != null) {
      FLog.w(ReactConstants.TAG,
        "Fresco has already been initialized with a different config. "
        + "The new Fresco configuration will be ignored!");
    }
  }

  public static void clearSensitiveData() {
    // Clear image cache.
    Fresco.getImagePipeline().clearCaches();
  }

  /**
   * Check whether the FrescoModule has already been initialized.
   *
   * @return true if FrescoModule has already been initialized
   */
  public static boolean hasBeenInitialized() {
    return sHasBeenInitialized;
  }

  private static class FrescoHandler implements SoLoaderShim.Handler {
    @Override
    public void loadLibrary(String libraryName) {
      SoLoader.loadLibrary(libraryName);
    }
  }
}
