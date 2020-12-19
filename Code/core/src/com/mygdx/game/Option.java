package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.database.Algorithms;
import com.mygdx.game.database.DataBase;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.HangarScreen;
import com.mygdx.game.screens.MyScreens;
import com.mygdx.game.screens.ObservatoryScreen;
import com.mygdx.game.screens.TheEnd;
import com.mygdx.game.storyline.Script;
import com.mygdx.game.structuredVariables.Equipment;
import com.mygdx.game.structuredVariables.MaterialManager;

import java.sql.SQLException;
import java.util.ArrayList;

import static com.mygdx.game.database.Algorithms.parseMessage;
import static com.mygdx.game.database.Algorithms.calculateFlightTime;

/**
 * Options displayed to each element when clicking on it
 */
public class Option extends Clickable {

    private Vector2 dimensions;
    private String display;
    float textWidth, textHeight;
    private Element parent;
    private int id;
    public static boolean triggered = false;
    static boolean abortChange = false;
    public boolean active = false;
    public boolean displayFavourites = false;

    private Option planet;
    private Option system;
    private boolean noMore = false; //blocks the creation of other options
    private Option s;

    /**
     * Initializes variables and parameters
     * @param display txt displayed by the option
     * @param parent Element which created this element
     * @param id id of the option
     */
    public Option(ProjectSurvival game, SpriteBatch batch, ShapeRenderer renderer, Vector2 position, Vector2 dimensions, String display, Element parent, int id){
        this.display = display;
        this.dimensions = dimensions;
        this.game = game;
        this.batch = batch;
        this.renderer = renderer;
        this.position = position;
        this.parent = parent;
        this.id = id;

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/goodTimes.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        layout = new GlyphLayout();

        parameter.size = 16;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        font = generator.generateFont(parameter);
        generator.dispose();

        chooseOptions();
    }

    /**
     * Filter default options depending on certain parameters like locations, state of the flight,
     * equipped modules etc...
     */
    private void chooseOptions(){
        if(this.display.equals("LAND")){
            String modules = GameScreen.database.getOneData("SELECT EquippedModules FROM Flights WHERE id = "+parent.names[0]);
            if(parent.names[1].equals("Lander")){
                System.out.println("First step");
                if(modules.contains("SI") && !parent.names[2].contains("PX-")){
                    String starType = GameScreen.database.getOneData("SELECT StarType FROM Systems WHERE SystemName = '"+parent.names[2]+"' ;");
                    System.out.println("Second step : "+starType);
                    if(starType.equals("Z")) this.display = "SINGULARIZE THIS STAR";
                }
            }
        }
        if(this.display.equals("NEW DESTINATION")){
            planet = new Option(game, batch, renderer, new Vector2(position.x-100, position.y), dimensions, "GO TO PLANET", parent, id);
            planet.noMore = true;
            system = new Option(game, batch, renderer, new Vector2(position.x+100, position.y), dimensions, "GO TO SYSTEM", parent, id);
            system.noMore = true;
            this.display = "";
        }
        if(this.display.equals("COLLECT")){
            planet = new Option(game, batch, renderer, new Vector2(position.x-100, position.y), dimensions, "REFUEL", parent, id);
            planet.noMore = true;
            String modules = GameScreen.database.getOneData("SELECT EquippedModules FROM Flights WHERE id = "+parent.names[0]);
            if(modules.contains("CO")){
                system = new Option(game, batch, renderer, new Vector2(position.x+100, position.y), dimensions, "COLLECT MATERIALS", parent, id);
                system.noMore = true;
            }
            this.display = "";
        }
        if(this.display.equals("BUILD")){
            int unlocked = 1;
            if(parent.type.equals("Rocket"))
                unlocked = Integer.parseInt(GameScreen.database.getOneData("SELECT Unlocked FROM Rocket WHERE RocketName = '"+parent.names[0]+"' ;"));
            else if(parent.type.equals("Equipment"))
                unlocked = Integer.parseInt(GameScreen.database.getOneData("SELECT Unlocked FROM Equipment WHERE EquipmentName = '"+parent.names[0]+"' ;"));
            if(unlocked == 0) this.display = "HOW TO UNLOCK";
        }
        if(this.display.equals("SCAN")){
            String modules = GameScreen.database.getOneData("SELECT EquippedModules FROM Flights WHERE id = "+parent.names[0]);
            if(parent.names[2].contains("PX-")){
                if(modules.contains("S3")) {
                    //if (!modules.contains("S1") && !modules.contains("S2")) layout = 100;
                    s = new Option(game, batch, renderer, new Vector2(position.x, position.y), dimensions, "SCAN PLANET", parent, id);
                    s.noMore = true;
                    this.display = "";
                    return;
                }
            }
            else{
                if(modules.contains("S2") || modules.contains("S1")) {
                    s  = new Option(game, batch, renderer, new Vector2(position.x, position.y), dimensions, "MID-RANGE SCANNER", parent, id);
                    s.noMore = true;
                    if(!modules.contains("S1")) {
                        this.display = "";
                        return;
                    }
                    else s.display = "LONG-RANGE SCANNER";
                    this.display = "";
                    return;
                }
            }
            this.display = "";
        }
    }

    /**
     * Render the options and sometimes create new ones
     * @param y where it should render other new options too or not
     */
    public void render(float y){
        manageActions();
        batch.begin();
        this.position.y = y+id*20-30;
        if (mouseOn(dimensions.x, dimensions.y))drawText(Color.RED);
        else drawText(Color.WHITE);
        batch.end();
        if(!noMore) renderOthers(y);
    }

    /**
     * render other options
     */
    private void renderOthers(float y){
        if(planet != null) planet.render(y);
        if(system != null) system.render(y);
        if(s != null) s.render(y);
    }

    /**
     * Draw the text displayed by the option...
     * @param color ...with a certain color
     */
    private void drawText(Color color){
        font.setColor(color);
        layout.setText(font, display);
        textWidth = layout.width;
        textHeight = layout.height;
        font.draw(batch, display, position.x - textWidth/2, position.y+textHeight/2);
    }

    /**
     * Order to collect materials that are on the planet
     */
    private void collectMaterials(){
        String availableMaterials = GameScreen.database.getOneData("SELECT PlanetType FROM Planet WHERE PlanetName = '"+parent.names[2]+"' ;");
        float level = Float.parseFloat(GameScreen.database.getOneData("SELECT Level FROM Player WHERE id = 1 ;"));
        if(level < 6) availableMaterials = availableMaterials.replace("G", "");
        if(level < 7) availableMaterials = availableMaterials.replace("L", "");
        if(level == 6){
            if(availableMaterials.contains("G")){
                GameScreen.database.updateDataBase("UPDATE Player SET Level = Level+1 WHERE id = 1 ;");
                GameScreen.database.updateDataBase("UPDATE Equipment SET Unlocked = 1 WHERE EquipmentName = 'CPU' ;");
            }
        }
        if(level == 7){
            if(availableMaterials.contains("L")){
                GameScreen.database.updateDataBase("UPDATE Player SET Level = Level+1 WHERE id = 1 ;");
                GameScreen.database.updateDataBase("UPDATE Equipment SET Unlocked = 1 WHERE EquipmentName = 'Scanner1' ;");
            }
        }
        GameScreen.database.updateDataBase("UPDATE Flights SET collectedMaterials = '"+availableMaterials+"' WHERE id = "+parent.names[0]);
        System.out.println("Materials collected ! "+availableMaterials);
    }

    /**
     * Order to refuel with kerosene
     */
    private void refuel(){
        try{
            GameScreen.database.updateDB("UPDATE Flights SET Fuel = 1");
            System.out.println("Refueled !");
        } catch(java.sql.SQLException e) {System.out.println(e.getMessage());}
    }

    /**
     * Order to scan the planet we're on to find water, oxygen etc...
     */
    private void scanPlanet(){
        String modules = GameScreen.database.getOneData("SELECT EquippedModules FROM Flights WHERE id = "+parent.names[0]);
        if (modules.contains("DO")) {
            System.out.println("CONTAINS DO");
            GameScreen.database.updateDataBase("UPDATE Planet SET Characteristics = Characteristics || 'O' WHERE PlanetName = '"+parent.names[2]+"' ;");
            System.out.println("The condition : "+Integer.parseInt(GameScreen.database.getOneData("SELECT Oxygen FROM Planet WHERE PlanetName = '"+parent.names[2]+"' ;")));
            if(Integer.parseInt(GameScreen.database.getOneData("SELECT Oxygen FROM Planet WHERE PlanetName = '"+parent.names[2]+"' ;")) == 1){//Faudra mettre un 1 ici.
                if((int)Float.parseFloat(GameScreen.database.getOneData("SELECT Level FROM Player WHERE id = 1")) == 1){
                    GameScreen.database.updateDataBase("UPDATE Player SET Level = Level+1 WHERE id = 1 ;");
                    GameScreen.database.updateDataBase("UPDATE Equipment SET Unlocked = 1 WHERE EquipmentName = 'dMagnetic' ;");
                }
                System.out.println("DOXYGEN ON DUTY");
            }
        }
        if (modules.contains("DM")) {
            System.out.println("CONTAINS DM");
            GameScreen.database.updateDataBase("UPDATE Planet SET Characteristics = Characteristics || 'M' WHERE PlanetName = '"+parent.names[2]+"' ;");
            if(Float.parseFloat(GameScreen.database.getOneData("SELECT MagneticField FROM Planet WHERE PlanetName = '"+parent.names[2]+"' ;")) > 0){//Faudra mettre une autre valeur ici.
                if((int)Float.parseFloat(GameScreen.database.getOneData("SELECT Level FROM Player WHERE id = 1")) == 2){
                    GameScreen.database.updateDataBase("UPDATE Player SET Level = Level+1 WHERE id = 1 ;");
                    GameScreen.database.updateDataBase("UPDATE Equipment SET Unlocked = 1 WHERE EquipmentName = 'dWater' ;");
                }
                System.out.println("DMAGNETIC ON DUTY");
            }
        }
        if (modules.contains("DW")) {
            System.out.println("CONTAINS DW");
            GameScreen.database.updateDataBase("UPDATE Planet SET Characteristics = Characteristics || 'W' WHERE PlanetName = '"+parent.names[2]+"' ;");
            if(Integer.parseInt(GameScreen.database.getOneData("SELECT Water FROM Planet WHERE PlanetName = '"+parent.names[2]+"' ;")) >= 0){//Faudra mettre un ==1 ici.
                if((int)Float.parseFloat(GameScreen.database.getOneData("SELECT Level FROM Player WHERE id = 1")) == 3){
                    GameScreen.database.updateDataBase("UPDATE Player SET Level = Level+1 WHERE id = 1 ;");
                    GameScreen.database.updateDataBase("UPDATE Equipment SET Unlocked = 1 WHERE EquipmentName = 'Robot' ;");
                    GameScreen.database.updateDataBase("UPDATE Rocket SET Unlocked = 1 WHERE RocketName = 'Lander' ;");
                    GameScreen.database.updateDataBase("UPDATE Player SET Xp = (Xp+10000) WHERE id = 1 ;");
                }
                System.out.println("DMAGNETIC ON DUTY");
            }
        }
        if(GameScreen.database.getOneData("SELECT Scanned FROM Planet WHERE PlanetName = '"+parent.names[2]+"' ;").equals("YES")) return;
        GameScreen.database.updateDataBase("UPDATE Planet SET Scanned = 'YES' WHERE PlanetName = '"+parent.names[2]+"'");
        int nbScannedPlanets = Integer.parseInt(GameScreen.database.getOneData("SELECT Count(Scanned) FROM Planet WHERE Scanned = 'YES' ;"));
        if(nbScannedPlanets == 2){
            GameScreen.database.updateDataBase("UPDATE Player SET Level = Level+1 WHERE id = 1 ;");
            GameScreen.database.updateDataBase("UPDATE Equipment SET Unlocked = 1 WHERE EquipmentName = 'dOxygen' ;");
            System.out.println("DONE!!!!!!!!!!!!!!!!!!!!!!!!!"+GameScreen.database.getOneData("SELECT Level FROM Player"));
        }
        System.out.println("Nb of Scans : "+nbScannedPlanets);
    }

    /**
     * Scan the systems in a certain range to discover the planets that they contain
     * @param radius radius of scanning
     */
    private void scanSystems(int radius){
        String currentSystemX = "nothing",  currentSystemY = "nothing";
        try {
            currentSystemX = GameScreen.database.getOneData("SELECT SystemX FROM Systems WHERE" +
                    " SystemName = '" + parent.names[2] + "' ;");
            currentSystemY = GameScreen.database.getOneData("SELECT SystemY FROM Systems WHERE" +
                    " SystemName = '" + parent.names[2] + "' ;");

            GameScreen.database.updateDB("UPDATE Systems SET Scanned = 'TRUE' " +
                    "WHERE SQRT((SystemX - " + currentSystemX + ") * (SystemX - " + currentSystemX + ")" +
                    " + (SystemY - " + currentSystemY + ") * (SystemY - " + currentSystemY + ")) <= " +
                    radius + " ;");

            System.out.println("Scanned the systems !");
        } catch(java.sql.SQLException e) {System.out.println(e.getMessage());}
        String modules = GameScreen.database.getOneData("SELECT EquippedModules FROM Flights WHERE id = "+parent.names[0]);
        if (modules.contains("S2")) {
            System.out.println("CONTAINS S2");
            //Ici, faudra surcharger les Characteristics et tout scanner...
            //GameScreen.database.updateDataBase("UPDATE Planet SET Characteristics = Characteristics || 'W' WHERE PlanetName = '"+parent.names[2]+"' ;");
            if((int)Float.parseFloat(GameScreen.database.getOneData("SELECT Level FROM Player WHERE id = 1")) == 5){
                GameScreen.database.updateDataBase("UPDATE Player SET Level = Level+1 WHERE id = 1 ;");
                GameScreen.database.updateDataBase("UPDATE Equipment SET Unlocked = 1 WHERE EquipmentName = 'Container' ;");
            }
            System.out.println("S2 ON DUTY");
        }
        if (modules.contains("S1")) {
            GameScreen.database.updateDataBase("UPDATE Systems SET Favourite = 1, Description = 'May_welcome_life' " +
                    "WHERE SQRT((SystemX - " + currentSystemX + ") * (SystemX - " + currentSystemX + ")" +
                    " + (SystemY - " + currentSystemY + ") * (SystemY - " + currentSystemY + ")) <= " +
                    radius + " AND id IN(SELECT id FROM Systems WHERE id IN (SELECT SystemId FROM Planet WHERE Water = 1 AND Oxygen = 1 AND AtmosphereQuantity >1.5 AND Density >3 AND MagneticField >2 AND Distance <500));");
            if((int)Float.parseFloat(GameScreen.database.getOneData("SELECT Level FROM Player WHERE id = 1")) == 5){
                GameScreen.database.updateDataBase("UPDATE Player SET Level = Level+1 WHERE id = 1 ;");
                GameScreen.database.updateDataBase("UPDATE Equipment SET Unlocked = 1 WHERE EquipmentName = 'Container' ;");
            }
            System.out.println("S2 ON DUTY");
        }
    }

    /**
     * Displays in the container message how to unlock a certain rocket or equipment
     */
    private void howToUnlock(){
        if(parent.type.equals("Rocket")){
            String rocket = parent.names[0];
            parent.c.messages = parseMessage(Script.rocketUnlocks.get(rocket));
        }
        if(parent.type.equals("Equipment")){
            String module = parent.names[0];
            parent.c.messages = parseMessage(Script.moduleUnlocks.get(module));
        }
    }

    /**
     * Displays some informations about a given rocket, equipment or material
     */
    private void informations(){
        if(parent.type.equals("Rocket")){
            String rocket = parent.names[0];
            parent.c.messages = parseMessage(Script.rocketInformations.get(rocket));
        }
        if(parent.type.equals("Equipment")){
            String module = parent.names[0];
            parent.c.messages = parseMessage(Script.moduleInformations.get(module));
        }
        if(parent.type.equals("Material")){
            String material = parent.names[0];
            parent.c.messages = parseMessage(Script.materialInformations.get(material));
        }
    }

    /**
     * Shows the required materials to craft this equipment
     */
    private void requiredMaterials(){
        String message = "";
        for(int i = 0; i<9; i++){
            message += Equipment.requiredMaterials[Equipment.mat.get(parent.names[0])][i];
            message += " ";
            message += MaterialManager.materials[i];
            message += "; ";
        }

        parent.c.messages = parseMessage(message);

        /*if(parent.type.equals("Equipment")){
            String module = parent.names[0];
            parent.c.messages = parseMessage(Script.moduleInformations.get(module));
        }*/
    }

    /**
     * Displays the locations at which we can find some materials
     */
    private void locations(){
        if(parent.type.equals("Material")){
            String material = parent.names[0];
            parent.c.messages = parseMessage(Script.materialLocations.get(material));
            System.out.println(Script.materialLocations.get(material));
        }
    }

    private void addMoney(int money){
        GameScreen.database.updateDataBase("UPDATE Player SET Xp = (Xp+"+money+") WHERE id = 1;");
    }

    /**
     * Execute a lot of different actions depending on the text displayed by the option
     * Most of the below code use SQL queries to access data within the database and update it.
     * ---This is one of the cores of the game---
     */
    private void manageActions(){
        if(triggered) {
            active = false;
            if(mouseOn(dimensions.x, dimensions.y)){
                active = true;
                abortChange = true;

                if(display.equals("GO TO PLANET")){
                    displayFavourites = true;
                    MyScreens.currentFlightId = Integer.parseInt(parent.names[0]);
                    MyScreens.currentOrigin = parent.names[2];
                    MyScreens.unDisplay = true;
                    MyScreens.nb = 5;
                    MyScreens.displaySpecific = true;
                }
                else if(display.equals("GO TO SYSTEM")){
                    displayFavourites = true;
                    MyScreens.currentFlightId = Integer.parseInt(parent.names[0]);
                    MyScreens.currentOrigin = parent.names[2];
                    MyScreens.unDisplay = true;
                    MyScreens.nb = 6;
                    MyScreens.displaySpecific = true;
                }
                else if(display.equals("BUILD")){
                    if(parent.type.equals("Equipment")){
                        float[] availableMaterials = new float[9];
                        int[] neededMaterials = Equipment.getNeededMaterials(parent.names[0]);
                        System.out.println(parent.names[0]+"    ---    "+neededMaterials);
                        String equipmentName = parent.names[0];
                        try {
                            for(int i = 0; i<9; i++){
                                availableMaterials[i] = Float.parseFloat(GameScreen.database.getOneData("SELECT Quantities FROM Material WHERE MaterialName = '"+MaterialManager.materials[i]+"' ;"));
                            }
                            if(Algorithms.compareArrays(neededMaterials, availableMaterials)){
                                GameScreen.database.updateDB("UPDATE Equipment SET Quantity = Quantity+1 WHERE EquipmentName = '" + equipmentName+"' ;");
                                for (int i = 0; i<9; i++){
                                    GameScreen.database.updateDB("UPDATE Material SET Quantities = Quantities-"+neededMaterials[i]+" WHERE MaterialName = '" +MaterialManager.materials[i]+"' ;");
                                }
                                System.out.println("New Equipment crafted! : "+equipmentName);
                            }
                            else {
                                parent.c.messages = parseMessage("You don't have enough resources to build this equipment");
                            }
                        }catch (SQLException e){
                            System.out.println("Problem in Option>manageActions");
                        }
                    }
                    else if(parent.type.equals("Rocket")){
                        displayFavourites = true;
                        MyScreens.unDisplay = true;
                        MyScreens.nb = 3;
                        MyScreens.displaySpecific = true;
                        HangarScreen.containers.get(2).addActionButton(new ActionButton(game, batch, renderer, new Vector2(1000, 120), new Vector2(200, 15), "Craft Rocket!", this.parent.c));
                        HangarScreen.containers.get(2).setRocketName(parent.names[0]);
                    }
                }
                else if(display.equals("TAKE OFF")){
                    try {
                        try{
                            GameScreen.database.updateDB("INSERT INTO Flights ('Description', 'Origin', 'Destination', 'Fuel', 'EquippedModules') VALUES ('"+parent.names[1]+"', '"+Const.PLANET_NAME+"', '"+Const.PLANET_NAME+"', '0', '"+parent.names[3]+"')");
                        } catch(ArrayIndexOutOfBoundsException ex){
                            GameScreen.database.updateDB("INSERT INTO Flights ('Description', 'Origin', 'Destination', 'Fuel', 'EquippedModules') VALUES ('"+parent.names[1]+"', '"+Const.PLANET_NAME+"', '"+Const.PLANET_NAME+"', '0', '')");
                        }
                        GameScreen.database.updateDB("DELETE FROM BuiltRocket WHERE id = "+parent.names[0]+" ;");
                        System.out.println("New Flight! : "+parent.names[1]);
                        parent.delete = true;
                        parent.c.allowedToDelete = true;
                    }catch (SQLException e){
                        System.out.println("Problem in Option>manageActions");
                    }
                }
                else if(display.equals(Container.loadOptions[2])){
                    if(parent.type.equals("Accounts")){
                        DataBase database = null;
                        try {
                            String fileName = parent.names[0]+".db";
                            String directory = System.getProperty("user.dir");
                            String path = Gdx.files.absolute(directory + "/saves/" + fileName).path();
                            database = new DataBase(false, path);
                            Const.PLAYER_NAME = database.getOneData("SELECT PlayerName FROM Player;");
                            Const.PLANET_NAME = database.getOneData("SELECT PlanetName FROM Player;");
                            database.closeDB();
                            game.setScreen(new GameScreen(game, true));
                        }catch (NullPointerException e){
                            e.printStackTrace();
                            database.closeDB();
                        }
                    }
                }
                else if(display.equals("TRAVEL TO PLANET")){
                    try {
                        String gearType = GameScreen.database.getOneData("SELECT Description FROM Flights WHERE id = "+MyScreens.currentFlightId+" ;");
                        long currentTime = TimeUtils.millis();
                        int destinationSystemId   = Integer.parseInt(GameScreen.database.getOneData("SELECT SystemId FROM Planet WHERE PlanetName = '"+parent.names[0]+"' ;"));
                        float destinationSystemX  = Float.parseFloat(GameScreen.database.getOneData("SELECT SystemX FROM Systems WHERE id = "+destinationSystemId));
                        float destinationSystemY  = Float.parseFloat(GameScreen.database.getOneData("SELECT SystemY FROM Systems WHERE id = "+destinationSystemId));
                        System.out.println(MyScreens.currentOrigin);
                        int currentOriginId       = Integer.parseInt(GameScreen.database.getPlaOrSys(MyScreens.currentOrigin));
                        float currentSystemX      = Float.parseFloat(GameScreen.database.getOneData("SELECT SystemX FROM Systems WHERE id = "+currentOriginId));
                        float currentSystemY      = Float.parseFloat(GameScreen.database.getOneData("SELECT SystemY FROM Systems WHERE id = "+currentOriginId));

                        GameScreen.database.updateDB("UPDATE Flights SET Destination = '"+parent.names[0]+"' WHERE id = "+MyScreens.currentFlightId+" ;");
                        GameScreen.database.updateDB("UPDATE Flights SET TakeOffTime = '"+currentTime+"' WHERE id = "+MyScreens.currentFlightId+" ;");
                        float flightTime = (int) calculateFlightTime(gearType, destinationSystemX, destinationSystemY, currentSystemX, currentSystemY);
                        System.out.println("destX : "+destinationSystemX+" destY : "+destinationSystemY+" curX : "+currentSystemX+"curY : "+currentSystemY);
                        GameScreen.database.updateDB("UPDATE Flights SET FlightDuration = '"+flightTime+"' WHERE id = "+MyScreens.currentFlightId+" ;");
                        for(Elements e : ObservatoryScreen.containers.get(0).elements){
                            ((Element) e).names[3] = GameScreen.database.getOneData("SELECT Destination FROM Flights WHERE id = "+((Element) e).names[0]);
                        }
                        System.out.println("Changed destination! : "+MyScreens.currentFlightId);
                    }catch (SQLException e){
                        System.out.println("Problem in Option>manageActions");
                    }
                }
                else if(display.equals("TRAVEL TO SYSTEM")){
                    try {
                        String gearType = GameScreen.database.getOneData("SELECT Description FROM Flights WHERE id = "+MyScreens.currentFlightId+" ;");
                        long currentTime = TimeUtils.millis();
                        float destinationSystemX  = Float.parseFloat(GameScreen.database.getOneData("SELECT SystemX FROM Systems WHERE SystemName = '"+parent.names[0]+"' ;"));
                        float destinationSystemY  = Float.parseFloat(GameScreen.database.getOneData("SELECT SystemY FROM Systems WHERE SystemName = '"+parent.names[0]+"' ;"));
                        int currentOriginId       = Integer.parseInt(GameScreen.database.getPlaOrSys(MyScreens.currentOrigin));
                        float currentSystemX      = Float.parseFloat(GameScreen.database.getOneData("SELECT SystemX FROM Systems WHERE id = "+currentOriginId));
                        float currentSystemY      = Float.parseFloat(GameScreen.database.getOneData("SELECT SystemY FROM Systems WHERE id = "+currentOriginId));

                        GameScreen.database.updateDB("UPDATE Flights SET Destination = '"+parent.names[0]+"' WHERE id = "+MyScreens.currentFlightId+" ;");
                        GameScreen.database.updateDB("UPDATE Flights SET TakeOffTime = '"+currentTime+"' WHERE id = "+MyScreens.currentFlightId+" ;");
                        float flightTime = (int) calculateFlightTime(gearType, destinationSystemX, destinationSystemY, currentSystemX, currentSystemY);
                        System.out.println("destX : "+destinationSystemX+" destY : "+destinationSystemY+" curX : "+currentSystemX+"curY : "+currentSystemY);
                        GameScreen.database.updateDB("UPDATE Flights SET FlightDuration = '"+flightTime+"' WHERE id = "+MyScreens.currentFlightId+" ;");
                        for(Elements e : ObservatoryScreen.containers.get(0).elements){
                            ((Element) e).names[3] = GameScreen.database.getOneData("SELECT Destination FROM Flights WHERE id = "+((Element) e).names[0]);
                        }
                        System.out.println("Changed destination! : "+MyScreens.currentFlightId);
                    }catch (SQLException e){
                        System.out.println("Problem in Option>manageActions");
                    }
                }
                else if(display.equals("COLLECT MATERIALS")){
                    collectMaterials();
                }
                else if(display.equals("REFUEL")){
                    refuel();
                }
                else if(display.equals("SCAN PLANET")){
                    scanPlanet();
                    addMoney(200);
                }
                else if(display.equals("MID-RANGE SCANNER")){
                    scanSystems(100);
                    addMoney(500);
                }
                else if(display.equals("LONG-RANGE SCANNER")){
                    scanSystems(400);
                    addMoney(1000);
                }
                else if(display.equals("HOW TO UNLOCK")){
                    howToUnlock();
                }
                else if(display.equals("INFORMATIONS")){
                    informations();
                }
                else if(display.equals("REQUIRED MATERIALS")){
                    requiredMaterials();
                }
                else if(display.equals("LOCATIONS")){
                    locations();
                }
                else if(display.equals("LAND")){
                    String modules = GameScreen.database.getOneData("SELECT EquippedModules FROM Flights WHERE id = "+parent.names[0]);
                    if(parent.names[2].equals(Const.PLANET_NAME)){
                        String collectedMaterials = GameScreen.database.getOneData("SELECT collectedMaterials FROM Flights WHERE id = "+parent.names[0]);
                        char[] materials = collectedMaterials.toCharArray();
                        System.out.println(materials.length);
                        if(materials.length > 1){
                            for(int i = 0; i<5; i++){
                                GameScreen.database.updateDataBase("UPDATE Material SET Quantities = (Quantities+"+20000/Math.pow(2, i)+") " +
                                        "WHERE MaterialName = '"+ MaterialManager.materialCodes.get(materials[i])+"' ;");
                            }
                            addMoney(20000);
                        }
                        GameScreen.database.updateDataBase("DELETE FROM Flights WHERE id = "+parent.names[0]+" ;");
                        GameScreen.database.updateDataBase("INSERT INTO BuiltRocket('RocketName', 'EquippedModules') VALUES('"+parent.names[1]+"', '"+parent.names[4]+"') ;");
                        parent.delete = true;
                        parent.c.allowedToDelete = true;
                        System.out.println("LANDED successfully and updatedMaterials");
                    }
                    if (modules.contains("RO") && !parent.names[2].equals(Const.PLANET_NAME)) {
                        System.out.println("CONTAINS RO");
                        if((int)Float.parseFloat(GameScreen.database.getOneData("SELECT Level FROM Player WHERE id = 1")) == 4){
                            GameScreen.database.updateDataBase("UPDATE Player SET Level = Level+1 WHERE id = 1 ;");
                            GameScreen.database.updateDataBase("UPDATE Equipment SET Unlocked = 1 WHERE EquipmentName = 'Scanner2' ;");
                        }
                        System.out.println("ROBOT ON DUTY");
                    }
                    ArrayList goodPlanets = GameScreen.database.getPlanets("SELECT id FROM Planet WHERE Water = 1 AND Oxygen = 1 AND AtmosphereQuantity >1.5 AND Density >3 AND MagneticField >2 AND Distance <500 ;");
                    int currentPlanetId = Integer.parseInt(GameScreen.database.getOneData("SELECT id FROM Planet WHERE PlanetName = '"+parent.names[2]+"' ;"));
                    if(goodPlanets.contains(currentPlanetId)){
                        if(!parent.names[0].equals("Cargo")){
                            GameScreen.database.updateDataBase("UPDATE Player SET Level = 13 WHERE id = 1 ;"); //Peut-être à revoir...Level+1/Level=11
                            GameScreen.database.updateDataBase("UPDATE Rocket SET Unlocked = 1 WHERE RocketName = 'Cargo' ;");
                        } else {
                            GameScreen.database.updateDataBase("UPDATE Player SET Level = 14 WHERE id = 1 ;");
                            //ENDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD//
                            game.setScreen(new TheEnd(game));
                        }
                    }
                }
                else if(display.equals("REMOVE FROM FAVOURITES")){
                    try {
                        GameScreen.database.updateDB("UPDATE Planet SET Favourite = 0 WHERE PlanetName = '"+parent.names[0]+"' ;");
                        System.out.println("Removed from favourites the following planet : "+parent.names[0]);
                    }catch (SQLException e){
                        GameScreen.database.updateDataBase("UPDATE Systems SET Favourite = 0 WHERE SystemName = '"+parent.names[0]+"' ;");
                        System.out.println("Removed from favourites the following system : "+parent.names[0]);
                    }
                    parent.delete = true;
                    parent.c.allowedToDelete = true;
                }
                else if(display.equals("SINGULARIZE THIS STAR")){
                    int x = (int)Float.parseFloat(GameScreen.database.getOneData("SELECT SystemX FROM Systems WHERE SystemName = '"+parent.names[2]+"' ;"));
                    int y = (int)Float.parseFloat(GameScreen.database.getOneData("SELECT SystemY FROM Systems WHERE SystemName = '"+parent.names[2]+"' ;"));
                    if(parent.names[2].equals(GameScreen.database.getOneData("SELECT Name FROM BlackHoles WHERE X = "+x+" AND Y = "+y+""))) return;
                    GameScreen.database.updateDataBase("INSERT INTO BlackHoles('Name', 'X', 'Y') VALUES('"+parent.names[2]+"', "+x+", "+y+");");
                    if((int)Float.parseFloat(GameScreen.database.getOneData("SELECT Level FROM Player")) == 10){
                        GameScreen.database.updateDataBase("UPDATE Player SET Level = Level+1 WHERE id = 1 ;");
                        GameScreen.database.updateDataBase("UPDATE Player SET Xp = (Xp+200000000) WHERE id = 1 ;");
                    }
                }
            }
        }
    }
}
