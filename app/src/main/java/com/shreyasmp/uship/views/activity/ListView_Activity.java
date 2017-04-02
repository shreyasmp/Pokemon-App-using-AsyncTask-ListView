package com.shreyasmp.uship.views.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.shreyasmp.uship.R;
import com.shreyasmp.uship.parser.PokemonParser;
import com.shreyasmp.uship.servicecall.ServiceCall;
import com.shreyasmp.uship.utils.Connectivity;
import com.shreyasmp.uship.utils.PokemonConstants;

import java.util.ArrayList;
import java.util.HashMap;

import static com.shreyasmp.uship.utils.PokemonConstants.ALERT_DIALOG_MESSAGE;
import static com.shreyasmp.uship.utils.PokemonConstants.ALERT_DIALOG_TITLE;
import static com.shreyasmp.uship.utils.PokemonConstants.FORWARD_SLASH;
import static com.shreyasmp.uship.utils.PokemonConstants.POKEMON_NAME;
import static com.shreyasmp.uship.utils.PokemonConstants.POKEMON_TYPE_DETAILS;
import static com.shreyasmp.uship.utils.PokemonConstants.PROGRESSDIALOG_DETAIL_MESSAGE;
import static com.shreyasmp.uship.utils.PokemonConstants.PROGRESSDIALOG_MESSAGE;
import static com.shreyasmp.uship.utils.PokemonConstants.pokeAPIURL;
import static com.shreyasmp.uship.utils.PokemonConstants.pokemonResourceURL;
import static com.shreyasmp.uship.utils.PokemonConstants.pokemonURL;

/**
 * Created by shreyasmp on 3/31/17.
 * Main Activity of the application which handles creating HTTP Get connection to POKEAPI
 * and fetching the response. Fetched response is sent to Parser class and retrieved as arraylist of hashmap
 * to display the 151 pokemon in List View. Most of the handling for fetching data/images are done in background
 * to keep the UI thread light
 */

public class ListView_Activity extends AppCompatActivity {

    // Class Tag name

    private static String TAG = PokemonConstants.APPNAME + "--" + ListView_Activity.class.getSimpleName();

    // List view to display the pokemon names

    private ListView listView;

    // Array list to store pokemon names and also reused for storing pokemon types of specific character

    public ArrayList<HashMap<String, String>> pokemonList;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokemon_list_view);

        pokemonList = new ArrayList<>();

        listView = (ListView) findViewById(R.id.pokemon_listview);

        // Invokes the network call for GET HTTP connection
        // Also checks for an network call, if true displays the data
        // Else displays a network error dialog to user with OK button for graceful shutdown

        if (Connectivity.isNetworkConnected(this)) {
            new getPokemonList().execute();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(ALERT_DIALOG_TITLE)
                    .setMessage(ALERT_DIALOG_MESSAGE).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).setIcon(android.R.drawable.ic_dialog_alert).show();
        }

        // Item on click listener for selecting a item from listview to make another network call
        // This network call fetches the detailed feature json data of selected pokemon

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                HashMap<String, String> hashMap = (HashMap) adapterView.getItemAtPosition(position);
                if (PokemonConstants.LOGGING_ENABLED) {
                    Log.d(TAG, "Pokemon Selected: " + hashMap);
                }

                String pokemonName = hashMap.get(POKEMON_NAME);
                String resourceURL = pokeAPIURL + pokemonResourceURL + pokemonName + FORWARD_SLASH;

                // Make a network call with specified URL with pokemon name
                // Also checks for an network call, if true displays the data
                // Else displays a network error dialog to user with OK button for graceful shutdown

                if (Connectivity.isNetworkConnected(ListView_Activity.this)) {
                    new getPokemonDetails().execute(resourceURL);
                } else {
                    new AlertDialog.Builder(ListView_Activity.this)
                            .setTitle(ALERT_DIALOG_TITLE)
                            .setMessage(ALERT_DIALOG_MESSAGE).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert).show();
                }

                if (PokemonConstants.LOGGING_ENABLED) {
                    Log.d(TAG, "Formatted URL: " + resourceURL);
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("ListView_ Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    /**
     * Class pokemonList main functionality is to get the json data from main activity call and
     * pass the retrieved data to parser class for storing the names in hashmap for list view display
     * This class extends AsyncTask for handling background activities
     */

    public class getPokemonList extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>> {

        // Class Tagging

        private String TAG = PokemonConstants.APPNAME + "--" + getPokemonList.class.getSimpleName();

        // Showing progress dialog for user while background task is in progress

        private ProgressDialog progressDialog;

        /**
         * Method on preexecute for showing progress dialog to user
         */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ListView_Activity.this);
            progressDialog.setMessage(PROGRESSDIALOG_MESSAGE);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        /**
         * @param params
         * @return This method does the background activity tasks of making a connection and retrieving the json data
         * Method returns the arraylist of pokemon names for list view display.
         */

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
            ServiceCall httpHandler = new ServiceCall();
            String jsonResponse = httpHandler.makeServiceCall(pokemonURL);
            if(PokemonConstants.LOGGING_ENABLED) {
                Log.d(TAG, "Response: " + jsonResponse);
            }
            PokemonParser pokemonParser = new PokemonParser();
            pokemonList = pokemonParser.parsePokeListJSONData(jsonResponse);
            return pokemonList;
        }

        /**
         * @param pokemonList Method to stop progress dialog once list is populated
         *                    List Adapter takes care of populating the listview in main activity
         */

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> pokemonList) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            if (PokemonConstants.LOGGING_ENABLED) {
                Log.d(TAG, "PokemonList = " + pokemonList.toString());
            }

            ListAdapter adapter = new SimpleAdapter(ListView_Activity.this, pokemonList,
                    R.layout.pokemon_list_item, new String[]{POKEMON_NAME}, new int[]{R.id.pokemon_name});

            listView.setAdapter(adapter);
        }
    }

    /**
     * Class pokemon Detail is the secondary functionality in main activity and is to get the json data for specific pokemon
     * character that is selected from item in list view
     */

    public class getPokemonDetails extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {

        private String TAG = PokemonConstants.APPNAME + "--" + getPokemonDetails.class.getSimpleName();

        private ProgressDialog progressDialog;

        /**
         * Method on preexecute for showing progress dialog to user
         */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ListView_Activity.this);
            progressDialog.setMessage(PROGRESSDIALOG_DETAIL_MESSAGE);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        /**
         * @param params
         * @return Method handles the background task of getting the detailed json data of specific pokemon character
         * and returns the list of pokemon types
         */

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
            ServiceCall httpHandler = new ServiceCall();
            String jsonResponse = httpHandler.makeServiceCall(params[0]);

            if (PokemonConstants.LOGGING_ENABLED) {
                Log.d(TAG, "Detailed Response: " + jsonResponse);
            }

            // second parser method called from Parser class

            PokemonParser pokemonParser = new PokemonParser();
            pokemonList = pokemonParser.parsePokeDetailsJSONData(jsonResponse);
            if (PokemonConstants.LOGGING_ENABLED) {
                Log.d(TAG, "Pokemon Types: " + pokemonList);
            }
            return pokemonList;
        }

        /**
         * @param pokemonList Method on Post Execute to dismiss the progress dialog once transition from current list activity
         *                    takes place to next target activity
         *                    Since we need pokemon types to be transferred from current activity to next thread, we use
         *                    Intents to move data and transition.
         *                    Remaining pokemon features like name, height and weight are stored in singleton class object
         */

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> pokemonList) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            if (PokemonConstants.LOGGING_ENABLED) {
                Log.d(TAG, "Pokemon Types = " + pokemonList.toString());
            }

            // Intent to transfer from one activity to next target activity
            // Sending arraylist with pokemon types to next activity
            Intent pokemonDetailIntent = new Intent(ListView_Activity.this, DetailView_Activity.class);
            pokemonDetailIntent.putExtra(POKEMON_TYPE_DETAILS, pokemonList);
            startActivity(pokemonDetailIntent);
        }
    }
}
