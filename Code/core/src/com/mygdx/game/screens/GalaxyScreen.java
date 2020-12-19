package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ActionButton;
import com.mygdx.game.Const;
import com.mygdx.game.DragonCurveGenerator;
import com.mygdx.game.ProjectSurvival;

import java.util.ArrayList;

/**
 * Screen that display the whole galaxy, created with 4 fractals named the 'Dragon-Curve'
 */
public class GalaxyScreen extends MyScreens {

    /**
     * Diffrent arrays containing the coordinates of all the systems, one array per fractal
     */
    private float[] dragonCurve1;
    private float[] dragonCurve2;
    private float[] dragonCurve3;
    private float[] dragonCurve4;
    private static final int RECURSIONS = 12; //Number of recursions of the fractal

    /**
     * Stuff used to visualise and interact with the galaxy...
     */
    private MyCamera myCamera; //Containing all what's used to zoom, move around, click etc...
    ArrayList<String> systems;
    static ActionButton ab; //Button used to return the the Observatory menu.
    private ArrayList<Float> favouriteX = new ArrayList<Float>();
    private ArrayList<Float> favouriteY = new ArrayList<Float>();

    /**
     * Stuff to create the fractal
     */
    private int counter = 0;
    private ArrayList<Vector2> origins;
    private ArrayList<Vector2> destinations;
    private int internCounter = 0;

    /**
     * Generate the 4 dragon-curves and create the whole galaxy
     * @param game
     */
    public GalaxyScreen(ProjectSurvival game){
        super();
        this.game = game;
        backGroundTexture = new Texture(Gdx.files.internal("images/GalaxyScreenBackground.jpg"));
        backgroundSprite = new Sprite(backGroundTexture);
        dragonCurve1 = DragonCurveGenerator.generateDragonCurve(Const.WORLD_WIDTH, Const.WORLD_HEIGHT, RECURSIONS, new Vector2(5, 0));
        dragonCurve2 = DragonCurveGenerator.generateDragonCurve(Const.WORLD_WIDTH, Const.WORLD_HEIGHT, RECURSIONS, new Vector2(-5, 0));
        dragonCurve3 = DragonCurveGenerator.generateDragonCurve(Const.WORLD_WIDTH, Const.WORLD_HEIGHT, RECURSIONS, new Vector2(0, 5));
        dragonCurve4 = DragonCurveGenerator.generateDragonCurve(Const.WORLD_WIDTH, Const.WORLD_HEIGHT, RECURSIONS, new Vector2(0, -5));
        myCamera = new MyCamera(game);
        ab = new ActionButton(game, batch, renderer, new Vector2(1400, 30), new Vector2(300, 15), "Return to Observatory");
        origins = new ArrayList<Vector2>();
        destinations = new ArrayList<Vector2>();
        layout = new GlyphLayout();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/goodTimes.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 15;
        font = generator.generateFont(parameter);
        font.setColor(Color.WHITE);

        int nb = Integer.parseInt(GameScreen.database.getOneData("SELECT Count(*) FROM Systems WHERE Description = 'May_welcome_life' "));
        for(int i = 0; i<nb; i++){
            favouriteX.add(Float.parseFloat(GameScreen.database.getOneData("SELECT SystemX FROM Systems WHERE Description = 'May welcome life' ;")));
            favouriteY.add(Float.parseFloat(GameScreen.database.getOneData("SELECT SystemY FROM Systems WHERE Description = 'May welcome life' ;")));
        }
    }

    /**
     * Calculate the transparency of the background image, depending on the the zoom.
     * @return the value of the alpha canal (from 0 to 1)
     */
    private float calculateAlpha() {
        float alpha = 1.2f*(myCamera.getViewportWidth()/(2*Const.WORLD_WIDTH-myCamera.getViewportWidth()))-0.2f;
        if(alpha < 0) alpha = 0;
        if(alpha > 1) alpha = 1;
        return alpha;
    }

    /**
     * Render the background image and the galaxy itself, updates the camera.
     * @param delta
     */
    @Override
    public void render(float delta) {
        if(counter>1000) counter = 1;
        super.render(delta);
        myCamera.update();
        myCamera.setCamera(renderer, batch);
        batch.begin();
        backgroundSprite.draw(batch, calculateAlpha()/2);
        batch.end();
        drawCurve();
        drawFlights();
        drawFavouritesAndLegend();
        ab.render();
        clicked = false;
        counter++;
    }

    /**
     * Display on screen the systems that are in favourites
     */
    private void drawFavouritesAndLegend(){
        renderer.begin();
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.CYAN);
        for(int i = 0; i<favouriteX.size(); i++){
            renderer.circle(favouriteX.get(i), favouriteY.get(i), 3);
        }
        renderer.setColor(Color.WHITE);
        renderer.circle(50, 900, 5);
        renderer.setColor(Color.GREEN);
        renderer.circle(50, 870, 5);
        renderer.setColor(Color.RED);
        renderer.circle(50, 840, 5);
        renderer.setColor(Color.CYAN);
        renderer.circle(50, 810, 5);
        renderer.end();
        batch.begin();
        font.draw(batch, Const.PLANET_NAME, 60, 905);
        font.draw(batch, "Flight Origin", 60, 875);
        font.draw(batch, "Flight Destination", 60, 845);
        font.draw(batch, "Habitable Planet", 60, 815);
        batch.end();
    }

    /**
     * Display green points at the position of different flights :
     * It analyzes the different flights and add them to arrays.
     * The arrays are then used to display the different positions and destinations, travels etc..
     */
    private void drawFlights(){
        if(counter%180 == 0){ //To not execute the SQL request every frame.
            internCounter = 0;
            origins.clear();
            destinations.clear();
            internCounter = 0 ;
            ArrayList<String> answer = GameScreen.database.readDatas("SELECT Origin, Destination FROM Flights");
            for(String s : answer){
                internCounter++;
                String[] parts = s.split(" ");
                String xyOrigin, xyDestination;
                try{
                    xyOrigin = GameScreen.database.readDatas("SELECT SystemX, SystemY FROM Systems WHERE SystemName = '"+parts[0]+"' ;").get(0);
                } catch (IndexOutOfBoundsException e){
                    String systemId = GameScreen.database.getOneData("SELECT SystemId FROM Planet WHERE PlanetName = '"+parts[0]+"' ;");
                    xyOrigin = GameScreen.database.readDatas("SELECT SystemX, SystemY FROM Systems WHERE id = '"+systemId+"' ;").get(0);
                }
                origins.add(new Vector2(Float.parseFloat(xyOrigin.split(" ")[0]), Float.parseFloat(xyOrigin.split(" ")[1])));

                try{
                    xyDestination = GameScreen.database.readDatas("SELECT SystemX, SystemY FROM Systems WHERE SystemName = '"+parts[1]+"' ;").get(0);
                }catch (IndexOutOfBoundsException e){
                    String systemId = GameScreen.database.getOneData("SELECT SystemId FROM Planet WHERE PlanetName = '"+parts[1]+"' ;");
                    xyDestination = GameScreen.database.readDatas("SELECT SystemX, SystemY FROM Systems WHERE id = '"+systemId+"' ;").get(0);
                }
                destinations.add(new Vector2(Float.parseFloat(xyDestination.split(" ")[0]), Float.parseFloat(xyDestination.split(" ")[1])));
            }
        }
        renderer.begin();
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.WHITE);
        renderer.circle(600, 380, 4);
        for(int i = 0; i<internCounter; i++){
            renderer.line(origins.get(i), destinations.get(i));
            renderer.setColor(Color.RED);
            renderer.circle(origins.get(i).x, origins.get(i).y, 3);
            renderer.setColor(Color.GREEN);
            renderer.circle(destinations.get(i).x, destinations.get(i).y, 3);
        }
        renderer.end();
    }

    /**
     * Draw the 4 dragon-curves; could be optimized a little bit by adding a for-loop
     */
    private void drawCurve(){
        renderer.begin();
        renderer.set(ShapeRenderer.ShapeType.Line);
        for (int i = 0; i<dragonCurve1.length-2; i+=2){
            renderer.setColor(new Color(1-(i/20000f),1-(i/20000f),1f,1f));
            renderer.line(dragonCurve1[i], dragonCurve1[i+1], dragonCurve1[i+2], dragonCurve1[i+3]);
        }
        for (int i = 0; i<dragonCurve2.length-2; i+=2){
            renderer.setColor(new Color(1-(i/20000f),1-(i/20000f),1f,1f));
            renderer.line(dragonCurve2[i], dragonCurve2[i+1], dragonCurve2[i+2], dragonCurve2[i+3]);
        }
        for (int i = 0; i<dragonCurve3.length-2; i+=2){
            renderer.setColor(new Color(1-(i/20000f),1-(i/20000f),1f,1f));
            renderer.line(dragonCurve3[i], dragonCurve3[i+1], dragonCurve3[i+2], dragonCurve3[i+3]);
        }
        for (int i = 0; i<dragonCurve4.length-2; i+=2){
            renderer.setColor(new Color(1-(i/20000f),1-(i/20000f),1f,1f));
            renderer.line(dragonCurve4[i], dragonCurve4[i+1], dragonCurve4[i+2], dragonCurve4[i+3]);
        }
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.RED);
        renderer.end();
        if(MyCamera.renderSystems){ //Put red dots on the systems that are close enough to a certain point while zoomed in
            systems = GameScreen.database.getSystems("SELECT SystemX, SystemY, id FROM 'Systems' WHERE ABS(SystemX-"+MyCamera.closeupCamera.position.x+")<25");
            for(int i = 0; i<systems.size(); i++){
                String[] parts = systems.get(i).split(" ");
                float x = Float.parseFloat(parts[0]);
                float y = Float.parseFloat(parts[1]);
                String id = String.valueOf(Float.parseFloat(parts[2]));
                renderer.begin();
                renderer.set(ShapeRenderer.ShapeType.Filled);
                renderer.circle(x, y, 0.5f);
                renderer.end();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
