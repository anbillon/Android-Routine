package com.anbillon.routine.app;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * General action to be performed. Application-specific actions should be prefixed with the
 * vendor's package name. Normally it's necessary if you want to start intent with action such as
 * ACTION_VIEW.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
@Documented @Target({ METHOD, PARAMETER }) @Retention(RUNTIME) public @interface Action {
  /**
   * General action to be performed.
   */
  String value() default "";
}
