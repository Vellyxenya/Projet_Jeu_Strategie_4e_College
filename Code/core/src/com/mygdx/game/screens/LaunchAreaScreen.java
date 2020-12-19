package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Container;
import com.mygdx.game.ProjectSurvival;
import com.mygdx.game.Widget;

import java.util.ArrayList;

/**
 * Screen In which the player can launch his rockets
 */
public class LaunchAreaScreen extends MyScreens {

    static Container container;
    public static ArrayList<Container> containers;

    /**
     * Initialize some variables, the widgets and containers
     * @param game
     */
    public LaunchAreaScreen(ProjectSurvival game){
        super();
        this.game = game;

        menus = new String[]{"Planet", "Launch"};
        widgets = new Widget[menus.length];
        cooY = new int[]{850, 150};
        for (int i = 0; i<menus.length; i++) {
            widgets[i] = new Widget(game, batch, renderer, new Vector2(50, cooY[i]), new Vector2(300, 50), menus[i]);
        }
        backGroundTexture = new Texture(Gdx.files.internal("images/LaunchAreaScreenBackground.jpg"));
        backgroundSprite = new Sprite(backGroundTexture);
        containers = null;
        containers = new ArrayList<Container>();
        containers.add(new Container(game, renderer, batch, "BuiltRocket"));
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        backgroundSprite.draw(batch);
        batch.end();
        for (int i = 0; i<2; i++){
            if(i!= nb) widgets[i].render(delta, true);
            else widgets[i].render(delta, false);
        }
        if(MyScreens.nb == 1 && !MyScreens.displaySpecific) containers.get(0).render();
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
