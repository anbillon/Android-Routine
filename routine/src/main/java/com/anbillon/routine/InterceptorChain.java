/*
 * Copyright (C) 2015 Square, Inc.
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

import java.util.List;

/**
 * A concrete interceptor chain that carries the entire interceptor chain.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class InterceptorChain implements Interceptor.Chain {
  private final List<Interceptor> interceptors;
  private final Router router;
  private final int index;
  private int calls;

  InterceptorChain(List<Interceptor> interceptors, int index, Router router) {
    this.interceptors = interceptors;
    this.index = index;
    this.router = router;
  }

  @Override public Router router() {
    return router;
  }

  @Override public Router proceed(Router router) throws RoutineException {
    if (router == null) {
      throw new IllegalArgumentException("Router in interceptor chain must not be null.");
    }

    if (index >= interceptors.size()) throw new AssertionError();

    calls++;

    /* call the next interceptor in the chain */
    InterceptorChain next = new InterceptorChain(interceptors, index + 1, router);
    Interceptor interceptor = interceptors.get(index);
    Router result = interceptor.intercept(next);

    /* confirm that the next interceptor made its required call to chain.proceed() */
    if (index + 1 < interceptors.size() && next.calls != 1) {
      throw new IllegalStateException(
          "Routine interceptor " + interceptor + " must call proceed() exactly once.");
    }

    return result;
  }
}
