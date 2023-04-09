package com.example.demo;




import java.io.IOException;


import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


//Import the necessary libraries for creating a REST API with Spring Boot
@RestController
public class NaturalDisasterController {


 // Define a GET endpoint for retrieving earthquake data
 @GetMapping("/earthquake")
 public String getEarthquakeData() {
     // Create an OkHttpClient object to send HTTP requests
     OkHttpClient client = new OkHttpClient();


     // Define the URL to request earthquake data from the USGS API
     Request request = new Request.Builder()
             .url("https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2022-03-01&endtime=2022-03-06&minmagnitude=5")
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


         // Create an HTML table to display the earthquake data
         String table = "<table><thead><tr><th>Place</th><th>Magnitude</th><th>Time</th></tr></thead><tbody>";


         // Loop through each earthquake in the "features" array
         for (int i = 0; i < features.length(); i++) {
             // Get the earthquake's properties as a JSONObject
             JSONObject feature = features.getJSONObject(i);
             JSONObject properties = feature.getJSONObject("properties");


             // Extract the earthquake's place, magnitude, and time from its properties
             String place = properties.getString("place");
             double magnitude = properties.getDouble("mag");
             long time = properties.getLong("time");


             // Add a new row to the HTML table for this earthquake
             table += "<tr><td>" + place + "</td><td>" + magnitude + "</td><td>" + time + "</td></tr>";
         }


         // Close the HTML table
         table += "</tbody></table>";


         // Return the HTML table as a String
         return table;
     } catch (IOException e) {
         // If an IOException occurs, print the stack trace to the console and return an error message
         e.printStackTrace();
         return "Error retrieving earthquake data.";
     }
 }
}





