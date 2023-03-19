package com.example.demo;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class EarthquakeController {

    /**
     * Retrieves earthquake data from the USGS API for a given city.
     *
     * @param city the city to retrieve earthquake data for
     * @param model the model to populate with the earthquake data
     * @return the name of the HTML template to render
     */
	

    @GetMapping("/")
    public String home() {
        return "home";
    }
	
    @PostMapping("/earthquake")
    public String getEarthquakeData(@RequestParam String city, Model model) {
        OkHttpClient client = new OkHttpClient();

        String url = String.format("https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&place=%s", city);
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String jsonData = response.body().string();

            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray features = jsonObject.getJSONArray("features");

                if (features.length() == 0) {
                    model.addAttribute("message", "No earthquake data found for " + city);
                    return "message";
                } else {
                    model.addAttribute("city", city);
                    model.addAttribute("features", features);
                    return "earthquake";
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return "error";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }
}