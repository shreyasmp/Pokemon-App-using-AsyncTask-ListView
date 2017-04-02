package com.shreyasmp.uship.utils;

/**
 * Created by shreyasmp on 3/30/17.
 *
 *  Constants which are used in app
 *  Includes all the string constants and connection oriented API URL's
 *  Trigger point to show / hide debug logs is also available in this interface
 *
 */

public interface PokemonConstants {

    String APPNAME = "USHIP";

    String pokeAPIURL = "http://pokeapi.co/api/v2/";
    String pokemonResourceURL = "pokemon/";
    String pokemonNamesQueryParams = "?limit=151";

    // PokeAPI URL

    String pokemonURL = pokeAPIURL + pokemonResourceURL + pokemonNamesQueryParams;

    String POKEMON_RESULTS = "results";

    String POKEMON_NAME = "name";
    String POKEMON_HEIGHT = "height";
    String POKEMON_WEIGHT = "weight";
    String POKEMON_TYPES = "types";
    String POKEMON_TYPE = "type";
    String POKEMON_TYPE_NAME = "name";
    String POKEMON_SPRITES = "sprites";
    String POKEMON_FRONT_SPRITE = "front_default";

    // Request method

    String GET_REQUEST_METHOD = "GET";

    String PROGRESSDIALOG_MESSAGE = "Loading pokemon list....";

    String PROGRESSDIALOG_DETAIL_MESSAGE = "Loading pokemon details....";

    // change value to false to hide logs

    boolean LOGGING_ENABLED = false;

    String POKEMON_TYPE_DETAILS = "pokemonTypesName";

    String POKEMON_WEIGHT_SCALE = " Kg";

    int POKEMON_WEIGHT_CORRECTION_FACTOR = 10;

    String FORWARD_SLASH = "/";

    String DISPLAY_NAME = "Name: ";
    String DISPLAY_HEIGHT = "Height: ";
    String DISPLAY_WEIGHT = "Weight: ";
    String DISPLAY_TYPES = "Types: ";

    String ALERT_DIALOG_TITLE = "No Internet Connection";

    String ALERT_DIALOG_MESSAGE = "Looks like your Internet connection is OFF. Please turn if ON and try again";

}


