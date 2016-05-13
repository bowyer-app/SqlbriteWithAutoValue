package com.bowyer.app.playermanage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (!BuildConfig.DEBUG) {
      Fabric.with(this, new Crashlytics());
    }
    setContentView(R.layout.activity_main);
  }
}
