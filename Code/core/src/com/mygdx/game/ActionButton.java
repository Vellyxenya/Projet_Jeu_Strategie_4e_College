package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.database.DataBase;
import com.mygdx.game.screens.GalaxyScreen;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.ObservatoryScreen;
import com.mygdx.game.structuredVariables.Equipment;

import static com.mygdx.game.database.Algorithms.parseMessage;

import java.sql.SQLException;

/**
 * Button used to do some actions, depending on the button's displayed text
 */
public class ActionButton extends Clickable{

    private Vector2 dimensions;
    private String display;
    private float textHeight;
    public boolean hovered;
    public static boolean triggered;
    private Container container;

    /**
     * A button that can be clicked to perform an action (change screen, update a database...)
     * @param game game
     * @param batch batch
     * @param renderer renderer
     * @param position position of the button
     * @param dimensions dimensions of the button
     * @param display text displayed by the button
     */
    public ActionButton(ProjectSurvival game, SpriteBatch batch, ShapeRenderer renderer, Vector2 position, Vector2 dimensions, String display){
        initParameters();
        this.game = game;
        this.batch = batch;
        this.renderer = renderer;
        layout.setText(font, display);
        this.dimensions = new Vector2(layout.width, 15);
        this.position = position;
        this.display = display;
    }

    /**
     * Another contianer used to add the container parameter
     */
    ActionButton(ProjectSurvival game, SpriteBatch batch, ShapeRenderer renderer, Vector2 position, Vector2 dimensions, String display, Container container){
        initParameters();
        this.game = game;
        this.batch = batch;
        this.renderer = renderer;
        layout.setText(font, display);
        this.dimensions = new Vector2(layout.width, 15);
        this.position = position;
        this.display = display;
        this.container = container;
    }

    private void initParameters(){
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/goodTimes.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        layout = new GlyphLayout();

        parameter.size = 14;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        font = generator.generateFont(parameter);
    }

    /**
     * Render the button with different colours depending on if it's hovered or not
     */
    public void render(){
        batch.begin();
        if(mouseOn(dimensions.x, dimensions.y)) {
            font.setColor(Color.RED);
            hovered = true;
        }
        else {
            font.setColor(Color.WHITE);
            hovered = false;
        }
        layout.setText(font, display);
        textHeight = layout.height;
        font.draw(batch, display, position.x-dimensions.x/2, position.y+dimensions.y/2-textHeight/2);
        batch.end();
    }

    /**
     * Special method for the button assigned to the progressInformation display
     */
    public void showOrHideProgress(){
        if(display.equals("WHAT TO DO NEXT?")) {
            display = ">>>>> GOT IT ! <<<<<";
            ProgressInformation.visible = true;
        }
        else {
            display = "WHAT TO DO NEXT?";
            ProgressInformation.visible = false;
        }
    }

    /**
     * Accept the offer made by the company and thus choose the first ending
     */
    public void acceptOffer(){
        GameScreen.database.updateDataBase("UPDATE Player SET Level = 10 WHERE id = 1 ;");
        GameScreen.database.updateDataBase("UPDATE Player SET Scenario = 1 WHERE id = 1 ;");
        Const.SCENARIO = 0;
        GameScreen.database.updateDataBase("UPDATE Player SET Xp = (Xp+2000000)");
    }

    /**
     * Refuse the offer made by the company and thus choose the second ending
     */
    public void refuseOffer(){
        GameScreen.database.updateDataBase("UPDATE Player SET Level = 10 WHERE id = 1 ;");
        GameScreen.database.updateDataBase("UPDATE Player SET Scenario = 2 WHERE id = 1 ;");
        Const.SCENARIO = 1;
        GameScreen.database.updateDataBase("UPDATE Equipment SET Unlocked = 1 WHERE EquipmentName = 'Singularizer' ;");
    }

    public void createWormHole(String name, int x1, int y1, int x2, int y2){
        GameScreen.database.updateDataBase("INSERT INTO WormHoles('Name', 'X1', 'Y1', 'X2', 'Y2') VALUES('"+name+"', "+x1+", "+y1+", "+x2+","+y2+") ;");
        GameScreen.database.updateDataBase("UPDATE Player SET Level = Level+1 WHERE id = 1 ;");
        System.out.println("Inserted WormHole");
    }

    /**
     * Build a new rocket and updates the database if the cost is affordable
     * @param rocketName Type of the rocket we want to build
     * @param equippedModules The different modules we want our rocket to be equipped with
     */
    void buildRocket(String rocketName, String equippedModules){
        try {
            int playerMoney = Integer.parseInt(GameScreen.database.getOneData("SELECT Xp FROM Player WHERE id = 1"));
            int cost;
            if(rocketName.equals("Orbiter")) cost = 10000; //cost = 0;
            else if(rocketName.equals("Lander")) cost = 50000; //cost = 0;
            else cost = 200000; //cost = 0;
            if(equippedModules.contains("S1")){
                cost = 2000000;
                if((int)Float.parseFloat(GameScreen.database.getOneData("SELECT Level FROM Player WHERE id = 1 ;")) == 8){
                    GameScreen.database.updateDataBase("UPDATE Player SET Level = Level+1 WHERE id = 1 ;");
                    container.messages = parseMessage("You have no money to equip an S3 on this rocket. Discuss what to do next with your collaborators.");
                    return;
                }
            }
            if(cost>playerMoney) {
                container.messages = parseMessage("You unfortunately don't have enough money to craft this rocket. (Costs : "+cost+")");
                System.out.println("Not Enough Money...");
                return;
            }
            GameScreen.database.updateDB("UPDATE Player SET Xp = (Xp-"+cost+") WHERE id = 1 ;");
            String[] symbols = equippedModules.split("_");
            for (int i = 0; i<symbols.length; i++){
                GameScreen.database.updateDB("UPDATE Equipment SET Quantity = (Quantity-1) WHERE EquipmentName = '"+ Equipment.codes.get(symbols[i])+"';");
            }
            GameScreen.database.updateDB("INSERT INTO BuiltRocket('RocketName', 'EquippedModules') VALUES('"+rocketName+"', '"+equippedModules+"') ; ");
            System.out.println("New Rocket crafted! : "+equippedModules);
        }catch (SQLException e){
            System.out.println("Problem in ActionButton>buildRocket");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getDisplay(){
        return display;
    }

    public void addSystemToFavourite(String action, int systemID){
        if(action.equals("Add System to favourite")){
            GameScreen.database.updateDataBase("UPDATE Systems SET Favourite = 1, Description = 'Interessant System' WHERE id = " + systemID);
            System.out.println("Added this system to favourites");
        }
    }

    public void addPlanetToFavourite(String action, int planetID){
        if(action.equals("Add Planet to favourite")){
            GameScreen.database.updateDataBase("UPDATE Planet SET Favourite = 1 WHERE id = "+planetID);
            System.out.println("Added this planet to favourites");
        }
    }

    public void returnToGalaxyScreen(){
        game.setScreen(new GalaxyScreen(game));
    }

    public void returnToObservatory(){
        game.setScreen(new ObservatoryScreen(game));
    }

    /**
     * Create a new game by creating a new 'account'
     * @param playerName name of the player
     * @param planetName name of the planet
     * @return the given parameters as a table of Strings
     */
    public String[] register(String playerName, String planetName){
        String[] infos = {playerName, planetName};
        DataBase db = new DataBase(false, "saves/"+playerName+".db");
        System.out.println(db.getOneData("SELECT Count(*) FROM Player ;"));
        try {
            String lu = String.valueOf(TimeUtils.millis());
            int time = (int) Math.floor((double)TimeUtils.millis()/1000);
            db.updateDB("INSERT INTO Player  ('PlayerName', 'PlanetName', 'Level', 'Time', 'Xp') VALUES('" + playerName + "', '" + planetName + "', 0, "+time+", 10000) ;");
            db.updateDB("UPDATE Planet SET PlanetName = '"+planetName+"', HumansNb = 7000000, Oxygen = 1, Water = 1, AtmosphereQuantity = 1.5, MagneticField = 3, Density = 8, Discovered = 1, Scanned = 1, Characteristics = 'WOM' WHERE id = 14201 ;");
            db.updateDB("UPDATE Material SET LastUpdate = '"+lu+"'; ");
        }catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println("Inserted New Player");
        db.closeDB();
        return infos;
    }
}
