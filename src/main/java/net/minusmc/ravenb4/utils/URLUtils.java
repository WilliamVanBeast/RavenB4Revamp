package net.minusmc.ravenb4.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLUtils {

    public static boolean isHypixelKeyValid(String string) {
        String string2 = getTextFromURL("https://api.hypixel.net/key?key=" + string);
        return !string2.isEmpty() && !string2.contains("Invalid API");
    }

    public static String getTextFromURL(String _url) {
        String text = "";
        HttpURLConnection connection = null;
        try {
            URL url = new URL(_url);
            connection = (HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            text = getTextFromConnection(connection);
        } catch (IOException ignored) {
        } finally {
            if (connection != null) {
                connection.disconnect();
            }

        }
        return text;
    }

    private static String getTextFromConnection(HttpURLConnection connection) {
        if (connection != null) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String result;
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    String input;
                    while((input = bufferedReader.readLine()) != null) {
                        stringBuilder.append(input);
                    }
                    String res = stringBuilder.toString();
                    connection.disconnect();
                    result = res;
                } finally {
                    bufferedReader.close();
                }
                return result;
            } catch (Exception ignored) {}
        }

        return "";
    }

}
