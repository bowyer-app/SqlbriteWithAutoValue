package com.bowyer.app.playermanage.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bowyer.app.playermanage.AnalyticsTracker;
import com.bowyer.app.playermanage.PlayerApplication;
import com.bowyer.app.playermanage.R;
import javax.inject.Inject;

public class WebViewActivity extends AppCompatActivity
    implements SwipeRefreshLayout.OnRefreshListener {

  public static final String KEY_URL = "key_url";
  public static final String DEF_URL = "http://ameblo.jp/bowyer-app/";

  @Inject AnalyticsTracker mAnalyticsTracker;

  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.swipe_layout) SwipeRefreshLayout mSwipeRefreshLayout;
  @Bind(R.id.web_view) WebView mWebView;

  public static void startWebViewActivity(final Context context, final String url) {
    Intent intent = new Intent(context, WebViewActivity.class);
    intent.putExtra(KEY_URL, url);
    context.startActivity(intent);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_webview);
    ButterKnife.bind(this);
    PlayerApplication.getComponent(this).inject(this);

    initActionBar();
    initWebView();
    initIntent();
    mSwipeRefreshLayout.setOnRefreshListener(this);
  }

  @Override protected void onStart() {
    super.onStart();
    mAnalyticsTracker.sendScreenView(this.getLocalClassName());
  }

  @Override protected void onResume() {
    super.onResume();
    mWebView.onResume();
    mWebView.resumeTimers();
  }

  @Override protected void onPause() {
    super.onPause();
    mWebView.onPause();
  }

  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
    }
    return false;
  }

  private void initActionBar() {
    mToolbar.setTitle("");
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
  }

  private void initIntent() {
    final Intent intent = getIntent();
    String url = intent.getExtras().getString(KEY_URL, DEF_URL);
    mWebView.loadUrl(url);
  }

  private void setTitle(final String title) {
    if (TextUtils.isEmpty(title)) {
      return;
    }
    mToolbar.setTitle(title);
  }

  private void initWebView() {
    mWebView.getSettings().setJavaScriptEnabled(true);
    mWebView.getSettings().setDomStorageEnabled(true);
    mWebView.getSettings().setUseWideViewPort(true);
    mWebView.setWebViewClient(new WebViewClient() {

      @Override public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        final String title = view.getTitle();
        setTitle(title);
        mSwipeRefreshLayout.setRefreshing(false);
      }

      @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        mSwipeRefreshLayout.setRefreshing(true);
      }
    });
  }

  @Override public void onRefresh() {
    mWebView.reload();
  }

  @Override public void onBackPressed() {
    if (mWebView.canGoBack()) {
      mWebView.goBack();
      return;
    }
    super.onBackPressed();
  }
}
