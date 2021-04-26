package com.bowyer.app.playermanage.ui.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import com.bowyer.app.playermanage.PlayerApplication;
import com.bowyer.app.playermanage.R;
import com.bowyer.app.playermanage.database.dao.PlayerDao;
import com.bowyer.app.playermanage.database.dto.Rank;
import com.bowyer.app.playermanage.database.dto.Sex;
import com.bowyer.app.playermanage.databinding.ActivityPlayerSearchBinding;
import com.bowyer.app.playermanage.gcm.GrowthPush;
import com.bowyer.app.playermanage.logic.GrowthPushLogic;
import com.bowyer.app.playermanage.logic.GrowthbeatLogic;
import com.bowyer.app.playermanage.logic.ReviewLogic;
import com.bowyer.app.playermanage.ui.adapter.PlayerAdapter;
import com.bowyer.app.playermanage.ui.dialog.RankSelectDialogFragment;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.squareup.sqlbrite.BriteDatabase;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity
    implements ObservableScrollViewCallbacks, RankSelectDialogFragment.OnRankSelectListener {

  @Inject BriteDatabase mDb;

  private PlayerAdapter mAdapter;
  private Subscription mSubscription;
  private ActivityPlayerSearchBinding binding = null;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_player_search);
    PlayerApplication.getComponent(getApplicationContext()).inject(this);
    GrowthPushLogic.initGrowthPushSetting(this, getIntent());
    launchWebIfHasGrowthPush();
    init();
    initRecyclerView();
    initSearchQuery();
    binding.header.sexGroup.setOnCheckedChangeListener((radioGroup, i) -> {
      doSearch();
    });
  }

  @Override protected void onResume() {
    super.onResume();
    doSearch();
    ReviewLogic.showReviewDialogIfNeed(this);
  }

  @Override protected void onStart() {
    super.onStart();
    GrowthbeatLogic.start();
  }

  @Override protected void onStop() {
    super.onStop();
    GrowthbeatLogic.stop();
  }

  @Override public void onDestroy() {
    super.onDestroy();
    if (mSubscription == null) {
      return;
    }
    mSubscription.unsubscribe();
  }

  private void init() {
    binding.header.expandActionButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        binding.header.expandLinear.toggle();
        binding.header.expandLinear.setListener(new ExpandableLayoutListenerAdapter() {
          @Override public void onOpened() {
            super.onOpened();
            final Animator rotateUpAnim = ObjectAnimator.ofPropertyValuesHolder(binding.header.icUp,
                PropertyValuesHolder.ofFloat(View.ROTATION, 0f, 180f));
            rotateUpAnim.setDuration(250L);
            rotateUpAnim.start();
          }

          @Override public void onClosed() {
            super.onClosed();
            final Animator rotateDownAnim =
                ObjectAnimator.ofPropertyValuesHolder(binding.header.icUp,
                    PropertyValuesHolder.ofFloat(View.ROTATION, 180f, 0f));
            rotateDownAnim.setDuration(250L);
            rotateDownAnim.start();
          }
        });
      }
    });
    binding.searchQueryDelete.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        binding.searchQueryText.setText("");
      }
    });
    binding.fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        startPlayerManage();
      }
    });
    binding.header.rank.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        onClickRank();
      }
    });
    binding.setting.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        onClickSetting();
      }
    });
    binding.privacy.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        onClickPrivacy();
      }
    });
  }

  private void startPlayerManage() {
    PlayerManageActivity.startActivity(this, null);
  }

  private void launchWebIfHasGrowthPush() {
    Intent intent = getIntent();
    if (intent.hasExtra(GrowthPush.KEY_GROWTH_PUSH)) {
      GrowthPush growthPush = intent.getParcelableExtra(GrowthPush.KEY_GROWTH_PUSH);
      String url = growthPush.url();
      if (!TextUtils.isEmpty(url)) {
        WebViewActivity.startWebViewActivity(this, url);
      }
    }
  }

  private void initRecyclerView() {
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    binding.recyclerView.setLayoutManager(linearLayoutManager);
    binding.recyclerView.setHasFixedSize(true);
    mAdapter = new PlayerAdapter(this);
    binding.recyclerView.setAdapter(mAdapter);
    binding.recyclerView.setScrollViewCallbacks(this);
  }

  private void initSearchQuery() {
    binding.searchQueryText.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (TextUtils.isEmpty(charSequence)) {
          binding.searchQueryDelete.setVisibility(View.INVISIBLE);
        } else {
          binding.searchQueryDelete.setVisibility(View.VISIBLE);
        }
        doSearch();
      }

      @Override public void afterTextChanged(Editable editable) {

      }
    });
  }

  private void doSearch() {
    String name = binding.searchQueryText.getText().toString();
    Sex sex = getCheckedSex(binding.header.sexGroup.getCheckedRadioButtonId());
    Rank rank = Rank.of(binding.header.rankText.getText().toString());
    PlayerDao.PlayerQuery playerQuery =
        PlayerDao.SQLBuilder.with().name(name).sex(sex).rank(rank).build();
    mSubscription = PlayerDao.getPlayersByQuery(mDb, playerQuery)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(mAdapter);
  }

  private Sex getCheckedSex(@IdRes int checkedSex) {
    switch (checkedSex) {
      case R.id.sex_male_rdb:
        return Sex.MALE;
      case R.id.sex_female_rdb:
        return Sex.FEMALE;
      default:
        return Sex.ALL;
    }
  }

  @Override public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

  }

  @Override public void onDownMotionEvent() {

  }

  @Override public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    if (binding.header.expandLinear.isExpanded()) {
      binding.header.expandLinear.collapse();
    }
    if (scrollState == ScrollState.UP) {
      binding.fab.hide();
    } else {
      binding.fab.show();
    }
  }

  private void onClickRank() {
    RankSelectDialogFragment.newInstance().show(getSupportFragmentManager(), "");
  }

  private void onClickSetting() {
    LicenseActivity.startActivity(this);
  }

  private void onClickPrivacy() {
    WebViewActivity.startWebViewActivity(this, "https://kyudo-bowyer.com/?p=656");
  }

  @Override public void onRankSelect(String rank) {
    if (!TextUtils.isEmpty(rank)) {
      binding.header.rankText.setText(rank);
    } else {
      binding.header.rankText.setText(getString(R.string.player_rank_all));
    }
    doSearch();
  }
}
