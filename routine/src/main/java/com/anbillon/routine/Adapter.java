package com.anbillon.routine;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Adapts a {@link Router} with return type into the type of {@code T}. Instances are
 * created by {@linkplain Factory a factory} which is {@linkplain Routine.Builder#addAdapterFactory(Factory)
 * installed} into the {@link Routine} instance.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public interface Adapter<T> {
  /**
   * Returns the value type that this adapter uses when calling router. This type is used to prepare
   * the {@code router} passed to {@link #adapt}.
   */
  Type callType();

  /**
   * Returns an instance of {@code T} which delegates to {@code router}.
   */
  T adapt(Router router);

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
