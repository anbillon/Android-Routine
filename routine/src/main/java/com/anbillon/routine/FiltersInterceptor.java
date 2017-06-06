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

import android.content.Intent;

import static com.anbillon.routine.Method.SCHEME_URL;

/**
 * A Routine interceptor which filters wanted {@link Intent} according to scheme url.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class FiltersInterceptor implements Interceptor {
  private final Matcher matcher;

  FiltersInterceptor(Matcher matcher) {
    this.matcher = matcher;
  }

  @Override public Router intercept(Chain chain) throws RoutineException {
    Router router = chain.router();
    if (router.method() != SCHEME_URL) {
      return chain.proceed(router);
    }

    Intent intent = matcher.match(router.context(), router.target());
    if (intent == null) {
      return chain.proceed(router);
    }
    Intent origin = router.intent();
    intent.putExtras(origin).setData(origin.getData());

    return chain.proceed(router.newBuilder().intent(intent).build());
  }
}