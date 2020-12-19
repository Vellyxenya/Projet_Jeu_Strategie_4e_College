package com.mygdx.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;

/**
 * Created a twinkling star on the home screen
 */
public class HomeStar {
    public Vector3 position;
    float radius;

    public HomeStar(Vector3 vector){
        position = new Vector3(vector.x, vector.y,0);
        radius = vector.z;
    }

    public void render(ShapeRenderer renderer){
        Random red = new Random();
        Random green = new Random();
        Random blue = new Random();
        Random alpha = new Random();
        //renderer.setColor(8*red.nextFloat()/20, 0, 19*blue.nextFloat()/20, 0.0f);
        //renderer.circle(position.x, position.y, radius);
    }
}
