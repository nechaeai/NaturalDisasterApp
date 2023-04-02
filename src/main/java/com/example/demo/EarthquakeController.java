package com.example.demo;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;



@RestController

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

    @GetMapping("/earthquakedata")
    public String getEarthquakeData(@RequestParam("city") String city,@RequestParam("orderby") String orderBy) throws URISyntaxException {
        OkHttpClient client = new OkHttpClient();
      
        URI uri = new URI("https", "earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&minmagnitude=5&orderby="+orderBy+"&eventtype=earthquake",null);
        String url = uri.toASCIIString();
        Request request = new Request.Builder()
                .url(url)
                .build();
        String responsePage = "<!DOCTYPE html>\n"
        		+ "<html><head>\n"
        		+ "<style>\n"
        		+ "#earthquake {\n"
        		+ "  font-family: Arial, Helvetica, sans-serif;\n"
        		+ "  border-collapse: collapse;\n"
        		+ "  width: 100%;\n"
        		+ "}\n"
        		+ "\n"
        		+ "#earthquake td, #earthquake th {\n"
        		+ "  border: 1px solid #ddd;\n"
        		+ "  padding: 8px;\n"
        		+ "}\n"
        		+ "\n"
        		+ "#earthquake tr:nth-child(even){background-color: #f2f2f2;}\n"
        		+ "\n"
        		+ "#earthquake tr:hover {background-color: #ddd;}\n"
        		+ "\n"
        		+ "#earthquake th {\n"
        		+ "  padding-top: 12px;\n"
        		+ "  padding-bottom: 12px;\n"
        		+ "  text-align: left;\n"
        		+ "  background-color: #04AA6D;\n"
        		+ "  color: white;\n"
        		+ "}\n"
        		+ "</style>\n"
        		+ "</head>\n"
        		+ "<body>";
        String table = "<table id=\"earthquake\"><thead><tr><th>Place</th><th>Magnitude</th><th>Time</th></tr></thead><tbody>";
        	
        try {
            Response response = client.newCall(request).execute();
            if (response.body() != null) {
                String jsonData = response.body().string();
               
                try {
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray features = jsonObject.getJSONArray("features");

                    
        	        for (int i = 0; i < features.length(); i++) {
        	            JSONObject feature = features.getJSONObject(i);
        	            JSONObject properties = feature.getJSONObject("properties");

        	            try {
        	            String place = properties.getString("place");
        	            
        	            if(place.toLowerCase().contains(city.toLowerCase())) {
        	            	double magnitude = properties.getDouble("mag");
        	            	long time = properties.getLong("time");
        	            	Date date = new Date(time);
        	            	table += "<tr><td>" + place + "</td><td>" + magnitude + "</td><td>" + date + "</td></tr>";
        	            	}
        	            }catch (JSONException je) {
						}
        	        }

        	       

        	    
                } catch (JSONException e) {
                    e.printStackTrace();
                    return "error";
                }
            } else {
            	table += "</tbody></table>";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return table += "</tbody></table>";
        }
        table += "</tbody></table>";
        return responsePage+table+"</body></html>";
    }
}


