package com.bowyer.app.playermanage.logic;

import android.support.v7.app.AppCompatActivity;
import com.bowyer.app.playermanage.preference.ReviewPreferences;
import com.bowyer.app.playermanage.ui.dialog.GooglePlusDialogFragment;
import com.bowyer.app.playermanage.ui.dialog.ReviewDialogFragment;

public class ReviewLogic {

  private static final int REVIEW_COUNT_1 = 10;
  private static final int GOOGLE_COUNT_1 = 15;

  public static void showReviewDialogIfNeed(AppCompatActivity activity) {
    ReviewPreferences preferences = new ReviewPreferences(activity);
    int count = preferences.getPlayerSaveCount();
    switch (count) {
      case REVIEW_COUNT_1:
        showDialog(activity, count, preferences);
        break;
      case GOOGLE_COUNT_1:
        showGoogleDialog(activity, count, preferences);
        break;
      default:
        break;
    }
  }

  private static void showDialog(final AppCompatActivity activity, final int count,
      final ReviewPreferences preferences) {
    if (preferences.isShowedReview(count)) {
      return;
    }
    ReviewDialogFragment.newInstance(count).show((activity).getSupportFragmentManager(), "");
  }

  private static void showGoogleDialog(AppCompatActivity activity, int count,
      ReviewPreferences preferences) {
    if (preferences.isShowedReview(count)) {
      return;
    }
    GooglePlusDialogFragment.newInstance(count).show((activity).getSupportFragmentManager(), "");
  }
}
