package com.bowyer.app.playermanage;

import android.app.Application;
import com.bowyer.app.playermanage.database.DbModule;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(includes = { DbModule.class }) public final class PlayerModule {

  private final Application application;

  PlayerModule(Application application) {
    this.application = application;
  }

  @Provides @Singleton Application provideApplication() {
    return application;
  }

  @Singleton @Provides public Tracker provideGoogleAnalyticsTracker(Application application) {
    GoogleAnalytics ga = GoogleAnalytics.getInstance(application);
    Tracker tracker = ga.newTracker(BuildConfig.GA_TRACKING_ID);
    tracker.enableAdvertisingIdCollection(true);
    tracker.enableExceptionReporting(true);
    return tracker;
  }
}
