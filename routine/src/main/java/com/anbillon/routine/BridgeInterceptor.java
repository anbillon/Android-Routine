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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import java.net.URISyntaxException;

import static android.content.Intent.ACTION_VIEW;
import static android.content.Intent.CATEGORY_BROWSABLE;

/**
 * Bridges from application code to router code. It builds an {@link Intent} for router.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class BridgeInterceptor implements Interceptor {

  @Override public Router intercept(Chain chain) throws RoutineException {
    Router router = chain.router();
    Router.Builder builder = router.newBuilder();
    String target = router.target();
    Intent intent = router.intent();
    Context context = router.context();

    /* check the method to build intent */
    switch (router.method()) {
      case SCHEME_URL:
        try {
          Uri uri = Intent.parseUri(target, 0).getData();
          intent.setAction(ACTION_VIEW)
              .setData(uri)
              .addCategory(CATEGORY_BROWSABLE)
              .setComponent(null);
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            intent.setSelector(null);
          }
        } catch (URISyntaxException ignore) {
        }
        break;

      case PAGE_NAME:
      case PAGE:
        try {
          intent.setClass(context, Class.forName(target));
        } catch (ClassNotFoundException ignore) {
        }
        break;

      case ACTION:
        intent.setAction(target);
        break;

      default:
        break;
    }

    return chain.proceed(builder.intent(intent).build());
  }
}
