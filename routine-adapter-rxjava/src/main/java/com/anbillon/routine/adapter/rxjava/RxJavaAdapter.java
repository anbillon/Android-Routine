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

package com.anbillon.routine.adapter.rxjava;

import com.anbillon.routine.Adapter;
import com.anbillon.routine.Router;
import com.anbillon.routine.RouterCall;
import com.anbillon.routine.RoutineException;
import java.lang.reflect.Type;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Scheduler;
import rx.Subscriber;
import rx.exceptions.CompositeException;
import rx.exceptions.Exceptions;
import rx.plugins.RxJavaPlugins;

/**
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class RxJavaAdapter implements Adapter<Object> {
  private final Type callType;
  private final Scheduler scheduler;
  private final boolean isSingle;
  private final boolean isCompletable;

  public RxJavaAdapter(Type callType, Scheduler scheduler, boolean isSingle,
      boolean isCompletable) {
    this.callType = callType;
    this.scheduler = scheduler;
    this.isSingle = isSingle;
    this.isCompletable = isCompletable;
  }

  @Override public Type callType() {
    return callType;
  }

  @Override public Object adapt(RouterCall call) {
    OnSubscribe<?> func = new VoidOnSubscribe(call);
    if (callType == Boolean.class) {
      func = new BooleanOnSubcribe(call);
    } else if (callType == Router.class) {
      func = new RouterOnSubscribe(call);
    }
    Observable<?> observable = Observable.create(func);

    if (scheduler != null) {
      observable = observable.subscribeOn(scheduler);
    }

    if (isSingle) {
      return observable.toSingle();
    }

    if (isCompletable) {
      return CompletableHelper.toCompletable(observable);
    }

    return observable;
  }

  /**
   * Separate static class defers classloading and bytecode verification since Completable is not an
   * RxJava stable API yet.
   */
  private static final class CompletableHelper {
    static Object toCompletable(Observable<?> observable) {
      return observable.toCompletable();
    }
  }

  final class RouterOnSubscribe implements OnSubscribe<Router> {
    private final RouterCall call;

    RouterOnSubscribe(RouterCall call) {
      this.call = call;
    }

    @Override public void call(Subscriber<? super Router> subscriber) {
      try {
        subscriber.onNext(call.router());
      } catch (RoutineException e) {
        try {
          subscriber.onError(e);
        } catch (Throwable inner) {
          Exceptions.throwIfFatal(inner);
          CompositeException composite = new CompositeException(e, inner);
          RxJavaPlugins.getInstance().getErrorHandler().handleError(composite);
        }
      }
      subscriber.onCompleted();
    }
  }

  final class BooleanOnSubcribe implements OnSubscribe<Boolean> {
    private final RouterCall call;

    BooleanOnSubcribe(RouterCall call) {
      this.call = call;
    }

    @Override public void call(Subscriber<? super Boolean> subscriber) {
      try {
        subscriber.onNext(call.execute());
      } catch (RoutineException e) {
        try {
          subscriber.onError(e);
        } catch (Throwable inner) {
          Exceptions.throwIfFatal(inner);
          CompositeException composite = new CompositeException(e, inner);
          RxJavaPlugins.getInstance().getErrorHandler().handleError(composite);
        }
      }
      subscriber.onCompleted();
    }
  }

  final class VoidOnSubscribe implements OnSubscribe<Void> {
    private final RouterCall call;

    VoidOnSubscribe(RouterCall call) {
      this.call = call;
    }

    @Override public void call(Subscriber<? super Void> subscriber) {
      try {
        call.execute();
        subscriber.onNext(null);
      } catch (RoutineException e) {
        try {
          subscriber.onError(e);
        } catch (Throwable inner) {
          Exceptions.throwIfFatal(inner);
          CompositeException composite = new CompositeException(e, inner);
          RxJavaPlugins.getInstance().getErrorHandler().handleError(composite);
        }
      }

      subscriber.onCompleted();
    }
  }
}
