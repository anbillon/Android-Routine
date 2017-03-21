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

package com.anbillon.routine.support;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import com.anbillon.routine.Resolver;

/**
 * A resolver for support fragment.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class SupportFragmentResolver implements Resolver {
  private final Fragment fragment;

  SupportFragmentResolver(Fragment fragment) {
    this.fragment = fragment;
  }

  @Override public String callerName() {
    return fragment.getClass().getCanonicalName();
  }

  @Override public Context context() {
    return fragment.getContext();
  }

  @Override public void startActivity(Intent intent, int enterAnim, int exitAnim)
      throws ActivityNotFoundException {
    fragment.startActivity(intent);
    if (enterAnim == 0 && exitAnim == 0) {
      return;
    }
    fragment.getActivity().overridePendingTransition(enterAnim, exitAnim);
  }

  @Override
  public void startActivityForResult(Intent intent, int requestCode, int enterAnim, int exitAnim)
      throws ActivityNotFoundException {
    fragment.startActivityForResult(intent, requestCode);
    if (enterAnim == 0 && exitAnim == 0) {
      return;
    }
    fragment.getActivity().overridePendingTransition(enterAnim, exitAnim);
  }
}
