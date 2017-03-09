package com.anbillon.routine;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

/**
 * A context to resolve different resolver.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public interface Resolver {
  String callerName();

  Context context();

  void startActivity(Intent intent, int enterAnim, int exitAnim) throws ActivityNotFoundException;

  void startActivityForResult(Intent intent, int requestCode, int enterAnim, int exitAnim)
      throws ActivityNotFoundException;

  abstract class Factory {

    public <T> Resolver create(T caller) {
      return null;
    }
  }
}
