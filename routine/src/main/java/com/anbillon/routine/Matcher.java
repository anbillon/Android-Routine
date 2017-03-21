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

import android.content.Context;
import android.content.Intent;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static com.anbillon.routine.Utils.resolveActivityInfo;
import static com.anbillon.routine.Utils.resolveSchemeUrl;

/**
 * A matcher to match scheme url. Instances of this class are immutable.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public final class Matcher {
  private final Map<String, Set<Class<?>>> pagesMap;

  private Matcher(Builder builder) {
    this.pagesMap = builder.pagesMap;
  }

  /**
   * Match an {@link Intent} with given scheme url.
   *
   * @param schemeUrl scheme url
   * @return {@link Intent} if matched, otherwise return null.
   */
  public Intent match(Context context, String schemeUrl) {
    Set<Class<?>> pages = null;
    for (String key : pagesMap.keySet()) {
      if (schemeUrl.contains(key)) {
        pages = pagesMap.get(key);
        break;
      }
    }

    if (pages != null && !pages.isEmpty()) {
      Intent intent = new Intent();
      for (Class<?> page : pages) {
        intent.setClass(context, page);
        if (resolveActivityInfo(context, intent) != null) {
          return intent;
        }
      }
    }

    return null;
  }

  public Builder newBuilder() {
    return new Builder(this);
  }

  public static final class Builder {
    private Map<String, Set<Class<?>>> pagesMap = new LinkedHashMap<>();

    Builder() {
    }

    Builder(Matcher matcher) {
      this.pagesMap = matcher.pagesMap;
    }

    public Builder addPage(String schemeUrl, Class<?> page) {
      String realSchemeUrl = resolveSchemeUrl(schemeUrl);
      Set<Class<?>> pages = pagesMap.get(realSchemeUrl);
      if (pages != null && !pages.isEmpty()) {
        pages.add(page);
      } else {
        LinkedHashSet<Class<?>> newPages = new LinkedHashSet<>();
        newPages.add(page);
        pagesMap.put(schemeUrl, newPages);
      }

      return this;
    }

    public Builder addPageName(String schemeUrl, String pageName) {
      try {
        return addPage(schemeUrl, Class.forName(pageName));
      } catch (ClassNotFoundException ignore) {
      }

      return this;
    }

    public Matcher build() {
      return new Matcher(this);
    }
  }
}
