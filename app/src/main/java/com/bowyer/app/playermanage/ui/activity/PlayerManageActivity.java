package com.bowyer.app.playermanage.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.bowyer.app.playermanage.PlayerApplication;
import com.bowyer.app.playermanage.R;
import com.bowyer.app.playermanage.database.dto.Player;
import com.bowyer.app.playermanage.database.dto.Sex;
import com.bowyer.app.playermanage.databinding.ActivityPlayerManageBinding;
import com.bowyer.app.playermanage.preference.ReviewPreferences;
import com.bowyer.app.playermanage.ui.dialog.RankSelectDialogFragment;
import com.squareup.sqlbrite.BriteDatabase;
import javax.inject.Inject;

public class PlayerManageActivity extends AppCompatActivity
    implements RankSelectDialogFragment.OnRankSelectListener {

  public static final String KEY_PLAYER = "key_player";

  @Inject BriteDatabase mDb;

  private Player mPlayer;

  private ActivityPlayerManageBinding binding = null;

  public static void startActivity(Context context, Player player) {
    Intent intent = new Intent(context, PlayerManageActivity.class);
    intent.putExtra(KEY_PLAYER, player);
    context.startActivity(intent);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_player_manage);
    PlayerApplication.getComponent(getApplicationContext()).inject(this);
    initActionBar();
    initPlayer();
    binding.rank.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        onClickRank();
      }
    });
  }

  private void initActionBar() {
    binding.toolbar.setTitle(getString(R.string.title_player_manage));
    setSupportActionBar(binding.toolbar);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
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

    binding.firstName.setText(mPlayer.firstName());
    binding.lastName.setText(mPlayer.lastName());
    binding.firstNamePhonetic.setText(mPlayer.firstNamePhonetic());
    binding.lastNamePhonetic.setText(mPlayer.lastNamePhonetic());
    binding.memo.setText(mPlayer.memo());
    binding.rankText.setText(mPlayer.rank());
    if (mPlayer.sex() == Sex.MALE.getSex()) {
      binding.maleRdb.setChecked(true);
    } else {
      binding.femaleRdb.setChecked(true);
    }
  }

  private void onClickSave() {
    if (TextUtils.isEmpty(binding.lastName.getText().toString())) {
      Toast.makeText(this, getString(R.string.message_need_first_name), Toast.LENGTH_SHORT).show();
      return;
    }
    if (mPlayer == null) {
      insertPlayer();
    } else {
      updatePlayer();
    }
  }

  private void onClickRank() {
    RankSelectDialogFragment.newInstance().show(getSupportFragmentManager(), "");
  }

  private void updatePlayer() {
    int sex = binding.maleRdb.isChecked() ? Sex.MALE.getSex() : Sex.FEMALE.getSex();
    mDb.update(Player.TABLE,
        new Player.ContentsBuilder().firstName(binding.firstName.getText().toString())
            .lastName(binding.lastName.getText().toString())
            .firstNamePhonetic(binding.firstNamePhonetic.getText().toString())
            .lastNamePhonetic(binding.lastNamePhonetic.getText().toString())
            .sex(sex)
            .memo(binding.memo.getText().toString())
            .rank(binding.rankText.getText().toString())
            .build(), Player.ID + " = ?", String.valueOf(mPlayer.id()));
    finish();
  }

  private void insertPlayer() {
    int sex = binding.maleRdb.isChecked() ? Sex.MALE.getSex() : Sex.FEMALE.getSex();
    mDb.insert(Player.TABLE,
        new Player.ContentsBuilder().firstName(binding.firstName.getText().toString())
            .lastName(binding.lastName.getText().toString())
            .firstNamePhonetic(binding.firstNamePhonetic.getText().toString())
            .lastNamePhonetic(binding.lastNamePhonetic.getText().toString())
            .sex(sex)
            .memo(binding.memo.getText().toString())
            .rank(binding.rankText.getText().toString())
            .build());
    finish();
    new ReviewPreferences(this).increasePlayerSaveCount();
  }

  @Override public void onRankSelect(String rank) {
    if (!TextUtils.isEmpty(rank)) {
      binding.rankText.setText(rank);
    } else {
      binding.rankText.setText(getString(R.string.player_no_rank));
    }
  }
}
