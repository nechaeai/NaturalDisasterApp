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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class EarthquakeController {

    /**
     * Retrieves earthquake data from the USGS API for a given city.
     *
     * @param city the city to retrieve earthquake data for
     * @param model the model to populate with the earthquake data
     * @return the name of the HTML template to render
     */



    @GetMapping("/index")
    public String index() {
        return "index";
    }
	
    @PostMapping("/index")
    public String getEarthquakeData(@RequestParam String city, Model model) throws URISyntaxException {
        OkHttpClient client = new OkHttpClient();
        URI uri = new URI("https", "earthquake.usgs.gov", "/fdsnws/event/1/query",
                "format=geojson&minmagnitude=5&limit=5&orderby=time&eventtype=earthquake&place=" + 
                URLEncoder.encode(city, StandardCharsets.UTF_8).replace("+", "%20"), null);
        String url = uri.toASCIIString();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.body() != null) {
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
                        return "index";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return "error";
                }
            } else {
                model.addAttribute("message", "No response from server");
                return "message";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
}
    
}