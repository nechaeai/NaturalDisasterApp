package com.example.demo;
//Import the necessary libraries
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
public class NaturalDisasterApp {
public static void main(String[] args) {
    // Create an OkHttpClient object to send HTTP requests
    OkHttpClient client = new OkHttpClient();
    // Define the URL to request earthquake data from the USGS API
    String url = String.format("https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2022-03-01&endtime=2022-03-06&minmagnitude=5");
    // Create a new HTTP request using the defined URL
    Request request = new Request.Builder()
            .url(url)
            .build();
    try {
        // Send the request and get the response
        Response response = client.newCall(request).execute();
        // Extract the JSON data from the response
        String jsonData = response.body().string();
        // Parse the JSON data into a JSONObject
        JSONObject jsonObject = new JSONObject(jsonData);
        // Get the "features" array from the JSONObject
        JSONArray features = jsonObject.getJSONArray("features");
        // Loop through each earthquake in the "features" array
        for (int i = 0; i < features.length(); i++) {
            // Get the earthquake's properties as a JSONObject
            JSONObject feature = features.getJSONObject(i);
            JSONObject properties = feature.getJSONObject("properties");
            // Extract the earthquake's place, magnitude, and time from its properties
            String place = properties.getString("place");
            double magnitude = properties.getDouble("mag");
            long time = properties.getLong("time");
            // Print the earthquake's place, magnitude, and time to the console
            System.out.printf("Place: %s\nMagnitude: %f\nTime: %d\n\n", place, magnitude, time);
        }
    } catch (IOException e) {
        // If an IOException occurs, print the stack trace to the console
        e.printStackTrace();
    }
}
}

