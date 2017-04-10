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

package com.anbillon.adapter.rxjava2;

import com.anbillon.routine.Adapter;
import com.anbillon.routine.Router;
import com.anbillon.routine.RouterCall;
import com.anbillon.routine.RoutineException;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;
import java.lang.reflect.Type;

/**
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class RxJava2Adapter implements Adapter<Object> {
  private final Type callType;
  private final Scheduler scheduler;
  private final boolean isFlowable;
  private final boolean isSingle;
  private final boolean isMaybe;
  private final boolean isCompletable;

  RxJava2Adapter(Type callType, Scheduler scheduler, boolean isFlowable, boolean isSingle,
      boolean isMaybe, boolean isCompletable) {
    this.callType = callType;
    this.scheduler = scheduler;
    this.isFlowable = isFlowable;
    this.isSingle = isSingle;
    this.isMaybe = isMaybe;
    this.isCompletable = isCompletable;
  }

  @Override public Type callType() {
    return callType;
  }

  @Override public Object adapt(RouterCall call) {
    Observable<?> observable;
    if (callType == Boolean.class) {
      observable = new BooleanObservable(call);
    } else if (callType == Router.class) {
      observable = new RouterObservable(call);
    } else {
      observable = new VoidObservable(call);
    }

    if (scheduler != null) {
      observable = observable.subscribeOn(scheduler);
    }

    if (isFlowable) {
      return observable.toFlowable(BackpressureStrategy.LATEST);
    }
    if (isSingle) {
      return observable.singleOrError();
    }
    if (isMaybe) {
      return observable.singleElement();
    }
    if (isCompletable) {
      return observable.ignoreElements();
    }

    return observable;
  }

  private static void handleException(Observer<?> observer, RoutineException e) {
    try {
      observer.onError(e);
    } catch (Throwable inner) {
      Exceptions.throwIfFatal(inner);
      CompositeException composite = new CompositeException(e, inner);
      RxJavaPlugins.onError(composite);
    }
  }

  final class RouterObservable extends Observable<Router> {
    private final RouterCall call;

    RouterObservable(RouterCall call) {
      this.call = call;
    }

    @Override protected void subscribeActual(Observer<? super Router> observer) {
      try {
        observer.onNext(call.router());
      } catch (RoutineException e) {
        handleException(observer, e);
      }
    }
  }

  final class BooleanObservable extends Observable<Boolean> {
    private final RouterCall call;

    BooleanObservable(RouterCall call) {
      this.call = call;
    }

    @Override protected void subscribeActual(Observer<? super Boolean> observer) {
      try {
        observer.onNext(call.execute());
      } catch (RoutineException e) {
        handleException(observer, e);
      }
    }
  }

  final class VoidObservable extends Observable<Void> {
    private final RouterCall call;

    VoidObservable(RouterCall call) {
      this.call = call;
    }

    @Override protected void subscribeActual(Observer<? super Void> observer) {
      try {
        call.execute();
        observer.onComplete();
      } catch (RoutineException e) {
        handleException(observer, e);
      }
    }
  }
}
