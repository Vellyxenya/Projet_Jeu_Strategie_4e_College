package com.mygdx.game.structuredVariables;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.ProjectSurvival;

public class Actors {

    protected ProjectSurvival game;
    protected Texture backGroundTexture;
    protected Sprite backgroundSprite;
    protected SpriteBatch batch;
    protected ShapeRenderer renderer;
    protected FreeTypeFontGenerator generator;
    protected FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    protected GlyphLayout layout;
    protected BitmapFont font;

}
