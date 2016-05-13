package com.bowyer.app.playermanage.logic;

import android.content.Context;
import android.content.Intent;
import com.bowyer.app.playermanage.BuildConfig;
import com.bowyer.app.playermanage.gcm.GrowthPushReceiver;
import com.growthpush.GrowthPush;
import com.growthpush.model.Environment;

public class GrowthPushLogic {

  public static final String SENDER_ID;

  public static final String TAG_ENABLE = "Enable";

  static {
    if (!BuildConfig.DEBUG && BuildConfig.BUILD_TYPE.equals("release")) {
      SENDER_ID = "325874332107";
    } else {
      SENDER_ID = "456119023145";
    }
  }

  public static void initGrowthPushSetting(final Context context, final Intent intent) {
    GrowthPush.getInstance()
        .requestRegistrationId(SENDER_ID,
            BuildConfig.DEBUG ? Environment.development : Environment.production);

    GrowthPush.getInstance().trackEvent(GrowthPushEvent.LAUNCH.name());
    GrowthPush.getInstance().setDeviceTags();
    //GrowthPush経由の起動かどうか
    if (GrowthPushReceiver.isLaunchFromGrowthPush(intent)) {
      trackEvent(GrowthPushEvent.LAUNCH_FROM_GROWTH_PUSH.name());
    }
  }

  public static void setTag(final String name, final boolean enable) {
    GrowthPush.getInstance().setTag(name, String.valueOf(enable));
  }

  public static void trackEvent(final String name) {
    GrowthPush.getInstance().trackEvent(name);
  }
}
