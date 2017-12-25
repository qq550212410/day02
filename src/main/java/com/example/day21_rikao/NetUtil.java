package com.example.day21_rikao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * author:Created by WangZhiQiang on 2017-10-31.
 */

public class NetUtil {
    private static String tag = "getNetJson";

    public static String getNetJson(String urlString) {
        try {
            //url对象封装接口字符串
            URL url = new URL(urlString);
            //用url打开连接, 返回值我们用HttpURLConnection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(8000);//设置链接超时时间
            int responseCode = urlConnection.getResponseCode(); //获取状态码
            if (responseCode == 200) {
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                //可拼接的字符串
                StringBuilder stringBuilder = new StringBuilder();
                String temp = "";
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp);

                }
                String jsonString = stringBuilder.toString();
                return jsonString;
            } else {
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(tag, "getNetJson: " + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(tag, "getNetJson: " + e.toString());
        }

        return "";
    }

    /**
     * 获取网络图片
     *
     * @param urlString
     * @return
     */
    public static Bitmap getNetBitmap(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection httpurlConnection = (HttpURLConnection) url.openConnection();
            httpurlConnection.setConnectTimeout(8000);//设置链接超时时间
            int responseCode = httpurlConnection.getResponseCode();

            if (responseCode == 200) {
                InputStream inputStream = httpurlConnection.getInputStream();
                //BitmapFactory:  android提供好的加工图片的工具类,
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            } else {
                //do nothing
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(tag, "getNetBitmap: " + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(tag, "getNetBitmap: " + e.toString());
        }
        return null;
    }

}
