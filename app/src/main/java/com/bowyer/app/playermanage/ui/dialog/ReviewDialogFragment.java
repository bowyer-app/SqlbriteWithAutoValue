package com.bowyer.app.playermanage.ui.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import com.bowyer.app.playermanage.BuildConfig;
import com.bowyer.app.playermanage.PlayerApplication;
import com.bowyer.app.playermanage.R;
import com.bowyer.app.playermanage.preference.ReviewPreferences;
import com.bowyer.app.playermanage.ui.activity.WebViewActivity;

public class ReviewDialogFragment extends DialogFragment {

  private static final String KEY_COUNT = "key_count";

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

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(R.string.review_dialog_title);
    builder.setMessage(R.string.review_dialog_text);

    builder.setPositiveButton(R.string.review_dialog_review, (dialog, which) -> {
      String applicationId = BuildConfig.APPLICATION_ID.replace("." + BuildConfig.BUILD_TYPE, "");
      final Intent intent =
          new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + applicationId));
      getActivity().startActivity(intent);
      dismiss();
    });
    builder.setNeutralButton(R.string.review_dialog_demand, (dialog, which) -> {
      WebViewActivity.startWebViewActivity(getActivity(), "http://goo.gl/forms/bfUL4n0S4T");
      dismiss();
    });
    builder.setNegativeButton(R.string.review_dialog_cancel, (dialog, which) -> {
      dismiss();
    });

    return builder.create();
  }
}
