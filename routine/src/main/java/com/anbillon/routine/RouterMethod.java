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
import android.text.TextUtils;
import com.anbillon.routine.app.Action;
import com.anbillon.routine.app.Anim;
import com.anbillon.routine.app.Caller;
import com.anbillon.routine.app.Extra;
import com.anbillon.routine.app.Flags;
import com.anbillon.routine.app.Page;
import com.anbillon.routine.app.PageName;
import com.anbillon.routine.app.RequestCode;
import com.anbillon.routine.app.SchemeUrl;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static com.anbillon.routine.Utils.getRawType;
import static com.anbillon.routine.Utils.hasUnresolvableType;

/**
 * Parse an invocation of an interface method into a router.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class RouterMethod<T> {
  private final MethodHandler<?>[] methodHandlers;
  private final ParameterHandler<?>[] parameterHandlers;
  final Adapter<T> adapter;

  RouterMethod(Builder<T> builder) {
    this.methodHandlers = builder.methodHandlers;
    this.parameterHandlers = builder.parameterHandlers;
    this.adapter = builder.adapter;
  }

  Router toRouter(Object... args) throws IllegalArgumentException {
    RouterBuilder routerBuilder = new RouterBuilder();

    for (MethodHandler<?> handler : methodHandlers) {
      handler.apply(routerBuilder);
    }

    /* It is an error to invoke a method with the wrong arg types. */
    @SuppressWarnings("unchecked") ParameterHandler<Object>[] handlers =
        (ParameterHandler<Object>[]) parameterHandlers;
    int argumentCount = args != null ? args.length : 0;
    if (argumentCount != handlers.length) {
      throw new IllegalArgumentException("Argument count ("
          + argumentCount
          + ") doesn't match expected count ("
          + handlers.length
          + ")");
    }

    for (int p = 0; p < argumentCount; p++) {
      handlers[p].apply(routerBuilder, args[p]);
    }

    return routerBuilder.build();
  }

  static final class Builder<T> {
    final Routine routine;
    final Method method;
    final Annotation[] methodAnnotations;
    final Annotation[][] parameterAnnotationsArray;
    final Type[] parameterTypes;

    boolean gotCaller;
    boolean gotSchemeUrl;
    boolean gotPageName;
    boolean gotAction;
    boolean gotRequestCode;
    ParameterHandler<?>[] parameterHandlers;
    MethodHandler<?>[] methodHandlers;
    Adapter<T> adapter;

    public Builder(Routine routine, Method method) {
      this.routine = routine;
      this.method = method;
      this.methodAnnotations = method.getAnnotations();
      this.parameterTypes = method.getGenericParameterTypes();
      this.parameterAnnotationsArray = method.getParameterAnnotations();
    }

    public RouterMethod build() {
      adapter = createAdapter();
      Type callType = adapter.callType();
      if (callType != void.class
          && callType != Void.class
          && callType != boolean.class
          && callType != Boolean.class
          && callType != Router.class) {
        throw methodError("'"
            + getRawType(callType).getName()
            + "' is not a valid call type. Routine supports void, boolean and Router.");
      }

      int methodCount = methodAnnotations.length;
      methodHandlers = new MethodHandler<?>[methodCount];
      for (int m = 0; m < methodCount; m++) {
        methodHandlers[m] = parseMethodAnnotation(methodAnnotations[m]);
      }

      int parameterCount = parameterAnnotationsArray.length;
      parameterHandlers = new ParameterHandler<?>[parameterCount];
      for (int p = 0; p < parameterCount; p++) {
        Type parameterType = parameterTypes[p];
        if (hasUnresolvableType(parameterType)) {
          throw parameterError(p, "Parameter type must not include a type variable or wildcard: %s",
              parameterType);
        }

        Annotation[] parameterAnnotations = parameterAnnotationsArray[p];
        if (parameterAnnotations == null) {
          throw parameterError(p, "No Routine annotation found.");
        }

        parameterHandlers[p] = parseParameter(p, parameterType, parameterAnnotations);
      }

      if (methodHandlers.length == 0 && !gotSchemeUrl && !gotPageName) {
        throw methodError(
            "Routine method annotation is required (@SchemeUrl, @Page, @PageName or @Action).");
      }

      if (!gotCaller) {
        throw methodError("A router must contain one @Caller.");
      }

      return new RouterMethod<>(this);
    }

    @SuppressWarnings("unchecked") private Adapter<T> createAdapter() {
      Type returnType = method.getGenericReturnType();
      if (hasUnresolvableType(returnType)) {
        throw methodError("Method return type must not include a type variable or wildcard: %s",
            returnType);
      }

      Annotation[] annotations = method.getAnnotations();
      try {
        return (Adapter<T>) routine.adapter(returnType, annotations);
      } catch (RuntimeException e) {
        throw methodError(e, "Unable to router adapter for %s", returnType);
      }
    }

    private MethodHandler<?> parseMethodAnnotation(Annotation annotation) {
      if (annotation instanceof SchemeUrl) {
        gotSchemeUrl = true;
        return new MethodHandler.SchemeUrl(((SchemeUrl) annotation).value());
      } else if (annotation instanceof PageName) {
        gotPageName = true;
        return new MethodHandler.PageName(((PageName) annotation).value());
      } else if (annotation instanceof Page) {
        return new MethodHandler.Page(((Page) annotation).value());
      } else if (annotation instanceof Action) {
        gotAction = true;
        return new MethodHandler.Action(((Action) annotation).value());
      } else if (annotation instanceof Flags) {
        Flags flags = (Flags) annotation;
        return new MethodHandler.Flags(flags.value(), (flags.set()));
      } else if (annotation instanceof RequestCode) {
        gotRequestCode = true;
        return new MethodHandler.RequestCode(((RequestCode) annotation).value());
      } else if (annotation instanceof Anim) {
        Anim anim = (Anim) annotation;
        return new MethodHandler.Anim(anim.enter(), anim.exit());
      }

      /* no annotation method */
      throw methodError("No routine method annotation found", annotation);
    }

    private ParameterHandler<?> parseParameterAnnotation(int p, Type type, Annotation annotation) {
      if (annotation instanceof Caller) {
        if (gotCaller) {
          throw parameterError(p, "Multiple @Caller parameter annotations found.");
        }

        gotCaller = true;

        return new ParameterHandler.Caller(routine);
      }

      if (annotation instanceof SchemeUrl) {
        if (gotSchemeUrl) {
          throw parameterError(p, "Multiple @SchemeUrl annotations found on method and parameter.");
        }

        gotSchemeUrl = true;

        return new ParameterHandler.SchemeUrl();
      }

      if (annotation instanceof PageName) {
        if (gotPageName) {
          throw parameterError(p, "Multiple @PageName annotations found on method and parameter.");
        }

        gotPageName = true;

        return new ParameterHandler.PageName();
      }

      if (annotation instanceof Action) {
        if (gotAction) {
          throw parameterError(p, "Multiple @Action annotations found on method and parameter.");
        }

        gotAction = true;

        return new ParameterHandler.Action();
      }

      if (annotation instanceof RequestCode) {
        if (gotRequestCode) {
          throw parameterError(p,
              "Multiple @RequestCode annotations found on method and parameter.");
        }

        gotRequestCode = true;

        if (type == int.class) {
          return new ParameterHandler.RequestCode();
        } else {
          throw parameterError(p, "@RequestCode must be int type.");
        }
      }

      if (annotation instanceof Extra) {
        String name = ((Extra) annotation).value();
        if (TextUtils.isEmpty(name)
            && type != Bundle.class
            && type != Intent.class
            && type != Uri.class) {
          throw parameterError(p,
              "@Extra must be android.os.Bundle, android.content.Intent or android.net.Uri type. "
                  + "Otherwise @Extra must have a name like @Extra(\"id\")");
        }

        return new ParameterHandler.Extra<>(name, type);
      }

      /* no annotation parameter */
      return null;
    }

    private ParameterHandler<?> parseParameter(int p, Type parameterType,
        Annotation[] annotations) {
      ParameterHandler<?> result = null;
      for (Annotation annotation : annotations) {
        ParameterHandler<?> annotationAction =
            parseParameterAnnotation(p, parameterType, annotation);
        if (annotationAction == null) {
          continue;
        }

        if (result != null) {
          throw parameterError(p, "Multiple Routine annotations found, only one allowed.");
        }

        result = annotationAction;
      }

      if (result == null) {
        throw parameterError(p, "No Routine annotation found.");
      }

      return result;
    }

    private RuntimeException methodError(String message, Object... args) {
      return methodError(null, message, args);
    }

    private RuntimeException methodError(Throwable cause, String message, Object... args) {
      message = String.format(message, args);
      return new IllegalArgumentException(message
          + "\n for method "
          + method.getDeclaringClass().getSimpleName()
          + "."
          + method.getName(), cause);
    }

    private RuntimeException parameterError(int p, String message, Object... args) {
      return methodError(message + " (parameter #" + (p + 1) + ")", args);
    }
  }
}
