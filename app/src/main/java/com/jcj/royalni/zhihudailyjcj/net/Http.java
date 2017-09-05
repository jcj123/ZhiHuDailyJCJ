package com.jcj.royalni.zhihudailyjcj.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Royal Ni on 2017/7/5.
 */

public class Http {

    public static String getJsonFromHtml(String address) {
        HttpURLConnection conn = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(address);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream inputStream = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String inputLine;

                while ((inputLine = reader.readLine()) != null) {
                    sb.append(inputLine);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            conn.disconnect();
        }
        return sb.toString();
    }

    public static String getJsonFromHtml(String baseUrl, String suffix){
        return getJsonFromHtml(baseUrl + suffix);
    }
}
