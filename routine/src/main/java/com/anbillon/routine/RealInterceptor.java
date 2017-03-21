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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.anbillon.routine.Utils.resolveActivityInfo;

/**
 * A Routine interceptor which checks if the {@link Intent} is available. This will be the final
 * interceptor and return real router.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class RealInterceptor implements Interceptor {

  @Override public Router intercept(Chain chain) throws RoutineException {
    Router router = chain.router();
    Context context = router.context();
    Intent intent = router.intent();
    Class<?> errorPage = router.errorPage();

    if (resolveActivityInfo(context, intent) != null) {
      return router;
    }
    intent = replaceWithErrorPage(context, intent, errorPage);

    return router.newBuilder().intent(intent).build();
  }

  private Intent replaceWithErrorPage(Context context, Intent origin, Class<?> errorPage) {
    if (errorPage == null) {
      return origin;
    }

    return origin.setClass(context, errorPage)
        .addFlags(context instanceof Activity ? 0 : FLAG_ACTIVITY_NEW_TASK);
  }
}
