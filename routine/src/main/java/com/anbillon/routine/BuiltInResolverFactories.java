package com.anbillon.routine;

import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

/**
 * Built in resolver resolver factories.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class BuiltInResolverFactories extends Resolver.Factory {

  @Override public <T> Resolver create(T caller) {
    if (caller instanceof Fragment) {
      return new FragmentResolver((Fragment) caller);
    } else if (caller instanceof Activity) {
      return new ActivityResolver((Activity) caller);
    } else if (caller instanceof Context) {
      return new ContextResolver((Context) caller);
    }

    return super.create(caller);
  }

  static final class ActivityResolver implements Resolver {
    private final Activity activity;

    public ActivityResolver(Activity activity) {
      this.activity = activity;
    }

    @Override public String callerName() {
      return activity.getClass().getCanonicalName();
    }

    @Override public Context context() {
      return activity;
    }

    @Override public void startActivity(Intent intent, int enterAnim, int exitAnim)
        throws ActivityNotFoundException {
      activity.startActivity(intent);
      activity.overridePendingTransition(enterAnim, exitAnim);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, int enterAnim, int exitAnim)
        throws ActivityNotFoundException {
      activity.startActivityForResult(intent, requestCode);
      activity.overridePendingTransition(enterAnim, exitAnim);
    }
  }

  static final class FragmentResolver implements Resolver {
    private final Fragment fragment;

    public FragmentResolver(Fragment fragment) {
      this.fragment = fragment;
    }

    @Override public String callerName() {
      return fragment.getClass().getCanonicalName();
    }

    @Override public Context context() {
      return fragment.getActivity();
    }

    @Override public void startActivity(Intent intent, int enterAnim, int exitAnim)
        throws ActivityNotFoundException {
      fragment.startActivity(intent);
      fragment.getActivity().overridePendingTransition(enterAnim, exitAnim);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, int enterAnim, int exitAnim)
        throws ActivityNotFoundException {
      fragment.startActivityForResult(intent, requestCode);
      fragment.getActivity().overridePendingTransition(enterAnim, exitAnim);
    }
  }

  static final class ContextResolver implements Resolver {
    private final Context context;

    public ContextResolver(Context context) {
      this.context = context;
    }

    @Override public String callerName() {
      return context.getClass().getCanonicalName();
    }

    @Override public Context context() {
      return context;
    }

    @Override public void startActivity(Intent intent, int enterAnim, int exitAnim)
        throws ActivityNotFoundException {
      context.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, int enterAnim, int exitAnim)
        throws ActivityNotFoundException {
      throw new RuntimeException("Unsopported method for this type of resolver");
    }
  }
}
