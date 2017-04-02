package com.shreyasmp.uship.views.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shreyasmp.uship.R;
import com.shreyasmp.uship.model.PokemonModel;
import com.shreyasmp.uship.utils.Connectivity;
import com.shreyasmp.uship.utils.PokemonConstants;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static com.shreyasmp.uship.utils.PokemonConstants.ALERT_DIALOG_MESSAGE;
import static com.shreyasmp.uship.utils.PokemonConstants.ALERT_DIALOG_TITLE;
import static com.shreyasmp.uship.utils.PokemonConstants.DISPLAY_HEIGHT;
import static com.shreyasmp.uship.utils.PokemonConstants.DISPLAY_NAME;
import static com.shreyasmp.uship.utils.PokemonConstants.DISPLAY_TYPES;
import static com.shreyasmp.uship.utils.PokemonConstants.DISPLAY_WEIGHT;
import static com.shreyasmp.uship.utils.PokemonConstants.POKEMON_TYPE_DETAILS;
import static com.shreyasmp.uship.utils.PokemonConstants.POKEMON_TYPE_NAME;
import static com.shreyasmp.uship.utils.PokemonConstants.POKEMON_WEIGHT_CORRECTION_FACTOR;
import static com.shreyasmp.uship.utils.PokemonConstants.POKEMON_WEIGHT_SCALE;
import static com.shreyasmp.uship.utils.PokemonConstants.PROGRESSDIALOG_DETAIL_MESSAGE;

/**
 * Created by shreyasmp on 4/1/17.
 *
 * Detailed activity for displaying the specific features of the pokemon selected.
 * and painting the UI with the front default sprite image and rest of the features
 *
 */

public class DetailView_Activity extends AppCompatActivity {

    // Class tagging

    private static String TAG = PokemonConstants.APPNAME + "--" + DetailView_Activity.class.getSimpleName();

    private ArrayList<HashMap<String, String>> pokemonTypes;

    // Instantiating the singleton model class

    PokemonModel pokemonModel = PokemonModel.getInstance();

    // Image view needed for displaying sprite image

    ImageView pokemonSpriteImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pokemon_list_detail_view);

        // Method call for handling software UI and hardware back button

        setTitleBackButton();

        // Assign intent data to array list

        pokemonTypes = (ArrayList<HashMap<String,String>>)getIntent().getSerializableExtra(POKEMON_TYPE_DETAILS);

        if(PokemonConstants.LOGGING_ENABLED) {
            Log.d(TAG, "Pokemon Front Sprite: " + pokemonModel.getFront_default());
            Log.d(TAG, "Pokemon Name: " + pokemonModel.getName() + " Pokemon Height: " + pokemonModel.getHeight() + " Pokemon Weight: " + pokemonModel.getWeight());
        }

        // Set values to UI elements for display
        pokemonSpriteImage = (ImageView)findViewById(R.id.pokemon_front_sprite);

        TextView name = (TextView)findViewById(R.id.pokemon_detail_name);
        name.setText(DISPLAY_NAME+pokemonModel.getName());

        TextView height = (TextView)findViewById(R.id.pokemon_detail_height);
        height.setText(DISPLAY_HEIGHT+pokemonModel.getHeight());

        // Since weight of pokemon characters are displayed in Kilograms,
        // Took an extra effort to calculate the exact weight as per the PokemonPedia.

        TextView weight = (TextView)findViewById(R.id.pokemon_detail_weight);
        weight.setText(DISPLAY_WEIGHT+(Double.parseDouble(pokemonModel.getWeight())/POKEMON_WEIGHT_CORRECTION_FACTOR)+POKEMON_WEIGHT_SCALE);

        // displaying types as some of the characters have multiple and need to read from hashmap

        TextView types = (TextView)findViewById(R.id.pokemon_detail_types);
        StringBuffer stringBuffer = new StringBuffer();
        if (pokemonTypes != null && pokemonTypes.size() != 0) {
            for (int index = 0; index < pokemonTypes.size(); index++) {
                HashMap<String, String> hashMap = pokemonTypes.get(index);
                stringBuffer.append(hashMap.get(POKEMON_TYPE_NAME) +" ");
            }
        }
        types.setText(DISPLAY_TYPES + stringBuffer.toString());

        // Network call to download sprite image
        // Also checks for an network call, if true displays the data
        // Else displays a network error dialog to user with OK button for graceful shutdown

        if(Connectivity.isNetworkConnected(this)) {
            new downloadPokemonSprite().execute(pokemonModel.getFront_default());
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(ALERT_DIALOG_TITLE)
                    .setMessage(ALERT_DIALOG_MESSAGE).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setIcon(android.R.drawable.ic_dialog_alert).show();
        }
    }

    private class downloadPokemonSprite extends AsyncTask<String, Void, Bitmap> {

        private String TAG = PokemonConstants.APPNAME + "--" + downloadPokemonSprite.class.getSimpleName();

        private ProgressDialog progressDialog;

        /**
         *  Method on preexecute for showing progress dialog to user
         */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DetailView_Activity.this);
            progressDialog.setMessage(PROGRESSDIALOG_DETAIL_MESSAGE);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        /**
         * @param URL
         * @return
         * Background method to download the sprite image of the specified pokemon
         */

        @Override
        protected Bitmap doInBackground(String... URL){
            String spriteURL = URL[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(spriteURL).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch(Exception e) {
                if(PokemonConstants.LOGGING_ENABLED) {
                    Log.e(TAG, "Exception: " + e.getMessage());
                }
            }
            return  bitmap;
        }

        /**
         *
         * @param result
         *
         * Publishing the sprite image to image view element in UI once it is downloaded.
         *
         */
        @Override
        protected void onPostExecute(Bitmap result) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();

             pokemonSpriteImage.setImageBitmap(result);

        }
    }

    /**
     *
     * Method to set back button on hardware to move to previous activity from detail screen
     *
     */

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_in_right);
        finish();
    }

    /**
     *  Method to set software backbutton on title bar to respond as same as hardware back button
     *  functionality
     *  Animations to slide the current detail view to right to show previous activity after calling
     *  Finish method
     */

    private void setTitleBackButton() {
        View view = findViewById(R.id.pokemon_list_detail_header_layout);
        ImageView pokemonBackClickView = (ImageView)view.findViewById(R.id.pokemon_back_button_icon);
        pokemonBackClickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
