package com.anbillon.routine;

/**
 * Typically interceptors add, remove, or transform intent on router.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public interface Interceptor {

  Router intercept(Chain chain) throws RoutineException;

  interface Chain {

    Router router();

    Router proceed(Router router) throws RoutineException;
  }
}
