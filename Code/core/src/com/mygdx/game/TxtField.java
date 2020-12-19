package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.RegisterScreen;

import java.sql.SQLException;

/**
 * A text area where the player can write down his name as well as his planet's
 */
public class TxtField extends Clickable {
    public Vector2 dimensions;
    String text;
    float textWidth, textHeight;
    public boolean hovered;
    public boolean active = false;
    public int id;

    /**
     * Initialize some variables and font parameters
     */
    public TxtField(ProjectSurvival game, SpriteBatch batch, ShapeRenderer renderer, Vector2 position, Vector2 dimensions, int id){
        this.game = game;
        this.batch = batch;
        this.renderer = renderer;
        this.position = position;
        this.dimensions = dimensions;
        this.id = id;
        text = "";

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/couri.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        layout = new GlyphLayout();
        parameter.size = 20;
        font = generator.generateFont(parameter);
        font.setColor(Color.BLACK);
    }

    /**
     * Render the text area, detect clicks and toggle between the 2 text fields
     */
    public void render(){
        if(RegisterScreen.clicked){
            this.active = false;
            //RegisterScreen.currentTextField = -1;
            if(mouseOn(dimensions.x, dimensions.y)){
                this.active = true;
                RegisterScreen.currentTextField = id;
                System.out.println(id);
            }
        }
        renderer.begin();
        renderer.set(ShapeRenderer.ShapeType.Filled);
        if(active) {
            renderer.setColor(new Color(0.8f, 0.8f, 1, 1));
        }
        else renderer.setColor(Color.WHITE);
        renderer.rect(position.x-dimensions.x/2, position.y-dimensions.y/2, dimensions.x, dimensions.y);
        renderer.end();
        batch.begin();
        textWidth = layout.width;
        textHeight = layout.height;
        font.draw(batch, text, position.x-dimensions.x/2, position.y);
        batch.end();
    }

    /**
     * Update the text written in the text area
     * @param key the key typed by the user and that should be displayed in the text area
     */
    public void updateText(int key){
        if(active) {
            try {
                if (key == -1) {
                    text = text.substring(0, text.length() - 1);
                    return;
                }
                if (text.length() < 10) {
                    String letter = Character.toString((char) key);
                    if (letter.matches("[A-Z]"))
                        this.text += letter;
                }
            } catch (StringIndexOutOfBoundsException e) {
                return;
            }
        }
    }

    public String getText(){
        return text;
    }

    /*public void performAction(String action, int id){
        if(action.equals("Add to favourite")){
            try {
                GameScreen.database.updateDB("UPDATE Planet SET Favourite = 1 WHERE id = " + id);
            }catch (SQLException e){}
            System.out.println("updated favourites");
        }
    }*/
}
