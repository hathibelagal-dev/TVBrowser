/* Copyright 2024 Ashraff Hathibelagal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.hathibelagal.tvbrowser;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String UA = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:133.0) Gecko/20100101 Firefox/133.0";
    HathiWebView browser;
    TextView status;

    SharedPreferences prefs;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        browser = findViewById(R.id.browser);
        status = findViewById(R.id.browser_status);

        prefs = getPreferences(MODE_PRIVATE);
        browser.setPreferences(prefs);

        browser.setParentActivity(this);

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

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return AdBlocker.processRequest(MainActivity.this, request, prefs);
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
                if (newProgress == 100) return;
                status.setText(getResources().getText(R.string.status_loading)
                        + " " + newProgress + "%");
            }

        });
        browser.goHome();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browser_menu, menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.browser_home) {
            browser.goHome();
        } else if (id == R.id.browser_back) {
            browser.goBack();
        } else if (id == R.id.browser_forward) {
            browser.goForward();
        } else if (id == R.id.browser_refresh) {
            browser.reload();
        } else if (id == R.id.browser_quit) {
            MainActivity.this.finish();
        } else if (id == R.id.browser_set_homepage) {
            String currentURL = browser.getUrl();
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putString("PREFS_HOME", currentURL);
            prefsEditor.apply();
        } else if (id == R.id.browser_search) {
            showSearchDialog();
        } else if (id == R.id.browser_adblock_count) {
            Snackbar.make(browser, String.format(Locale.ENGLISH, getString(R.string.snack_number_of_ads_blocked),
                    prefs.getInt("BLOCKED_COUNT", 0)), Snackbar.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View searchLayout = LayoutInflater.from(MainActivity.this).inflate(R.layout.search_dialog, null);
        final EditText keywords = searchLayout.findViewById(R.id.search_keywords);

        builder.setTitle("Query");

        builder.setSingleChoiceItems(R.array.search_engine_items, browser.getCurrentSearchEngine(), (dialogInterface, i) -> browser.setCurrentSearchEngine(i));
        builder.setPositiveButton("Search", (dialogInterface, i) -> {
            if (keywords.getText().length() > 0) {
                String query = keywords.getText().toString();
                String searchEngineBase = getResources().getString(R.string.google_query);
                switch (browser.getCurrentSearchEngine()) {
                    case 1:
                        searchEngineBase = getResources().getString(R.string.ddg_query);
                        break;
                    case 2:
                        searchEngineBase = getResources().getString(R.string.bing_query);
                        break;
                    case 3:
                        searchEngineBase = getResources().getString(R.string.wiki_query);
                        break;
                }
                browser.loadUrl(searchEngineBase + query);
            }

        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> Toast.makeText(MainActivity.this, "Search cancelled.", Toast.LENGTH_SHORT).show());

        builder.setView(searchLayout);
        builder.create().show();
    }
}