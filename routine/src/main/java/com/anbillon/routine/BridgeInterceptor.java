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

      default:
        break;
    }

    return chain.proceed(builder.intent(intent).build());
  }
}
