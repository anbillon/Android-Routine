package com.anbillon.routine;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.anbillon.routine.Utils.checkNotNull;
import static com.anbillon.routine.Utils.resolveActivityInfo;

/**
 * A router. Instances of this class are immutable.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public final class Router {
  private final Method method;
  private final String target;
  private final Class<?> errorPage;
  private final Resolver resolver;
  private final Intent intent;
  private final int requestCode;
  private final int enterAnim;
  private final int exitAnim;

  private Router(Builder builder) {
    this.method = builder.method;
    this.target = builder.target;
    this.errorPage = builder.errorPage;
    this.resolver = builder.resolver;
    this.intent = checkNotNull(builder.intent, "intent == null in Router");
    this.requestCode = builder.requestCode;
    this.enterAnim = builder.enterAnim;
    this.exitAnim = builder.exitAnim;
  }

  /**
   * Start current router to open new page.
   *
   * @return ture if open successfully, otherwise return false
   */
  public boolean start() {
    try {
      if (requestCode >= 0) {
        resolver.startActivityForResult(intent, requestCode, enterAnim, exitAnim);
      } else {
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        resolver.startActivity(intent, enterAnim, exitAnim);
      }
    } catch (ActivityNotFoundException ignore) {
      return false;
    }

    return true;
  }

  public Method method() {
    return method;
  }

  public String target() {
    return target;
  }

  public String origin() {
    return resolver.callerName();
  }

  public String destination() {
    ActivityInfo activityInfo = resolveActivityInfo(resolver.context(), intent());
    return activityInfo == null ? null : activityInfo.name;
  }

  Class<?> errorPage() {
    return errorPage;
  }

  public Context context() {
    return resolver.context();
  }

  public Intent intent() {
    return intent;
  }

  public int requestCode() {
    return requestCode;
  }

  public Builder newBuilder() {
    return new Builder(this);
  }

  public static final class Builder {
    private Method method;
    private String target;
    private Class<?> errorPage;
    private Resolver resolver;
    private Intent intent;
    private int requestCode;
    private int enterAnim;
    private int exitAnim;

    Builder() {
    }

    Builder(Router router) {
      this.method = router.method;
      this.target = router.target;
      this.errorPage = router.errorPage;
      this.resolver = router.resolver;
      this.intent = router.intent;
      this.requestCode = router.requestCode;
      this.enterAnim = router.enterAnim;
      this.exitAnim = router.exitAnim;
    }

    Builder method(Method method) {
      this.method = method;
      return this;
    }

    Builder target(String target) {
      this.target = target;
      return this;
    }

    Builder errorPage(Class<?> errorPage) {
      this.errorPage = errorPage;
      return this;
    }

    Builder resolver(Resolver resolver) {
      this.resolver = resolver;
      return this;
    }

    public Builder intent(Intent intent) {
      this.intent = intent;
      return this;
    }

    public Builder requestCode(int requestCode) {
      this.requestCode = requestCode;
      return this;
    }

    public Builder anim(int enterAnim, int exitAnim) {
      this.enterAnim = enterAnim;
      this.exitAnim = exitAnim;
      return this;
    }

    public Router build() {
      return new Router(this);
    }
  }
}
