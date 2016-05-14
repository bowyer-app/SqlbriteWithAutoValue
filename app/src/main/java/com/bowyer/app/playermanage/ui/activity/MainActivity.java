package com.bowyer.app.playermanage.ui.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bowyer.app.playermanage.AnalyticsTracker;
import com.bowyer.app.playermanage.PlayerApplication;
import com.bowyer.app.playermanage.R;
import com.bowyer.app.playermanage.database.dao.PlayerDao;
import com.bowyer.app.playermanage.database.dto.Player;
import com.bowyer.app.playermanage.database.dto.Rank;
import com.bowyer.app.playermanage.database.dto.Sex;
import com.bowyer.app.playermanage.logic.GrowthPushLogic;
import com.bowyer.app.playermanage.logic.GrowthbeatLogic;
import com.bowyer.app.playermanage.ui.adapter.PlayerAdapter;
import com.bowyer.app.playermanage.ui.dialog.RankSelectDialogFragment;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.squareup.sqlbrite.BriteDatabase;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity
    implements ObservableScrollViewCallbacks, RankSelectDialogFragment.OnRankSelectListener {

  @Inject BriteDatabase mDb;
  @Inject AnalyticsTracker mAnalyticsTracker;

  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.recycler_view) ObservableRecyclerView mRecyclerView;
  @Bind(R.id.expand_linear) ExpandableRelativeLayout mExpandableHeader;
  @Bind(R.id.ic_up) ImageView mIcUp;
  @Bind(R.id.search_query_text) EditText mSearchQueryText;
  @Bind(R.id.search_query_delete) ImageButton mSearchWordDelete;
  @Bind(R.id.fab) FloatingActionButton mFab;
  @Bind(R.id.sex_group) RadioGroup mSexGroup;
  @Bind(R.id.rank_text) TextView mRankText;

  private PlayerAdapter mAdapter;
  private Subscription mSubscription;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player_search);
    ButterKnife.bind(this);
    PlayerApplication.getComponent(getApplicationContext()).inject(this);
    GrowthPushLogic.initGrowthPushSetting(this, getIntent());

    initRecyclerView();
    initSearchQuery();
    mSexGroup.setOnCheckedChangeListener((radioGroup, i) -> {
      mAnalyticsTracker.sendEvent(AnalyticsTracker.Category.DEFAULT,
          AnalyticsTracker.Action.PLAYER_SEX_TAP, getCheckedSex(i).name());
      doSearch();
    });
  }

  @Override protected void onResume() {
    super.onResume();
    doSearch();
  }

  @Override protected void onStart() {
    super.onStart();
    mAnalyticsTracker.sendScreenView(this.getLocalClassName());
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
    ButterKnife.unbind(this);
  }

  private void initRecyclerView() {
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    mRecyclerView.setLayoutManager(linearLayoutManager);
    mRecyclerView.setHasFixedSize(true);
    mAdapter = new PlayerAdapter(this);
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.setScrollViewCallbacks(this);
  }

  private void initSearchQuery() {
    mSearchQueryText.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (TextUtils.isEmpty(charSequence)) {
          mSearchWordDelete.setVisibility(View.INVISIBLE);
        } else {
          mSearchWordDelete.setVisibility(View.VISIBLE);
        }
        doSearch();
      }

      @Override public void afterTextChanged(Editable editable) {

      }
    });
  }

  @OnClick(R.id.expand_action_button) void headerTranslation() {
    mAnalyticsTracker.sendEvent(AnalyticsTracker.Category.DEFAULT,
        AnalyticsTracker.Action.EXPAND_SEARCH_PLAYER);
    mExpandableHeader.toggle();
    mExpandableHeader.setListener(new ExpandableLayoutListenerAdapter() {
      @Override public void onOpened() {
        super.onOpened();
        final Animator rotateUpAnim = ObjectAnimator.ofPropertyValuesHolder(mIcUp,
            PropertyValuesHolder.ofFloat(View.ROTATION, 0f, 180f));
        rotateUpAnim.setDuration(250L);
        rotateUpAnim.start();
      }

      @Override public void onClosed() {
        super.onClosed();
        final Animator rotateDownAnim = ObjectAnimator.ofPropertyValuesHolder(mIcUp,
            PropertyValuesHolder.ofFloat(View.ROTATION, 180f, 0f));
        rotateDownAnim.setDuration(250L);
        rotateDownAnim.start();
      }
    });
  }

  @OnClick(R.id.search_query_text) void searchQueryTextClick() {
    mAnalyticsTracker.sendEvent(AnalyticsTracker.Category.DEFAULT,
        AnalyticsTracker.Action.SEARCH_PLAYER_TAP);
  }

  @OnClick(R.id.search_query_delete) void deleteSearchQuery() {
    mSearchQueryText.setText("");
  }

  private void doSearch() {
    String name = mSearchQueryText.getText().toString();
    Sex sex = getCheckedSex(mSexGroup.getCheckedRadioButtonId());
    Rank rank = Rank.of(mRankText.getText().toString());
    PlayerDao.PlayerQuery playerQuery =
        new PlayerDao.SQLBuilder().name(name).sex(sex).rank(rank).build();
    mSubscription = (PlayerDao.getPlayerByQuery(mDb, playerQuery)
        .mapToList(Player.MAPPER)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(mAdapter));
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
    if (mExpandableHeader.isExpanded()) {
      mExpandableHeader.collapse();
    }
    if (scrollState == ScrollState.UP) {
      mFab.hide();
    } else {
      mFab.show();
    }
  }

  @OnClick(R.id.fab) void onClickAddPlayer() {
    PlayerManageActivity.startActivity(this, null);
    mAnalyticsTracker.sendEvent(AnalyticsTracker.Category.DEFAULT,
        AnalyticsTracker.Action.ADD_PLAYER_FAB_TAP);
  }

  @OnClick(R.id.rank) void onClickRank() {
    RankSelectDialogFragment.newInstance().show(getSupportFragmentManager(), "");
  }

  @OnClick(R.id.setting) void onClickSetting() {
    LicenseActivity.startActivity(this);
  }

  @Override public void onRankSelect(String rank) {
    if (!TextUtils.isEmpty(rank)) {
      mRankText.setText(rank);
    } else {
      mRankText.setText(getString(R.string.player_rank_all));
    }
    doSearch();
  }
}
