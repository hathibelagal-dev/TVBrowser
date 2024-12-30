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

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.os.Process;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HathiWebView extends WebView {
    public static final String HOMEPAGE = "https://hathibelagal-dev.github.io/tv/home.html";
    private SharedPreferences prefs;
    private int currentSearchEngine = 0;
    private Activity parentActivity;

    public void setParentActivity(Activity parentActivity) {
        this.parentActivity = parentActivity;
    }

    public HathiWebView(@NonNull Context context) {
        super(context);
    }

    public HathiWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HathiWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getCurrentSearchEngine() {
        return currentSearchEngine;
    }

    public void setCurrentSearchEngine(int currentSearchEngine) {
        this.currentSearchEngine = currentSearchEngine;
    }

    public void goHome() {
        this.loadUrl(prefs.getString("PREFS_HOME", HOMEPAGE));
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (event.isCtrlPressed() && event.isShiftPressed()) {
            setAlpha(0.5f);
            switch (keyCode) {
                case KeyEvent.KEYCODE_1:
                    goBack();
                    break;
                case KeyEvent.KEYCODE_2:
                    goForward();
                    break;
                case KeyEvent.KEYCODE_3:
                    goHome();
                    break;
                case KeyEvent.KEYCODE_5:
                    reload();
                    break;
            }
        } else {
            setAlpha(1f);
        }
        if(keyCode == KeyEvent.KEYCODE_ESCAPE) {
            if(parentActivity != null) {
                parentActivity.finish();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public void setPreferences(SharedPreferences prefs) {
        this.prefs = prefs;
    }
}
