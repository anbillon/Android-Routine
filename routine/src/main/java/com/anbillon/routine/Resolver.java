/*
 * Copyright (C) 2017 Tourbillon Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.anbillon.routine;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

/**
 * A context to resolve different resolver.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public interface Resolver {

  String callerName();

  Context context();

  void startActivity(Intent intent, int enterAnim, int exitAnim) throws ActivityNotFoundException;

  void startActivityForResult(Intent intent, int requestCode, int enterAnim, int exitAnim)
      throws ActivityNotFoundException;

  /**
   * Creates {@link Resolver} instances based on the caller.
   */
  abstract class Factory {
    /**
     * Returns a {@link Resolver} to resolve the caller.
     */
    protected <T> Resolver create(T caller) {
      return null;
    }
  }
}
