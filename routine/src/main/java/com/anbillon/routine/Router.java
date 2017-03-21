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

  /**
   * Routine supports three {@link Method}.
   *
   * @return {@link Method}
   */
  public Method method() {
    return method;
  }

  /**
   * The target of this router, it will be a page nanme or scheme url.
   */
  public String target() {
    return target;
  }

  /**
   * Origin page. Generally it's the name of caller.
   */
  public String origin() {
    return resolver.callerName();
  }

  /**
   * Destination page. This means the available destination page, maybe null.
   */
  public String destination() {
    ActivityInfo activityInfo = resolveActivityInfo(resolver.context(), intent());
    return activityInfo == null ? null : activityInfo.name;
  }

  Class<?> errorPage() {
    return errorPage;
  }

  /**
   * Get {@link Context} this router uses.
   */
  public Context context() {
    return resolver.context();
  }

  /**
   * Get full {@link Intent} this router creates.
   */
  public Intent intent() {
    return intent;
  }

  /**
   * Get request code for this router.
   */
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
