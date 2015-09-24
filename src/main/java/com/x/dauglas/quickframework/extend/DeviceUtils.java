package com.x.dauglas.quickframework.extend;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.telephony.TelephonyManager;

import com.lidroid.xutils.util.LogUtils;

import java.util.UUID;

import in.srain.cube.util.LocalDisplay;

/**
 * DeviceInfoReader class
 * Created by dauglas on 15/3/31.
 */
@SuppressWarnings("unused")
public class DeviceUtils {
    public final static String UUID_KEY = "uuid";
    public final static String IP = "ip";
    public static int SCREEN_WIDTH_PIXELS;
    public static int SCREEN_HEIGHT_PIXELS;
    public static float SCREEN_DENSITY;
    public static int SCREEN_WIDTH_DP;
    public static int SCREEN_HEIGHT_DP;
    public static String DEV_UUID;
    public static String IP_ADDRESS;
    public static String ConfigName = "device";
    private static DeviceUtils _instance;
    private static Context _context;
    private ConfigUtils _config;

    private PowerManager.WakeLock _wl;

    public DeviceUtils(Context context) {
        _context = context;
        initial();
    }

    public static DeviceUtils create(Context context) {
        if (_instance == null) {
            _instance = new DeviceUtils(context);
        }
        return _instance;
    }

    public ConfigUtils getConfig() {
        return _config;
    }

    private void initial() {
        _config = ConfigUtils.create(_context, ConfigName);
        LocalDisplay.init(_context);

        SCREEN_WIDTH_PIXELS = LocalDisplay.SCREEN_WIDTH_PIXELS;
        SCREEN_HEIGHT_PIXELS = LocalDisplay.SCREEN_HEIGHT_PIXELS;
        SCREEN_DENSITY = LocalDisplay.SCREEN_DENSITY;
        SCREEN_WIDTH_DP = LocalDisplay.SCREEN_WIDTH_DP;
        SCREEN_HEIGHT_DP = LocalDisplay.SCREEN_HEIGHT_DP;

        IP_ADDRESS = obtainIPAddress();
        DEV_UUID = obtainUUID();
    }


    public void printLog() {
        String dev = String.format("device Information: \n" +
                        "SCREEN_DENSITY:[%f]\n" +
                        "SCREEN_DP(W x H):[%d x %d]\n" +
                        "SCREEN_PIXELS(W x H):[%d x %d]\n" +
                        "1dp = %dpx\n" +
                        "1dp_designed = %dpx\n" +
                        "IPAddress:[%s]\n" +
                        "UUID:{%s}\n" +
                        "SDK_VERSION:%d",
                LocalDisplay.SCREEN_DENSITY,
                LocalDisplay.SCREEN_WIDTH_DP,
                LocalDisplay.SCREEN_HEIGHT_DP,
                LocalDisplay.SCREEN_WIDTH_PIXELS,
                LocalDisplay.SCREEN_HEIGHT_PIXELS,
                LocalDisplay.dp2px(1.0f),
                LocalDisplay.designedDP2px(1.0f),
                IP_ADDRESS,
                DEV_UUID,
                Build.VERSION.SDK_INT);

        LogUtils.d(dev);
    }

    public String getUUID() {
        String uuid = _config.getString(UUID_KEY);
        if (uuid == null || uuid.isEmpty()) {
            uuid = DEV_UUID;
        }

        if (uuid == null || uuid.isEmpty()) {
            uuid = obtainUUID();
        }

        return uuid;
    }

    public boolean isKeyguardLocked() {
        KeyguardManager km = (KeyguardManager) _context.getSystemService(Context.KEYGUARD_SERVICE);
        if (Build.VERSION.SDK_INT >= 16) {
            return km.isKeyguardLocked();
        } else {
            return km.inKeyguardRestrictedInputMode();
        }
    }

    public static boolean checkCamera() {
        try {
            Camera camera = null;
            int count = Camera.getNumberOfCameras();
            //noinspection LoopStatementThatDoesntLoop
            for (int i = 0; i < count; i++) {
                camera = Camera.open(i);
                break;
            }

            boolean ret = camera != null;
            if (ret) {
                camera.release();
            }
            return ret;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean checkAudioRecord() {
        int bufferSize = AudioRecord.getMinBufferSize(
                8000,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        try {
            AudioRecord audioRecord = new AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    8000,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSize);

            audioRecord.startRecording();

            byte[] data = new byte[bufferSize];
            int ret = audioRecord.read(data, 0, bufferSize);
            LogUtils.d("checkAudioRecord: ret = " + ret);

            audioRecord.stop();
            audioRecord.release();
            return ret == AudioRecord.ERROR_BAD_VALUE || ret > 0;
        } catch (IllegalStateException e) {
            LogUtils.e(e.getMessage());
        }

        return false;
    }

    public static String getVersion() {
        String version = "";
        try {
            version = _context.getPackageManager().getPackageInfo(_context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e(e.getMessage());
        }

        return version;
    }

    public static String getVersionCode() {
        String versionCode = "";
        try {
            versionCode = String.valueOf(_context.getPackageManager().getPackageInfo(_context.getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e(e.getMessage());
        }

        return versionCode;
    }

    private String obtainUUID() {
        ConfigUtils config = ConfigUtils.create(_context, ConfigName);
        WifiManager Wifi = (WifiManager) _context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = Wifi.getConnectionInfo();

        final TelephonyManager tm = (TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmPhone, androidID, macAddress;
        tmDevice = tm.getDeviceId();
        androidID = android.provider.Settings.Secure.getString(_context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        macAddress = info.getMacAddress();
        UUID deviceUuid = new UUID(androidID.hashCode(), ((long) tmDevice.hashCode() << 32) | macAddress.hashCode());
        String uniqueId = deviceUuid.toString();
        LogUtils.i("fetch Device UUID : " + uniqueId);
        config.config(UUID_KEY, uniqueId);
        return uniqueId;
    }

    private String obtainIPAddress() {
        WifiManager wifi = (WifiManager) _context.getSystemService(Context.WIFI_SERVICE);
        if (!wifi.isWifiEnabled()) {
            return null;
        }

        WifiInfo wifiInfo = wifi.getConnectionInfo();
        int IPAddress = wifiInfo.getIpAddress();
        ConfigUtils config = ConfigUtils.create(_context, ConfigName);
        String ret = convertIP(IPAddress);
        config.config(IP, ret);
        return ret;
    }

    private String convertIP(int address) {
        return (address & 0xFF) + "." +
                ((address >> 8) & 0xFF) + "." +
                ((address >> 16) & 0xFF) + "." +
                (address >> 24 & 0xFF);
    }
}
