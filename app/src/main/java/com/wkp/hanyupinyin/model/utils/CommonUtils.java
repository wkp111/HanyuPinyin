package com.wkp.hanyupinyin.model.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by user on 2017/5/9.
 */

public class CommonUtils {
    private static Toast sToast;
    private static Executor sExecutor = Executors.newCachedThreadPool();
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    /**
     * 获取Handler
     *
     * @return
     */
    public static Handler getHandler() {
        return sHandler;
    }

    /**
     * 开启子线程执行任务
     *
     * @param r
     */
    public static void runInSubThread(Runnable r) {
        sExecutor.execute(r);
    }

    /**
     * 主线程执行任务
     *
     * @param r
     */
    public static void runInMainThread(Runnable r) {
        sHandler.post(r);
    }

    /**
     * 普通跳转界面
     *
     * @param context
     * @param aClass
     */
    public static void startActivity(Context context, Class<? extends Activity> aClass) {
        context.startActivity(new Intent(context, aClass));
    }

    /**
     * 带参跳转界面
     *
     * @param context
     * @param aClass
     */
    public static <T extends Serializable> void startActivity(Context context, Class<? extends Activity> aClass, String key, T t) {
        Intent intent = new Intent(context, aClass);
        intent.putExtra(key, t);
        context.startActivity(intent);
    }

    /**
     * 带参跳转界面
     *
     * @param context
     * @param aClass
     */
    public static <T extends Serializable> void startActivity(Context context, Class<? extends Activity> aClass, Map<String, T> params) {
        Intent intent = new Intent(context, aClass);
        for (Map.Entry<String, T> param : params.entrySet()) {
            intent.putExtra(param.getKey(), param.getValue());
        }
        context.startActivity(intent);
    }

    /**
     * 主线程吐司
     *
     * @param context
     * @param text
     */
    public static void showToast(final Context context, final String text) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                if (sToast == null) {
                    sToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                }
                sToast.setText(text);
                sToast.show();
            }
        });
    }


    /**
     * 获取设备唯一标示
     *
     * @return
     */
    public static String getMacNumber() {
        String macSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }

    /**
     * 对指定字符串进行md5加密
     *
     * @param s
     * @return 加密后的数据
     */
    public static String EncryptMD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
