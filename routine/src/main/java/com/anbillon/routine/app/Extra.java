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

package com.anbillon.routine.app;

import android.content.Intent;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Extended data added to the {@link Intent}. The value must include a package prefix, for example
 * the app com.android.contacts would use names like "com.android.contacts.ShowAll".
 * <p>
 * For example:
 * <pre><code>
 *   void navigateToContacts(@Extra("com.android.id") long id);
 * </code></pre>
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
@Documented @Target(PARAMETER) @Retention(RUNTIME) public @interface Extra {
  /**
   * The value of the extended data, with package prefix.
   */
  String value() default "";
}
