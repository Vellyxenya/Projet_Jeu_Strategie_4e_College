package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.database.DataBase;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.HangarScreen;

import java.util.ArrayList;
import java.util.HashMap;

import static com.mygdx.game.database.Algorithms.joinTable;
import static com.mygdx.game.structuredVariables.MaterialManager.materials;
import static com.mygdx.game.structuredVariables.MaterialManager.updateMaterial;

/**
 * Object qui 'contient' plusieurs éléments l'un sous l'autre et qui peut les faire défiler
 */
public class Container extends Clickable {

    ArrayList<Elements> elements; //Les éléments contenus dans le container
    public static float position = 0;
    private float min , max;
    private final float displayHeight;
    private float totalHeight;
    private float delta;
    private Color black;
    static float fadeIn;
    private String type;
    private boolean updateMaterials = true;
    private int materialToUpdate = 0;

    /**
     * Type du container avec ses dimensions
     */
    private float[] defaultType;
    private float[] flightType           = new float[]{10, 20, 20, 20, 30};
    private float[] materialType         = new float[]{20, 50, 14, 16};
    private float[] rocketType           = new float[]{20, 40, 20, 20};
    private float[] builtRocketType      = new float[]{10, 20, 40, 30};
    private float[] equipmentType        = new float[]{24, 40, 10, 10, 16};
    private float[] planetType           = new float[]{20, 20, 20, 20, 20};
    private float[] systemType           = new float[]{20, 32, 16, 16, 16};
    private float[] blackHoleType        = new float[]{10, 50, 20, 20};
    private float[] wormHoleType         = new float[]{12, 40, 12, 12, 12, 12};
    private float[] loadType             = new float[]{100};

    /**
     * Différentes options disponibles pour chaque type de container
     */
    String defaultOptions[];
    private String flightOptions[]       = new String[]{"NEW DESTINATION", "LAND", "COLLECT", "SCAN"};
    private String materialOptions[]     = new String[]{"", "INFORMATIONS", "LOCATIONS"};
    private String rocketOptions[]       = new String[]{"", "INFORMATIONS", "BUILD"};
    private String builtRocketOptions[]  = new String[]{"", "EQUIPPED MODULES", "TAKE OFF", "DESTROY"};
    private String equipmentOptions[]    = new String[]{"INFORMATIONS", "BUILD", "LEVEL UP", "REQUIRED MATERIALS"};
    private String planetOptions[]       = new String[]{"", "INFORMATIONS ABOUT TYPE", "TRAVEL TO PLANET", "REMOVE FROM FAVOURITES"};
    private String systemOptions[]       = new String[]{"", "INFORMATIONS ABOUT TYPE", "TRAVEL TO SYSTEM", "REMOVE FROM FAVOURITES"};
    private String blackHoleOptions[]    = new String[]{"", "INFORMATIONS ABOUT", "TRAVEL TO BlackHole", "ABANDON"};
    private String wormHoleOptions[]     = new String[]{"", "INFORMATIONS ABOUT", "TRAVEL TO WormHole", "ABANDON"};
    static  String loadOptions[]         = new String[]{"", "", "LOAD THIS GAME"};

    /**
     * L'en-tête des colonnes des différents types de container
     */
    private String[] defaultHeading;
    private String[] flightHeading       = new String[]{"id", "Description", "Origin", "Destination", "EquippedModules"};
    private String[] materialHeading     = new String[]{"MaterialName", "MaterialDescription", "Rarity", "Quantities"};
    private String[] rocketHeading       = new String[]{"RocketName", "RocketDescription", "Capacity", "Speed"};
    private String[] builtRocketHeading  = new String[]{"id", "RocketName", "PersonalDescription", "EquippedModules"};
    private String[] equipmentHeading    = new String[]{"EquipmentName", "EquipmentDescription", "Weight", "Radius", "Quantity"};
    private String[] planetHeading       = new String[]{"PlanetName", "Distance", "PlanetType", "Discovered", "Scanned"};
    private String[] systemHeading       = new String[]{"SystemName", "Description", "StarType", "Discovered", "Scanned"};
    private String[] blackHoleHeading    = new String[]{"id", "Name", "X", "Y"};
    private String[] wormHoleHeading     = new String[]{"id", "Name", "X1", "Y1", "X2", "Y2"};
    private String[] loadHeading         = new String[]{"PlayerName"};

    private ActionButton actionButton;
    private ActionButton actionButton2;
    private String rocketName = null;

    private int counter = 0;
    private ArrayList <String> selectedBlackHoles = new ArrayList<String>();
    ArrayList<String> messages = new ArrayList<String>();
    boolean allowedToDelete = false; //devient true quand un élément est sur le point d'être enlevé

    /**
     * Initialize some variables and parameters
     * @param type type of the container (materials, flights, equipments etc...)
     */
    public Container(ProjectSurvival game, ShapeRenderer renderer, SpriteBatch batch, String type){
        elements = null;
        elements = new ArrayList<Elements>();
        System.gc();
        this.renderer = renderer;
        this.type = type;
        this.batch = batch;
        this.game = game;

        initContainer();
        initVars();

        displayHeight = 600;
        allowedToDelete = false;

        layout = new GlyphLayout();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/goodTimes.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 14;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 0.5f;
        font = generator.generateFont(parameter);
    }

    /**
     * Select the data corresponding to the type of the container
     */
    private void initContainer(){
        if(type.equals("Flights")){
            defaultType = flightType;
            defaultOptions = flightOptions;
            defaultHeading = flightHeading;
            retrieveDatas("SELECT "+joinTable(", ", defaultHeading)+" FROM "+type, false);
            updateMaterials = false;
        }
        else if(type.equals("Material")){
            defaultType = materialType;
            defaultOptions = materialOptions;
            defaultHeading = materialHeading;
            retrieveDatas("SELECT "+joinTable(", ", defaultHeading)+" FROM "+type, false);
        }
        else if(type.equals("Rocket")){
            defaultType = rocketType;
            defaultOptions = rocketOptions;
            defaultHeading = rocketHeading;
            retrieveDatas("SELECT "+joinTable(", ", defaultHeading)+" FROM "+type, false);
            updateMaterials = false;
        }
        else if(type.equals("BuiltRocket")){
            defaultType = builtRocketType;
            defaultOptions = builtRocketOptions;
            defaultHeading = builtRocketHeading;
            retrieveDatas("SELECT "+joinTable(", ", defaultHeading)+" FROM "+type, false);
            updateMaterials = false;
        }
        else if(type.equals("Equipment")){
            defaultType = equipmentType;
            defaultOptions = equipmentOptions;
            defaultHeading = equipmentHeading;
            retrieveDatas("SELECT "+joinTable(", ", defaultHeading)+" FROM "+type+" ;", false);
        }
        else if(type.equals("Built Modules")){
            defaultType = equipmentType;
            defaultOptions = equipmentOptions;
            defaultHeading = equipmentHeading;
            retrieveDatas("SELECT "+joinTable(", ", defaultHeading)+" FROM Equipment WHERE Quantity > 0 ;", true);
            updateMaterials = false;
        }
        else if(type.equals("Planet")){
            defaultType = planetType;
            defaultOptions = planetOptions;
            defaultHeading = planetHeading;
            retrieveDatas("SELECT "+joinTable(", ", defaultHeading)+" FROM "+type+" WHERE Favourite = 1 ;", false);
            updateMaterials = false;
        }
        else if(type.equals("Systems")){
            defaultType = systemType;
            defaultOptions = systemOptions;
            defaultHeading = systemHeading;
            retrieveDatas("SELECT "+joinTable(", ", defaultHeading)+" FROM "+type+" WHERE Favourite = 1 ;", false);
            updateMaterials = false;
        }
        else if(type.equals("Accounts")){
            defaultType = loadType;
            defaultOptions = loadOptions;
            defaultHeading = loadHeading;
            retrieveAccounts("SELECT "+joinTable(", ", defaultHeading)+" FROM "+type+";");
            updateMaterials = false;
        }
        else if(type.equals("BlackHoles")){
            defaultType = blackHoleType;
            defaultOptions = blackHoleOptions;
            defaultHeading = blackHoleHeading;
            retrieveDatas("SELECT "+joinTable(", ", defaultHeading)+" FROM "+type+";", true);
            addActionButton2(new ActionButton(game, batch, renderer, new Vector2(1000, 120), new Vector2(200, 15), "Create Wormhole!", this));
            updateMaterials = false;
        }
        else if(type.equals("WormHoles")){
            defaultType = wormHoleType;
            defaultOptions = wormHoleOptions;
            defaultHeading = wormHoleHeading;
            retrieveDatas("SELECT "+joinTable(", ", defaultHeading)+" FROM "+type+";", true);
            updateMaterials = false;
        }
        System.out.println("container finished");
    }

    void addActionButton(ActionButton actionButton){
        this.actionButton = actionButton;
        System.out.println("Added an actionButton1");
    }

    private void addActionButton2(ActionButton actionButton){
        this.actionButton2 = actionButton;
        System.out.println("Added an actionButton2");
    }

    void setRocketName(String rocketName){
        this.rocketName = rocketName;
    }

    /**
     * Manage actions related to the action button, like hovering and clicks
     */
    private void renderActionButton(){
        if(actionButton != null){
            actionButton.render();
            if(ActionButton.triggered) {
                if (actionButton.hovered) {
                    if(rocketName == null){
                        ActionButton.triggered = false;
                        return;
                    }
                    ArrayList<String> usedEquipment = new ArrayList<String>();
                    for(Elements element : elements){
                        CheckElement e = (CheckElement)element;
                        if(e.checked) usedEquipment.add(e.names[0]);
                    }
                    String equippedModules = fromStringToCode(usedEquipment);
                    actionButton.buildRocket(rocketName, equippedModules);
                    ActionButton.triggered = false;
                }
            }
        }
        if(actionButton2 != null){
            selectedBlackHoles.clear();
            for(Elements element : elements){
                CheckElement e = (CheckElement)element;
                if(e.checked) selectedBlackHoles.add(e.names[1]);
            }
            if(selectedBlackHoles.size() == 2) actionButton2.render();
            else return;
            if(ActionButton.triggered) {
                System.out.println("Triggered");
                if (actionButton2.hovered) {
                    System.out.println("Hovered");
                    System.out.println();
                    int x1 = Integer.parseInt(GameScreen.database.getOneData("SELECT X FROM BlackHoles WHERE Name = '"+selectedBlackHoles.get(0)+"' ;"));
                    int y1 = Integer.parseInt(GameScreen.database.getOneData("SELECT Y FROM BlackHoles WHERE Name = '"+selectedBlackHoles.get(0)+"' ;"));
                    int x2 = Integer.parseInt(GameScreen.database.getOneData("SELECT X FROM BlackHoles WHERE Name = '"+selectedBlackHoles.get(1)+"' ;"));
                    int y2 = Integer.parseInt(GameScreen.database.getOneData("SELECT Y FROM BlackHoles WHERE Name = '"+selectedBlackHoles.get(1)+"' ;"));
                    actionButton2.createWormHole(selectedBlackHoles.get(0)+"-"+selectedBlackHoles.get(1),x1, y1, x2, y2);
                    ActionButton.triggered = false;
                }
            }
        }
    }

    /**
     * Transform an array of Strings to a one String code
     * @param list
     * @return
     */
    private String fromStringToCode (ArrayList<String> list){
        String code = "";

        HashMap<String, String> codes = new HashMap<String, String>();
        codes.put("Scanner1", "S1");
        codes.put("Scanner2", "S2");
        codes.put("Scanner3", "S3");
        codes.put("dOxygen", "DO");
        codes.put("dWater", "DW");
        codes.put("dMagnetic", "DM");
        codes.put("Robot", "RO");
        codes.put("CPU", "CP");
        codes.put("Container", "CO");
        codes.put("Singularizer", "SI");

        for (String s : list){
            code += codes.get(s);
            code += "_";
        }
        return code;
    }

    private void initVars(){
        totalHeight = elements.size()*100;
        delta = 0;
        min = 0;
        max = totalHeight - displayHeight;
        black = new Color(0, 0, 0, 0.98f);
        fadeIn = 600;
    }

    /**
     * Retrieve datas from the Accounts DB
     * @param query
     */
    private void retrieveAccounts(String query){
        DataBase database = new DataBase("Accounts/accounts.db");
        ArrayList<String> answer = database.readDatas(query);
        for(int i = 0; i<answer.size(); i++){
            String[] part = {answer.get(i)};
            elements.add(new Element(game, batch, renderer, new Vector2(1000, 700-i*100),
                    new Vector2(800, 90), defaultType, part, type, this, i));
        }
        database.closeDB();
    }

    /**
     * Retrieves datas from the database, creates elements from these datas
     * @param query SQL query
     * @param check wheter the elements created should be CheckElements or not
     */
    private void retrieveDatas(String query, boolean check){
        ArrayList<String> answer = GameScreen.database.readDatas(query);
        for(int i = 0; i<answer.size(); i++){
            String[] parts = answer.get(i).split(" ");
            String[] teils = new String[parts.length];
            for(int ii = 0; ii<teils.length; ii++){
                teils[ii] = String.valueOf(parts[ii]);
            }
            if(!check)  elements.add(new Element(game, batch, renderer, new Vector2(1000, 700-i*100),
                        new Vector2(800, 90), defaultType, teils, type, this, i));
            else        elements.add(new CheckElement(game, batch, renderer, new Vector2(1000, 700-i*100),
                        new Vector2(800, 90), defaultType, teils, type, this));
        }
    }

    /**
     * Delete an element from this container
     */
    private void deleteElement(){
        int counter = 0;
        for(Elements e : elements){
            if(((Element) e).delete){
                elements.remove(counter);
                return;
            }
            counter++;
        }
    }

    /**
     * Displays some animations for the container
     * Updates materials, flights and elements
     * Manage the position of the different elements of the container
     */
    public void render(){
        counter++;
        if(counter%30 == 0 && updateMaterials){
            updateMaterial(materials[materialToUpdate]);
            materialToUpdate += (materialToUpdate == 8) ? -materialToUpdate : 1 ; //ternary
        }
        delta += position;
        if (delta < min) delta = min;
        if (delta > max) delta = max;
        int i = 0;
        if(allowedToDelete) deleteElement();
        allowedToDelete = false;
        for (Elements element : elements) {
            if (element.position.y > (700 - i * 100) + delta) element.position.y -= 10;
            if (element.position.y < (700 - i * 100) + delta) element.position.y += 10;
            if (element.position.y + delta < 850 + i * 100 && element.position.y > 120){
                if(counter%120 == 0 && type.equals("Material")){
                    ((Element) element).updateQuantity();
                }
                if(counter%60 == 0 && type.equals("Flights")){
                    ((Element) element).updateFlight();
                }
                try {
                    ((Element) element).render();
                }catch (ClassCastException e){
                    ((CheckElement) element).render();
                }
            } else {
                try {
                    ((Element) element).active = false;
                    ((Element) element).h = 0;
                }catch (ClassCastException e){
                    ((CheckElement) element).active = false;
                    ((CheckElement) element).h = 0;
                }
            }
            i++;
        }
        //System.out.println("delta "+delta+" position "+position+" element 0 "+elements.get(7).position.y);
        position = 0;
        renderer.begin();
        renderer.set(ShapeRenderer.ShapeType.Filled);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderer.rect(560, 50 , 900, 100, Color.BLACK, Color.BLACK, black, black);
        renderer.rect(560, 750, 900, 100, Color.BLACK, Color.BLACK, black, black);
        renderer.setColor(Color.BLACK);
        if (fadeIn > 0) {
            renderer.rect(560, 150, 900, fadeIn);
            fadeIn -= 15;
        }
        renderer.end();
        renderMessage();
        renderHeading();
        renderActionButton();
        Elements.triggered = false;
        Option.triggered = false;
        ActionButton.triggered = false;
    }

    /**
     * Rendering the name of the columns of the elements
     */
    private void renderHeading(){
        renderer.begin();
        renderer.set(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.WHITE);
        renderer.rect(600, 775, 800, 40);
        for (int i = 0; i<defaultType.length-1; i++){
            float ratio = 0;
            int j = 0;
            while (j<=i){
                ratio += defaultType[j];
                j++;
            }
            float x = 600+(800*(ratio/100));
            renderer.line(x, 775, x, 775+40);
        }
        renderer.end();
        batch.begin();
        font.draw(batch, defaultHeading[0], 605, 800);
        for (int i = 0; i<defaultType.length-1; i++){
            float ratio = 0;
            int j = 0;
            while (j<=i){
                ratio += defaultType[j];
                j++;
            }
            float x = 605+(800*(ratio/100));
            font.draw(batch, defaultHeading[i+1], x, 800);
        }
        batch.end();
    }

    /**
     * Renders the message displayed at the bottom of the container
     */
    private void renderMessage(){
        batch.begin();
        font.draw(batch, "Message :", 600, 125);
        for(int i = 0; i<messages.size(); i++){
            font.draw(batch, messages.get(i), 700, 125-i*25);
        }
        batch.end();
    }
}
