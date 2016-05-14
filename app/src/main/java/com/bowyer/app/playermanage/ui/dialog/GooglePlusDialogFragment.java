package com.bowyer.app.playermanage.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bowyer.app.playermanage.AnalyticsTracker;
import com.bowyer.app.playermanage.BuildConfig;
import com.bowyer.app.playermanage.PlayerApplication;
import com.bowyer.app.playermanage.R;
import com.bowyer.app.playermanage.preference.ReviewPreferences;
import com.google.android.gms.plus.PlusOneButton;
import javax.inject.Inject;

public class GooglePlusDialogFragment extends DialogFragment {

  private static final String KEY_COUNT = "key_count";

  @Bind(R.id.plus_one_button) PlusOneButton mPlusOneButton;
  @Inject AnalyticsTracker mAnalyticsTracker;

  public static GooglePlusDialogFragment newInstance(int count) {
    GooglePlusDialogFragment f = new GooglePlusDialogFragment();
    Bundle args = new Bundle();
    args.putInt(KEY_COUNT, count);
    f.setArguments(args);
    return f;
  }

  @Override public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    LayoutInflater layoutInflater =
        (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View content = layoutInflater.inflate(R.layout.dialog_google_plus, null);

    PlayerApplication.getComponent(getContext()).inject(this);
    ReviewPreferences preferences = new ReviewPreferences(getActivity());
    int count = getArguments().getInt(KEY_COUNT, 0);
    preferences.reviewShowed(count);

    mAnalyticsTracker.sendEvent(AnalyticsTracker.Category.GOOGLE_PLUS,
        AnalyticsTracker.Action.GOOGLE_PLUS_SHOW, String.valueOf(count));

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(R.string.google_plus_dialog_title);
    builder.setView(content);

    builder.setPositiveButton(android.R.string.cancel, (dialogInterface, i) -> {
      mAnalyticsTracker.sendEvent(AnalyticsTracker.Category.GOOGLE_PLUS,
          AnalyticsTracker.Action.GOOGLE_PLUS_CANCEL, String.valueOf(count));
      dismiss();
    });

    Dialog dialog = builder.create();
    ButterKnife.bind(this, content);
    initGooglePlus();
    this.setCancelable(false);
    return dialog;
  }

  private void initGooglePlus() {
    String applicationId = BuildConfig.APPLICATION_ID.replace("." + BuildConfig.BUILD_TYPE, "");
    String url = "https://market.android.com/details?id=" + applicationId;
    int requestCode = 1;
    mPlusOneButton.initialize(url, requestCode);
  }
}
