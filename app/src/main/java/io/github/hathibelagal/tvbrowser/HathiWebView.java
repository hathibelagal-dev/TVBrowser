package io.github.hathibelagal.tvbrowser;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HathiWebView extends WebView {
    public static final String HOMEPAGE = "https://hathibelagal-dev.github.io/tv/home.html";

    public HathiWebView(@NonNull Context context) {
        super(context);
    }

    public HathiWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HathiWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void goHome() {
        this.loadUrl(HOMEPAGE);
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
        return super.dispatchKeyEvent(event);
    }
}
