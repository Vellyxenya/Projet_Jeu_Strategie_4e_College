package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

/**
 * Maybe to be removed, loads images etc...
 */
public class Assets {
    public static final AssetManager manager = new AssetManager();

    public static void load(){
        manager.load("images/ObservatoryScreenBackground.jpg", Texture.class);
    }

    public static void dispose(){
        manager.dispose();
    }
}
