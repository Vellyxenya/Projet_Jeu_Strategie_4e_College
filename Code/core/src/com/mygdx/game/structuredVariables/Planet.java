package com.mygdx.game.structuredVariables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ActionButton;
import com.mygdx.game.ProjectSurvival;
import com.mygdx.game.database.Algorithms;
import com.mygdx.game.screens.DisplaySystemScreen;
import com.mygdx.game.screens.GameScreen;

import java.util.ArrayList;

/**
 * Contains all informations about a given planet, displays its informations and its position
 */
public class Planet extends Actors {

    /*
    Variables membres de la classe
     */
    int     condition; //condition selon laquelle on affiche certaines infos ou pas
    int     id;
    int     SystemId;
    long    HumansNb;
    float   Distance;
    float   Density;
    float   xPosition, yPosition;
    float   radius;
    String  PlanetName;
    String  PlanetType;
    String  Oxygen;
    String  Water;
    String  AtmosphereQuantity;
    String  MagneticField;
    String  Discovered;
    String  Scanned;
    boolean display;
    boolean createdFlights = false;
    double  alpha;
    ActionButton ab;
    String[]     actions = {"Add Planet to favourite"};

    /**
     * Initialise une nouvelle planète ainsi que ses attributs à partir de la base de données
     * @param game
     * @param id id de la planète
     * @param batch
     * @param renderer
     * @param SystemId id du système auquel appartient la planète
     */
    public Planet(ProjectSurvival game, int id, SpriteBatch batch, ShapeRenderer renderer, int SystemId){
        alpha = 0;
        this.game = game;
        this.batch = batch;
        this.renderer = renderer;
        this.id = id;
        this.SystemId = SystemId;
        this.Distance = Float.parseFloat(GameScreen.database.getOneData("SELECT Distance FROM 'Planet' WHERE id = "+id));
        this.PlanetName = GameScreen.database.getOneData("SELECT PlanetName FROM 'Planet' WHERE id = "+id);
        this.HumansNb = Long.parseLong(GameScreen.database.getOneData("SELECT HumansNb FROM 'Planet' WHERE id = "+id));

        String whatToShow = GameScreen.database.getOneData("SELECT Characteristics FROM Planet WHERE id = "+id);

        condition = Integer.parseInt(GameScreen.database.getOneData("SELECT Water FROM 'Planet' WHERE id = "+id));
        if (whatToShow.contains("W")) Water = (condition == 1) ? "YES" : "NO";
        else Water = "Unknown";
        condition = Integer.parseInt(GameScreen.database.getOneData("SELECT Oxygen FROM 'Planet' WHERE id = "+id));
        if (whatToShow.contains("O")) Oxygen = (condition == 1) ? "YES" : "NO";
        else Oxygen = "Unknown";
        if (whatToShow.contains("M")) MagneticField = GameScreen.database.getOneData("SELECT MagneticField FROM 'Planet' WHERE id = "+id);
        else MagneticField = "Unknown";
        AtmosphereQuantity = GameScreen.database.getOneData("SELECT AtmosphereQuantity FROM 'Planet' WHERE id = "+id);
        condition = Integer.parseInt(GameScreen.database.getOneData("SELECT Discovered FROM 'Planet' WHERE id = "+id));
        Discovered = (condition == 1) ? "YES" : "NO";
        condition = Integer.parseInt(GameScreen.database.getOneData("SELECT Scanned FROM 'Planet' WHERE id = "+id));
        Scanned = (condition == 1) ? "YES" : "NO";
        PlanetType = GameScreen.database.getOneData("SELECT PlanetType FROM 'Planet' WHERE id = "+id);

        backGroundTexture = new Texture(Gdx.files.internal("images/stars/C.png"));
        backgroundSprite = new Sprite(backGroundTexture);
        Density = Float.parseFloat(GameScreen.database.getOneData("SELECT Density FROM 'Planet' WHERE id = "+id));
        backgroundSprite.setPosition(500+Distance/3, 500);
        radius = Density*3;

        layout = new GlyphLayout();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Anony.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 0.5f;
        font = generator.generateFont(parameter);

        ab = new ActionButton(game, batch, renderer, new Vector2(1185, 450), new Vector2(200, 15), actions[0]);
    }

    /**
     * Give the planet a circular motion around its sun
     */
    public void render(){
        if(display){
            ab.render();
        }
        alpha += (2/Distance);
        xPosition = 500+Distance/2*(float)Math.cos(alpha%(2*Math.PI));
        yPosition = 500+Distance/2*(float)Math.sin(alpha%(2*Math.PI));
        renderer.begin();
        renderer.setColor(Color.DARK_GRAY);
        renderer.set(ShapeRenderer.ShapeType.Line);
        renderer.circle(500, 500, Distance/2);
        renderer.setColor(Color.RED);
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.circle(xPosition, yPosition, radius);
        renderer.end();

        handleClicks();
        displayInfos();
        if(!createdFlights) {
            createFlights();
            createdFlights = true;
        }
    }

    /**
     * Add a new flight if there's one on this planet
     */
    private void createFlights(){
        ArrayList<String> flights = GameScreen.database.readDatas("SELECT id, Description, EquippedModules FROM Flights WHERE Origin = Destination AND Origin = '"+PlanetName+"' ;");
        System.out.println("flights.size() : "+flights.size());
        for(String s : flights){
            String parts[] = s.split(" ");
            try{
                DisplaySystemScreen.flights.add(new Flights(game, renderer, batch, Integer.parseInt(parts[0]), parts[1], parts[2], Distance));
            }catch (ArrayIndexOutOfBoundsException e){
                DisplaySystemScreen.flights.add(new Flights(game, renderer, batch, Integer.parseInt(parts[0]), parts[1], "", Distance));
            }
        }
        System.out.println("createFlights() seem to be completed");
    }

    /**
     * Display the properties of the planet
     */
    private void displayInfos(){
        if(display){
            batch.begin();
            font.draw(batch, "Planet ID           : "+id, 1050, 700);
            font.draw(batch, "Name                : "+PlanetName, 1050, 660);
            font.draw(batch, "Distance            : "+Distance, 1050, 640);
            font.draw(batch, "Density             : "+Density, 1050, 620);
            font.draw(batch, "Oxygen              : "+Oxygen, 1050, 600);
            font.draw(batch, "Water               : "+Water, 1050, 580);
            font.draw(batch, "Magnetic Field      : "+MagneticField, 1050, 560);
            font.draw(batch, "Atmosphere Quantity : "+AtmosphereQuantity, 1050, 540);
            font.draw(batch, "Discovered          : "+Discovered, 1050, 520);
            font.draw(batch, "Scanned             : "+Scanned, 1050, 500);
            batch.end();
        }
    }

    /**
     * Display certain informations if the planet has been clicked, allow adding the planet to the favourites
     */
    private void handleClicks(){
        if(ActionButton.triggered){
            if(ab.hovered){
                ab.addPlanetToFavourite(ab.getDisplay(), id);
                return;
            }
            else if(!ab.hovered){
                this.display = false;
                int xMouse = DisplaySystemScreen.xCoo;
                int yMouse = DisplaySystemScreen.yCoo;
                if(Algorithms.getDistance(xPosition, yPosition, (float)xMouse, (float)yMouse)<radius){
                    this.display = true;
                }
            }
        }
    }
}

