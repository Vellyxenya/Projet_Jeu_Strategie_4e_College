package com.mygdx.game.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.Assets;
import com.mygdx.game.Const;
import com.mygdx.game.DragonCurveGenerator;
import com.mygdx.game.ProjectSurvival;
import com.mygdx.game.database.DataBase;
import com.mygdx.game.database.Algorithms;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Random;

/**
 * THE Class that has been used all along the development process to modify and update the database
 * Once the game is completed, this class could be removed, since all the informations that have been
 * created and structured here are in the main database in android/assets/...
 */
public class ConstructDataBaseScreen extends MenuScreens {

    public static float[] dragonCurve1;
    public static float[] dragonCurve2;
    public static float[] dragonCurve3;
    public static float[] dragonCurve4;
    private static final int RECURSIONS = 12;
    DataBase db;
    String query = "";
    int temps = 0;

    List<Algorithms.IntContainer> array;
    List<Algorithms.IntContainer> norepetition;
    Set<Algorithms.IntContainer> withrepetition;
    Iterator i1, i2;
    Object object;

    public static float[] all;

    /**
     * Constructor allowing us to basiclally initialize the database.
     * @param game reference to the current game.
     */
    public ConstructDataBaseScreen(ProjectSurvival game){
        super();
        this.game = game;
        db = new DataBase(true, "database.db");
        /*dragonCurve1 = DragonCurveGenerator.generateDragonCurve(Const.WORLD_WIDTH, Const.WORLD_HEIGHT, RECURSIONS, new Vector2(5, 0));
        dragonCurve2 = DragonCurveGenerator.generateDragonCurve(Const.WORLD_WIDTH, Const.WORLD_HEIGHT, RECURSIONS, new Vector2(-5, 0));
        dragonCurve3 = DragonCurveGenerator.generateDragonCurve(Const.WORLD_WIDTH, Const.WORLD_HEIGHT, RECURSIONS, new Vector2(0, 5));
        dragonCurve4 = DragonCurveGenerator.generateDragonCurve(Const.WORLD_WIDTH, Const.WORLD_HEIGHT, RECURSIONS, new Vector2(0, -5));
        all = joinArray(dragonCurve1, dragonCurve2, dragonCurve3, dragonCurve4);
        System.out.println(dragonCurve1.length);
        System.out.println(dragonCurve2.length);
        System.out.println(dragonCurve3.length);
        System.out.println(dragonCurve4.length);
        System.out.println(all.length);

        List<Algorithms.IntContainer> array = new LinkedList<Algorithms.IntContainer>(Arrays.<Algorithms.IntContainer>asList());
        for (int i = 0; i<all.length; i+=2){
            array.add(new Algorithms.IntContainer((int)all[i], (int)all[i+1]));
        }
        List<Algorithms.IntContainer> norepetition = new ArrayList<Algorithms.IntContainer>();
        Set<Algorithms.IntContainer> withrepetition = new HashSet<Algorithms.IntContainer>();
        for (Algorithms.IntContainer element : array) {
            if (Collections.frequency(array, element) > 1) {
                withrepetition.add(element);
            } else {
                norepetition.add(element);
            }
        }

        System.out.println("NoRep: " + Arrays.toString(norepetition.toArray()));
        System.out.println("Rep: " + Arrays.toString(withrepetition.toArray()));
        System.out.println(norepetition.size());
        System.out.println(withrepetition.size());*/
    }

    /**
     * a lot of functions that have been used to insert data related to the planets and systems in the database
     */

    private String generateStarType(int x, int y){
        String types = "OWBAFGKMCSLZ";
        String likely = "K";
        Random r;
        int randomNum;
        double distance = Math.sqrt(Math.pow(x-800, 2)+Math.pow(y-500, 2));
        r = new Random();
        randomNum = r.nextInt(100);
        if(distance<10){
            likely = "O";
            if(randomNum < 80) return likely;
            else {
                r = new Random();
                likely = ""+types.charAt(r.nextInt(12));
                return likely;
            }
        }
        else if(distance>10 && distance<30){
            likely = "W";
            if(randomNum < 80) return likely;
            else {
                r = new Random();
                likely = ""+types.charAt(r.nextInt(12));
                return likely;
            }
        }
        else if(distance>30 && distance<60){
            likely = "B";
            if(randomNum < 80) return likely;
            else {
                r = new Random();
                likely = ""+types.charAt(r.nextInt(12));
                return likely;
            }
        }
        else if(distance>60 && distance<100){
            likely = "A";
            if(randomNum < 80) return likely;
            else {
                r = new Random();
                likely = ""+types.charAt(r.nextInt(12));
                return likely;
            }
        }
        else if(distance>100 && distance<150){
            likely = "F";
            if(randomNum < 80) return likely;
            else {
                r = new Random();
                likely = ""+types.charAt(r.nextInt(12));
                return likely;
            }
        }
        else if(distance>150 && distance<210){
            likely = "G";
            if(randomNum < 80) return likely;
            else {
                r = new Random();
                likely = ""+types.charAt(r.nextInt(12));
                return likely;
            }
        }
        else if(distance>210 && distance<270){
            likely = "K";
            if(randomNum < 80) return likely;
            else {
                r = new Random();
                likely = ""+types.charAt(r.nextInt(12));
                return likely;
            }
        }
        else if(distance>270 && distance<320){
            likely = "M";
            if(randomNum < 80) return likely;
            else {
                r = new Random();
                likely = ""+types.charAt(r.nextInt(12));
                return likely;
            }
        }
        else if(distance>320 && distance<360){
            likely = "C";
            if(randomNum < 80) return likely;
            else {
                r = new Random();
                likely = ""+types.charAt(r.nextInt(12));
                return likely;
            }
        }
        else if(distance>360 && distance<390){
            likely = "S";
            if(randomNum < 80) return likely;
            else {
                r = new Random();
                likely = ""+types.charAt(r.nextInt(12));
                return likely;
            }
        }
        else if(distance>390 && distance<410){
            likely = "L";
            if(randomNum < 80) return likely;
            else {
                r = new Random();
                likely = ""+types.charAt(r.nextInt(12));
                return likely;
            }
        }
        else if(distance>410){
            likely = "Z";
            if(randomNum < 80) return likely;
            else {
                r = new Random();
                likely = ""+types.charAt(r.nextInt(12));
                return likely;
            }
        }
        return likely;
    }

    private String generateStarName(){
        final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String code = "";
        Random r = new Random();
        for (int i = 0; i<8; i++){
            code += alphabet.charAt(r.nextInt(36));
        }
        return code;
    }

    private String generatePlanetName(){
        final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String code = "PX-";
        Random r = new Random();
        for (int i = 0; i<7; i++){
            code += alphabet.charAt(r.nextInt(26));
        }
        return code;
    }

    private float generateDistance(){
        Random r = new Random();
        float distance = 100 + r.nextInt(900);
        return distance;
    }

    private int oxygen(){
        Random r = new Random();
        int nb = r.nextInt(4);
        if(nb == 3) return 1;
        return 0;
    }

    private int water(){
        Random r = new Random();
        int nb = r.nextInt(8);
        if(nb == 7) return 1;
        return 0;
    }

    private float generateAtmosphereQuantity(){
        Random r = new Random();
        float nb = (float)(r.nextInt(200))/100f;
        return nb;
    }

    private float generateMagneticField(){
        Random r = new Random();
        float nb = (float)(r.nextInt(400))/100f;
        return nb;
    }

    private float generateDensity(){
        Random r = new Random();
        float nb = 0.5f+(float)(r.nextInt(950))/100f;
        return nb;
    }

    /**
     * Function used to merge 2 different arrays into one.
     * https://www.mkyong.com/java/java-how-to-join-arrays/
     * @param arrays the arrays to join
     * @return the newly created array
     */
    static float[] joinArray(float[]... arrays) {
        int length = 0;
        for (float[] array : arrays) {
            length += array.length;
        }
        final float[] result = new float[length];
        int offset = 0;
        for (float[] array : arrays) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    /**
     * Only used to be sure that the screen has successfully been opened.
     * Used all along the development to update the database with new queries.
     * There are still some of the last queries
     * @param delta
     */
    @Override
    public void render(float delta) {
        super.render(delta);
        renderer.begin();
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(0, 0, Const.WORLD_WIDTH, Const.WORLD_HEIGHT, Color.GRAY, Color.GRAY, Color.BLACK, Color.BLACK);
        renderer.end();
        temps++;

        Algorithms.initMap(); //Initialize the map corresponding the the diffrent materials
        if(temps == 10) { //Create queries...
            //query += "DELETE FROM Accounts";
            //query += "ALTER TABLE Systems ADD Description VARCHAR DEFAULT ''";
            //query += "UPDATE Systems SET Discovered = 'NO' ;";
            //query += "UPDATE Systems SET Scanned    = 'NO' ;";
            //query += "UPDATE Systems SET Scanned    = 'YES' WHERE SystemName = 'O4W72Z9Q' ;";
            //query += "UPDATE Systems SET Discovered = 'YES' WHERE SystemName = 'O4W72Z9Q' ;";
            //query += "UPDATE Systems SET Discoverd  = 'YES' WHERE id =  ;";
            //query += "INSERT INTO Equipment ('EquipmentName', 'EquipmentDescription', 'Weight', 'Unlocked', 'Quantity') VALUES ('Singularizer', 'Used_to_simulate_a_black_hole', 10000, 0, 1); ";
            //query += "ALTER TABLE Player ADD Scenario INTEGER DEFAULT 0 ;";
            //query += "ALTER TABLE Planet ADD Characteristics VARCHAR DEFAULT '' ;";
            //query += "UPDATE Planet SET Characteristics = ";
            /*for (int i = 1; i<=30106; i++){
                query += "UPDATE Planet SET PlanetType = '"+Algorithms.generatePlanetTypeCode()+"' WHERE id = "+i+" ; " ;
                if(i%1000 == 0) System.out.println("cursor : "+i);
            }*/
            //query += "UPDATE Material SET Quantities = 0 WHERE MaterialName IN('Geniusium', 'Lentilium') ;";
            //query += "UPDATE TABLE Planet SET PlanetType = '' WHERE ";
            //query += "ALTER TABLE Flights ADD collectedMaterials VARCHAR ;";
            //query += "ALTER TABLE Flights ADD EquippedModules VARCHAR(100)";
            //query += "ALTER TABLE Flights ADD FlightDuration VARCHAR";
            //query += "ALTER TABLE Flights ADD TakeOffTime VARCHAR";
            //query += "DELETE FROM Flights ;";
            //query += "ALTER TABLE Rocket ADD EquippedModules VARCHAR(100) ; ";
            //query += "UPDATE Equipment SET Quantity = 1 ; ";
            //query += "UPDATE Rocket SET Quantity = 0 ; ";
        }
        if(temps == 15) System.out.println("query constructed"); //See if the queries have been created successfully
        if(temps == 20) { //Execute the queries.
            /*for (int i = 28160; i<=30106; i++){
                query = "UPDATE Planet SET PlanetType = '"+Algorithms.generatePlanetTypeCode()+"' WHERE id = "+i+" ; " ;
                System.out.println(i);
                try{
                    db.updateDB(query);
                }catch (SQLException e){e.printStackTrace();}
            }*/
            try{
                db.updateDB(query);
            }catch (SQLException e){e.printStackTrace();}
        }
        if(temps == 25) System.out.println("updated DBBBB");
        if(temps == 30) db.closeDB(); //close the database once all modifications have successfully been done.
        if(temps == 35){
            game.setScreen(new HomeScreen(game)); //Return to the main screen
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        Assets.dispose();
    }
}
