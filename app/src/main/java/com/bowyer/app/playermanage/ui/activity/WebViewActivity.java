package com.bowyer.app.playermanage.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.bowyer.app.playermanage.PlayerApplication;
import com.bowyer.app.playermanage.R;
import com.bowyer.app.playermanage.databinding.ActivityWebviewBinding;

public class WebViewActivity extends AppCompatActivity
    implements SwipeRefreshLayout.OnRefreshListener {

  public static final String KEY_URL = "key_url";
  public static final String DEF_URL = "https://ameblo.jp/bowyer-app/";

  private ActivityWebviewBinding binding = null;

  public static void startWebViewActivity(final Context context, final String url) {
    Intent intent = new Intent(context, WebViewActivity.class);
    intent.putExtra(KEY_URL, url);
    context.startActivity(intent);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_webview);
    PlayerApplication.getComponent(this).inject(this);

    initActionBar();
    initWebView();
    initIntent();
    binding.swipeLayout.setOnRefreshListener(this);
  }

  @Override protected void onResume() {
    super.onResume();
    binding.webView.onResume();
    binding.webView.resumeTimers();
  }

  @Override protected void onPause() {
    super.onPause();
    binding.webView.onPause();
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
    binding.toolbar.setTitle("");
    setSupportActionBar(binding.toolbar);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
  }

  private void initIntent() {
    final Intent intent = getIntent();
    String url = intent.getExtras().getString(KEY_URL, DEF_URL);
    binding.webView.loadUrl(url);
  }

  private void setTitle(final String title) {
    if (TextUtils.isEmpty(title)) {
      return;
    }
    binding.toolbar.setTitle(title);
  }

  private void initWebView() {
    binding.webView.getSettings().setJavaScriptEnabled(true);
    binding.webView.getSettings().setDomStorageEnabled(true);
    binding.webView.getSettings().setUseWideViewPort(true);
    binding.webView.setWebViewClient(new WebViewClient() {

      @Override public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        final String title = view.getTitle();
        setTitle(title);
        binding.swipeLayout.setRefreshing(false);
      }

      @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        binding.swipeLayout.setRefreshing(true);
      }
    });
  }

  @Override public void onRefresh() {
    binding.webView.reload();
  }

  @Override public void onBackPressed() {
    if (binding.webView.canGoBack()) {
      binding.webView.goBack();
      return;
    }
    super.onBackPressed();
  }
}
