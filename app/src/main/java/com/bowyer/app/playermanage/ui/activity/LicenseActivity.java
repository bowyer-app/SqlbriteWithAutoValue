package com.bowyer.app.playermanage.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bowyer.app.playermanage.AnalyticsTracker;
import com.bowyer.app.playermanage.PlayerApplication;
import com.bowyer.app.playermanage.R;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import net.yslibrary.licenseadapter.LicenseAdapter;
import net.yslibrary.licenseadapter.LicenseEntry;
import net.yslibrary.licenseadapter.Licenses;

public class LicenseActivity extends AppCompatActivity {
  @Inject AnalyticsTracker mAnalyticsTracker;

  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.recycler_view) RecyclerView mRecyclerView;

  public static void startActivity(Context context) {
    Intent intent = new Intent(context, LicenseActivity.class);
    context.startActivity(intent);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_license);
    ButterKnife.bind(this);
    initActionBar();
    initLicense();
    PlayerApplication.getComponent(this).inject(this);
  }

  @Override protected void onStart() {
    super.onStart();
    mAnalyticsTracker.sendScreenView(this.getLocalClassName());
  }

  public void initActionBar() {
    mToolbar.setTitle(getString(R.string.title_app_license));
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
  }

  private void initLicense() {
    mRecyclerView.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    List<LicenseEntry> licenses = new ArrayList<>();
    //Google
    licenses.add(Licenses.noContent("Android SDK", "Google Inc.",
        "https://developer.android.com/sdk/terms.html"));


    //DI
    licenses.add(Licenses.fromGitHub("google/dagger", Licenses.FILE_TXT));

    //Injection
    licenses.add(Licenses.fromGitHub("jakewharton/butterknife", Licenses.FILE_TXT));

    //UI
    licenses.add(Licenses.fromGitHub("ksoichiro/android-observablescrollview", Licenses.FILE_TXT));
    licenses.add(Licenses.fromGitHub("AAkira/ExpandableLayout", Licenses.FILE_NO_EXTENSION));

    //Log
    licenses.add(Licenses.fromGitHub("jakewharton/timber", Licenses.FILE_TXT));

    //DB
    licenses.add(Licenses.fromGitHub("square/sqlbrite", Licenses.FILE_TXT));

    //Parcel
    licenses.add(Licenses.fromGitHub("google/auto", Licenses.FILE_TXT));
    licenses.add(Licenses.fromGitHub("rharter/auto-value-parcel", Licenses.FILE_TXT));

    //Rx
    licenses.add(Licenses.fromGitHub("ReactiveX/RxAndroid", Licenses.FILE_NO_EXTENSION));
    licenses.add(Licenses.fromGitHub("ReactiveX/RxJava", Licenses.FILE_NO_EXTENSION));
    licenses.add(Licenses.fromGitHub("jakewharton/rxbinding", Licenses.FILE_TXT));

    //License
    licenses.add(Licenses.fromGitHub("yshrsmz/LicenseAdapter", Licenses.FILE_NO_EXTENSION));

    //Lambda
    licenses.add(Licenses.fromGitHub("orfjackal/retrolambda", Licenses.FILE_TXT));

    //Crash
    licenses.add(Licenses.noContent("Fabric", "Twitter", "https://fabric.io/terms"));

    LicenseAdapter adapter = new LicenseAdapter(licenses);
    mRecyclerView.setAdapter(adapter);

    Licenses.load(licenses);
  }

  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
    }
    return false;
  }
}
