package com.bowyer.app.playermanage;

import android.app.Application;
import com.bowyer.app.playermanage.database.DbModule;
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
}
