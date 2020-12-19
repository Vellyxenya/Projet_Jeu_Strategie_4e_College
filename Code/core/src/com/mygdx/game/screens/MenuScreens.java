package com.mygdx.game.screens;

import com.badlogic.gdx.Input;
import com.mygdx.game.Const;

/**
 * Class holding some methods to go from screen to screen
 */
public class MenuScreens extends MyScreens {

    public MenuScreens(){
        super();
    }

    /**
     * Go to certain screens, depending on the button that has been pressed
     * @param key
     * @return
     */
    @Override
    public boolean keyUp(int key){
        if(key == Input.Keys.F2) game.setScreen(new HomeScreen(game));
        if(key == Input.Keys.F1) game.setScreen(new GalaxyScreen(game));
        if(key == Input.Keys.ENTER){
            if(game.getScreen().getClass().equals(IntroductionScreen.class)){
                try{
                    game.setScreen(new IntroductionScreen(game, Const.texts[IntroductionScreen.currentText], true));
                }catch (ArrayIndexOutOfBoundsException e){
                    game.setScreen(new GameScreen(game, true));
                }
            }
        }
        return true;
    }

    /**
     * Return to HomeScreen if the backspace is pressed
     * @param key
     * @return
     */
    @Override
    public boolean keyDown(int key){
        if(key == Input.Keys.BACK) game.setScreen(new HomeScreen(game));
        return true;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
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
