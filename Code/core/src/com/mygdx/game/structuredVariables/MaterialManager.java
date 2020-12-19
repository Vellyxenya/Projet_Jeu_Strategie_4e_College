package com.mygdx.game.structuredVariables;

import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.screens.GameScreen;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe permettant de gérer la production des différents matériaux ainsi que leurs mises à jour.
 */
public class MaterialManager {
    /*
    Taux d'extraction des matériaux
     */
    private static final float IRON_EXTRACTION = 0.0001f;
    private static final float ALUMINIUM_EXTRACTION = 0.0002f;
    private static final float STEEL_EXTRACTION = 0.0003f;
    private static final float KEROSENE_EXTRACTION = 0.0004f;
    private static final float SODIUM_EXTRACTION = 0.0005f;
    private static final float RADIUM_EXTRACTION = 0.0006f;
    private static final float SILICIUM_EXTRACTION = 0.0007f;
    private static final float GENIUSIUM_EXTRACTION = 0f;
    private static final float LENTILIUM_EXTRACTION = 0f;

    public static float material = 0;
    static long elapsedTime;
    static long lastUpdate;

    private static Map<String, Float> map = new HashMap<String, Float>();
    public static Map<Character, String> materialCodes = new HashMap<Character, String>();
    public static String[] materials = {"Iron", "Aluminium", "Steel", "Kerosene", "Sodium", "Radium", "Silicium", "Geniusium", "Lentilium"};

    /**
     * constructeur initialisant le HashMap.
     */
    public MaterialManager(){
        map.clear();
        map.put("Iron", IRON_EXTRACTION);
        map.put("Aluminium", ALUMINIUM_EXTRACTION);
        map.put("Steel", STEEL_EXTRACTION);
        map.put("Kerosene", KEROSENE_EXTRACTION);
        map.put("Sodium", SODIUM_EXTRACTION);
        map.put("Radium", RADIUM_EXTRACTION);
        map.put("Silicium", SILICIUM_EXTRACTION);
        map.put("Geniusium", GENIUSIUM_EXTRACTION);
        map.put("Lentilium", LENTILIUM_EXTRACTION);

        materialCodes.clear();
        materialCodes.put('I', "Iron");
        materialCodes.put('A', "Aluminium");
        materialCodes.put('C', "Steel");
        materialCodes.put('K', "Kerosene");
        materialCodes.put('N', "Sodium");
        materialCodes.put('R', "Radium");
        materialCodes.put('S', "Silicium");
        materialCodes.put('G', "Geniusium");
        materialCodes.put('L', "Lentilium");
    }

    /**
     * Met à jour les matériaux
     * @param m nom du matériau à mettre à jour
     * @return Retourne la nouvelle quantité du matériau
     */
    public static float updateMaterial(String m){
        lastUpdate = Long.parseLong(GameScreen.database.getOneData("SELECT LastUpdate FROM Material WHERE MaterialName = '"+m+"';"));
        elapsedTime = TimeUtils.millis()-lastUpdate;
        material = Float.parseFloat(GameScreen.database.getOneData("SELECT Quantities FROM Material WHERE MaterialName = '"+m+"';"));
        material += elapsedTime*map.get(m);

        GameScreen.database.updateDataBase("UPDATE Material SET LastUpdate = " + String.valueOf(TimeUtils.millis()) + " WHERE MaterialName = '" + m + "';");
        GameScreen.database.updateDataBase("UPDATE Material SET Quantities = " + material + " WHERE MaterialName = '" + m + "';");

        return material;
    }
}
