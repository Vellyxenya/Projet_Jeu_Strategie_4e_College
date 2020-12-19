package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ActionButton;
import com.mygdx.game.Const;
import com.mygdx.game.ProjectSurvival;
import com.mygdx.game.TxtField;
import com.mygdx.game.database.DataBase;

import java.util.ArrayList;

import static com.mygdx.game.database.Algorithms.copyFile;

/**
 * Screen used to register the player name and its planet into the accounts database
 */
public class RegisterScreen extends MenuScreens {

    private static ArrayList<TxtField> tfs = new ArrayList<TxtField>();
    public static int currentTextField;
    private ActionButton actionButton1, actionButton2;
    private boolean displayErrorMessage = false;
    private BitmapFont font2;

    /**
     * Initialize some variables and creates some objects like text areas and buttons
     * @param game reference to our game
     */
    RegisterScreen(ProjectSurvival game){
        super();
        this.game = game;
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/goodTimes.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        font = generator.generateFont(parameter);
        font.setColor(Color.WHITE);
        parameter.size = 36;
        parameter.borderColor = Color.RED;
        parameter.borderWidth = 1;
        font2 = generator.generateFont(parameter);
        font2.setColor(Color.WHITE);
        generator.dispose();
        layout = new GlyphLayout();

        tfs.add(0, new TxtField(game, batch, renderer, new Vector2(Const.WORLD_WIDTH/2, 700), new Vector2(200, 30), 0));
        tfs.add(1, new TxtField(game, batch, renderer, new Vector2(Const.WORLD_WIDTH/2, 650), new Vector2(200, 30), 1));
        actionButton1 = new ActionButton(game, batch, renderer, new Vector2(Const.WORLD_WIDTH/2, 500), new Vector2(200, 20), "Create profile!");
        actionButton2 = new ActionButton(game, batch, renderer, new Vector2(Const.WORLD_WIDTH/2, 450), new Vector2(200, 20), "Back to Main Menu!");
        clicked  = false;
        currentTextField = -1;
    }

    /**
     * Draw labels, the areas and buttons
     * Mange the clicks and proceeds to register the data in the accounts database if they are correct
     * Once it's done, moves to the introduction screen to begin the game
     * Displays an error message if something went wrong
     */
    @Override
    public void render(float delta) {
        super.render(delta);
        renderer.begin();
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(0, 0, Const.WORLD_WIDTH, Const.WORLD_HEIGHT, new Color(0.2f, 0, 0, 1), new Color(0.2f, 0, 0, 1), Color.BLACK, Color.BLACK);
        renderer.end();
        drawLabels();
        batch.begin();
        layout.setText(font2, "CREATE AN ACCOUNT");
        font2.draw(batch, layout, Const.WORLD_WIDTH/2-layout.width/2, 900);
        batch.end();
        for(TxtField tf : tfs){
            tf.render();
        }
        actionButton1.render();
        actionButton2.render();
        if(clicked){
            if(actionButton1.hovered){
                if(tfs.get(0).getText().length()>2 && tfs.get(1).getText().length()>2){
                    DataBase db = new DataBase("Accounts/accounts.db");
                    boolean insertionSucceeded = db.addNewPlayer(tfs.get(0).getText());
                    db.closeDB();
                    if(insertionSucceeded){
                        copyFile("database.db", "saves/"+tfs.get(0).getText()+".db");
                        String[] infos = actionButton1.register(tfs.get(0).getText(), tfs.get(1).getText()); //Insert name and planet into db.
                        Const.PLAYER_NAME = infos[0];
                        Const.PLANET_NAME = infos[1];
                        Const.initTexts();
                        game.setScreen(new IntroductionScreen(game, Const.texts[0], false));
                    } else {
                        System.out.println("Insertion failed! Try again with another user name!");
                        displayErrorMessage = true;
                    }
                } else {
                    displayErrorMessage = true;
                }

            }
            else if(actionButton2.hovered){
                clicked = false;
                game.setScreen(new HomeScreen(game));
            }
        }
        if(displayErrorMessage){
            batch.begin();
            font.setColor(Color.RED);
            font.draw(batch, "This user name is already used or incorrect. If you want to continue an existing game, go to the Load menu.", 200, 200);
            batch.end();
        }
        clicked = false;
    }

    /**
     * Add or remove focus on the given text area, according to the clicks/tabs
     * Writes text in the text areas
     * @param key name of the key that has been pressed
     * @return true if a key has been pressed
     */
    @Override
    public boolean keyUp(int key){
        if(key == Input.Keys.TAB){
            if(!RegisterScreen.tfs.get(0).active) {
                RegisterScreen.tfs.get(0).active = true;
                RegisterScreen.tfs.get(1).active = false;
                currentTextField = 0;
            }
            else {
                RegisterScreen.tfs.get(0).active = false;
                RegisterScreen.tfs.get(1).active = true;
                currentTextField = 1;
            }
        }
        if(currentTextField != -1) {
            if (key == Input.Keys.BACKSPACE) {
                tfs.get(currentTextField).updateText(-1);
                return true;
            }
            int realKey = key + 36;
            tfs.get(currentTextField).updateText(realKey);
        }
        return true;
    }

    /**
     * Draw the labels corresponding to the fields
     */
    private void drawLabels(){
        batch.begin();
        font.setColor(Color.WHITE);
        font.draw(batch, "Identifier : ", 500, 700);
        font.draw(batch, "Planet name : ", 500, 650);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        font.dispose();
    }
}
