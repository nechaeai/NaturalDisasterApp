package com.example.demo;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class NaturalDisasterApp {
    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient();

        String url = String.format("https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2022-03-01&endtime=2022-03-06&minmagnitude=5");
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String jsonData = response.body().string();

            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray features = jsonObject.getJSONArray("features");

            for (int i = 0; i < features.length(); i++) {
                JSONObject feature = features.getJSONObject(i);
                JSONObject properties = feature.getJSONObject("properties");

                String place = properties.getString("place");

                double magnitude = properties.getDouble("mag");
                long time = properties.getLong("time");

                System.out.printf("Place: %s\nMagnitude: %f\nTime: %d\n\n", place, magnitude, time);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}