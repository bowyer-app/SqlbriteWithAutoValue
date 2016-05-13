package com.bowyer.app.playermanage.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bowyer.app.playermanage.AnalyticsTracker;
import com.bowyer.app.playermanage.PlayerApplication;
import com.bowyer.app.playermanage.R;
import javax.inject.Inject;

public class PlayerManageActivity extends AppCompatActivity {

  @Inject AnalyticsTracker mAnalyticsTracker;

  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.first_name) EditText mFirstName;
  @Bind(R.id.last_name) EditText mLastName;
  @Bind(R.id.first_name_phonetic) EditText mFirstNamePhonetic;
  @Bind(R.id.last_name_phonetic) EditText mLastNamePhonetic;
  @Bind(R.id.memo) EditText mMemo;
  @Bind(R.id.male_rdb) RadioButton mMaleRadioButton;
  @Bind(R.id.female_rdb) RadioButton mFeMaleRadioButton;
  @Bind(R.id.rank_text) TextView mRankText;

  public static void startActivity(Context context) {
    Intent intent = new Intent(context, PlayerManageActivity.class);
    context.startActivity(intent);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player_manage);
    PlayerApplication.getComponent(getApplicationContext()).inject(this);
    ButterKnife.bind(this);
    initActionBar();
  }

  @Override protected void onResume() {
    super.onResume();
    mAnalyticsTracker.sendScreenView(this.getLocalClassName());
  }

  private void initActionBar() {
    mToolbar.setTitle(getString(R.string.title_player_manage));
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
  }
}
