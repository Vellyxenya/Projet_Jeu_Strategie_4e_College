package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.game.ProjectSurvival;

/**
 * Screen displayed at the very beginning of the game, explaining to the player what he's supposed to do etc...
 * The letters of the text are progressively displayed
 */
public class IntroductionScreen extends MenuScreens {

    /**
     * Some variables needed in the following algorithm
     */
    int a, b, c, j, visible, drawed;
    double transparencyCounter;
    String text;
    String dimension[][];
    char table[];
    public static int currentText;

    FreeTypeFontGenerator generator2;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter2;
    BitmapFont font2;

    /**
     * Initialize the different fonts and their parameters
     * @param game
     * @param text
     * @param next
     */
    public IntroductionScreen(ProjectSurvival game, String text, boolean next){
        super();
        if(!next) currentText = 1;
        else currentText++;
        this.game = game;
        this.text = text;
        initVar();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ErikaType.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        generator2 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/goodTimes.ttf"));
        parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 44;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        parameter2.size = 24;
        parameter2.borderColor = Color.RED;
        parameter2.borderWidth = 1;
        font = generator.generateFont(parameter);
        font.setColor(Color.WHITE);
        font2 = generator2.generateFont(parameter2);
        generator.dispose();
        table = text.toUpperCase().toCharArray();
        dimension = new String[35][(int)Math.ceil(table.length/35)+2];
        calculateText();
    }

    /**
     * Initiate some variables
     */
    private void initVar(){
        visible = 0;
        drawed = 0;
        a = 0;
        b = 0;
        c = 0;
        j = 0;
    }

    /**
     * Algorithm to create a text by doing line braks without cutting words in half
     * Extracted from 'Nightmare Gravity'
     */
    private void calculateText(){
        for(int i = 0; i<table.length; i++) {
            if(table[i] == ' ') {
                dimension[a][b] = Character.toString(table[i]);
                a++;
            }
            else if(table[i] != ' ') {
                c = i;
                j = 0;
                while(table[c] != ' ') {
                    if(c<table.length) {
                        c++;
                        j++;
                    }
                }
                if(j<35-a) {
                    dimension[a][b] = Character.toString(table[i]);
                    a++;
                }
                else {
                    b++;
                    a = 0;
                    dimension[a][b] = Character.toString(table[i]);
                    a++;
                }
            }
            if(a==35) {
                a = 0;
                b++;
            }
        }
    }

    /**
     * Write the text, letter after the other
     */
    public void write() {
        drawed = 0;
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(int i = 0; i<b+1; i++){
            for(int k = 0; k<35; k++){
                drawed++;
                try{
                    if(drawed<visible) font.draw(batch, dimension[k][i], 100f+40f*(float)k, 800f-50f*(float)i);
                }catch(NullPointerException e){}
            }
        }
        batch.end();
        visible++;
    }

    /**
     * Display a text asking the player to press on Enter once he's finished reading
     */
    private void pressEnter(){
        transparencyCounter+=0.03;
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font2.setColor(new Color(1, 1, 1, (float) Math.abs(Math.sin(transparencyCounter))));
        font2.draw(batch, "Press Enter to continue...", 1100, 100);
        batch.end();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        write();
        pressEnter();
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
