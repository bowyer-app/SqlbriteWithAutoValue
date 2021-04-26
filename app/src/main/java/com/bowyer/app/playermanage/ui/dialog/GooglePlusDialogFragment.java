package com.bowyer.app.playermanage.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bowyer.app.playermanage.BuildConfig;
import com.bowyer.app.playermanage.PlayerApplication;
import com.bowyer.app.playermanage.R;
import com.bowyer.app.playermanage.databinding.DialogGooglePlusBinding;
import com.bowyer.app.playermanage.preference.ReviewPreferences;

public class GooglePlusDialogFragment extends DialogFragment {

  private static final String KEY_COUNT = "key_count";
  private DialogGooglePlusBinding binding = null;

  public static GooglePlusDialogFragment newInstance(int count) {
    GooglePlusDialogFragment f = new GooglePlusDialogFragment();
    Bundle args = new Bundle();
    args.putInt(KEY_COUNT, count);
    f.setArguments(args);
    return f;
  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = DialogGooglePlusBinding.inflate(inflater, container, false);

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(R.string.google_plus_dialog_title);
    builder.setView(binding.getRoot());

    builder.setPositiveButton(android.R.string.cancel, (dialogInterface, i) -> {
      dismiss();
    });
    initGooglePlus();
    this.setCancelable(false);
    return binding.getRoot();
  }

  @Override public void onAttach(Context context) {
    PlayerApplication.getComponent(getContext()).inject(this);
    ReviewPreferences preferences = new ReviewPreferences(getActivity());
    int count = getArguments().getInt(KEY_COUNT, 0);
    preferences.reviewShowed(count);
    super.onAttach(context);
  }

  private void initGooglePlus() {
    String applicationId = BuildConfig.APPLICATION_ID.replace("." + BuildConfig.BUILD_TYPE, "");
    String url = "https://market.android.com/details?id=" + applicationId;
    int requestCode = 1;
    binding.plusOneButton.initialize(url, requestCode);
  }
}
