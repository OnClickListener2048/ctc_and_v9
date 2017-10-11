package com.tohier.cartercoin.config;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2017/5/11.
 */

public class IPUtil {

    /**
     * 获取外网的IP(必须放到子线程里处理)
     */
    public static String getNetIp() {
        String ip = "";
        InputStream inStream = null;
        try {
            URL infoUrl = new URL("http://1212.ip138.com/ic.asp");
            URLConnection connection = infoUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "gb2312"));
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                    //builder.append(line).append("\n");
                }
                inStream.close();
                int start = builder.indexOf("[");
                int end = builder.indexOf("]");
                if (!TextUtils.isEmpty(builder)){
                    ip = builder.substring(start + 1, end);
                }else{
                    ip = "";
                }

                return ip;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
