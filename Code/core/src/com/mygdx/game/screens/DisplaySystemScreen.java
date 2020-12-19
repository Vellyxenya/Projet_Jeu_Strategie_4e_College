package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Const;
import com.mygdx.game.ProjectSurvival;
import com.mygdx.game.structuredVariables.Flights;
import com.mygdx.game.structuredVariables.Systems;

import java.util.ArrayList;

/**
 * Screen that is displayed when we want to see a system and its planets
 */
public class DisplaySystemScreen extends MenuScreens {

    int systemId;
    Systems system;
    public static ArrayList<Flights> flights; //flights displayed in the current system

    /**
     * Initialize some variables
     * @param game reference to our game
     * @param systemId id of the current system
     */
    public DisplaySystemScreen(ProjectSurvival game, int systemId){
        super();
        this.game = game;
        this.systemId = systemId;
        system = new Systems(game, systemId, batch, renderer);
        flights = new ArrayList<Flights>();
    }

    @Override
    public void show() {
    }

    /**
     * render the system (which will render the planets) and flights
     * @param delta intervalle of time between each rendering cycle
     */
    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.begin();
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.DARK_GRAY);
        renderer.rect(1000, 0, 600, 1000);
        renderer.end();
        system.render();
        for (Flights f : flights){
            f.render();
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }
    @Override
    public void pause() {
    }
    @Override
    public void resume() {
    }
    @Override
    public void hide() {
    }
    @Override
    public void dispose() {
        super.dispose();
    }
}
