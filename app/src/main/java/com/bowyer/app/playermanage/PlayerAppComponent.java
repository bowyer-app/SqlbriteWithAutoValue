package com.bowyer.app.playermanage;

import dagger.Component;
import javax.inject.Singleton;

@Singleton @Component(modules = PlayerModule.class) public interface PlayerAppComponent {

  void inject(MainActivity activity);
}
