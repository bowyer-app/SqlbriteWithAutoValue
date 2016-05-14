package com.bowyer.app.playermanage;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import com.bowyer.app.playermanage.logic.GrowthbeatLogic;
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
    GrowthbeatLogic.init(this);
  }

  public static PlayerAppComponent getComponent(Context context) {
    return ((PlayerApplication) context.getApplicationContext()).playerAppComponent;
  }

  @Override protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }
}
