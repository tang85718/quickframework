package com.x.dauglas.quickframework.extend;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * KeyboardUtils class
 * <p/>
 * Created by dauglas on 15/5/8.
 */
@SuppressWarnings("unused")
public class KeyboardUtils {
    private Activity _activity;
    private InputMethodManager _inputMgr;

    public KeyboardUtils(Activity activity) {
        _activity = activity;
        _inputMgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void show(View view) {
        _inputMgr.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public void showForce() {
        _inputMgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void hide() {
        _inputMgr.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void hide(EditText edit) {
        if (edit == null) {
            return;
        }

        _inputMgr.hideSoftInputFromWindow(edit.getWindowToken(), 0);
        edit.clearFocus();
    }
}
