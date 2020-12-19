package com.mygdx.game.structuredVariables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ActionButton;
import com.mygdx.game.ProjectSurvival;
import com.mygdx.game.database.Algorithms;
import com.mygdx.game.screens.DisplaySystemScreen;
import com.mygdx.game.screens.GameScreen;

import java.util.ArrayList;

/**
 * Class representing a system (star + its planets)
 * Contains all the informations about a give star
 */
public class Systems extends Actors {

    /*
    Variables membres de la classe
     */
    int     id;
    String  systemName;
    String  starType;
    float   systemX;
    float   systemY;
    int     dual;
    String  discovered;
    String  scanned;
    int     planetNb;

    ArrayList<Planet> planets = new ArrayList<Planet>();
    ActionButton ab;
    ActionButton returnToGalaxy;
    String[] actions = {"Add System to favourite", "Return To Galaxy Screen"};
    boolean createdFlights = false;

    /**
     * Initialise un nouveal système stellaire à partir de la base de données
     * @param game
     * @param id id du système
     * @param batch
     * @param renderer
     */
    public Systems(ProjectSurvival game, int id , SpriteBatch batch, ShapeRenderer renderer){
        this.game = game;
        this.batch = batch;
        this.id = id;
        this.renderer = renderer;
        this.systemName = GameScreen.database.getOneData("SELECT SystemName FROM 'Systems' WHERE id = "+id);
        this.systemX = Float.parseFloat(GameScreen.database.getOneData("SELECT SystemX FROM 'Systems' WHERE id = "+id));
        this.systemY = Float.parseFloat(GameScreen.database.getOneData("SELECT SystemY FROM 'Systems' WHERE id = "+id));
        this.starType = GameScreen.database.getOneData("SELECT StarType FROM 'Systems' WHERE id = "+id);
        this.dual = Integer.parseInt(GameScreen.database.getOneData("SELECT Dual FROM 'Systems' WHERE id = "+id));
        discovered = GameScreen.database.getOneData("SELECT Discovered FROM 'Systems' WHERE id = "+id);
        scanned = GameScreen.database.getOneData("SELECT Scanned FROM 'Systems' WHERE id = "+id);

        layout = new GlyphLayout();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Anony.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 0.5f;
        font = generator.generateFont(parameter);

        backGroundTexture = new Texture(Gdx.files.internal("images/stars/"+starType+".png"));
        backgroundSprite = new Sprite(backGroundTexture);
        backgroundSprite.setPosition(500-backgroundSprite.getWidth()/2, 500-backgroundSprite.getHeight()/2);
        planetNb = Integer.parseInt(GameScreen.database.getOneData("SELECT Count(*) FROM 'Planet' WHERE SystemId = "+id));
        ArrayList<Integer> planetIds = GameScreen.database.getPlanets("SELECT id FROM 'Planet' WHERE SystemId = "+id);
        ab = new ActionButton(game, batch, renderer, new Vector2(1185, 750), new Vector2(300, 15), actions[0]);
        returnToGalaxy = new ActionButton(game, batch, renderer, new Vector2(1185, 300), new Vector2(300, 15), actions[1]);
        //A laisser en dernier!
        if (planetIds.size() == 0) return;
        System.out.println("Scanned : "+scanned);
        if(scanned.equals("YES")){
            System.out.println("Should display planets...");
            for(int i = 0; i<planetIds.size(); i++){
                planets.add(new Planet(game, planetIds.get(i), batch, renderer, id));
            }
        }
    }

    /**
     * Add flights to an array if there's any in this system
     */
    private void createFlights(){
        ArrayList<String> flights = GameScreen.database.readDatas("SELECT id, Description, EquippedModules FROM Flights WHERE Origin = Destination AND Origin = '"+systemName+"' ;");
        System.out.println("flights.size() : "+flights.size());
        for(String s : flights){
            String parts[] = s.split(" ");
            DisplaySystemScreen.flights.add(new Flights(game, renderer, batch, Integer.parseInt(parts[0]), parts[1], parts[2], 0));
        }
    }

    /**
     * Display the properties of the star, its coordinates etc...
     */
    private void displayInfos(){
        batch.begin();
        backgroundSprite.draw(batch);
        font.draw(batch, "System            : "+systemName, 1050, 950);
        font.draw(batch, "X-Coordinates     : "+systemX, 1050, 900);
        font.draw(batch, "Y-Coordinates     : "+systemY, 1050, 880);
        font.draw(batch, "Number of planets : "+planetNb, 1050, 860);
        font.draw(batch, "Discovered        : "+discovered, 1050, 840);
        font.draw(batch, "Scanned           : "+scanned, 1050, 820);
        batch.end();
    }

    /**
     * detect clicks, their coordinates, trigger buttons, allow them to change screen etc...
     */
    private void handleClicks(){
        if(DisplaySystemScreen.clicked){
            int x = DisplaySystemScreen.xCoo;
            int y = DisplaySystemScreen.yCoo;
            if(Algorithms.getDistance(500f, 500f, (float)x, (float)y)<backgroundSprite.getWidth()/2){
                DisplaySystemScreen.clicked = false;
            }
            ActionButton.triggered = true;
        }
        ab.render();
        returnToGalaxy.render();
        if(ActionButton.triggered){
            if(ab.hovered){
                ab.addSystemToFavourite(actions[0], id);
                ActionButton.triggered = false;
            }
            else if(returnToGalaxy.hovered){
                returnToGalaxy.returnToGalaxyScreen();
                ActionButton.triggered = false;
            }
        }
    }

    /**
     * Render the star, it's planets, flights and some general informations
     */
    public void render(){
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        displayInfos();
        handleClicks();

        for(Planet planet : planets){
            planet.render();
        }
        ActionButton.triggered = false;
        DisplaySystemScreen.clicked = false;
        if(!createdFlights) {
            createFlights();
            createdFlights = true;
        }
    }
}
