package com.bowyer.app.playermanage.logic;

import com.bowyer.app.playermanage.BuildConfig;
import com.bowyer.app.playermanage.PlayerApplication;
import com.growthbeat.Growthbeat;

public class GrowthbeatLogic {
  static final String APPLICATION_ID;
  static final String CREDENTIAL_ID = "Smlbhkv1XcirnPbK8c8lU3Am0eBvyCKo";

  static {
    if (!BuildConfig.DEBUG && BuildConfig.BUILD_TYPE.equals("release")) {
      APPLICATION_ID = "Pl5M0qKnnzDD1WnJ";
    } else {
      APPLICATION_ID = "Pl5LYV4q4i9lntjy";
    }
  }

  public static void init(final PlayerApplication app) {
    Growthbeat.getInstance().setLoggerSilent(!BuildConfig.DEBUG);
    Growthbeat.getInstance().initialize(app, APPLICATION_ID, CREDENTIAL_ID);
  }

  public static void start() {
    Growthbeat.getInstance().start();
  }

  public static void stop() {
    Growthbeat.getInstance().stop();
  }
}
