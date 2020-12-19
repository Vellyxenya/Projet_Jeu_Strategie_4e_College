package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.screens.HomeScreen;

/**
 * Buttons displayed in the Home Screen. The very first buttons created and therefore a little bit outdated
 */
public class Button extends Actor{
    private int width, height, xMouse, yMouse;
    private Color transparent, light, dark;
    Vector2 position;
    Viewport viewport;
    Camera camera;
    ProjectSurvival game;
    SpriteBatch batch;
    BitmapFont font;

    /**
     * Creates a new fancy button in the HomeScreen
     * @param game
     * @param viewport
     * @param position
     * @param width
     * @param height
     */
    public Button(ProjectSurvival game, Viewport viewport, Vector2 position, int width, int height){
        this.game = game;
        this.viewport = viewport;
        this.position = position;
        this.width = width;
        this.height = height;
        camera = new OrthographicCamera();
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.GOLD);
        transparent = new Color(0f,0f,0f,0f);
        light = Color.LIGHT_GRAY;
        dark = Color.RED;
    }

    /**
     * Display the button each frame
     * @param renderer
     */
    public void render(ShapeRenderer renderer){
        if (mouseOn()){
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            renderer.rect(position.x-width/2,position.y-height/2,width, height, dark, transparent, dark, transparent);
            renderer.rect(position.x-width/2+1,position.y-height/2+1,width-2, height-2, dark, transparent, dark, transparent);
        }
        else if (!mouseOn()){
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            renderer.rect(position.x-width/2,position.y-height/2,width, height, light, transparent, light, transparent);
            renderer.rect(position.x-width/2+1,position.y-height/2+1,width-2, height-2, light, transparent, light, transparent);
        }
    }

    /**
     * Returns wheter our mouse is hovering the button or not.
     * @return
     */
    public boolean mouseOn() {
        xMouse = (int) HomeScreen.vector3.x;
        yMouse = (int) HomeScreen.vector3.y;
        if( (xMouse < (position.x + width/2)) &&
                (xMouse > (position.x - width/2)) &&
                (yMouse < (position.y + height/2)) &&
                (yMouse > (position.y - height/2)))
        {
            return true;
        }
        return false;
    }
}
