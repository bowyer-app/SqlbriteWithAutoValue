package com.bowyer.app.playermanage;

import android.app.Application;
import android.content.Context;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class PlayerApplication extends Application {
  private PlayerAppComponent playerAppComponent;

  @Override public void onCreate() {
    super.onCreate();
    if (!BuildConfig.DEBUG) {
      Fabric.with(this, new Crashlytics());
    }
    playerAppComponent = DaggerPlayerAppComponent.builder()
        .playerModule(new PlayerModule(this)).build();
  }

  public static PlayerAppComponent getComponent(Context context) {
    return ((PlayerApplication) context.getApplicationContext()).playerAppComponent;
  }
}
