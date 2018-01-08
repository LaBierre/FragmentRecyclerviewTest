package com.example.standard.fragmentrecyclerviewtest.data;

import android.content.Context;
import android.graphics.Movie;
import android.text.TextUtils;
import android.util.Log;

import com.example.standard.fragmentrecyclerviewtest.R;

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

/**
 * Created by vince on 26.12.2017.
 */

public class Utils {

    private static final String LOG_TAG = Utils.class.getName();

    public Utils(){

    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(Context context, String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(Context context, URL url) throws IOException {

        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod(context.getResources().getString(R.string.get_http));
            urlConnection.connect();

            /*
            *  If the request was successful (response code 200),
            *  then read the input stream and parse the response.
            */
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(context, inputStream);
            } else {
                Log.e(LOG_TAG, context.getResources().getString(R.string.error_resp_code) + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, context.getResources().getString(R.string.io_exeption), e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                /*
                * Closing the input stream could throw an IOException, which is why
                * the makeHttpRequest(URL url) method signature specifies than an IOException
                * could be thrown.
                */
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(Context context, InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
                    Charset.forName(context.getResources().getString(R.string.utf_8)));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Query the USGS dataset and return a list of {@link Recipe} objects.
     */
    public static List<Recipe> fetchRecipeData (Context context, String requestUrl){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create URL object
        URL url = createUrl(context, requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(context, url);
        } catch (IOException e) {
            Log.e(LOG_TAG, context.getResources().getString(R.string.io_exeption_http), e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Movie}s
        List<Recipe> recipes = extractFeatureFromJson(context, jsonResponse);

        // Return the list of {@link Book}s
        return recipes;
    }

    /**
     * Return a list of {@link Movie} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Recipe> extractFeatureFromJson (Context context, String recipeJSON){
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(recipeJSON)){
            return null;
        }

        // Create an empty ArrayList that i can start adding recipes
        List<Recipe> recipes = new ArrayList<>();

        try {
            String name = "";
            String ingredient = "";
            String quantity = "";
            String measure = "";
            String shortDescription = "";
            String description = "";
            String videoURL = "";

            JSONArray baseJsonResponseArray = new JSONArray(recipeJSON);
            /*
            * Each item of the baseJsonResponseArray persists in
            * an id, a name, an array of ingredients and an array of steps
            * each array of ingredients includes the attributes quantity, measure and ingredient
            * each array of steps includes the attributes id, shortDescription, description, videoURL and thumbnailUrl
            * */
            for (int i = 0; i < baseJsonResponseArray.length(); i++){
                JSONObject currentRecipe = baseJsonResponseArray.getJSONObject(i);


                if (currentRecipe.has("name"))
                    name = currentRecipe.getString("name");

                // Now the array "ingredients"------------------------------------------------------
                JSONArray ingredientsArray = currentRecipe.getJSONArray("ingredients");

                // Objects and attributes within the array "ingredients"
                for (int j = 0; j < ingredientsArray.length(); j++){
                    JSONObject currentIngredient = ingredientsArray.getJSONObject(j);

                    if (currentIngredient.has("ingredient"))
                        ingredient = currentIngredient.getString("ingredient");

                    int quantityValue;
                    if (currentIngredient.has("quantity")){
                        quantityValue = currentIngredient.getInt("quantity");
                        quantity = String.valueOf(quantityValue);
                    }
                    if (currentIngredient.has("measure"))
                        measure = currentIngredient.getString("measure");
                }
                // ---------------------------------------------------------------------------------

                // And now the array "steps"--------------------------------------------------------
                JSONArray stepsArray = currentRecipe.getJSONArray("steps");
                // Objects and attributes within the array "steps"
                for (int k = 0; k < stepsArray.length(); k++){
                    JSONObject currentStep = stepsArray.getJSONObject(k);

                    if (currentStep.has("shortDescription"))
                        shortDescription = currentStep.getString("shortDescription");
                    if (currentStep.has("description"))
                        description = currentStep.getString("description");
                    if (currentStep.has("videoURL"))
                        videoURL = currentStep.getString("videoURL");
                }
                // ---------------------------------------------------------------------------------

                Recipe recipe = new Recipe(name, ingredient, quantity, measure, shortDescription, description, videoURL);
                recipes.add(recipe);
            }

        } catch (JSONException e) {
            /*
            * If an error is thrown when executing any of the above statements in the "try" block,
            * catch the exception here, so the app doesn't crash. Print a log message
            * with the message from the exception.
            */
            Log.e(LOG_TAG, context.getResources().getString(R.string.io_exeption_three), e);
        }
        return recipes;
    }
}
