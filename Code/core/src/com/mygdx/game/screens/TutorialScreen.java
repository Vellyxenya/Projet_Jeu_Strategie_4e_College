package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ActionButton;
import com.mygdx.game.Const;
import com.mygdx.game.ProjectSurvival;

/**
 * Class that should contain some help for the new player
 */
class TutorialScreen extends MenuScreens {

    private GlyphLayout layout1;
    private ActionButton returnToMenu;

    TutorialScreen(ProjectSurvival game){
        super();
        this.game = game;
        layout1 = new GlyphLayout();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/goodTimes.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 36;
        parameter.borderColor = Color.RED;
        parameter.borderWidth = 1;
        font = generator.generateFont(parameter);
        returnToMenu = new ActionButton(game, batch, renderer, new Vector2(Const.WORLD_WIDTH/2, 150), new Vector2(200, 20), "Back to Main Menu!");
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        renderer.begin();
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(0, 0, Const.WORLD_WIDTH, Const.WORLD_HEIGHT, new Color(0.2f, 0, 0, 1), new Color(0.2f, 0, 0, 1), Color.BLACK, Color.BLACK);
        renderer.end();
        batch.begin();
        layout1.setText(font, "Will soon contain a little tutorial");
        font.draw(batch, layout1, Const.WORLD_WIDTH/2-layout1.width/2, 700);
        batch.end();
        returnToMenu.render();
        if(clicked){
            if(returnToMenu.hovered){
                clicked = false;
                game.setScreen(new HomeScreen(game));
            }
        }
        clicked = false;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
