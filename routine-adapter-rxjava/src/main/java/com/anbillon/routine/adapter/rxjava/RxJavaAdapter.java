package com.anbillon.routine.adapter.rxjava;

import com.anbillon.routine.Adapter;
import com.anbillon.routine.Router;
import java.lang.reflect.Type;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Scheduler;
import rx.Subscriber;

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

  @Override public Object adapt(Router router) {
    OnSubscribe<?> func = new VoidOnSubscribe(router);
    if (callType == Boolean.class) {
      func = new BooleanOnSubcribe(router);
    } else if (callType == Router.class) {
      func = new RouterOnSubscribe(router);
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
    private final Router router;

    RouterOnSubscribe(Router router) {
      this.router = router;
    }

    @Override public void call(Subscriber<? super Router> subscriber) {
      subscriber.onNext(router);
      subscriber.onCompleted();
    }
  }

  final class BooleanOnSubcribe implements OnSubscribe<Boolean> {
    private final Router router;

    BooleanOnSubcribe(Router router) {
      this.router = router;
    }

    @Override public void call(Subscriber<? super Boolean> subscriber) {
      subscriber.onNext(router.start());
      subscriber.onCompleted();
    }
  }

  final class VoidOnSubscribe implements OnSubscribe<Void> {
    private final Router router;

    VoidOnSubscribe(Router router) {
      this.router = router;
    }

    @Override public void call(Subscriber<? super Void> subscriber) {
      router.start();
      subscriber.onNext(null);
      subscriber.onCompleted();
    }
  }
}
