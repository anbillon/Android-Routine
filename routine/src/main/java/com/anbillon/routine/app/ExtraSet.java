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
 * A set of extended data added to the {@link Intent}. The value and keys must include a package
 * prefix, for example the app com.android.contacts would use keys like "com.android.contacts.ID".
 * <p>
 * Simple example:
 * <pre><code>
 *   void navigateToContacts(@ExtraSet("com.android.IDS") Bundle ids);
 * </code></pre>
 * If there's only one set of extended data, then you can just ignore:
 * <pre><code>
 * void navigateToContacts(@ExtraSet Bundle ids);
 * void navigateToContacts(@ExtraSet("com.android.ORIGIN") Intent intent);
 * </code></pre>
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 * @see Extra
 */
@Documented @Target(PARAMETER) @Retention(RUNTIME) public @interface ExtraSet {
  /**
   * The value of the extended data, with package prefix.
   */
  String name() default "";
}
