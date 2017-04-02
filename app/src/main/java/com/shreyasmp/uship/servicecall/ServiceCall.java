package com.shreyasmp.uship.servicecall;

import android.util.Log;

import com.shreyasmp.uship.utils.PokemonConstants;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static com.shreyasmp.uship.utils.PokemonConstants.GET_REQUEST_METHOD;

/**
 * Created by shreyasmp on 3/31/17.
 * Service Call class for creating a HTTP GET Request to the pokiAPI v2 provided.
 *
 */

public class ServiceCall {

    // Class name Tags which help in logs to understand where the statements or flow goes throught

    private static String TAG = PokemonConstants.APPNAME+"--"+ServiceCall.class.getSimpleName();

    public ServiceCall() {

    }

    /**
     *
     * @param pokemonListURL
     * @return
     *
     *  Single unique method for creating a HTTP connection using GET method for API
     *  Methods contain LOGGING ENABLE tags which can be helpful in turning on/off of logs based on release/debug mode
     */

    public String makeServiceCall(String pokemonListURL) {
        String response = null;
        try {
            URL url = new URL(pokemonListURL);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod(GET_REQUEST_METHOD);
            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            response = convertStreamToString(inputStream);
        }
        catch (MalformedURLException e) {
            if(PokemonConstants.LOGGING_ENABLED) {
                Log.e(TAG, "MalformedURL Exception: " + e.getMessage());
            }
        }
        catch (ProtocolException e) {
            if(PokemonConstants.LOGGING_ENABLED) {
                Log.e(TAG, "Protocol Exception: " + e.getMessage());
            }
        }
        catch (IOException e) {
            if(PokemonConstants.LOGGING_ENABLED) {
                Log.e(TAG, "IOException: " + e.getMessage());
            }
        }
        catch (Exception e) {
            if(PokemonConstants.LOGGING_ENABLED) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
        return response;
    }

    /**
     *
     * @param inputStream
     * @return
     *
     * Converting received json response from stream to String with proper delimiters
     *
     */

    private String convertStreamToString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = new StringBuilder();

        String line;
        try {
            while((line = reader.readLine()) != null) {
                builder.append(line).append(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            if(PokemonConstants.LOGGING_ENABLED) {
                Log.e(TAG, "IOException: " + e.getMessage());
            }
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                if(PokemonConstants.LOGGING_ENABLED) {
                    Log.e(TAG, "IOException: " + e.getMessage());
                }
            }
        }
        return builder.toString();
    }
}
