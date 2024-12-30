package io.github.hathibelagal.tvbrowser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String HOMEPAGE = "https://hathibelagal-dev.github.io/tv/home.html";
    private static final String UA = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:133.0) Gecko/20100101 Firefox/133.0";
    WebView browser;
    TextView status;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        browser = findViewById(R.id.browser);
        status = findViewById(R.id.browser_status);

        WebSettings settings = browser.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUserAgentString(UA);
        settings.setDomStorageEnabled(true);

        CookieManager.getInstance().setAcceptCookie(true);

        browser.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                status.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                status.setVisibility(View.VISIBLE);
            }
        });
        browser.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                MainActivity.this.setTitle(title);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress == 100) return;
                status.setText(getResources().getText(R.string.status_loading).toString()
                        + " " + newProgress + "%");
            }

        });
        browser.loadUrl(HOMEPAGE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browser_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.browser_home) {
            browser.loadUrl(HOMEPAGE);
        } else if(id == R.id.browser_back) {
            browser.goBack();
        } else if(id == R.id.browser_forward) {
            browser.goForward();
        } else if(id == R.id.browser_refresh) {
            browser.reload();
        } else if(id == R.id.browser_quit) {
            MainActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}