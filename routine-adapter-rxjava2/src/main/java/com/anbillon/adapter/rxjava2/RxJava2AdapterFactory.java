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
import com.anbillon.routine.Routine;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * A {@linkplain Adapter.Factory adapter} which uses RxJava2 for creating observables.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public final class RxJava2AdapterFactory extends Adapter.Factory {
  private final Scheduler scheduler;

  private RxJava2AdapterFactory(Scheduler scheduler) {
    this.scheduler = scheduler;
  }

  /**
   * Returns an instance which creates observables that do not operate on any scheduler by default.
   */
  public static RxJava2AdapterFactory create() {
    return new RxJava2AdapterFactory(null);
  }

  /**
   * Returns an instance which creates synchronous observables that {@linkplain
   * Observable#subscribeOn(Scheduler) subscribe on} {@code scheduler} by default.
   */
  public static RxJava2AdapterFactory create(Scheduler scheduler) {
    if (scheduler == null) {
      throw new NullPointerException("scheduler == null");
    }
    return new RxJava2AdapterFactory(scheduler);
  }

  @Override public Adapter<?> get(Type returnType, Annotation[] annotations, Routine routine) {
    Class<?> rawType = getRawType(returnType);

    boolean isFlowable = rawType == Flowable.class;
    boolean isSingle = rawType == Single.class;
    boolean isMaybe = rawType == Maybe.class;
    boolean isCompletable = rawType == Completable.class;
    if (rawType != Observable.class && !isFlowable && !isSingle && !isMaybe) {
      return null;
    }

    Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
    Class<?> rawObservableType = getRawType(observableType);
    Type callType;

    if (isCompletable) {
      callType = Void.class;
    } else {
      callType = rawObservableType;
    }

    return new RxJava2Adapter(callType, scheduler, isFlowable, isSingle, isMaybe, isCompletable);
  }
}
