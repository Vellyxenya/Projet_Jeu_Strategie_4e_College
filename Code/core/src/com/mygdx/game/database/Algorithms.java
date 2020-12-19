package com.mygdx.game.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Class which only contains algorithms used in other classes
 */
public class Algorithms {

    static Random random;
    static String finalStr;
    static double[] numb;
    static int[] num;
    static Map<Integer, String> map;

    /**
     * Initialize a hashmap, the values represent the different materials
     */
    public static void initMap(){
        map = new HashMap<Integer, String>();
        map.put(0, "I");
        map.put(1, "A");
        map.put(2, "C");
        map.put(3, "K");
        map.put(4, "N");
        map.put(5, "R");
        map.put(6, "S");
        map.put(7, "G");
        map.put(8, "L");
    }

    /**
     * Divide the message into multiple lines
     * @param message the text that we want to treat
     * @return the different lines
     */
    public static ArrayList<String> parseMessage(String message){
        int nbOfWordsPerLine = 11;
        ArrayList<String> array = new ArrayList<String>();
        String[] words = message.split(" ");
        int length = words.length;
        int nbLine = (int)Math.ceil((double)(length)/nbOfWordsPerLine);
        String sentence;
        for(int i = 0; i<nbLine; i++){
            sentence = "";
            for(int j = 0; j<nbOfWordsPerLine; j++){
                try{
                    sentence += words[i*nbOfWordsPerLine+j];
                    sentence += " ";
                    sentence.trim();
                } catch (ArrayIndexOutOfBoundsException e){

                }
            }
            array.add(sentence);
        }
        return array;
    }

    /**
     * Compare arrays by telling if one has values bigger than another
     * @param needed array of the different amounts needed in a transaction
     * @param available array of the diffrent quantities of materials available
     * @return true if the quantities in the avaiable array are higher than in the needed array
     */
    public static boolean compareArrays(int[] needed, float[] available){
        for (int i = 0; i<needed.length; i++){
            if(needed[i]>available[i]) return false;
        }
        return true;
    }

    /**
     * Method used in a #generatePlanetTypeCode
     * It's used to replace a syntax like this : val1 == val2 || val1 == val3 || val1 == val4 etc...
     * @param value number of recursions of the function
     * @return false if all the values were identical
     */
    private static boolean decide(int value){
        for(int i = 0; i<value; i++){
            if(num[value] == num[i]) return true;
        }
        return false;
    }

    /**
     * Calculate the flight time for certain origins and destinations
     * @param gearType rocket type
     * @param destinationSystemX x-coordinate of the destination system
     * @param destinationSystemY y-coordinate of the destination system
     * @param currentSystemX x-coordinate of the current system
     * @param currentSystemY y-coordinate of the current system
     * @return the calculated flight time
     */
    public static float calculateFlightTime(String gearType, float destinationSystemX, float destinationSystemY, float currentSystemX, float currentSystemY){
        float speed = 10;
        if(gearType.equals("Orbiter")) speed = 10;
        else if(gearType.equals("Lander")) speed = 5;
        else if(gearType.equals("Cargo")) speed = 0.1f;
        float fictionalDistance = Algorithms.getDistance(currentSystemX, currentSystemY, destinationSystemX, destinationSystemY)*10000;
        float flightDuration = fictionalDistance/speed;
        System.out.println(flightDuration);
        return flightDuration;
    }

    /**
     *
     * @param separator
     * @param table
     * @return
     */
    public static String joinTable(String separator, String[] table){
        String result = "";
        for (int i = 0; i<table.length-1; i++){
            result += table[i];
            result += separator;
        }
        result += table[table.length-1];
        return result;
    }

    /**
     * Function that has been used to generate some codes that have been inserted into the DB
     * These codes are used to determine which materials are available on each planet
     * @return the generated code.
     */
    public static String generatePlanetTypeCode (){
        random = new Random();
        finalStr = "";
        numb = new double[9];
        num = new int[9];
        num[8] = -1;
        for(int i = 0; i<9; i++){
            numb[i] = random.nextGaussian();
        }
        for(int i = 0; i<9; i++){
            if(numb[i] <= 2.02){num[i] = random.nextInt(7);} else num[i] = 7 + random.nextInt(2);
            if(i == 0) continue;
            while(decide(i)){
                if(i == 7){
                    if(num[0] < 7 && num[1] < 7 && num[2] < 7 && num[3] < 7 && num[4] < 7 && num[5] < 7 && num[6] < 7){
                        num[7] = 7 + random.nextInt(2);
                    } else num[7] = random.nextInt(7);
                }
                else if(i == 8){
                    num[8]++;
                }
                else{
                    if(numb[i] <= 2.02){num[i] = random.nextInt(7);} else num[i] = 7 + random.nextInt(2);
                }
            }
        }
        for(int i = 0; i<9; i++){
            if(map.get(num[i]) == null) continue;
            finalStr += map.get(num[i]);
        }
        return finalStr;
    }

    /**
     * Calculate the distance between 2 points
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return the distance bet the point A(x1, y1) and B(x2, y2)
     */
    public static float getDistance(float x1, float y1, float x2, float y2){
        float distance = (float) Math.sqrt(Math.pow((x2-x1),2)+Math.pow((y2-y1),2));
        return distance;
    }

    /**
     * Make a copy of a file
     * @param source source path of the file
     * @param dest destination path of the file
     * @return true if the file has been copied
     */
    public static boolean copyFile(String source,String dest) {
        byte[] buffer = new byte[10000];
        int length;
        try {
            FileInputStream  input  = new FileInputStream(source);
            FileOutputStream output = new FileOutputStream(dest);

            while((length=input.read(buffer)) != -1) {
                output.write(buffer, 0, length);
            }
            System.out.println("File copied");
            output.close();
            input.close();
        }
        catch(IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Get the last modified file in a given folder
     * @param folder the path of the file.
     * @return the name of the last modified file
     */
    public static String lastModifiedFile(String folder){
        ArrayList<Long> dates = new ArrayList<Long>();
        File[] files = new File(folder).listFiles();
        for(File file : files){
            dates.add(file.lastModified());
        }
        return files[dates.indexOf(Collections.max(dates))].getName();
        //return "UU.db";
    }

    /**
     * (StackOverFlow)
     * Class containing two integers.
     */
    public static class IntContainer {
        public int a;
        public int b;

        public IntContainer(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass())
                return false;
            IntContainer that = (IntContainer) o;
            if (a != that.a) return false;
            return b == that.b;
        }

        @Override
        public int hashCode() {
            int result = a;
            result = 31 * result + b;
            return result;
        }

        @Override
        public String toString() {
            return "{" + a + "," + b + "}";
        }
    }
}