package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Const;
import com.mygdx.game.ProjectSurvival;

import java.util.HashMap;
import java.util.Map;

import static com.mygdx.game.screens.GameScreen.database;

/**
 * Camera used in the galaxy Screen
 * It has a lot of methods that are duplicated as it was initially supposed to support
 * Desktop, android and iOS
 */
public class MyCamera extends InputAdapter implements GestureDetector.GestureListener {

    private static final float SCALE_RATE = 500;
    private static final float MOVE_RATE = 200;
    private static final float INITIAL_ZOOM = 1f;
    private OrthographicCamera overviewCamera;
    public static OrthographicCamera closeupCamera;
    public static boolean renderSystems = false;
    boolean detectClicks = true;
    int xRatio, yRatio;

    InputMultiplexer multiplexer; //allow to use many input processors
    GestureDetector detector;
    ProjectSurvival game;

    class TouchInfo{
        public float touchX = 0;
        public float touchY = 0;
        public boolean touched = false;
    }
    private Map<Integer, TouchInfo> touches = new HashMap<Integer, TouchInfo>();

    /**
     * Initialize some variables, set the different input processors etc...
     * @param game
     */
    public MyCamera(ProjectSurvival game) {
        this.game = game;
        overviewCamera = new OrthographicCamera();
        closeupCamera = new OrthographicCamera();
        closeupCamera.setToOrtho(false, MyScreens.viewport.getWorldWidth() * INITIAL_ZOOM, MyScreens.viewport.getWorldHeight() * INITIAL_ZOOM);
        overviewCamera.setToOrtho(false, MyScreens.viewport.getWorldWidth(), MyScreens.viewport.getWorldHeight());
        multiplexer = new InputMultiplexer();
        detector = new GestureDetector(this);
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(detector);
        Gdx.input.setInputProcessor(multiplexer);
        for (int i = 0; i<5; i++){
            touches.put(i, new TouchInfo());
        }
    }

    /**
     * Called when the screen is resized to readjust some parameters
     * @param width
     * @param height
     */
    public void resize(float width, float height) {
        overviewCamera.setToOrtho(false, width, height);
    }

    /**
     * Reset the initial size of the galaxy
     * @param keycode
     * @return
     */
    @Override
    public boolean keyUp(int keycode) {
        // Reset
        if (keycode == Input.Keys.R) {
            closeupCamera.setToOrtho(false, Gdx.graphics.getWidth() * INITIAL_ZOOM, Gdx.graphics.getHeight() * INITIAL_ZOOM);
        }
        return super.keyUp(keycode);
    }

    /**
     * Manage movements and zooms
     */
    public void update() {
        float delta = Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)) {
            game.setScreen(new ObservatoryScreen(game));
        }
        // Movement
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if(closeupCamera.position.x-closeupCamera.viewportWidth/2 > 0) closeupCamera.translate(-MOVE_RATE * delta, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if(closeupCamera.position.x+closeupCamera.viewportWidth/2 < Const.WORLD_WIDTH) closeupCamera.translate(MOVE_RATE * delta, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if(closeupCamera.position.y-closeupCamera.viewportHeight/2 > 0) closeupCamera.translate(0, -MOVE_RATE * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if(closeupCamera.position.y+closeupCamera.viewportHeight/2 < Const.WORLD_HEIGHT) closeupCamera.translate(0, MOVE_RATE * delta);
        }
        // Zoom
        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            if (closeupCamera.viewportWidth>Const.WORLD_WIDTH/16) proportionalZoom(-delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.X)) {
            if (closeupCamera.viewportWidth<Const.WORLD_WIDTH) proportionalZoom(delta);
        }
        closeupCamera.update();
        if(closeupCamera.viewportWidth<120) renderSystems = true;
        else renderSystems = false;
    }

    public float getViewportWidth(){
        return closeupCamera.viewportWidth;
    }

    /**
     * Zoom propertionnally to the screen ratio
     * @param delta
     */
    private void proportionalZoom(float delta) {
        float aspectRatio = overviewCamera.viewportWidth / overviewCamera.viewportHeight;
            closeupCamera.viewportWidth += SCALE_RATE * delta;
            closeupCamera.viewportHeight += SCALE_RATE / aspectRatio * delta;
    }

    /**
     * Set some camera parameters
     * @param renderer
     * @param batch
     */
    public void setCamera(ShapeRenderer renderer, SpriteBatch batch) {
            closeupCamera.update();
            renderer.setProjectionMatrix(closeupCamera.combined);
            batch.setProjectionMatrix(closeupCamera.combined);
    }

    /**
     * Detect the beginning of the click
     * @param x
     * @param y
     * @param pointer
     * @param button
     * @return
     */
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        if(pointer < 5){
            touches.get(pointer).touchX = x;
            touches.get(pointer).touchY = y;
            touches.get(pointer).touched = true;
        }
        return true;
    }

    /**
     * Used to detect the position of the last click
     * Displays a system if it has been clicked
     * @param x
     * @param y
     * @param pointer
     * @param button
     * @return
     */
    @Override
    public boolean touchUp(int x, int y, int pointer, int button){
        if(detectClicks) {
            if(pointer < 5){
                touches.get(pointer).touchX = 0;
                touches.get(pointer).touchY = 0;
                touches.get(pointer).touched = false;
                float ratio = closeupCamera.viewportWidth/1600;
                xRatio = (int)closeupCamera.position.x-(int)closeupCamera.viewportWidth/2+(int)(ratio*x);
                yRatio = (int)(1000-closeupCamera.position.y)-(int)closeupCamera.viewportHeight/2+(int)(ratio*y);
                System.out.println(xRatio+"   "+yRatio);
            }
        }
        detectClicks = true;
        if(GalaxyScreen.ab.hovered){
            GalaxyScreen.ab.returnToObservatory();
        }
        int id;
        try{ //Look if there's a system near the click, if so, display the system
            id = database.getSystem("SELECT id FROM 'Systems' WHERE SQRT(POWER(SystemX-"+xRatio+",2)+POWER(SystemY-"+(1000-yRatio)+",2))<3");
            if(id != -1) game.setScreen(new DisplaySystemScreen(game, id));
        } catch(NoSuchMethodError e){
            System.out.println("error");
        }
        return true;
    }

    /**
     * Used on smartphones to detect clicks
     * @param x
     * @param y
     * @param count
     * @param button
     * @return
     */
    @Override
    public boolean tap(float x, float y, int count, int button) {
        System.out.println("tapped");
        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    /**
     * Used to move around the galaxy (on smartphones)
     * @param x
     * @param y
     * @param deltaX
     * @param deltaY
     * @return
     */
    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        System.out.println("PANNED");
        if(closeupCamera.position.x-closeupCamera.viewportWidth/2 > 0 &&
        closeupCamera.position.x+closeupCamera.viewportWidth/2 < Const.WORLD_WIDTH &&
        closeupCamera.position.y-closeupCamera.viewportHeight/2 > 0 &&
        closeupCamera.position.y+closeupCamera.viewportHeight/2 < Const.WORLD_HEIGHT)
        closeupCamera.translate(-0.2f*deltaX, 0.2f*deltaY);
        if (closeupCamera.position.x-closeupCamera.viewportWidth/2 <= 0) closeupCamera.position.x = closeupCamera.viewportWidth/2+0.01f;
        if (closeupCamera.position.x+closeupCamera.viewportWidth/2 >= Const.WORLD_WIDTH) closeupCamera.position.x = Const.WORLD_WIDTH - closeupCamera.viewportWidth/2-0.01f;
        if (closeupCamera.position.y-closeupCamera.viewportHeight/2 <= 0) closeupCamera.position.y = closeupCamera.viewportHeight/2+0.01f;
        if (closeupCamera.position.y+closeupCamera.viewportHeight/2 >= Const.WORLD_HEIGHT) closeupCamera.position.y = Const.WORLD_HEIGHT - closeupCamera.viewportHeight/2-0.01f;
        detectClicks = false;
        return true;
    }

    /**
     * For smartphones, useless here
     */
    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    /**
     * Zoom in and out, preserving the same ratio
     * @param initialDistance
     * @param distance
     * @return
     */
    @Override
    public boolean zoom(float initialDistance, float distance) {
        if (closeupCamera.viewportWidth>=Const.WORLD_WIDTH) {
            closeupCamera.viewportWidth = Const.WORLD_WIDTH - 0.0016f;
            closeupCamera.viewportHeight = Const.WORLD_HEIGHT - 0.0010f;
        }
        if (closeupCamera.viewportWidth<=100) {
            closeupCamera.viewportWidth = 100f + 0.0016f;
            closeupCamera.viewportHeight = 62.5f + 0.0010f;
        }
        if (closeupCamera.viewportWidth>Const.WORLD_WIDTH/16 && closeupCamera.viewportWidth<Const.WORLD_WIDTH) {
            float aspectRatio = overviewCamera.viewportWidth / overviewCamera.viewportHeight;
            closeupCamera.viewportWidth -= 0.08 * (distance - initialDistance);
            closeupCamera.viewportHeight -= 0.08 * (distance - initialDistance) / aspectRatio;
        }
        return true;
    }

    /**
     * Used to detect pinch-movement on smartphones
     * @param initialPointer1
     * @param initialPointer2
     * @param pointer1
     * @param pointer2
     * @return
     */
    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return true;
    }

    /**
     * Used on smartphones
     */
    @Override
    public void pinchStop() {}
}