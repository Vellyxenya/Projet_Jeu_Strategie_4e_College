package com.mygdx.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class containing all the stars of the screens
 */
public class HomeStars {
    public static ArrayList<HomeStar> stars;

    public HomeStars() {
        init();
    }

    /**
     * Creates the stars
     */
    public void init(){
        stars = new ArrayList<HomeStar>();
        Random random = new Random();
        for(int i = 0; i<Const.HOME_STARS_NUMBER; i++)
        {
            int x = random.nextInt((int) Const.WORLD_WIDTH);
            int y = random.nextInt((int) Const.WORLD_HEIGHT);
            int z = random.nextInt(Const.HOME_STARS_MAX_RADIUS);
            stars.add(new HomeStar(new Vector3(x, y, z)));
        }
    }

    /**
     * Render the stars
     * @param renderer
     */
    public void render(ShapeRenderer renderer){
        for(HomeStar star : stars)
        {
            star.render(renderer);
        }
    }
}
