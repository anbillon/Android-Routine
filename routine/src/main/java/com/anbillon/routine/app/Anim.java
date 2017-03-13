package com.anbillon.routine.app;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Animation for new page. Enter and exit animation can be set. Use 0 for no animation.
 * <p>
 * For example:
 * <pre><code>
 * {@code} @Anim(enter = R.anim.enter, exit = R.anim.exit)
 * {@code} void navigateToDemo();
 * </code></pre>
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
@Documented @Target(METHOD) @Retention(RUNTIME) public @interface Anim {
  /**
   * Enter animation resource id.
   */
  int enter() default 0;

  /**
   * Exit animation resource id.
   */
  int exit() default 0;
}
