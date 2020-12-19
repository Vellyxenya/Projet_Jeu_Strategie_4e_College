package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.screens.HomeScreen;

/**
 * Abstract class from which classes that need to detect clicks inherit
 */
abstract class Clickable extends Actor{

    /**
     * Some general tools used in almost every classes
     */
    public Vector2 position;
    Texture backGroundTexture;
    Sprite backgroundSprite;
    SpriteBatch batch;
    ShapeRenderer renderer;
    String path;
    String imageName;
    ProjectSurvival game;
    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    GlyphLayout layout;
    BitmapFont font;

    /**
     * Detect if the mouse is hovering a certain object
     * @param width width of the object
     * @param height height of the object
     * @return true if the object is being hovered
     */
    public boolean mouseOn(float width, float height) {
        float x =  HomeScreen.vector3.x;
        float y =  HomeScreen.vector3.y;
        if( (x < (position.x + width/2)) &&
                (x > (position.x - width/2)) &&
                (y < (position.y + height/2)) &&
                (y > (position.y - height/2)))
        {
            return true;
        }
        return false;
    }
}
