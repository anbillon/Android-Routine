package com.anbillon.routine;

/**
 * This will be the final interceptor and return real router.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class RealInterceptor implements Interceptor {

  @Override public Router intercept(Chain chain) throws RoutineException {
    return chain.router();
  }
}
