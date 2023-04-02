package com.example.demo;


import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RestController

public class NaturalDisasterController {
	@GetMapping("/earthquake")
	public String getEarthquakeData() {
	    OkHttpClient client = new OkHttpClient();

	    Request request = new Request.Builder()
	            .url("https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2022-03-01&endtime=2022-03-06&minmagnitude=5")
	            .build();

	    try {
	        Response response = client.newCall(request).execute();
	        String jsonData = response.body().string();

	        JSONObject jsonObject = new JSONObject(jsonData);
	        JSONArray features = jsonObject.getJSONArray("features");

	        String table = "<table><thead><tr><th>Place</th><th>Magnitude</th><th>Time</th></tr></thead><tbody>";

	        for (int i = 0; i < features.length(); i++) {
	            JSONObject feature = features.getJSONObject(i);
	            JSONObject properties = feature.getJSONObject("properties");

	            String place = properties.getString("place");
	            double magnitude = properties.getDouble("mag");
	            long time = properties.getLong("time");

	            table += "<tr><td>" + place + "</td><td>" + magnitude + "</td><td>" + time + "</td></tr>";
	        }

	        table += "</tbody></table>";

	        return table;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return "Error retrieving earthquake data.";
	    }
	}

}


