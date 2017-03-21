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

import android.app.Activity;
import android.content.Context;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The caller which will open the given page. The value must be {@link Context} or it's child
 * class which is able to open {@link Activity}.
 * <p>
 * For example:
 * <pre><code>
 * void navigateToDemo(@Caller Context context);
 * </code></pre>
 * </p>
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
@Documented @Target(PARAMETER) @Retention(RUNTIME) public @interface Caller {
}
