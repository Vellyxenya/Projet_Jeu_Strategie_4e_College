package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Elements that behave like 'checkboxes'
 */
public class CheckElement extends Elements {

    private Vector2 dimensions;
    private int iterator = 0;
    private Color transGray;
    private Color transGray2;
    private Color darkGray;
    float[] table;
    float[] cooX;
    float[] cooXE;
    String[] names;
    Sprite sprite;
    GlyphLayout layout;
    BitmapFont font;
    public boolean active = false;
    public float h;
    private float pxdx2;
    public String type;
    Container c;

    public boolean checked;

    /**
     * Initialize some variables
     * @param game reference to the game
     * @param position position of the element
     * @param dimensions dimensions of the element
     * @param values dimension of the columns
     * @param names strings written in the element
     * @param type type of the element (material, flight...)
     * @param c parent container
     */
    public CheckElement(ProjectSurvival game, SpriteBatch batch, ShapeRenderer renderer, Vector2 position, Vector2 dimensions, float[] values, String[] names, String type, Container c){
        this.game = game;
        this.batch = batch;
        this.renderer = renderer;
        this.position = position;
        this.dimensions = dimensions;
        this.table = values;
        this.names = names;
        this.type = type;
        this.c = c;

        checked = false;

        sprite = new Sprite(new Texture(Gdx.files.internal("images/fly.png")));
        layout = new GlyphLayout();
        font = new BitmapFont();

        cooX = new float[table.length];
        cooXE = new float[table.length];
        transGray = new Color(0.8f, 0.8f, 0.8f, 0.4f);
        transGray2 = new Color(0.8f, 0.8f, 0.9f, 0.6f);
        darkGray = new Color(0.05f, 0.05f, 0.05f, 1);
        for(int i = 0; i<cooX.length; i++){
            iterator+=table[i]/2;
            cooXE[i] = position.x-dimensions.x/2+iterator*dimensions.x/100;
            iterator+=table[i]/2;
            cooX[i] = position.x-dimensions.x/2+iterator*dimensions.x/100;
        }
        pxdx2 = position.x-dimensions.x/2;
    }

    /**
     * Display the elements with different colours depending on if it's hovered or not
     */
    public void render(){
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        toggleCheck();
        if(mouseOn(dimensions.x, dimensions.y)){
            if(!checked) draw(transGray2);
            else draw(new Color(0, 1, 0, 0.5f));
        } else {
            if(!checked) draw(transGray);
            else draw(new Color(0, 1, 0, 0.8f));
        }
        renderData();
        if(active) {
            renderer.begin();
            renderer.setColor(darkGray);
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.rect(pxdx2, position.y-dimensions.y/2, dimensions.x-1, h);
            renderer.rect(pxdx2, position.y+dimensions.y/2-h-1, dimensions.x-1, h);
            renderer.end();
        }
        /*if(triggered){ //Peut-être à enlever.
            if(!Option.abortChange){
                active = false;
                h = 0;
                if(mouseOn(dimensions.x, dimensions.y)){
                    active = true;
                }
            }
            Option.abortChange = false;
        }*/
    }

    /**
     * Check or uncheck the element
     */
    private void toggleCheck(){
        if(triggered && mouseOn(dimensions.x, dimensions.y)){
            if(checked) {
                checked = false;
                return;
            }
            if(!checked){
                checked = true;
                return;
            }
        }
    }

    /**
     * Render the strings inside the elements at their appropriate positions
     */
    private void renderData(){
        try{
            batch.begin();
            sprite.setPosition(cooXE[0]-sprite.getWidth()/2, position.y-sprite.getHeight()/2);
            sprite.draw(batch);
            for (int i = 0; i<table.length; i++){
                layout.setText(new BitmapFont(), names[i]);
                font.draw(batch, layout, cooXE[i]-layout.width/2, position.y+layout.height/2);
            }
            batch.end();
        }catch (NullPointerException e){
            System.out.println("NullPointException");
            batch.end();
        }
    }

    /**
     * Draw the colour, columns and borders of the elements
     */
    private void draw(Color color){
        renderer.begin();
        renderer.setColor(color);
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(position.x-dimensions.x/2, position.y-dimensions.y/2, dimensions.x, dimensions.y);
        renderer.setColor(Color.WHITE);
        renderer.set(ShapeRenderer.ShapeType.Line);
        renderer.rect(position.x-dimensions.x/2, position.y-dimensions.y/2, dimensions.x, dimensions.y);
        for(int i = 0; i<cooX.length; i++){
            renderer.line(cooX[i], position.y-dimensions.y/2, cooX[i], position.y+dimensions.y/2);
        }
        renderer.end();
    }
}
