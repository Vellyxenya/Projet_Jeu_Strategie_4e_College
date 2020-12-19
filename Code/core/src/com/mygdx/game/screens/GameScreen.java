package com.mygdx.game.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.ActionButton;
import com.mygdx.game.Const;
import com.mygdx.game.ProgressInformation;
import com.mygdx.game.ProjectSurvival;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.mygdx.game.database.DataBase;
import com.mygdx.game.storyline.Script;
import com.mygdx.game.structuredVariables.MaterialManager;

import java.util.concurrent.TimeUnit;

public class GameScreen extends MenuScreens {

    /**
     * Code found by asking some questions on StackOverFlow.
     * It's used to create a 3d shape...
     */
    private static class GameObject extends ModelInstance {
        Vector3 center = new Vector3();
        Vector3 dimensions = new Vector3();
        float radius;

        BoundingBox bounds = new BoundingBox();

        GameObject (Model model, String rootNode, boolean mergeTransform) {
            super(model, rootNode, mergeTransform);
            calculateBoundingBox(bounds);
            bounds.getCenter(center);
            bounds.getDimensions(dimensions);
            radius = dimensions.len()/2f;
        }
    }

    public static DataBase database; //Database that is used overall in the game

    private int counter = 0;
    private Sprite[] sprites;
    private Vector3 position = new Vector3();
    private Environment environment;
    private PerspectiveCamera cam;
    private ModelBatch modelBatch;
    private Model model;
    private AssetManager manager = new AssetManager();
    private boolean loading;
    private Array<GameObject> instances = new Array<GameObject>();
    private ModelInstance scene;
    private int image = 0;
    private float alpha[] = new float[]{1, 0.7f, 0.4f, 0.2f, 0.1f};
    private int selected = -1, selecting = -1;
    private boolean finalAnimation = false;
    private int animationCounter = 0;
    private ActionButton showInformationsButton;
    private ProgressInformation information;
    private float level;
    private int money;
    private String date = "";
    private int population;

    MaterialManager mm;
    Script script;

    /**
     * Create the scene and open the database
     * @param game
     * @param openDataBase whether it should open a new DB or no
     */
    public GameScreen(ProjectSurvival game, boolean openDataBase){
        super();
        this.game = game;
        if(openDataBase) database = new DataBase(false, "saves/"+Const.PLAYER_NAME+".db"); //Open the database

        //Create the environment and add some light
        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 1f, 1f));
        environment.add(new PointLight().set(0.9f, 0.9f, 0.3f, -30, 0, 20, 10000));

        // Initialize camera's parameters
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(20f, 20f, 20f);
        cam.lookAt(-70,-10,0);
        cam.near = 0.01f;
        cam.far = 300f;
        cam.update();

        manager = new AssetManager();
        manager.load("models/scene.g3db", Model.class); //load the models
        loading = true;
        sprites = new Sprite[6];
        for(int i = 0; i<sprites.length; i++){
            sprites[i] = new Sprite(new Texture(Gdx.files.internal("images/"+(i+1)+".png")));
        }
        backGroundTexture = new Texture(Gdx.files.internal("images/GameScreenBackground.png"));
        backgroundSprite = new Sprite(backGroundTexture);
        backgroundSprite.setBounds(0, 0, 1800, 1000);

        layout = new GlyphLayout();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Anony.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        font = generator.generateFont(parameter);
        font.setColor(Color.WHITE);

        showInformationsButton = new ActionButton(game, batch, renderer, new Vector2(1380, 950), new Vector2(200, 15), "WHAT TO DO NEXT?");
        try{
            information = new ProgressInformation(game, renderer, batch, (int)Float.parseFloat(database.getOneData("SELECT Level FROM Player")));
        }catch (NumberFormatException e){
            information = new ProgressInformation(game, renderer, batch, 0);
        }

        /**
         * Initialize some informations
         */
        mm = new MaterialManager();
        script = new Script();
        level = Float.parseFloat(GameScreen.database.getOneData("SELECT Level FROM Player"));
        money = Integer.parseInt(GameScreen.database.getOneData("SELECT Xp FROM Player"));
        int time = (int) Math.floor((double)TimeUtils.millis()/1000) - Integer.parseInt(database.getOneData("SELECT Time FROM Player WHERE id = 1 ;"));
        int days;
        try{
            days = (int)TimeUnit.SECONDS.toDays(time);
            long hours = TimeUnit.SECONDS.toHours(time) - (days *24);
            long minutes = TimeUnit.SECONDS.toMinutes(time) - (TimeUnit.SECONDS.toHours(time)* 60);
            long seconds = TimeUnit.SECONDS.toSeconds(time) - (TimeUnit.SECONDS.toMinutes(time) *60);
            date = days+" D, "+hours+" H, "+minutes+" Min, "+seconds+" Sec";
        } catch (Exception e){
            days = (int) ((double)time/3600/24);
            date = "Requires API level 9";
        }

        double initPop = 7000000;
        if(Const.SCENARIO == 0) initPop /= 2; //Si le joueur a accepté l'offre de l'entreprise
        population = (int) Math.ceil(5*(initPop*Math.pow(Math.E, 0.015*(double)days)/(5+Math.pow(Math.E, 0.15*(double)days))));
        System.out.println("Population : "+population);

        //To be removed
        System.out.println("Number 1 : "+database.getOneData("SELECT PlanetName FROM Planet WHERE id = 30106"));
        System.out.println("code : "+database.getOneData("SELECT PlanetType FROM Planet WHERE id = 8"));
        System.out.println("Number of z-type stars  : "+database.getOneData("SELECT Count(*) FROM Systems WHERE StarType = 'Z' ;"));
        System.out.println("Number of total planets : "+database.getOneData("SELECT Count(*) FROM Planet WHERE Water = 1 AND Oxygen = 1 AND AtmosphereQuantity >1.5 AND Density >3 AND MagneticField >2 AND Distance <500 ;"));
        System.out.println("SystemId : "+database.getOneData("SELECT SystemId FROM Planet WHERE Water = 1 AND Oxygen = 1 AND AtmosphereQuantity >1.5 AND Density >3 AND MagneticField >2 AND Distance <500 ;"));
        System.out.println("X : "+database.getOneData("SELECT SystemX FROM Systems WHERE id = " +
                "(SELECT SystemId FROM Planet WHERE Water = 1 AND Oxygen = 1 AND AtmosphereQuantity >1.5 AND Density >3 AND MagneticField >2 AND Distance <500) ;"));
        System.out.println("Y : "+database.getOneData("SELECT SystemY FROM Systems WHERE id = " +
                "(SELECT SystemId FROM Planet WHERE Water = 1 AND Oxygen = 1 AND AtmosphereQuantity >1.5 AND Density >3 AND MagneticField >2 AND Distance <500) ;"));
        System.out.println("Name : "+database.readDatas("SELECT id FROM Systems WHERE id IN (SELECT SystemId FROM Planet WHERE Water = 1 AND Oxygen = 1 AND AtmosphereQuantity >1.5 AND Density >3 AND MagneticField >2 AND Distance <500) ;"));
    }

    /**
     * Executed once everything has been initialized
     * Adds the different objects into arrays etc...
     * Helps with creating the scene
     */
    private void doneLoading() {
        model = manager.get("models/scene.g3db", Model.class);
        for (int i = 0; i<model.nodes.size; i++){
            String id = model.nodes.get(i).id;
            GameObject instance = new GameObject(model, id, false);
            instances.add(instance);
        }
        scene = new ModelInstance(model, 0, 0, 0);
        loading = false;
    }

    /**
     * render the scene
     * @param delta interval of time between each rendering cycle
     */
    @Override
    public void render(float delta) {
        super.render(delta);

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        batch.begin();
        sprites[image%4].setBounds(0-4*animationCounter, 2*animationCounter, Const.WORLD_WIDTH, Const.WORLD_HEIGHT);
        sprites[5].setBounds(0-4*animationCounter, 2*animationCounter, Const.WORLD_WIDTH, Const.WORLD_HEIGHT);
        sprites[image%4].draw(batch, alpha[image%4]);
        sprites[5].draw(batch, 0.7f);
        if (counter%2 == 0)image++;
        batch.end();

        modelBatch.begin(cam);
        if(scene!= null) {
            if(image%3 == 0) modelBatch.render(scene);
            else modelBatch.render(scene, environment);
        }
        modelBatch.end();

        showInformationsButton.render();
        renderGeneralInformations();
        if(ProgressInformation.visible) information.render();
        if(finalAnimation) goToScreen(selected);
        counter++;
        if (loading && manager.update()) doneLoading();
    }

    /**
     * Display some informations like the names of the planet and player, money, number of humans etc...
     */
    private void renderGeneralInformations(){
        renderer.begin();
        renderer.setColor(Color.WHITE);
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(1000+3*animationCounter, 650+3*animationCounter, 480, 250);
        renderer.setColor(Color.DARK_GRAY);
        renderer.rect(1005+3*animationCounter, 655+3*animationCounter, 470, 240);
        if(animationCounter <= 0){
            renderer.set(ShapeRenderer.ShapeType.Line);
            renderer.setColor(Color.WHITE);
            renderer.polyline(new float[]{860, 95, 960, 170, 1230, 170});
            renderer.polyline(new float[]{820, 345, 940, 345});
            renderer.polyline(new float[]{1010, 555, 1080, 555, 1120, 490});
            renderer.polyline(new float[]{1400, 540, 1400, 430});
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.circle(860, 95, 4);
            renderer.circle(820, 345, 4);
            renderer.circle(1010, 555, 4);
            renderer.circle(1400, 540, 4);
        }
        renderer.end();
        batch.begin();
        font.draw(batch, "Name    :  "+Const.PLAYER_NAME, 1050+3*animationCounter, 850+3*animationCounter);
        font.draw(batch, "Planet  :  "+Const.PLANET_NAME, 1050+3*animationCounter, 820+3*animationCounter);
        font.draw(batch, "Humans  :  "+population       , 1050+3*animationCounter, 790+3*animationCounter);
        font.draw(batch, "Money   :  "+money            , 1050+3*animationCounter, 760+3*animationCounter);
        font.draw(batch, "Time    :  "+date             , 1050+3*animationCounter, 730+3*animationCounter);
        font.draw(batch, "Level   :  "+(int) level      , 1050+3*animationCounter, 700+3*animationCounter);
        if(animationCounter <= 0){
            font.draw(batch, "Laboratory", 1320, 560);
            font.draw(batch, "Observatory", 650, 350);
            font.draw(batch, "Hangar", 750, 100);
            font.draw(batch, "Launch Area", 850, 560);
        }
        batch.end();
    }

    /**
     * @param screenX
     * @param screenY
     * @param pointer
     * @param button
     * @return true if an object has been found at the coordinates (x,y)
     */
    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        selecting = getObject(screenX, screenY);
        return selecting >= 0;
    }

    /**
     * @param screenX
     * @param screenY
     * @param pointer
     * @return true if the mouse has been dragged
     */
    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        return selecting >= 0;
    }

    /**
     * Executed once the click has been released
     * @param screenX
     * @param screenY
     * @param pointer
     * @param button
     * @return true if an object has been selected during the click
     */
    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        if(showInformationsButton.hovered){
            showInformationsButton.showOrHideProgress();
            return true;
        }
        try{
            if(information.accept.hovered){
                information.accept.acceptOffer();
                return true;
            }
            if(information.refuse.hovered){
                information.refuse.refuseOffer();
                return true;
            }
        } catch (NullPointerException e){}
        if(!showInformationsButton.getDisplay().equals("GOT IT !")){
            if (selecting >= 0) {
                if (selecting == getObject(screenX, screenY))
                    setSelected(selecting);
                selecting = -1;
                return true;
            }
        }
        return false;
    }

    /**
     * Allow the display of screen-swap animation if an object has been clicked
     * @param value
     */
    private void setSelected (int value) {
        if (selected == value) return;
        selected = value;
        if (selected >= 0) {
            finalAnimation = true;
        }
    }

    /**
     * Chose which screen the player should go to, depending on where he clicked
     * @param screen
     */
    private void goToScreen(int screen){
        cam.position.set(cam.position.x-0.15f, cam.position.y-0.15f, cam.position.z-0.15f);
        cam.rotate(Vector3.Y, -0.15f);
        cam.update();
        animationCounter++;
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderer.begin();
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(new Color(1, 1, 1, ((float)animationCounter/100)));
        //renderer.setColor(new Color(1, 1, 1, 0.5f));
        renderer.rect(0, 0, 1600, 1000);
        renderer.end();
        if(animationCounter == 100){
            switch (screen){
                case 0:
                    game.setScreen(new HangarScreen(game));
                    break;
                case 1:
                    game.setScreen(new LaboratoryScreen(game));
                    break;
                case 2:
                    game.setScreen(new LaunchAreaScreen(game));
                    break;
                case 3:
                    game.setScreen(new LoadingScreen(game));
                    break;
                default:
                    game.setScreen(new LoadingScreen(game));
                    break;
            }
        }
    }

    /**
     * Detect the objects in a certain range by using projections on the screen etc...
     * Code found on StackOverFlow, and adapted
     * @param screenX
     * @param screenY
     * @return
     */
    private int getObject (int screenX, int screenY) {
        Ray ray = cam.getPickRay(screenX, screenY);

        int result = -1;
        float distance = -1;

        for (int i = 0; i < instances.size; ++i) {
            GameObject instance = instances.get(i);
            instance.transform.getTranslation(position);
            position.add(instance.center);

            float len = ray.direction.dot(position.x-ray.origin.x, position.y-ray.origin.y, position.z-ray.origin.z);
            if (len < 0f)
                continue;

            float dist2 = position.dst2(ray.origin.x+ray.direction.x*len, ray.origin.y+ray.direction.y*len, ray.origin.z+ray.direction.z*len);
            if (distance >= 0f && dist2 > distance)
                continue;

            if (dist2 <= instance.radius * instance.radius) {
                result = i;
                distance = dist2;
            }
        }
        return result;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    /**
     * Free memory by disposing useless objects
     */
    @Override
    public void dispose() {
        super.dispose();
        modelBatch.dispose();
        model.dispose();
        instances.clear();
        manager.dispose();
        database.closeDB();
        System.out.println("Disposed...");
    }
}
