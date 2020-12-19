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
 * Screen in which the user can build modules and rockets
 */
public class HangarScreen extends MyScreens {

    static Container container;
    public static ArrayList<Container> containers;

    /**
     * Initialize the contains and the background image
     * @param game
     */
    public HangarScreen(ProjectSurvival game){
        super();
        this.game = game;

        menus = new String[]{"Planet", "Ships", "Modules", "Built Modules"};
        cooY = new int[]{850, 350, 250, 150};
        widgets = new Widget[4];
        for(int i = 0; i<menus.length; i++){
            widgets[i] = new Widget(game, batch, renderer, new Vector2(50, cooY[i]), new Vector2(300, 50), menus[i]);
        }
        backGroundTexture = new Texture(Gdx.files.internal("images/HangarScreenBackground.jpg"));
        backgroundSprite = new Sprite(backGroundTexture);

        containers = null;
        containers = new ArrayList<Container>();
        containers.add(new Container(game, renderer, batch, "Rocket"));
        containers.add(new Container(game, renderer, batch, "Equipment"));
        containers.add(new Container(game, renderer, batch, "Built Modules"));
    }

    /**
     * Display the different buttons and containers corresponding the to clicked buttons
     */
    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        backgroundSprite.draw(batch);
        batch.end();

        for(int i = 0; i<menus.length; i++){
            if(i!= nb) widgets[i].render(delta, true);
            else widgets[i].render(delta, false);
        }

        if(MyScreens.nb == 1 && !MyScreens.displaySpecific) containers.get(0).render();
        if(MyScreens.nb == 2 && !MyScreens.displaySpecific) containers.get(1).render();
        if(MyScreens.nb == 3 && !MyScreens.displaySpecific) containers.get(2).render();
        animations(menus.length); //Animate the opening/closing of the containers
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        System.out.println("HangarScreen disposed ::::::::::::::");
        super.dispose();
    }
}