package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.ActionButton;
import com.mygdx.game.Const;
import com.mygdx.game.Elements;
import com.mygdx.game.Option;
import com.mygdx.game.ProjectSurvival;
import com.mygdx.game.Widget;

/**
 * Super-class of all the other screens, contains a lot of variables and methods
 * that are inherited by many other classes
 */
public class MyScreens extends InputAdapter implements Screen, InputProcessor {

    Texture backGroundTexture;
    Sprite backgroundSprite;
    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    GlyphLayout layout;
    BitmapFont font;

    protected final float ANIM_AXE_Y = 1000;
    protected String[] menus;
    protected int[] cooY;

    public static boolean clicked = false;
    public static int xCoo;
    public static int yCoo;

    public ProjectSurvival game;
    public Camera camera;
    public ShapeRenderer renderer; //Ici on instancie le premier objet qui nous permettra de dessiner
    public SpriteBatch batch; //Et ici le deuxième
    public AssetManager manager;

    public static Viewport viewport;
    public static Widget[] widgets;
    public static Vector3 vector3;
    public static boolean touched;
    public static boolean dragged;
    public static boolean released;
    public static boolean displaySpecific;
    public static boolean unDisplay;
    public static int nb;

    public static int currentFlightId;
    public static String currentOrigin;

    public MyScreens(){
        //On initialise certains trucs (caméra, blend, etc...) qui nous permettront de dessiner.
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        camera = new OrthographicCamera();
        viewport = new FitViewport(Const.WORLD_WIDTH, Const.WORLD_HEIGHT, camera);
        viewport.setScreenBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        renderer = new ShapeRenderer(); //on initialise le renderer
        takeInput();
        Gdx.input.setCatchBackKey(true);
        vector3 = new Vector3(0,0,0);
        batch = new SpriteBatch(); //on initialise le batch
        manager = new AssetManager();
        initVar();
    }

    /**
     * Detect if the user has clicked on the given locations (x,y)
     * @param x x-coordinates of the click
     * @param y y-coordinates of the click
     */
    public static void detectClick(int x, int y){
        clicked = true;
        xCoo = x;
        yCoo = (int)Const.WORLD_HEIGHT-y;
    }

    public void takeInput(){
        Gdx.input.setInputProcessor(this);
    } //S'auto-assigne la commande des inputs

    private void initVar(){
        touched = false;
        dragged = false;
        released = false;
        displaySpecific = false;
        unDisplay = true;
        nb = -1;
    }

    /**
     * Produit une animation de glissement des différents widgets/containers
     * @param howMany
     */
    protected void animations(int howMany){
        if(unDisplay){
            unDisplay(howMany);
        }
        if(displaySpecific && !unDisplay){
            switch (nb){
                case 0:
                    displaySpecific(0, howMany);
                    break;
                case 1:
                    displaySpecific(1, howMany);
                    break;
                case 2:
                    displaySpecific(2, howMany);
                    break;
                case 3:
                    displaySpecific(3, howMany);
                    break;
                case 4:
                    displaySpecific(4, howMany);
                    break;
                case 5:
                    displaySpecific(5, howMany);
                    break;
                case 6:
                    displaySpecific(6, howMany);
                    break;
            }
        }
    }

    /**
     * Ordonne à un container de se remettre sous la forme d'un widget
     * @param howMany
     */
    public void unDisplay(int howMany){
        int changes = 0;
        for(int i = 0; i<howMany; i++){
            if(widgets[i].position.x<200) {
                widgets[i].position.x+=10;
                changes++;
            }
            if(widgets[i].position.x>200) {
                widgets[i].position.x-=20;
                changes++;
            }
            if(widgets[i].position.y<cooY[i]) {
                widgets[i].position.y+=10;
                changes++;
            }
            if(widgets[i].position.y>cooY[i]) {
                widgets[i].position.y-=10;
                changes++;
            }
            if(widgets[i].dimensions.x>300) {
                widgets[i].dimensions.x-=20;
                changes++;
            }
            if(widgets[i].dimensions.y>50) {
                widgets[i].dimensions.y-=20;
                changes++;
            }
        }
        if (changes == 0) {
            unDisplay = false;
        }
    }

    /**
     * Ordonne à un widget de se mettre sous la forme d'un container
     * @param nb
     * @param howMany
     */
    public void displaySpecific(int nb, int howMany){
        int changes = 0;
        if(widgets[nb].position.x<ANIM_AXE_Y) {
            widgets[nb].position.x+=20;
            changes++;
        }
        if(widgets[nb].position.y>500) {
            widgets[nb].position.y-=10;
            changes++;
        }
        if(widgets[nb].position.y<500) {
            widgets[nb].position.y+=10;
            changes++;
        }
        if(widgets[nb].dimensions.x<1000) {
            widgets[nb].dimensions.x+=20;
            changes++;
        }
        if(widgets[nb].dimensions.y<900) {
            widgets[nb].dimensions.y+=20;
            changes++;
        }
        for (int i = nb+1; i<howMany; i++){
            if (widgets[i].position.y < cooY[i]+100) widgets[i].position.y+=5;
        }
        if(changes == 0){
            displaySpecific = false;
        }
    }

    /**
     * Detect le scrolling de la souris et notifie les différents containers pour qu'ils puissent faire
     * défiler les informations qui y sont contenues
     * @param amount
     * @return
     */
    @Override
    public boolean scrolled (int amount){
        if(game.getScreen().getClass().equals(ObservatoryScreen.class)){
            if(!displaySpecific){
                if(nb==2 || nb==5 || nb==6){
                    ObservatoryScreen.container.position+=30*amount;
                }
            }
        }
        else if(game.getScreen().getClass().equals(LaboratoryScreen.class)){
            if(nb==1 && !displaySpecific) {
                LaboratoryScreen.container.position+=30*amount;
            }
        }
        else if(game.getScreen().getClass().equals(HangarScreen.class)){
            if(!displaySpecific){
                if(nb==1 || nb==2 || nb==3){
                    HangarScreen.container.position+=30*amount;
                }
            }
        }
        else if(game.getScreen().getClass().equals(LaunchAreaScreen.class)){
            if(nb==1 && !displaySpecific) {
                LaunchAreaScreen.container.position+=30*amount;
            }
        }
        else if(game.getScreen().getClass().equals(LoadScreen.class)){
            if(nb==1 && !displaySpecific) {
                LoadScreen.container.position+=30*amount;
            }
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer){
        dragged = true;
        released = false;
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button){
        touched = true;
        released = false;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button){
        System.out.println(screenX);
        System.out.println(screenY);
        touched = false;
        dragged = false;
        released = true;
        if(game.getScreen().getClass().equals(GalaxyScreen.class)){
            GalaxyScreen.detectClick(screenX, screenY);
        }
        if(game.getScreen().getClass().equals(DisplaySystemScreen.class)){
            DisplaySystemScreen.detectClick(screenX, screenY);
        }
        if(game.getScreen().getClass().equals(RegisterScreen.class)){
            RegisterScreen.detectClick(screenX, screenY);
        }
        if(game.getScreen().getClass().equals(CreditsScreen.class)){
            CreditsScreen.detectClick(screenX, screenY);
        }
        if(game.getScreen().getClass().equals(TutorialScreen.class)){
            TutorialScreen.detectClick(screenX, screenY);
        }
        if(game.getScreen().getClass().equals(ObservatoryScreen.class)){
            for(int i = 0; i<7; i++){
                ObservatoryScreen.widgets[i].changeScreen();
            }
            ActionButton.triggered = true; //Attention !!! (19.03.2017)
        }
        else if (game.getScreen().getClass().equals(LaboratoryScreen.class)){
            for(int i = 0; i<2; i++){
                LaboratoryScreen.widgets[i].changeScreen();
            }
        }
        else if (game.getScreen().getClass().equals(HangarScreen.class)){
            for(int i = 0; i<4; i++){
                HangarScreen.widgets[i].changeScreen();
            }
            ActionButton.triggered = true; //Attention !!! (05.02.2017)
        }
        else if (game.getScreen().getClass().equals(LaunchAreaScreen.class)){
            for(int i = 0; i<2; i++){
                LaunchAreaScreen.widgets[i].changeScreen();
            }
        }
        else if (game.getScreen().getClass().equals(LoadScreen.class)){
            for(int i = 0; i<2; i++){
                LoadScreen.widgets[i].changeScreen();
            }
        }
        Elements.triggered = true;
        Option.triggered = true;
        return true;
    }

    @Override
    public void resize(int width, int height){
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1); //On repeint la fenêtre en noir pour tout effacer
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //on ajoute une caméra, on accorde les projections etc..
        viewport.apply();
        vector3.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(vector3);
        renderer.setProjectionMatrix(camera.combined);
        renderer.setAutoShapeType(true);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void show() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        renderer.dispose();
        batch.dispose();
    }
}
