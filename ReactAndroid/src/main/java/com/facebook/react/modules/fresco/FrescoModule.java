/**
 * Copyright (c) 2015-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

package com.facebook.react.modules.fresco;

import android.support.annotation.Nullable;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.common.ModuleDataCleaner;

/**
 * Module to initialize the Fresco library.
 *
 * <p>Does not expose any methods to JavaScript code. For initialization and cleanup only.
 */
@ReactModule(name = "FrescoModule")
public class FrescoModule extends ReactContextBaseJavaModule implements
    ModuleDataCleaner.Cleanable {

  private @Nullable ImagePipelineConfig mConfig;

  /**
   * Create a new Fresco module with a default configuration (or the previously given
   * configuration via {@link #FrescoModule(ReactApplicationContext, ImagePipelineConfig)}.
   *
   * @param reactContext the context to use
   */
  public FrescoModule(ReactApplicationContext reactContext) {
    this(reactContext, null);
  }

  /**
   * Create a new Fresco module with a given ImagePipelineConfig.
   * This should only be called when the module has not been initialized yet.
   * You can use {@link FrescoHelpers#hasBeenInitialized()} to check this and call
   * {@link #FrescoModule(ReactApplicationContext)} if it is already initialized.
   * Otherwise, the given Fresco configuration will be ignored.
   *
   * @param reactContext the context to use
   * @param config the Fresco configuration, which will only be used for the first initialization
   */
  public FrescoModule(ReactApplicationContext reactContext, @Nullable ImagePipelineConfig config) {
    super(reactContext);
    mConfig = config;
  }

  @Override
  public void initialize() {
    super.initialize();
    FrescoHelpers.initialize(getReactApplicationContext(), mConfig);
  }

  @Override
  public String getName() {
    return "FrescoModule";
  }

  @Override
  public void clearSensitiveData() {
    FrescoHelpers.clearSensitiveData();
  }
}
