package com.wkp.hanyupinyin.model.utils;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.support.v4.util.Pair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2018/3/15.
 * 有道语音合成工具类
 */

public class YouDaoUtil {

    private static AudioTrack sTrack;

    private static final String RIFF_HEADER = "RIFF";
    private static final String WAVE_HEADER = "WAVE";
    private static final String FMT_HEADER = "fmt ";
    private static final String DATA_HEADER = "data";

    private static final int HEADER_SIZE = 44;

    private static final String CHARSET = "ASCII";

    /**
     * 读取文件头
     * @param wavStream
     * @return
     * @throws IOException
     */
    public static Pair<Integer, Integer> readHeader(InputStream wavStream) throws IOException {

        ByteBuffer buffer = ByteBuffer.allocate(HEADER_SIZE);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        wavStream.read(buffer.array(), buffer.arrayOffset(), buffer.capacity());

        buffer.rewind();
        buffer.position(buffer.position() + 20);
        int format = buffer.getShort(); //==1
        System.out.println(format);
        int channels = buffer.getShort();   //==1 ==2
        System.out.println(channels);
        int rate = buffer.getInt(); //11025--48000
        buffer.position(buffer.position() + 6);
        int bits = buffer.getShort();   //==16 ==8
        System.out.println(bits);
        int dataSize = 0;
        while (buffer.getInt() != 0x61746164) { // "data" marker
            int size = buffer.getInt();
            wavStream.skip(size);
            buffer.rewind();
            wavStream.read(buffer.array(), buffer.arrayOffset(), 8);
            buffer.rewind();
        }
        dataSize = buffer.getInt();

        return new Pair<>(rate, dataSize);
    }

    /**
     * 播放语音，播放有误
     *
     * @param is
     * @return
     */
    private static boolean play(InputStream is) {
        try {
            Pair<Integer, Integer> header = readHeader(is);
            int minBufferSize = AudioTrack.getMinBufferSize(header.first, AudioFormat.CHANNEL_IN_LEFT, AudioFormat.ENCODING_PCM_16BIT);
            sTrack = new AudioTrack(AudioManager.STREAM_MUSIC, header.first, AudioFormat.CHANNEL_IN_LEFT,
                    AudioFormat.ENCODING_PCM_16BIT, minBufferSize, AudioTrack.MODE_STREAM);
            sTrack.setStereoVolume(1.0f, 1.0f);
            sTrack.play();
            byte[] buffer = new byte[header.second];
            int len = 0;
            while ((len = is.read(buffer,0,buffer.length)) > 0) {
                sTrack.write(buffer, 0, len);
                sTrack.flush();
            }
            is.close();
            sTrack.stop();
            sTrack.release();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 联网获取合成语音
     *
     * @param url
     * @return
     */
    public static boolean getWav(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.connect();
            String contentType = connection.getHeaderField("Content-Type");
            for (Map.Entry<String, List<String>> entry : connection.getHeaderFields().entrySet()) {
                System.out.println(entry.getKey());
                System.out.println(entry.getValue());
            }
            System.out.println(connection.getResponseCode());
            if ("audio/x-wav".equals(contentType)) {
                InputStream is = connection.getInputStream();
                return play(is);
            } else {
                InputStream inputStream = connection.getInputStream();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) > 0) {
                    os.write(buffer, 0, len);
                    os.flush();
                }
                inputStream.close();
                os.close();
                System.out.println(os.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * 生成32位MD5摘要
     *
     * @param string
     * @return
     */
    public static String md5(String string) {
        if (string == null) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] btInput = string.getBytes();
        try {
            /** 获得MD5摘要算法的 MessageDigest 对象 */
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            /** 使用指定的字节更新摘要 */
            mdInst.update(btInput);
            /** 获得密文 */
            byte[] md = mdInst.digest();
            /** 把密文转换成十六进制的字符串形式 */
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * 根据api地址和参数生成请求URL
     *
     * @param url
     * @param params
     * @return
     */
    public static String getUrlWithQueryString(String url, Map<String, String> params) {
        if (params == null) {
            return url;
        }

        StringBuilder builder = new StringBuilder(url);
        if (url.contains("?")) {
            builder.append("&");
        } else {
            builder.append("?");
        }

        int i = 0;
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (value == null) { // 过滤空的key
                continue;
            }

            if (i != 0) {
                builder.append('&');
            }

            builder.append(key);
            builder.append('=');
            builder.append(encode(value));

            i++;
        }

        return builder.toString();
    }

    /**
     * 进行URL编码
     *
     * @param input
     * @return
     */
    public static String encode(String input) {
        if (input == null) {
            return "";
        }
        try {
            return URLEncoder.encode(input, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return input;
    }
}
