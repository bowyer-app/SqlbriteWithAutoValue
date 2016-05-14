package com.bowyer.app.playermanage;

import com.bowyer.app.playermanage.ui.activity.LicenseActivity;
import com.bowyer.app.playermanage.ui.activity.MainActivity;
import com.bowyer.app.playermanage.ui.activity.PlayerManageActivity;
import dagger.Component;
import javax.inject.Singleton;

@Singleton @Component(modules = PlayerModule.class) public interface PlayerAppComponent {

  void inject(MainActivity activity);

  void inject(PlayerManageActivity activity);

  void inject(LicenseActivity activity);
}
