package com.bowyer.app.playermanage.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bowyer.app.playermanage.AnalyticsTracker;
import com.bowyer.app.playermanage.PlayerApplication;
import com.bowyer.app.playermanage.R;
import com.bowyer.app.playermanage.database.dto.Player;
import com.bowyer.app.playermanage.database.dto.Sex;
import com.bowyer.app.playermanage.preference.ReviewPreferences;
import com.bowyer.app.playermanage.ui.dialog.RankSelectDialogFragment;
import com.squareup.sqlbrite.BriteDatabase;
import javax.inject.Inject;

public class PlayerManageActivity extends AppCompatActivity
    implements RankSelectDialogFragment.OnRankSelectListener {

  public static final String KEY_PLAYER = "key_player";

  @Inject BriteDatabase mDb;
  @Inject AnalyticsTracker mAnalyticsTracker;

  private Player mPlayer;

  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.first_name) EditText mFirstName;
  @Bind(R.id.last_name) EditText mLastName;
  @Bind(R.id.first_name_phonetic) EditText mFirstNamePhonetic;
  @Bind(R.id.last_name_phonetic) EditText mLastNamePhonetic;
  @Bind(R.id.memo) EditText mMemo;
  @Bind(R.id.male_rdb) RadioButton mMaleRadioButton;
  @Bind(R.id.female_rdb) RadioButton mFeMaleRadioButton;
  @Bind(R.id.rank_text) TextView mRankText;

  public static void startActivity(Context context, Player player) {
    Intent intent = new Intent(context, PlayerManageActivity.class);
    intent.putExtra(KEY_PLAYER, player);
    context.startActivity(intent);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player_manage);
    PlayerApplication.getComponent(getApplicationContext()).inject(this);
    ButterKnife.bind(this);
    initActionBar();
    initPlayer();
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

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate menu resource file.
    getMenuInflater().inflate(R.menu.menu_player_manage, menu);
    return super.onCreateOptionsMenu(menu);
  }

  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
      case R.id.action_save:
        onClickSave();
        return true;
    }
    return false;
  }

  private void initPlayer() {
    mPlayer = getIntent().getParcelableExtra(KEY_PLAYER);
    if (mPlayer == null) {
      return;
    }
    mFirstName.setText(mPlayer.firstName());
    mLastName.setText(mPlayer.lastName());
    mFirstNamePhonetic.setText(mPlayer.firstNamePhonetic());
    mLastNamePhonetic.setText(mPlayer.lastNamePhonetic());
    mMemo.setText(mPlayer.memo());
    mRankText.setText(mPlayer.rank());
    if (mPlayer.sex() == Sex.MALE.getSex()) {
      mMaleRadioButton.setChecked(true);
    } else {
      mFeMaleRadioButton.setChecked(true);
    }
  }

  private void onClickSave() {
    if (TextUtils.isEmpty(mLastName.getText().toString())) {
      Toast.makeText(this, getString(R.string.message_need_first_name), Toast.LENGTH_SHORT).show();
      return;
    }
    if (mPlayer == null) {
      insertPlayer();
      mAnalyticsTracker.sendEvent(AnalyticsTracker.Category.DEFAULT,
          AnalyticsTracker.Action.PLAYER_SAVE, "insert");
    } else {
      updatePlayer();
      mAnalyticsTracker.sendEvent(AnalyticsTracker.Category.DEFAULT,
          AnalyticsTracker.Action.PLAYER_SAVE, "update");
    }
  }

  @OnClick(R.id.rank) void onClickRank() {
    RankSelectDialogFragment.newInstance().show(getSupportFragmentManager(), "");
  }

  private void updatePlayer() {
    int sex = mMaleRadioButton.isChecked() ? Sex.MALE.getSex() : Sex.FEMALE.getSex();
    mDb.update(Player.TABLE, new Player.Builder().firstName(mFirstName.getText().toString())
        .lastName(mLastName.getText().toString())
        .firstNamePhonetic(mFirstNamePhonetic.getText().toString())
        .lastNamePhonetic(mLastNamePhonetic.getText().toString())
        .sex(sex)
        .memo(mMemo.getText().toString())
        .rank(mRankText.getText().toString())
        .build(), Player.ID + " = ?", String.valueOf(mPlayer.id()));
    finish();
  }

  private void insertPlayer() {
    int sex = mMaleRadioButton.isChecked() ? Sex.MALE.getSex() : Sex.FEMALE.getSex();
    mDb.insert(Player.TABLE, new Player.Builder().firstName(mFirstName.getText().toString())
        .lastName(mLastName.getText().toString())
        .firstNamePhonetic(mFirstNamePhonetic.getText().toString())
        .lastNamePhonetic(mLastNamePhonetic.getText().toString())
        .sex(sex)
        .memo(mMemo.getText().toString())
        .rank(mRankText.getText().toString())
        .build());
    finish();
    new ReviewPreferences(this).increasePlayerSaveCount();
  }

  @Override public void onRankSelect(String rank) {
    if (!TextUtils.isEmpty(rank)) {
      mRankText.setText(rank);
    } else {
      mRankText.setText(getString(R.string.player_no_rank));
    }
  }
}
