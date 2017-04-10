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

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Open a new page via scheme. The scheme should be configured in AndroidManifest.xml for specified
 * page. If {@link Page} or {@link PageName} was also set, then scheme will grant high priority.
 * <p>
 * The format of scheme should like this:
 * <pre><code>
 * {@code @SchemeUrl("scheme://[host]/[value]?[parameters]")}
 * </code></pre>
 * And parameters should be 'key=value'.
 * </p>
 * <p>
 * For example:
 * <pre><code>
 * {@code} @SchemeUrl("demo://test/login?id=2")
 * {@code} void navigateToDemo();
 * </code></pre>
 * And then add the scheme in AndroidManifest.xml:
 * <pre><code>
 * &lt;activity android:value=".DemoActivity"&gt;
 *   &lt;intent-filter&gt;
 *    &lt;action android:value="android.intent.action.VIEW"/&gt
 *
 *    &lt;category android:value="android.intent.category.DEFAULT"/&gt
 *
 *    &lt;category android:value="android.intent.category.BROWSABLE"/&gt
 *
 *    &lt;data
 *        android:scheme="demo"
 *        android:host="test"
 *        android:value="/login?id=2"/&gt;
 *    &lt;/intent-filter&gt;
 * &lt;/activity&gt;
 * </code></pre>
 * </p>
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 * @see Page
 * @see PageName
 */
@Documented @Target({ METHOD, PARAMETER }) @Retention(RUNTIME) public @interface SchemeUrl {
  /**
   * The full value of scheme.
   */
  String value() default "";
}
