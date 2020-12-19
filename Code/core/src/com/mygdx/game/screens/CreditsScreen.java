package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ActionButton;
import com.mygdx.game.Const;
import com.mygdx.game.ProjectSurvival;

/**
 * Screen displaying the Credits
 */
class CreditsScreen extends MenuScreens {

    private GlyphLayout layout1;
    private GlyphLayout layout2;
    private GlyphLayout layout3;
    private ActionButton returnToMenu;

    CreditsScreen(ProjectSurvival game){
        super();
        this.game = game;
        layout1 = new GlyphLayout();
        layout2 = new GlyphLayout();
        layout3 = new GlyphLayout();
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
        batch.begin();
        layout1.setText(font, "An original concept imagined and developed by");
        layout2.setText(font, "Gueddach Noureddine & Kqiku Genc");
        layout3.setText(font, "2017, Fribourg, Switzerland");
        font.draw(batch, layout1, Const.WORLD_WIDTH/2-layout1.width/2, 700);
        font.draw(batch, layout2, Const.WORLD_WIDTH/2-layout2.width/2, 550);
        font.draw(batch, layout3, Const.WORLD_WIDTH/2-layout3.width/2, 400);
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

