package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.Const;
import com.mygdx.game.ProjectSurvival;

public class TheEnd extends MyScreens {

    private int population;

    public TheEnd(ProjectSurvival game){
        super();
        this.game = game;
        layout = new GlyphLayout();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/goodTimes.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 36;
        parameter.borderColor = Color.RED;
        parameter.borderWidth = 1;
        font = generator.generateFont(parameter);

        int time = (int) Math.floor((double) TimeUtils.millis()/1000) - Integer.parseInt(GameScreen.database.getOneData("SELECT Time FROM Player WHERE id = 1 ;"));
        int nbWormHoles = Integer.parseInt(GameScreen.database.getOneData("SELECT Count(*) FROM WormHoles ;"));
        if (nbWormHoles > 10) nbWormHoles = 10;
        int days = (int) ((double)time/3600/24 +10 - nbWormHoles);

        double initPop = 7000000;
        if(Const.SCENARIO == 0) initPop /= 2; //Si le joueur a accepté l'offre de l'entreprise
        population = (int) Math.ceil(5*(initPop*Math.pow(Math.E, 0.015*(double)days)/(5+Math.pow(Math.E, 0.15*(double)days))));
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        layout.setText(font, "Congratulations!");
        font.draw(batch, layout, Const.WORLD_WIDTH/2-layout.width/2, 600);
        layout.setText(font, "You saved "+population+" people!");
        font.draw(batch, layout, Const.WORLD_WIDTH/2-layout.width/2, 500);
        layout.setText(font, "You are everyone's hero!");
        font.draw(batch, layout, Const.WORLD_WIDTH/2-layout.width/2, 400);
        batch.end();
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
