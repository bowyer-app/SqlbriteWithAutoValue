package com.bowyer.app.playermanage.ui.activity;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.MenuItem;
import com.bowyer.app.playermanage.PlayerApplication;
import com.bowyer.app.playermanage.R;
import com.bowyer.app.playermanage.databinding.ActivityLicenseBinding;
import java.util.ArrayList;
import java.util.List;
import net.yslibrary.licenseadapter.LicenseAdapter;
import net.yslibrary.licenseadapter.LicenseEntry;
import net.yslibrary.licenseadapter.Licenses;

public class LicenseActivity extends AppCompatActivity {

  private ActivityLicenseBinding binding = null;

  public static void startActivity(Context context) {
    Intent intent = new Intent(context, LicenseActivity.class);
    context.startActivity(intent);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_license);
    initActionBar();
    initLicense();
    PlayerApplication.getComponent(this).inject(this);
  }

  public void initActionBar() {
    binding.toolbar.setTitle(getString(R.string.title_app_license));
    setSupportActionBar(binding.toolbar);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
  }

  private void initLicense() {
    binding.recyclerView.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    List<LicenseEntry> licenses = new ArrayList<>();
    //Google
    licenses.add(Licenses.noContent("Android SDK", "Google Inc.",
        "https://developer.android.com/sdk/terms.html"));

    //DI
    licenses.add(Licenses.fromGitHub("google/dagger", Licenses.FILE_TXT));

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

    LicenseAdapter adapter = new LicenseAdapter(licenses);
    binding.recyclerView.setAdapter(adapter);

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
