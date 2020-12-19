package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.screens.GalaxyScreen;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.HomeScreen;
import com.mygdx.game.screens.MyScreens;
import com.mygdx.game.screens.ObservatoryScreen;

/**
 * Buttons used overall in the game to change screens, display containers etc...
 */
public class Widget extends Clickable {

    public Vector2 dimensions;
    String display;
    float textWidth, textHeight;
    private final int gap = 6;
    private final int shift = 3;
    private Color filling;
    private Color border;
    private Color border2;

    /**
     * Initialize the widget and some font parameters
     * @param display text displayed by the widget, which is used to perform actions
     */
    public Widget(ProjectSurvival game, SpriteBatch batch, ShapeRenderer renderer, Vector2 position, Vector2 dimensions, String display){
        this.display = display;
        this.dimensions = dimensions;
        this.game = game;
        this.batch = batch;
        this.renderer = renderer;
        this.position = position;

        filling = Color.BLACK;
        border = new Color(1, 0.3f, 0, 1);
        border2 = Color.RED;
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/goodTimes.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        layout = new GlyphLayout();
        parameter.size = 20;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        font = generator.generateFont(parameter);
    }

    /**
     * Display the widget with different colours depending on if it's hovered or not
     */
    public void render(float delta, boolean normal){
        //if(MyScreens.touched && !MyScreens.dragged) changeScreen(delta);
        renderer.begin();
        renderer.set(ShapeRenderer.ShapeType.Filled);

        if (mouseOn(dimensions.x, dimensions.y)){
            if(normal) drawBorder(border);
            else drawBorder(border2);
        }
        else {
            drawBorder(border2);
        }

        renderer.end();
        batch.begin();
        layout.setText(font, display);
        textWidth = layout.width;
        textHeight = layout.height;
        font.draw(batch, display, position.x - textWidth / 2, position.y + dimensions.y/2 -18);
        batch.end();
    }

    /**
     * Draw the border of the widgets
     * @param color colour of the border
     */
    private void drawBorder(Color color){
        renderer.rect(position.x-dimensions.x/2, position.y-dimensions.y/2+gap,dimensions.x, dimensions.y-2*gap, color, color, color, color);
        renderer.rect(position.x-dimensions.x/2+gap, position.y-dimensions.y/2,dimensions.x-2*gap, dimensions.y, color, color, color, color);
        renderer.setColor(color);
        renderer.circle(position.x-dimensions.x/2+gap, position.y-dimensions.y/2+gap, gap);
        renderer.circle(position.x-dimensions.x/2+gap, position.y+dimensions.y/2-gap, gap);
        renderer.circle(position.x+dimensions.x/2-gap, position.y-dimensions.y/2+gap, gap);
        renderer.circle(position.x+dimensions.x/2-gap, position.y+dimensions.y/2-gap, gap);

        renderer.rect(position.x-dimensions.x/2+shift, position.y-dimensions.y/2+gap+shift,dimensions.x-2*shift, dimensions.y-2*gap-2*shift, filling, filling, filling, filling);
        renderer.rect(position.x-dimensions.x/2+gap+shift, position.y-dimensions.y/2+shift,dimensions.x-2*gap-2*shift, dimensions.y-2*shift, filling, filling, filling, filling);
        renderer.setColor(filling);
        renderer.circle(position.x-dimensions.x/2+gap+shift, position.y-dimensions.y/2+gap+shift, gap);
        renderer.circle(position.x-dimensions.x/2+gap+shift, position.y+dimensions.y/2-gap-shift, gap);
        renderer.circle(position.x+dimensions.x/2-gap-shift, position.y-dimensions.y/2+gap+shift, gap);
        renderer.circle(position.x+dimensions.x/2-gap-shift, position.y+dimensions.y/2-gap-shift, gap);

        font.setColor(color);
    }

    /**
     * Change the screen depending on the text displayed on the widget
     */
    public void changeScreen() {
        MyScreens.vector3.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        MyScreens.viewport.unproject(MyScreens.vector3);
        if (this.mouseOn(dimensions.x, dimensions.y)) {
            if(display.equals("Galaxy")) game.setScreen(new GalaxyScreen(game));
            else if (display.equals("Flights") && MyScreens.nb != 2) {
                MyScreens.unDisplay = true;
                MyScreens.nb = 2;
                MyScreens.displaySpecific = true;
                Container.fadeIn = 600;
            }
            else if (display.equals("BlackHoles") && MyScreens.nb != 3) {
                MyScreens.unDisplay = true;
                MyScreens.nb = 3;
                MyScreens.displaySpecific = true;
                Container.fadeIn = 600;
            }
            else if (display.equals("WormHoles") && MyScreens.nb != 4) {
                MyScreens.unDisplay = true;
                MyScreens.nb = 4;
                MyScreens.displaySpecific = true;
                Container.fadeIn = 600;
            }
            else if (display.equals("Favourite Planets") && MyScreens.nb != 5) {
                MyScreens.unDisplay = true;
                MyScreens.nb = 5;
                MyScreens.displaySpecific = true;
                Container.fadeIn = 600;
            }
            else if (display.equals("Favourite Systems") && MyScreens.nb != 6) {
                MyScreens.unDisplay = true;
                MyScreens.nb = 6;
                MyScreens.displaySpecific = true;
                Container.fadeIn = 600;
            }
            else if (display.equals("Materials") && MyScreens.nb != 1) {
                MyScreens.unDisplay = true;
                MyScreens.nb = 1;
                MyScreens.displaySpecific = true;
                Container.fadeIn = 600;
            }
            else if (display.equals("Launch") && MyScreens.nb != 1) {
                MyScreens.unDisplay = true;
                MyScreens.nb = 1;
                MyScreens.displaySpecific = true;
                Container.fadeIn = 600;
            }
            else if (display.equals("Ships") && MyScreens.nb != 1) {
                MyScreens.unDisplay = true;
                MyScreens.nb = 1;
                MyScreens.displaySpecific = true;
                Container.fadeIn = 600;
            }
            else if (display.equals("Modules") && MyScreens.nb != 2) {
                MyScreens.unDisplay = true;
                MyScreens.nb = 2;
                MyScreens.displaySpecific = true;
                Container.fadeIn = 600;
            }
            else if (display.equals("Built Modules") && MyScreens.nb != 3) {
                MyScreens.unDisplay = true;
                MyScreens.nb = 3;
                MyScreens.displaySpecific = true;
                Container.fadeIn = 600;
            }
            else if (display.equals("Loads") && MyScreens.nb != 1) {
                MyScreens.unDisplay = true;
                MyScreens.nb = 1;
                MyScreens.displaySpecific = true;
                Container.fadeIn = 600;
            }

            else if (display.equals("Home")) game.setScreen(new HomeScreen(game));
            else if (display.equals("Planet")) {
                if(game.getScreen().equals(ObservatoryScreen.class)){
                    //ObservatoryScreen.containers.
                }
                game.setScreen(new GameScreen(game, false));
            }
            else if (display.equals("Observatory")) game.setScreen(new ObservatoryScreen(game));
            else if (display.equals("Back")) game.setScreen(new HomeScreen(game));
        }
    }
}
