/*
 * Copyright (C) 2015 Square, Inc.
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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Adapts a {@link Router} with return type into the type of {@code T}. Instances are
 * created by {@linkplain Factory a factory} which is {@linkplain Routine.Builder#addAdapterFactory(Factory)
 * installed} into the {@link Routine} instance.
 */
public interface Adapter<T> {
  /**
   * Returns the value type that this adapter uses when calling router. This type is used to prepare
   * the {@code router} passed to {@link #adapt}.
   */
  Type callType();

  /**
   * Returns an instance of {@code T} which delegates to {@code call}.
   */
  T adapt(RouterCall call);

  /**
   * Creates {@link Adapter} instances based on the return type of {@linkplain
   * Routine#create(Class) the router interface} methods.
   */
  abstract class Factory {
    /**
     * Returns an adapter for interface methods that return {@code returnType}, or null if it
     * cannot be handled by this factory.
     */
    public abstract Adapter<?> get(Type returnType, Annotation[] annotations, Routine routine);

    /**
     * Extract the upper bound of the generic parameter at {@code index} from {@code type}. For
     * example, index 1 of {@code Map<String, ? extends Runnable>} returns {@code Runnable}.
     */
    protected static Type getParameterUpperBound(int index, ParameterizedType type) {
      return Utils.getParameterUpperBound(index, type);
    }

    /**
     * Extract the raw class type from {@code type}.
     */
    protected static Class<?> getRawType(Type type) {
      return Utils.getRawType(type);
    }
  }
}
