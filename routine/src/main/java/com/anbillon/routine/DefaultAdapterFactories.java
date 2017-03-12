package com.anbillon.routine;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Creates default call adapters for those who uses main thread to call.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class DefaultAdapterFactories extends Adapter.Factory {

  @Override public Adapter<?> get(Type returnType, Annotation[] annotations, Routine routine) {
    if (returnType == void.class || returnType == Void.class) {
      return new VoidAdapter();
    } else if (returnType == boolean.class || returnType == Boolean.class) {
      return new BooleanAdapter();
    } else if (returnType == Router.class) {
      return new RouterAdapter();
    }

    return null;
  }

  static final class VoidAdapter implements Adapter<Void> {
    @Override public Type callType() {
      return void.class;
    }

    @Override public Void adapt(Router router) {
      router.start();
      return null;
    }
  }

  static final class BooleanAdapter implements Adapter<Boolean> {
    @Override public Type callType() {
      return boolean.class;
    }

    @Override public Boolean adapt(Router router) {
      return router.start();
    }
  }

  static final class RouterAdapter implements Adapter<Router> {
    @Override public Type callType() {
      return Router.class;
    }

    @Override public Router adapt(Router router) {
      return router;
    }
  }
}
