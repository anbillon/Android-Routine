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

    @Override public Void adapt(RouterCall call) {
      try {
        call.execute();
      } catch (RoutineException ignore) {
      }
      return null;
    }
  }

  static final class BooleanAdapter implements Adapter<Boolean> {
    @Override public Type callType() {
      return boolean.class;
    }

    @Override public Boolean adapt(RouterCall call) {
      try {
        return call.execute();
      } catch (RoutineException e) {
        return false;
      }
    }
  }

  static final class RouterAdapter implements Adapter<Router> {
    @Override public Type callType() {
      return Router.class;
    }

    @Override public Router adapt(RouterCall call) {
      try {
        return call.router();
      } catch (RoutineException e) {
        return null;
      }
    }
  }
}
