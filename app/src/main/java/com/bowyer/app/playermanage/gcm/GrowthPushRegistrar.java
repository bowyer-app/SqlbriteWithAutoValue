package com.bowyer.app.playermanage.gcm;

import android.content.Context;
import android.content.Intent;
import com.growthpush.GrowthPush;
import com.growthpush.model.Environment;

public class GrowthPushRegistrar {
  private GrowthPushRegistrar() {
  }

  public static void handleRegistration(final Context context, final Intent intent) {

    if (intent.getExtras().containsKey("error")) {
      GrowthPush.getInstance()
          .getLogger()
          .error(
              String.format("GCM Registration failed. %s", intent.getExtras().getString("error")));
    }

    if (intent.getExtras().containsKey("registration_id")) {
      GrowthPush.getInstance()
          .registerClient(intent.getExtras().getString("registration_id"), Environment.production);
    }
  }
}
