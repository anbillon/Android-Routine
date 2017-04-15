package com.anbillon.routine;

import android.content.Intent;
import android.net.Uri;

/**
 * This will be the final interceptor and return real router.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class RealInterceptor implements Interceptor {
  private static final String SCHEME_HTTP = "http";
  private static final String SCHEME_HTTPS = "https";

  @Override public Router intercept(Chain chain) throws RoutineException {
    Router router = chain.router();

    Uri uri = router.intent().getData();
    String scheme;
    if (uri != null && (scheme = uri.getScheme()) != null && (SCHEME_HTTP.equals(scheme)
        || SCHEME_HTTPS.equals(scheme))) {
      /* clear all the data */
      return router.newBuilder().intent(new Intent()).build();
    }

    return chain.router();
  }
}
