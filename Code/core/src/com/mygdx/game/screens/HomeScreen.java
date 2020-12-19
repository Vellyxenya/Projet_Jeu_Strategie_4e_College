package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.Button;
import com.mygdx.game.Const;
import com.mygdx.game.HomeStar;
import com.mygdx.game.HomeStars;
import com.mygdx.game.ProjectSurvival;
import com.mygdx.game.database.Algorithms;
import com.mygdx.game.database.DataBase;
import com.mygdx.game.structuredVariables.Equipment;

import java.util.NoSuchElementException;

public class HomeScreen extends MyScreens {

    Button[] buttons = new Button[7];
    HomeStars homeStars;
    BitmapFont font2;
    String[] menus;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter2;
    String title;
    int drawContinueButton = 0; //Le bouton 'continue' est affiché à l'écran.

    public HomeScreen (ProjectSurvival game) {
        super();
        this.game = game;
        try { //Important pour savoir s'il y a déjà une sauvegarde ou pas.
            String fileName = Algorithms.lastModifiedFile("saves");
            //String fileName = "UU.db";
        } catch(NoSuchElementException e){
            drawContinueButton = 1;
        }
        menus = new String[]{"Continue", "New Game", "Generate Datas", "Loads", "Tutorial", "Credits", "Quit"};
        for (int i = drawContinueButton; i<7; i++)
        {
            buttons[i] = new Button(game, viewport, new Vector2(Const.WORLD_WIDTH/2, Const.HOME_SCREEN_BUTTONS_POSITION-i*70), 250, 60);
        }

        homeStars = new HomeStars();
        layout = new GlyphLayout();
        backGroundTexture = new Texture(Gdx.files.internal("images/HomeScreenBackground.jpg"));
        backgroundSprite = new Sprite(backGroundTexture);
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/goodTimes.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        title = "Project : Survival";

        parameter.size = 24;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        font = generator.generateFont(parameter);
        font.setColor(new Color(150/255f,180/255f,1,1));

        parameter2.size = 80;
        parameter2.borderColor = Color.BLACK;
        parameter2.borderWidth = 2;
        font2 = generator.generateFont(parameter2);
        font2.setColor(new Color(150/255f,180/255f,1,1));
        generator.dispose();

        Equipment.initMap();
    }

    public void changeScreen() {
        if(touched && !dragged) {
            vector3.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(vector3);
            try{
                if (buttons[0].mouseOn()) {
                    DataBase database = null;
                    try {
                        //Const.INITIAL_TIME = 1485076678885L; //A changer
                        String fileName = Algorithms.lastModifiedFile("saves");
                        //String fileName = "UU.db";
                        //String path = Gdx.files.internal("saves/" + fileName).path();
                        String directory = System.getProperty("user.dir");
                        String path = Gdx.files.absolute(directory+"/saves/" + fileName).path();
                        System.out.println(path);
                        database = new DataBase(false, path);
                        Const.PLAYER_NAME = database.getOneData("SELECT PlayerName FROM Player;");
                        Const.PLANET_NAME = database.getOneData("SELECT PlanetName FROM Player;");
                        Const.SCENARIO    = Integer.parseInt(database.getOneData("SELECT Scenario FROM Player"))-1;
                        //peut-être à rajouter des trucs.
                        database.closeDB();
                        game.setScreen(new GameScreen(game, true));
                    }catch (NullPointerException e){
                        database.closeDB();
                        e.printStackTrace();
                    }
                }
            }catch (NullPointerException ex){}
            if (buttons[1].mouseOn()) {
                game.setScreen(new RegisterScreen(game));
            }
            if (buttons[2].mouseOn()) game.setScreen(new ConstructDataBaseScreen(game));
            if (buttons[3].mouseOn()) game.setScreen(new LoadScreen(game));
            if (buttons[4].mouseOn()) game.setScreen(new TutorialScreen(game));
            if (buttons[5].mouseOn()) game.setScreen(new CreditsScreen(game));
            if (buttons[6].mouseOn()) Gdx.app.exit();
        }
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY){
        return false;
    }
    @Override
    public void render(float delta) {
        super.render(delta);
        renderer.setAutoShapeType(true);
        batch.begin();
        backgroundSprite.draw(batch);
        batch.end();
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(1,1,1,1);
        renderer.set(ShapeRenderer.ShapeType.Line);
        drawEffect();
        drawButtons();
        changeScreen();
    }

    private void drawEffect(){
        if(touched == true) {
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.circle(vector3.x, vector3.y, 5);
            renderer.set(ShapeRenderer.ShapeType.Line);
            for (HomeStar star : HomeStars.stars) {
                if (vector3.dst(star.position) < Const.MAX_DIST_MOUSE_STAR) {
                    renderer.line(vector3.x, vector3.y, star.position.x, star.position.y, Color.WHITE, Color.BLUE);
                }
            }
        }
    }

    private void drawButtons(){
        for (Button button: buttons) {
            try {
                button.render(renderer);
            }catch (NullPointerException e){}
        }
        renderer.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (int i = drawContinueButton; i<7; i++) {
            layout.setText(font, menus[i]);
            font.draw(batch, menus[i], Const.WORLD_WIDTH/2-layout.width/2, Const.HOME_SCREEN_BUTTONS_POSITION+layout.height/2-i*70);
        }
        layout.setText(font2, title);
        font2.draw(batch, title, Const.WORLD_WIDTH/2-layout.width/2, 850);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        font.dispose();
    }
}
