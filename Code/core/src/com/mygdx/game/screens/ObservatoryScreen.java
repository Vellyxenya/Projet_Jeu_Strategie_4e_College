package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;
import com.mygdx.game.Container;
import com.mygdx.game.ProjectSurvival;
import com.mygdx.game.Widget;

import java.util.ArrayList;

/**
 * Observatory screen displaying the flights, favourites, Galaxy etc...
 */
public class ObservatoryScreen extends MyScreens {

    static Container container;
    public static ArrayList<Container> containers;

    /**
     * Initialize widgets, containers
     * @param game
     */
    public ObservatoryScreen(ProjectSurvival game){
        super();
        this.game = game;
        widgets = new Widget[7];
        menus = new String[]{"Planet", "Galaxy", "Flights", "BlackHoles", "WormHoles", "Favourite Planets", "Favourite Systems"};
        cooY = new int[]{850, 650, 550, 450, 350, 250, 150};
        backgroundSprite = new Sprite(Assets.manager.get("images/ObservatoryScreenBackground.jpg", Texture.class));
        for (int i = 0; i<menus.length; i++) {
            widgets[i] = new Widget(game, batch, renderer, new Vector2(50, cooY[i]), new Vector2(300, 50), menus[i]);
        }
        containers = null;
        containers = new ArrayList<Container>();
        containers.add(new Container(game, renderer, batch, "Flights"));
        containers.add(new Container(game, renderer, batch, "BlackHoles"));
        containers.add(new Container(game, renderer, batch, "WormHoles"));
        containers.add(new Container(game, renderer, batch, "Planet"));
        containers.add(new Container(game, renderer, batch, "Systems"));
    }

    /**
     * Render the background, the widgets and the different containers and their animations
     * @param delta
     */
    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        backgroundSprite.draw(batch);
        batch.end();
        for (int i = 0; i<7; i++){
            if(i!= nb) widgets[i].render(delta, true);
            else widgets[i].render(delta, false);
        }
        if(MyScreens.nb == 2 && !MyScreens.displaySpecific) containers.get(0).render();
        if(MyScreens.nb == 3 && !MyScreens.displaySpecific) containers.get(1).render();
        if(MyScreens.nb == 4 && !MyScreens.displaySpecific) containers.get(2).render();
        if(MyScreens.nb == 5 && !MyScreens.displaySpecific) containers.get(3).render();
        if(MyScreens.nb == 6 && !MyScreens.displaySpecific) containers.get(4).render();
        animations(menus.length);
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