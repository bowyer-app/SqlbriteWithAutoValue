package com.bowyer.app.playermanage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

  @Inject AnalyticsTracker mAnalyticsTracker;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    PlayerApplication.getComponent(getApplicationContext()).inject(this);
  }

  @Override protected void onResume() {
    super.onResume();
    mAnalyticsTracker.sendScreenView(this.getLocalClassName());
  }
}
