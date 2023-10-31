package net.minusmc.ravenb4.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class URLUtils {

    public static final String hypixelKey = "";

    public static String getTextFromURL(String _url, Boolean newline) {
        String text = "";
        HttpURLConnection connection = null;
        try {
            URL url = new URL(_url);
            connection = (HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            text = getTextFromConnection(connection, newline);
        } catch (IOException ignored) {
        } finally {
            if (connection != null) {
                connection.disconnect();
            }

        }
        return text;
    }

    public static String getTextFromURL(String _url, List<ArrayList<String>> headers, Boolean newline) {
        String text = "";
        HttpURLConnection connection = null;
        try {
            URL url = new URL(_url);
            connection = (HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.37 (KHTML, like Gecko) Chrome/51.0.2705.103 Safari/537.37");
            if (headers != null && !headers.isEmpty()) {
                Iterator header_it = headers.iterator();

                while (header_it.hasNext()) {
                    String[] header = (String[]) header_it.next();
                    connection.setRequestProperty(header[0], header[1]);
                }
            }
            text = getTextFromConnection(connection, newline);
        } catch (IOException ignored) {
        } finally {
            if (connection != null) {
                connection.disconnect();
            }

        }
        return text;
    }

    private static String getTextFromConnection(HttpURLConnection connection, Boolean newline) {
        if (connection != null) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String result;
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    String input;
                    while((input = bufferedReader.readLine()) != null) {
                        stringBuilder.append(input + (newline ? "\n": ""));
                    }
                    String res = stringBuilder.toString();
                    connection.disconnect();
                    result = res;
                } finally {
                    bufferedReader.close();
                }
                if (newline) return result.trim();
                return result;
            } catch (Exception ignored) {}
        }

        return "";
    }

    // fun d?
    public static final String getTextFromURL(String _url) {
        return getTextFromURL(_url, false);
    }
}
