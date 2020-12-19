package com.mygdx.game.structuredVariables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.ProjectSurvival;
import com.mygdx.game.screens.DisplaySystemScreen;

/**
 * Class containing informations about a given flight and displaying it in a system
 */
public class Flights extends Actors {

    int    id;
    String Description;
    String Origin;
    String Destination;
    String Rocket;
    String Modules;
    float  Fuel;
    float  Distance;

    float   xPosition, yPosition;
    double alpha;

    /**
     * Initialize some variables
     * Create an image corresponding to it and places it on the screen
     * @param game
     * @param renderer
     * @param batch
     * @param id
     * @param rocket
     * @param modules
     * @param distance
     */
    public Flights(ProjectSurvival game, ShapeRenderer renderer, SpriteBatch batch, int id, String rocket, String modules, float distance){
        this.game = game;
        this.renderer = renderer;
        this.batch = batch;
        this.id = id;
        this.Rocket = rocket;
        this.Modules = modules;
        this.Distance = distance;

        backGroundTexture = new Texture(Gdx.files.internal("images/rockets/"+rocket+".png"));
        backgroundSprite = new Sprite(backGroundTexture);
        backgroundSprite.setPosition(500-backgroundSprite.getWidth()/2, 500-backgroundSprite.getHeight()/2);
    }

    /**
     * Render the flight at the position of the planet or sun the flight is at
     */
    public void render(){
        if(Distance != 0){
            alpha += (2/Distance);
            xPosition = 500+Distance/2*(float)Math.cos(alpha%(2*Math.PI));
            yPosition = 500+Distance/2*(float)Math.sin(alpha%(2*Math.PI));
        }
        else {
            xPosition = 500;
            yPosition = 500;
        }

        batch.begin();
        batch.draw(backgroundSprite, xPosition, yPosition);
        batch.end();
    }
}
