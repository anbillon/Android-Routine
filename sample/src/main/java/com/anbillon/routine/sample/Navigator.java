package com.anbillon.routine.sample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import com.anbillon.routine.Router;
import com.anbillon.routine.app.Action;
import com.anbillon.routine.app.Anim;
import com.anbillon.routine.app.Caller;
import com.anbillon.routine.app.Extra;
import com.anbillon.routine.app.ExtraSet;
import com.anbillon.routine.app.Flags;
import com.anbillon.routine.app.Page;
import com.anbillon.routine.app.PageName;
import com.anbillon.routine.app.RequestCode;
import com.anbillon.routine.app.SchemeUrl;
import com.anbillon.routine.sample.ui.AnimActivity;
import com.anbillon.routine.sample.ui.DemoActivity;
import com.anbillon.routine.sample.ui.HtmlActivity;
import rx.Observable;

/**
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public interface Navigator {
  String EXTRA_ID = "com.anbillon.EXTRA_ID";
  Uri PICK_IMAGE = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
  Uri BROWSER = Uri.parse("http://www.baidu.com");

  /**
   * Navigate to {@link DemoActivity} with scheme url.
   *
   * @param context context to use
   */
  @SchemeUrl("demo://test/login?id=2") void navigateToDemoWithSchemeUrl(@Caller Context context);

  /**
   * Navigate to {@link DemoActivity} with activity page value.
   *
   * @param context context to use
   */
  @PageName("com.anbillon.sample.ui.DemoActivity") @Flags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
  void navigateToDemoWithPageName(@Caller Context context, @Extra(EXTRA_ID) String id,
      @RequestCode int requestCode);

  @SchemeUrl("demo://test/check") void navigateWithFilters(@Caller Context context);

  /**
   * Navigate to {@link DemoActivity} with activity page.
   */
  @Page(DemoActivity.class) Router navigateToDemoWithPage(@Caller Fragment fragment);

  /**
   * Navigate to a page but not found.
   *
   * @param context context to use
   */
  @PageName("com.anbillon.DemoActivity") Observable<Boolean> navigateWithNotFound(
      @Caller Context context);

  /**
   * Navigate with animation.
   *
   * @param context activity context to use
   */
  @Page(AnimActivity.class) @Anim(enter = R.anim.enter) void navigateWithAnim(
      @Caller Context context);

  /**
   * Navigate to {@link HtmlActivity} with activity page.
   *
   * @param context context to use
   */
  @Page(HtmlActivity.class) void navigateToHtml(@Caller Context context);

  /**
   * Navigate with dynamic scheme url.
   *
   * @param context context to use
   * @param url dynamic scheme url
   */
  void navigateWithDynamicSchemeUrl(@Caller Context context, @SchemeUrl String url);

  /**
   * Navigate with dynamic page name.
   *
   * @param fragment fragment to use
   * @param pageName page name
   */
  void navigateWithDynamicPageName(@Caller Fragment fragment, @PageName String pageName);

  /**
   * Navigate with action.
   *
   * @param fragment fragment to use
   * @param data data
   */
  @Action(Intent.ACTION_VIEW) void navigateWithAction(@Caller Fragment fragment,
      @ExtraSet Uri data);
}
