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

import java.util.List;

/**
 * A concrete filter chain that carries the entire filter chain.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class FilterChain implements Filter.Chain {
  private final List<Filter> filters;
  private final Matcher matcher;
  private final int index;
  private int calls;

  FilterChain(List<Filter> filters, int index, Matcher matcher) {
    this.filters = filters;
    this.index = index;
    this.matcher = matcher;
  }

  @Override public Matcher matcher() {
    return matcher;
  }

  @Override public Matcher proceed(Matcher matcher) {
    if (matcher == null) {
      throw new IllegalArgumentException("Matcher in filter must not be null.");
    }

    if (index >= filters.size()) {
      return matcher;
    }

    calls++;

    /* call the next filter in the chain */
    FilterChain next = new FilterChain(filters, index + 1, matcher);
    Filter filter = filters.get(index);
    Matcher result = filter.filter(next);

    /* confirm that the next filter made its required call to chain.proceed() */
    if (index + 1 < filters.size() && next.calls != 1) {
      throw new IllegalStateException(
          "Routine interceptor " + filter + " must call proceed() exactly once.");
    }

    return result;
  }
}
