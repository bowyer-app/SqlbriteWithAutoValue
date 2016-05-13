package com.bowyer.app.playermanage.gcm;

import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import com.google.auto.value.AutoValue;

@AutoValue public abstract class GrowthPush implements Parcelable {
  public static final String KEY_GROWTH_PUSH = "key_growth_push";
  public static final String TITLE = "title";
  public static final String MESSAGE = "message";
  public static final String URL = "url";

  public abstract String title();

  public abstract String message();

  public abstract String url();

  public static GrowthPush of(Bundle extras) {
    String title = getEmptyIfNull(extras.getString(TITLE));
    String message = getEmptyIfNull(extras.getString(MESSAGE));
    String url = getEmptyIfNull(extras.getString(URL));
    return new AutoValue_GrowthPush(title, message, url);
  }

  private static String getEmptyIfNull(String val) {
    if (TextUtils.isEmpty(val)) {
      return "";
    }
    return val;
  }
}
