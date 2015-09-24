package com.x.dauglas.quickframework.extend;

import android.os.Handler;

import com.lidroid.xutils.util.LogUtils;

public class MainThread {

    private static Handler sHandler;

    /**
     * 必须在主线程中调用。
     */
    public static void bind() {
        if (sHandler == null) {
            sHandler = new Handler();
            LogUtils.d("MainThread.bind()");
        }
    }

    public static void run(Runnable action) {
        if (sHandler == null) {
            LogUtils.e("must be invoke bind() in main thread.");
            return;
        }
        sHandler.post(action);
    }

    public static void runDelay(Runnable action, int delayMillis) {
        if (sHandler == null) {
            LogUtils.e("must be invoke bind() in main thread.");
            return;
        }
        sHandler.postDelayed(action, delayMillis);
    }

    public static boolean sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            LogUtils.e(e.getMessage(), e);
            return false;
        }
        return true;
    }
}
