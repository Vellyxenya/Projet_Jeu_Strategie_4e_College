package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Const;
import com.mygdx.game.Container;
import com.mygdx.game.ProjectSurvival;
import com.mygdx.game.Widget;

import java.util.ArrayList;

/**
 * Screen containing all the previous games.
 * In this screen, the player can load an old game and continue it
 */
public class LoadScreen extends MenuScreens {

    static Container container;
    ArrayList<Container> containers;

    public LoadScreen(ProjectSurvival game){
        super();
        this.game = game;

        menus = new String[]{"Back", "Loads"};
        widgets = new Widget[menus.length];
        cooY = new int[]{550, 450};
        for (int i = 0; i<menus.length; i++) {
            widgets[i] = new Widget(game, batch, renderer, new Vector2(50, cooY[i]), new Vector2(300, 50), menus[i]);
        }
        containers = null;
        containers = new ArrayList<Container>();
        containers.add(new Container(game, renderer, batch, "Accounts"));
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.begin();
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(0, 0, Const.WORLD_WIDTH, Const.WORLD_HEIGHT, new Color(0.2f, 0, 0, 1), new Color(0.2f, 0, 0, 1), Color.BLACK, Color.BLACK);
        renderer.end();
        for (int i = 0; i<menus.length; i++){
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