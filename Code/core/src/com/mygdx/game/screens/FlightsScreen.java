package com.mygdx.game.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ProjectSurvival;
import com.mygdx.game.Widget;

/**
 * Could be removed...
 */
public class FlightsScreen extends MyScreens{

    public Widget[] widgets;
    String[] menus = {"Observatory", "Galaxy", "Flights", "BlackHoles", "WormHoles", "Spatial Stations", "Favourite"};

    private float[] dragonCurve1;
    private float[] dragonCurve2;
    private float[] dragonCurve3;
    private float[] dragonCurve4;
    private static final int RECURSIONS = 13;
    Texture backGroundTexture;
    Sprite backgroundSprite;

    public FlightsScreen(ProjectSurvival game){
        super();
        this.game = game;
        widgets = new Widget[7];
        for (int i = 0, j = 100; i<7; i++, j--){
            widgets[i] = new Widget(game, batch, renderer, new Vector2(50, 750-100*i+100*(j/100)), new Vector2(300, 50), menus[i]);
        }
    }
    @Override
    public void show() {
    }
    @Override
    public void render(float delta) {
        super.render(delta);
        for (Widget widget : widgets){
            widget.render(delta, true);
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
