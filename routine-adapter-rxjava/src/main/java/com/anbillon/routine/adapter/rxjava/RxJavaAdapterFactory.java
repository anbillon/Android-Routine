package com.anbillon.routine.adapter.rxjava;

import com.anbillon.routine.Adapter;
import com.anbillon.routine.Routine;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import rx.Observable;
import rx.Scheduler;
import rx.Single;

/**
 * A {@linkplain Adapter.Factory adapter} which uses RxJava for creating observables.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public final class RxJavaAdapterFactory extends Adapter.Factory {
  private final Scheduler scheduler;

  private RxJavaAdapterFactory(Scheduler scheduler) {
    this.scheduler = scheduler;
  }

  /**
   * Returns an instance which creates observables that do not operate on any scheduler by default.
   */
  public static RxJavaAdapterFactory create() {
    return new RxJavaAdapterFactory(null);
  }

  /**
   * Returns an instance which creates synchronous observables that {@linkplain
   * Observable#subscribeOn(Scheduler) subscribe on} {@code scheduler} by default.
   */
  public static RxJavaAdapterFactory create(Scheduler scheduler) {
    return new RxJavaAdapterFactory(scheduler);
  }

  @Override public Adapter<?> get(Type returnType, Annotation[] annotations, Routine routine) {
    Class<?> rawType = getRawType(returnType);
    boolean isSingle = rawType == Single.class;
    boolean isCompletable = "rx.Completable".equals(rawType.getCanonicalName());
    if (rawType != Observable.class && !isSingle && !isCompletable) {
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

    return new RxJavaAdapter(callType, scheduler, isSingle, isCompletable);
  }
}
