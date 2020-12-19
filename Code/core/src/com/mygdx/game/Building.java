package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.screens.HangarScreen;
import com.mygdx.game.screens.LaboratoryScreen;
import com.mygdx.game.screens.LaunchAreaScreen;
import com.mygdx.game.screens.LoadingScreen;

/**
 * Should be removed
 */
public class Building extends Clickable {

    public Building(ProjectSurvival game, SpriteBatch batch, Vector2 position, String name){
        this.game = game;
        this.batch = batch;
        this.position = position;
        this.imageName = name;
        path = "images/"+imageName;
        backGroundTexture = new Texture(Gdx.files.internal(path));
        backgroundSprite = new Sprite(backGroundTexture);
    }
    public void render(ShapeRenderer renderer){
        batch.begin();
        backgroundSprite.setPosition(position.x-backgroundSprite.getWidth()/2, position.y-backgroundSprite.getHeight()/2);
        backgroundSprite.draw(batch);
        batch.end();
        changeScreen();
    }
    public void changeScreen()
    {
        if(com.mygdx.game.screens.GameScreen.touched&& !com.mygdx.game.screens.GameScreen.dragged) {
            com.mygdx.game.screens.GameScreen.vector3.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            com.mygdx.game.screens.GameScreen.viewport.unproject(com.mygdx.game.screens.GameScreen.vector3);
            if (this.mouseOn(backgroundSprite.getWidth(), backgroundSprite.getHeight())) {
                if(imageName=="Hangar.png") game.setScreen(new HangarScreen(game));
                //if(imageName=="Observatory.png") game.setScreen(new ObservatoryScreen(game));
                if(imageName=="Observatory.png") game.setScreen(new LoadingScreen(game));
                if(imageName=="Laboratory.png") game.setScreen(new LaboratoryScreen(game));
                if(imageName=="LaunchArea.png") game.setScreen(new LaunchAreaScreen(game));
            }
        }
    }
}
