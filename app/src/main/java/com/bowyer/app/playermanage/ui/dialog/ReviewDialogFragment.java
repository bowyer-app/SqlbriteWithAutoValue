package com.bowyer.app.playermanage.ui.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import com.bowyer.app.playermanage.AnalyticsTracker;
import com.bowyer.app.playermanage.BuildConfig;
import com.bowyer.app.playermanage.PlayerApplication;
import com.bowyer.app.playermanage.R;
import com.bowyer.app.playermanage.preference.ReviewPreferences;
import com.bowyer.app.playermanage.ui.activity.WebViewActivity;
import javax.inject.Inject;

public class ReviewDialogFragment extends DialogFragment {

  private static final String KEY_COUNT = "key_count";

  @Inject AnalyticsTracker mAnalyticsTracker;

  public static ReviewDialogFragment newInstance(int count) {
    ReviewDialogFragment f = new ReviewDialogFragment();
    Bundle args = new Bundle();
    args.putInt(KEY_COUNT, count);
    f.setArguments(args);
    return f;
  }

  @Override public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    PlayerApplication.getComponent(getContext()).inject(this);
    ReviewPreferences preferences = new ReviewPreferences(getActivity());
    int count = getArguments().getInt(KEY_COUNT, 0);
    preferences.reviewShowed(count);

    mAnalyticsTracker.sendEvent(AnalyticsTracker.Category.REVIEW,
        AnalyticsTracker.Action.REVIEW_SHOW, String.valueOf(count));

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(R.string.review_dialog_title);
    builder.setMessage(R.string.review_dialog_text);

    builder.setPositiveButton(R.string.review_dialog_review, (dialog, which) -> {
      mAnalyticsTracker.sendEvent(AnalyticsTracker.Category.REVIEW,
          AnalyticsTracker.Action.REVIEW_OK, String.valueOf(count));
      String applicationId = BuildConfig.APPLICATION_ID.replace("." + BuildConfig.BUILD_TYPE, "");
      final Intent intent =
          new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + applicationId));
      getActivity().startActivity(intent);
      dismiss();
    });
    builder.setNeutralButton(R.string.review_dialog_demand, (dialog, which) -> {
      mAnalyticsTracker.sendEvent(AnalyticsTracker.Category.REVIEW,
          AnalyticsTracker.Action.REVIEW_QUESTIONATE, String.valueOf(count));
      WebViewActivity.startWebViewActivity(getActivity(), "http://goo.gl/forms/bfUL4n0S4T");
      dismiss();
    });
    builder.setNegativeButton(R.string.review_dialog_cancel, (dialog, which) -> {
      mAnalyticsTracker.sendEvent(AnalyticsTracker.Category.REVIEW,
          AnalyticsTracker.Action.REVIEW_CANCEL, String.valueOf(count));
      dismiss();
    });

    return builder.create();
  }
}
