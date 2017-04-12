package com.anbillon.routine.sample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.anbillon.routine.sample.Navigator;
import com.anbillon.routine.sample.R;
import com.anbillon.routine.sample.SampleApplication;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.anbillon.routine.sample.Navigator.PICK_IMAGE;

/**
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public final class MainFragment extends Fragment implements View.OnClickListener {
  private static final int REQUEST_CODE = 0x1209;
  private Navigator navigator;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_main, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    view.findViewById(R.id.btn_scheme_url).setOnClickListener(this);
    view.findViewById(R.id.btn_page_name).setOnClickListener(this);
    view.findViewById(R.id.btn_page).setOnClickListener(this);
    view.findViewById(R.id.btn_action).setOnClickListener(this);
    view.findViewById(R.id.btn_dynamic_page_name).setOnClickListener(this);
    view.findViewById(R.id.btn_scheme_filter).setOnClickListener(this);
    view.findViewById(R.id.btn_animation).setOnClickListener(this);
    view.findViewById(R.id.btn_html_scheme).setOnClickListener(this);
    view.findViewById(R.id.btn_not_found).setOnClickListener(this);
    navigator = ((SampleApplication) getActivity().getApplication()).navigator();
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    Log.d("TAG", "requestCode: " + requestCode);
  }

  @Override public void onClick(View v) {
    int id = v.getId();
    switch (id) {
      case R.id.btn_scheme_url:
        navigator.navigateToDemoWithSchemeUrl(getActivity());
        break;

      case R.id.btn_page_name:
        navigator.navigateToDemoWithPageName(getActivity(), "3", REQUEST_CODE);
        break;

      case R.id.btn_page:
        navigator.navigateToDemoWithPage(this).start();
        break;

      case R.id.btn_action:
        navigator.navigateWithAction(this, PICK_IMAGE);
        break;

      case R.id.btn_dynamic_page_name:
        navigator.navigateWithDynamicPageName(this, DemoActivity.class.getCanonicalName());
        break;

      case R.id.btn_scheme_filter:
        navigator.navigateWithFilters(getActivity());
        break;

      case R.id.btn_animation:
        navigator.navigateWithAnim(getActivity());
        break;

      case R.id.btn_html_scheme:
        navigator.navigateToHtml(getActivity());
        break;

      case R.id.btn_not_found:
        new Thread(new Runnable() {
          @Override public void run() {
            navigator.navigateWithNotFound(getActivity())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                  @Override public void call(Boolean result) {
                    Log.d("TAG", "result: " + result);
                  }
                });
          }
        }).start();

        break;
    }
  }
}
