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
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.screens.GameScreen;

import java.sql.SQLException;

/**
 * Elements that display datas retrieved from the database
 */
public class Element extends Elements {

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
    private Option options[];
    public String type;
    public Container c;
    public int number;

    float ratio = 0; //Correspond à l'arrivée ou non du vaisseau à destination. Change aussi la couleur du cadre.
    public boolean delete = false;

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
    public Element(ProjectSurvival game, SpriteBatch batch, ShapeRenderer renderer, Vector2 position, Vector2 dimensions, float[] values, String[] names, String type, Container c, int number){
        this.game = game;
        this.batch = batch;
        this.renderer = renderer;
        this.position = position;
        this.dimensions = dimensions;
        this.table = values;
        this.names = names;//remove_(names);
        this.type = type;
        this.c = c;
        this.number = number;

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
        createOptions();
    }

    //Maybe to remove
    private String[] remove_(String[] table){
        int length = table.length;
        for(int i = 0; i<length; i++){
            table[i] = table[i].replace("_", " ");
            table[i].trim();
        }
        return table;
    }

    /**
     * Display some options when clicked
     */
    private void createOptions(){
        options = new Option[c.defaultOptions.length];
        for (int i = 0; i<options.length; i++){
            options[i] = new Option(game, batch, renderer, new Vector2(1000,
                    position.y-dimensions.y+20*i), new Vector2(200, 15), c.defaultOptions[i], this, i);
        }
    }

    /**
     * Display the elements with different colours depending on if it's hovered or not.
     * Manage the changement between the display of this element and the display of the associated options
     */
    public void render(){
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        if(mouseOn(dimensions.x, dimensions.y)) draw(transGray2);
        else{
            if(type.equals("Flights")) {
                draw(new Color(0, ratio, 0, 1));
            }
            else draw(transGray);
        }
        renderData();
        if(active) {
            renderer.begin();
            renderer.setColor(darkGray);
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.rect(pxdx2, position.y-dimensions.y/2, dimensions.x-1, h);
            renderer.rect(pxdx2, position.y+dimensions.y/2-h-1, dimensions.x-1, h);
            renderer.end();
            if(h<dimensions.y/2) h+=2;
            else displayOptions();
        }
        if(triggered){
            if(!Option.abortChange){
                active = false;
                h = 0;
                if(mouseOn(dimensions.x, dimensions.y)){
                    active = true;
                }
            }
            Option.abortChange = false;
        }
    }

    /**
     * Update material quantities when in the laboratory screen
     */
    public void updateQuantity(){
        names[3] = GameScreen.database.getOneData("SELECT Quantities FROM Material WHERE MaterialName = '"+names[0]+"';");
    }

    /**
     * Update The flight destinations, the progress of the flight and time remaing etc...
     */
    public void updateFlight(){
        String destination = GameScreen.database.getOneData("SELECT Destination FROM Flights WHERE id = "+names[0]+" ;");
        String origin      = GameScreen.database.getOneData("SELECT Origin FROM Flights WHERE id = "+names[0]+" ;");
        if(destination.equals(origin)) return;
        long currentTime = TimeUtils.millis();
        System.out.println(currentTime);
        long takeOffTime = Long.parseLong(GameScreen.database.getOneData("SELECT TakeOffTime FROM Flights WHERE id = "+names[0]+" ;"));
        System.out.println("TakeOff Time : "+takeOffTime);
        long duration = Double.valueOf(GameScreen.database.getOneData("SELECT FlightDuration FROM Flights WHERE id = "+names[0]+" ;")).longValue();
        System.out.println("Duration : "+duration);
        ratio = (float)(currentTime-takeOffTime)/(float)duration*20; //A enlever, le /20...
        if(ratio >= 1){ //On est arrivé à destination
            ratio = 1;
            try{
                GameScreen.database.updateDB("UPDATE Flights SET Origin = '"+destination+"' WHERE id = "+names[0]+" ;");
                names[2] = destination; //la nouvelle origine devient l'ancienne destination.
                System.out.println("Arrived at destination !!!");
            } catch (SQLException e){
                System.out.println("Failed to stop the flight...");
            }
        }
        System.out.println(ratio);
    }

    /**
     * Display the options associated to the current element
     */
    private void displayOptions(){
        for(Option option : options){
            option.render(this.position.y);
        }
    }

    /**
     * render the datas retrieved from the database inside the elements
     */
    private void renderData(){
        try{
            batch.begin();
            //sprite.setPosition(cooXE[0]-sprite.getWidth()/2, position.y-sprite.getHeight()/2);
            //sprite.draw(batch);
            for (int i = 0; i<table.length; i++){
                try{
                    layout.setText(font, names[i]);
                    font.draw(batch, layout, cooXE[i]-layout.width/2, position.y+layout.height/2);
                } catch (ArrayIndexOutOfBoundsException e){}
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
