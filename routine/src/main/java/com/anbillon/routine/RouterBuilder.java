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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.anbillon.routine.Utils.getExtraRawType;

/**
 * A builder to build {@link Router} with methods and parameters.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class RouterBuilder {
  private final Intent intent;
  private Method method;
  private String target;
  private Resolver resolver;
  private int requestCode = -1;
  private int enterAnim;
  private int exitAnim;

  RouterBuilder() {
    this.intent = new Intent();
  }

  /**
   * Add scheme url into current builder if existed.
   *
   * @param schemeUrl scheme url
   */
  void schemeUrl(String schemeUrl) {
    this.method = Method.SCHEME_URL;
    this.target = schemeUrl;
  }

  /**
   * Add page target into current builder if existed.
   *
   * @param pageName page pageName
   */
  void pageName(String pageName) {
    this.method = Method.PAGE_NAME;
    this.target = pageName;
  }

  /**
   * Add page into current builder if existed.
   *
   * @param page page
   */
  void page(Class<?> page) {
    this.method = Method.PAGE;
    this.target = page.getCanonicalName();
  }

  /**
   * Add action into current builder.
   *
   * @param action action
   */
  void action(String action) {
    this.method = Method.ACTION;
    this.target = action;
  }

  /**
   * Add flags into current builder if existed.
   *
   * @param flags flags
   * @param setFlags use set method or not
   */
  void flags(int flags, boolean setFlags) {
    if (setFlags) {
      intent.setFlags(flags);
    } else {
      intent.addFlags(flags);
    }
  }

  /**
   * Add animation resource id into builder.
   *
   * @param enterAnim enter animation resource id
   * @param exitAnim exit animation resource id
   */
  void anim(int enterAnim, int exitAnim) {
    this.enterAnim = enterAnim;
    this.exitAnim = exitAnim;
  }

  /**
   * Add {@link Resolver} into this builder.
   *
   * @param resolver {@link Resolver}
   */
  void resolver(Resolver resolver) {
    this.resolver = resolver;
  }

  /**
   * Add request code into current builder.
   *
   * @param requestCode request code
   */
  void requestCode(int requestCode) {
    this.requestCode = requestCode;
  }

  /**
   * Put extended data into {@link Intent}.
   *
   * @param name target of extra
   * @param type parameter type
   * @param value value to add
   * @param <T> type of @{code value}
   */
  @SuppressWarnings("unchecked") <T> void putExtra(String name, Type type, T value) {
    Class<?> rawParameterType = getExtraRawType(type);
    if (rawParameterType == boolean.class) {
      intent.putExtra(name, Boolean.parseBoolean(value.toString()));
    } else if (rawParameterType == byte.class) {
      intent.putExtra(name, Byte.parseByte(value.toString()));
    } else if (rawParameterType == char.class) {
      intent.putExtra(name, value.toString().toCharArray()[0]);
    } else if (rawParameterType == CharSequence.class) {
      intent.putExtra(name, (CharSequence) value);
    } else if (rawParameterType == CharSequence[].class) {
      intent.putExtra(name, (CharSequence[]) value);
    } else if (rawParameterType == double.class) {
      intent.putExtra(name, Double.parseDouble(value.toString()));
    } else if (rawParameterType == float.class) {
      intent.putExtra(name, Float.parseFloat(value.toString()));
    } else if (rawParameterType == int.class) {
      intent.putExtra(name, Integer.parseInt(value.toString()));
    } else if (rawParameterType == long.class) {
      intent.putExtra(name, Long.parseLong(value.toString()));
    } else if (rawParameterType == short.class) {
      intent.putExtra(name, Short.parseShort(value.toString()));
    } else if (rawParameterType == Parcelable.class) {
      intent.putExtra(name, (Parcelable) value);
    } else if (rawParameterType == Parcelable[].class) {
      intent.putExtra(name, (Parcelable[]) value);
    } else if (rawParameterType == Serializable.class
        || rawParameterType.getEnumConstants() != null) {
      intent.putExtra(name, (Serializable) value);
    } else if (rawParameterType == Serializable[].class) {
      intent.putExtra(name, (Serializable[]) value);
    } else if (rawParameterType == ArrayList.class) {
      if (type instanceof ParameterizedType) {
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (actualTypeArguments == null || actualTypeArguments.length != 1) {
          throw new IllegalArgumentException(
              "Unsupported type " + rawParameterType + " with parameter: " + name);
        }

        Type actualTypeArgument = actualTypeArguments[0];
        if (actualTypeArgument == CharSequence.class) {
          intent.putCharSequenceArrayListExtra(name, (ArrayList<CharSequence>) value);
        } else if (actualTypeArgument == String.class) {
          intent.putStringArrayListExtra(name, (ArrayList<String>) value);
        } else if (actualTypeArgument == Integer.class) {
          intent.putIntegerArrayListExtra(name, (ArrayList<Integer>) value);
        } else if (actualTypeArgument instanceof Class<?>) {
          Class<?>[] interfaces = ((Class<?>) actualTypeArgument).getInterfaces();
          for (Class<?> interfaceClazz : interfaces) {
            if (interfaceClazz == Parcelable.class) {
              intent.putParcelableArrayListExtra(name, (ArrayList<Parcelable>) value);
              return;
            }
          }

          throw new IllegalArgumentException(
              "Unsupported type " + rawParameterType + " with parameter: " + name);
        }
      }
    } else {
      throw new IllegalArgumentException(
          "Unsupported type " + rawParameterType + " with parameter: " + name);
    }
  }

  /**
   * Put extended data set into intent.
   *
   * @param extras extended data to add
   * @param <T> type of extended data
   */
  <T> void putExtras(T extras) {
    if (extras instanceof Bundle) {
      intent.putExtras((Bundle) extras);
    } else if (extras instanceof Intent) {
      intent.putExtras((Intent) extras);
    } else if (extras instanceof Uri) {
      intent.setData((Uri) extras);
    } else {
      throw new IllegalArgumentException("Unsupported type");
    }
  }

  Router build() {
    return new Router.Builder().method(method)
        .target(target)
        .resolver(resolver)
        .intent(intent)
        .requestCode(requestCode)
        .anim(enterAnim, exitAnim)
        .build();
  }
}
