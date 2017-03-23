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

import java.lang.reflect.Type;

/**
 * A handler to handle parameters with annotation(e.g @Extra, @ExtraSet).
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
abstract class ParameterHandler<T> {
  abstract void apply(RouterBuilder builder, T value) throws IllegalArgumentException;

  static final class Caller<T> extends ParameterHandler<T> {
    private final Routine routine;

    public Caller(Routine routine) {
      this.routine = routine;
    }

    @Override void apply(RouterBuilder builder, T value) throws IllegalArgumentException {
      if (value == null) {
        throw new IllegalArgumentException("Caller parameter value must not be null.");
      }

      builder.resolver(routine.resolver(value));
    }
  }

  static final class PageName extends ParameterHandler<String> {
    @Override void apply(RouterBuilder builder, String value) throws IllegalArgumentException {
      if (value == null) {
        throw new IllegalArgumentException("PageName parameter value must not be null.");
      }

      builder.pageName(value);
    }
  }

  static final class SchemeUrl extends ParameterHandler<String> {
    @Override void apply(RouterBuilder builder, String value) throws IllegalArgumentException {
      if (value == null) {
        throw new IllegalArgumentException("SchemeUrl parameter value must not be null.");
      }

      builder.schemeUrl(value);
    }
  }

  static final class RequestCode extends ParameterHandler<Integer> {
    @Override void apply(RouterBuilder builder, Integer value) throws IllegalArgumentException {
      if (value != null) {
        builder.requestCode(value);
      }
    }
  }

  static final class Extra<T> extends ParameterHandler<T> {
    private final String name;
    private final Type type;

    public Extra(String name, Type type) {
      this.name = name;
      this.type = type;
    }

    @Override void apply(RouterBuilder builder, T value) throws IllegalArgumentException {
      if (value != null) {
        builder.putExtra(name, type, value);
      }
    }
  }

  static final class ExtraSet<T> extends ParameterHandler<T> {
    @Override void apply(RouterBuilder builder, T value) throws IllegalArgumentException {
      if (value != null) {
        builder.putExtras(value);
      }
    }
  }
}
