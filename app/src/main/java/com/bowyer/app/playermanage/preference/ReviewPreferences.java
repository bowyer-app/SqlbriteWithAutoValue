package com.bowyer.app.playermanage.preference;

import android.content.Context;

public class ReviewPreferences extends PreferenceHelper {

  private static final String REVIEW_PREFERENCES = "review_preferences";
  private static final String KEY_PLAYER_SAVE_COUNT = "key_player_save_count";

  public ReviewPreferences(Context context) {
    super(context, REVIEW_PREFERENCES, Context.MODE_PRIVATE);
  }

  public int getPlayerSaveCount() {
    return getInt(KEY_PLAYER_SAVE_COUNT, 0);
  }

  public void increasePlayerSaveCount() {
    int addCount = getPlayerSaveCount() + 1;
    putInt(KEY_PLAYER_SAVE_COUNT, addCount);
  }

  public boolean isShowedReview(int count) {
    return getBoolean(KEY_PLAYER_SAVE_COUNT + count, false);
  }

  public void reviewShowed(int count) {
    putBoolean(KEY_PLAYER_SAVE_COUNT + count, true);
  }
}
