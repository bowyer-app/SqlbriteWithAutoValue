package com.bowyer.app.playermanage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.bowyer.app.playermanage.logic.GrowthPushLogic;
import com.bowyer.app.playermanage.logic.GrowthbeatLogic;
import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

  @Inject AnalyticsTracker mAnalyticsTracker;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    PlayerApplication.getComponent(getApplicationContext()).inject(this);
    GrowthPushLogic.initGrowthPushSetting(this, getIntent());
  }

  @Override protected void onResume() {
    super.onResume();
    mAnalyticsTracker.sendScreenView(this.getLocalClassName());
  }

  @Override protected void onStart() {
    super.onStart();
    GrowthbeatLogic.start();
  }

  @Override protected void onStop() {
    super.onStop();
    GrowthbeatLogic.stop();
  }
}
