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

/**
 * A handler to handle method with annotation(e.g @SchemeUrl, @PageName).
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
abstract class MethodHandler<T> {
  protected final T value;

  MethodHandler(T value) {
    this.value = value;
  }

  abstract void apply(RouterBuilder builder) throws IllegalArgumentException;

  static final class SchemeUrl extends MethodHandler<String> {
    SchemeUrl(String value) {
      super(value);
    }

    @Override void apply(RouterBuilder builder) throws IllegalArgumentException {
      if (value == null) {
        throw new IllegalArgumentException("SchemeUrl value must not be null.");
      }

      builder.schemeUrl(value);
    }
  }

  static final class PageName extends MethodHandler<String> {
    public PageName(String value) {
      super(value);
    }

    @Override void apply(RouterBuilder builder) throws IllegalArgumentException {
      if (value == null) {
        throw new IllegalArgumentException("PageName value must not be null.");
      }

      builder.pageName(value);
    }
  }

  static final class Page extends MethodHandler<Class<?>> {
    Page(Class<?> value) {
      super(value);
    }

    @Override void apply(RouterBuilder builder) throws IllegalArgumentException {
      if (value == null) {
        throw new IllegalArgumentException("Page value must not be null.");
      }

      builder.page(value);
    }
  }

  static final class Flags extends MethodHandler<Integer> {
    final boolean set;

    Flags(int value, boolean set) {
      super(value);
      this.set = set;
    }

    @Override void apply(RouterBuilder builder) throws IllegalArgumentException {
      if (value == null) {
        throw new IllegalArgumentException("Flags value must not be null.");
      }

      builder.flags(value, set);
    }
  }

  static final class RequestCode extends MethodHandler<Integer> {
    public RequestCode(Integer value) {
      super(value);
    }

    @Override void apply(RouterBuilder builder) throws IllegalArgumentException {
      if (value != null) {
        builder.requestCode(value);
      }
    }
  }

  static final class Action extends MethodHandler<String> {
    public Action(String value) {
      super(value);
    }

    @Override void apply(RouterBuilder builder) throws IllegalArgumentException {
      if (value == null) {
        throw new IllegalArgumentException("Flags value must not be null.");
      }

      builder.action(value);
    }
  }

  static final class Anim extends MethodHandler<Integer> {
    private final Integer exit;

    Anim(Integer enter, Integer exit) {
      super(enter);
      this.exit = exit;
    }

    @Override void apply(RouterBuilder builder) throws IllegalArgumentException {
      if (value == null || exit == null) {
        throw new IllegalArgumentException("Anim value must not be null.");
      }

      builder.anim(value, exit);
    }
  }
}
