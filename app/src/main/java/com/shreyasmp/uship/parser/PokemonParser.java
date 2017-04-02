package com.shreyasmp.uship.parser;

import android.text.TextUtils;
import android.util.Log;

import com.shreyasmp.uship.model.PokemonModel;
import com.shreyasmp.uship.utils.PokemonConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.shreyasmp.uship.utils.PokemonConstants.POKEMON_FRONT_SPRITE;
import static com.shreyasmp.uship.utils.PokemonConstants.POKEMON_HEIGHT;
import static com.shreyasmp.uship.utils.PokemonConstants.POKEMON_NAME;
import static com.shreyasmp.uship.utils.PokemonConstants.POKEMON_RESULTS;
import static com.shreyasmp.uship.utils.PokemonConstants.POKEMON_SPRITES;
import static com.shreyasmp.uship.utils.PokemonConstants.POKEMON_TYPE;
import static com.shreyasmp.uship.utils.PokemonConstants.POKEMON_TYPES;
import static com.shreyasmp.uship.utils.PokemonConstants.POKEMON_TYPE_NAME;
import static com.shreyasmp.uship.utils.PokemonConstants.POKEMON_WEIGHT;

/**
 * Created by shreyasmp on 3/31/17.
 *
 *  Parser class for handling GET Request response data
 *  This class has two different parser methods one for parsing the names of first 151 pokemon's as
 *   required and another parser method for displaying only the required feature of specific pokemon
 */

public class PokemonParser  {

    // class Tagging for better debug logs to get the flow

    private static String TAG = PokemonConstants.APPNAME+"--"+PokemonParser.class.getSimpleName();

    // Arraylist of type HashMap to store feature values as key/ value pair

    public ArrayList<HashMap<String, String>> pokemonList;

    // Instance of singleton model class
    PokemonModel pokemonModel = PokemonModel.getInstance();

    /**
     *
     * @param responseData
     * @return
     *
     * This method takes in the responseData which is received from HTTP GET connection for list of first 151
     *  pokemon's to be displayed in list view.
     *  Method returns the hashmap of all the 151 pokemon names store as key value pairs
     */
    public ArrayList<HashMap<String, String>> parsePokeListJSONData(String responseData) {

        pokemonList = new ArrayList<>();

        // check if response data is empty

        if(!TextUtils.isEmpty(responseData)) {
            try {

                // converting the string response data to JSONObject

                JSONObject jsonObject = new JSONObject(responseData);

                // Looking for results tag in json object which is an array to retrieve 151 pokemon names

                JSONArray pokemonResults = jsonObject.getJSONArray(POKEMON_RESULTS);

                // Loop through the Array and store the strings into HashMap
                // Look for name tag based values which would fetch the names of pokemon to be displayed

                for(int index = 0; index < pokemonResults.length(); index++) {
                    JSONObject results = pokemonResults.getJSONObject(index);
                    pokemonModel.setName(hasString(results, POKEMON_NAME));

                    HashMap<String, String> mapResults = new HashMap<>();
                    mapResults.put(POKEMON_NAME, pokemonModel.getName());

                    pokemonList.add(mapResults);
                    if(PokemonConstants.LOGGING_ENABLED) {
                        Log.d(TAG, "Name of Pokemon: " + pokemonList.get(index));
                    }
                }
            } catch (JSONException e){
                if(PokemonConstants.LOGGING_ENABLED) {
                    Log.e(TAG, "JSONException: " + e.getMessage());
                }
            }
            return pokemonList;
        }
        else {
            if(PokemonConstants.LOGGING_ENABLED) {
                Log.e(TAG, "PokeMon List Data is Empty");
            }
        }
        return null;
    }

    /**
     *
     * @param responseData
     * @return
     *
     * This method takes the response received from HTTP GET request as a query about specific pokemon
     *  to the pokiAPI. The received response includes the whole list of features of one specific pokemon
     *  whose value needs to be parsed and populated in next activity screen.
     *
     *  Method returns the pokemon types in Hashmap, and other features of pokemon are store in singleton class object
     *
     */

    public ArrayList<HashMap<String, String>> parsePokeDetailsJSONData(String responseData) {

        pokemonList = new ArrayList<>();

        // check if response data is empty

        if(!TextUtils.isEmpty(responseData)) {
            try {
                JSONObject jsonObject = new JSONObject(responseData);

                    // Look for sprite images and front default sprite image for image view display

                    JSONObject pokemonSprite = jsonObject.getJSONObject(POKEMON_SPRITES);
                    pokemonModel.setFront_default(hasString(pokemonSprite, POKEMON_FRONT_SPRITE));

                    // Look for pokemon name, height weight from json response object

                    pokemonModel.setName(hasString(jsonObject, POKEMON_NAME));
                    pokemonModel.setHeight(hasString(jsonObject, POKEMON_HEIGHT));
                    pokemonModel.setWeight(hasString(jsonObject, POKEMON_WEIGHT));

                // Look for types of pokemon, which would be a json array
                // So we iterate to find the type and store them in Hashmap

                JSONArray pokemonTypes = jsonObject.getJSONArray(POKEMON_TYPES);
                for(int index = 0; index < pokemonTypes.length(); index++) {
                    JSONObject types = pokemonTypes.getJSONObject(index);
                    JSONObject type = types.getJSONObject(POKEMON_TYPE);
                    pokemonModel.setTypename(hasString(type, POKEMON_TYPE_NAME));

                    HashMap<String, String> typeResults = new HashMap<>();
                    typeResults.put(POKEMON_TYPE_NAME, pokemonModel.getTypeName());

                    pokemonList.add(typeResults);
                }
            } catch(JSONException e) {
                if(PokemonConstants.LOGGING_ENABLED) {
                    Log.e(TAG, "JSONException: "+e.getMessage());
                }
            }
            return pokemonList;
        }
         else {
            if(PokemonConstants.LOGGING_ENABLED) {
                Log.e(TAG, "Pokemon Details are empty");
            }
        }
        return null;
    }

    /**
     *
     * @param jsonObject
     * @param key
     * @return
     *
     *  A very important method to check for making sure that the json string is not null.
     *  This check for null is needed in order to not make application crash
     */

    private String hasString(JSONObject jsonObject, String key) {
        if(jsonObject == null || TextUtils.isEmpty(key)) {
            return "";
        }
        try {
            if(jsonObject.has(key)) {
                return jsonObject.getString(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }
}
