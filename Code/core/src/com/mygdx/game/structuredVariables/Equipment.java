package com.mygdx.game.structuredVariables;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains informations about the name and materials used to craft each equipment
 */
public class Equipment extends Actors {

    private static String[] equipments = {"Scanner1", "Scanner2", "Scanner3", "dOxygen",
            "dWater", "dMagnetic", "Robot", "CPU", "Container", "Singularizer"};
    private static Map<String, int[]>  map = new HashMap<String, int[]>();
    public static Map<String, Integer> mat = new HashMap<String, Integer>();
    public static HashMap<String, String> codes = new HashMap<String, String>();

    //Required Materials for each equipment
    public static int[][] requiredMaterials = {
            {50000, 50000, 10000, 0,     0,     10000, 10000, 20000, 20000},
            {5000,  5000,  1000,  0,     0,     1000,  3000,  0,     0},
            {20000, 1000,  10000, 0,     1000,  0,     1000,  0,     0},
            {1000,  1000,  1000,  2000,  5000,  0,     3000,  0,     0},
            {500,   500,   500,   1000,  4000,  0,     3000,  0,     0},
            {1000,  1000,  1000,  2000,  5000,  1000,  1000,  0,     0},
            {8000,  8000,  6000,  5000,  3000,  1000,  10000, 0,     0},
            {1000,  1000,  500,   5000,  10000, 10000, 15000, 0,     0},
            {20000, 20000, 20000, 8000,  0,     0,     0,     0,     0},
            {5000,  5000,  5000,  0,     5000,  5000,  5000,  50000, 50000}
    };

    /**
     * Initialize the hashmap
     */
    public static void initMap(){
        map.clear();
        mat.clear();
        for (int i = 0; i<equipments.length; i++){
            map.put(equipments[i], requiredMaterials[i]);
            mat.put(equipments[i], i);
        }
        codes.clear();
        codes.put("S1", "Scanner1");
        codes.put("S2", "Scanner2");
        codes.put("S3", "Scanner3");
        codes.put("DO", "dOxygen");
        codes.put("DW", "dWater");
        codes.put("DM", "dMagnetic");
        codes.put("RO", "Robot");
        codes.put("CP", "CPU");
        codes.put("CO", "Container");
        codes.put("SI", "Singularizer");
    }

    public static int[] getNeededMaterials(String objectToCraft){
        return map.get(objectToCraft);
    }
}
