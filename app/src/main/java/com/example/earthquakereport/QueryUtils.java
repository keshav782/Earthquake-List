package com.example.earthquakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {

    }

    public static List<Earthqauke> fetchEarthquakeData(String requestUrl){

        URL url = createUrl(requestUrl); 
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

            List<Earthqauke> earthqaukes = extractFeatureFromjson(jsonResponse);

            return earthqaukes;

        }
    

    private static URL createUrl(String StringUrl){
        URL url= null;
        try {
            url = new URL(StringUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"Problem building the URL",e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = null;

        if(url==null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection= null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if(urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                jsonResponse= readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG,"Error response code: "+ urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        }finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if (inputStream!=null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder builder = new StringBuilder();
        if(inputStream!=null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line!=null){
                builder.append(line);
                line= reader.readLine();
            }
        }
        return builder.toString();
    }

    private static List<Earthqauke> extractFeatureFromjson(String earthquakejson){
        // If the JSON string is empty or null, then return early
        if(TextUtils.isEmpty(earthquakejson)){
            return null;
        }
        // Create an empty ArrayList that we can start adding earthquakes to
        List<Earthqauke> earthqaukes = new ArrayList<>();
        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try{
            JSONObject object = new JSONObject(earthquakejson);

            JSONArray array = object.getJSONArray("features");

            for (int i =0;i<array.length();i++){
                JSONObject currentEarthquake = array.getJSONObject(i);
                // For a given earthquake, extract the JSONObject associated with the
                // key called "properties", which represents a list of all properties
                // for that earthquake.

                JSONObject properties=  currentEarthquake.getJSONObject("properties");
                double magnitude = properties.getDouble("mag");
                String location = properties.getString("place");
                long time = properties.getLong("time");

                String url = properties.getString("url");

                Earthqauke earthqauke = new Earthqauke(magnitude,location,time,url);
                earthqaukes.add(earthqauke);
            }
        }catch (JSONException e){
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);

        }
        return earthqaukes;
    }
}

