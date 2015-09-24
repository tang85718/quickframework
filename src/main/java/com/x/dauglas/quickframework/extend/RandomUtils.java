package com.x.dauglas.quickframework.extend;

import com.lidroid.xutils.util.LogUtils;

import java.util.HashSet;
import java.util.Random;

/**
 * RandomStringUtils class.
 * <p/>
 * Created by dauglas on 15/4/10.
 */
public class RandomUtils {

    public static String randomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int num = random.nextInt(62);
            buf.append(str.charAt(num));
        }
        return buf.toString();
    }

    public static boolean thump(float p) {
        HashSet<Integer> hash = new HashSet<Integer>();
        int count = (int) (100 * p);
        Random random = new Random();
        while (hash.size() < count) {
            int r = random.nextInt(100);
            hash.add(r);
        }
        int r = random.nextInt(100);
        boolean ret = hash.contains(r);
        LogUtils.v(String.format("thump %.1f%% - %b", p * 100, ret));
        return ret;
    }
}
