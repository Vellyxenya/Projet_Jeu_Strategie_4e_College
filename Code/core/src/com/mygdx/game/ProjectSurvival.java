package com.mygdx.game;

import com.badlogic.gdx.Game;

/**
 * The main-class of the game
 */
public class ProjectSurvival extends Game {

    public ProjectSurvival() {}
    
    @Override
    public void create(){
        showHomeScreen();
    }

    /**
     * Set the homeScreen to begin the game
     */
    public void showHomeScreen()
    {
        setScreen(new com.mygdx.game.screens.HomeScreen(this));
    }

    @Override
    public void dispose() {
    }
}
