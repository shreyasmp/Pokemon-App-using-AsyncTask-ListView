package com.shreyasmp.uship.model;

/**
 * Created by shreyasmp on 3/31/17.
 *
 *  Pokemon model class for having this as a single point of data access across the application.
 *  This class is made singleton in order to store values of pokemon required that is accessible anywhere in activities
 */

public class PokemonModel {

    private static PokemonModel pokemonModelInstance = null;

    // Data variables which are strings used for pokemon feature display

    private String name = "";
    private String height = "";
    private String weight = "";
    private String typename = "";
    private String front_default = "";


    // private constructor one of features of singleton class

    private PokemonModel() {

    }

    // A static accessor method for instance of class

    public static PokemonModel getInstance() {
        if(pokemonModelInstance == null) {
            pokemonModelInstance = new PokemonModel();
        }
        return pokemonModelInstance;
    }

    // method to clear singleton objects before invocation of each call

    public static void clearPokeMonModel(){
        pokemonModelInstance = null;
    }

    // GETTER and SETTER methods for each of the data variables listed above.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getTypeName(){
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getFront_default() {
        return front_default;
    }

    public void setFront_default(String front_default) {
        this.front_default = front_default;
    }

}
