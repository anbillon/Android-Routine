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

import java.util.ArrayList;
import java.util.List;

/**
 * A router call which creates the router and invokes the router.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public final class RouterCall<T> {
  private final RouterMethod<T> routerMethod;
  private final List<Interceptor> interceptors;
  private final List<Filter> filters;
  private final Object[] args;

  RouterCall(RouterMethod<T> routerMethod, List<Interceptor> interceptors, List<Filter> filters,
      Object[] args) {
    this.routerMethod = routerMethod;
    this.interceptors = interceptors;
    this.filters = filters;
    this.args = args;
  }

  /**
   * Execute this router call.
   *
   * @return true if successfully, otherwise return false
   */
  public boolean execute() throws RoutineException {
    return router().start();
  }

  /**
   * Create a {@link Router} from router interface.
   */
  public Router router() throws RoutineException {
    Router originRouter = routerMethod.toRouter(args);

    /* build filters with matcher */
    Matcher matcher = new Matcher.Builder().build();
    FilterChain filterChain = new FilterChain(filters, 0, matcher);
    Matcher realMatcher = filterChain.proceed(matcher);
    FiltersInterceptor filtersInterceptor = new FiltersInterceptor(realMatcher);

    /* build a full stack of interceptors */
    List<Interceptor> fullInterceptors = new ArrayList<>();
    fullInterceptors.add(new BridgeInterceptor());
    fullInterceptors.add(filtersInterceptor);
    fullInterceptors.addAll(interceptors);
    fullInterceptors.add(new RealInterceptor());
    Interceptor.Chain interceptorChain = new InterceptorChain(fullInterceptors, 0, originRouter);

    /* proceed the chain to get real router */
    return interceptorChain.proceed(originRouter);
  }
}
