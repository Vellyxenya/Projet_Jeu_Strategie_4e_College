package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;

/**
 * Class the generate the fractal named the 'dragon-curve'
 * Inspired from www.udacity.com
 */
public class DragonCurveGenerator {
    //2 different directions : turn right or turn left
    enum Direction {
        LEFT,
        RIGHT;
        public static Vector2 turn(Vector2 heading, Direction turn){
            Vector2 newHeading = new Vector2();
            switch (turn){
                case LEFT:
                    newHeading.x = -heading.y;
                    newHeading.y = heading.x;
                    break;
                case RIGHT:
                    newHeading.x = heading.y;
                    newHeading.y = -heading.x;
            }
            return newHeading;
        }
    }

    /**
     * Create all the rotations in the curve
     * @param recursions the number of recursions of the fractal
     * @return an array of all the directions used to create the curve
     */
    private static LinkedList<Direction> dragonTurns(int recursions){
        LinkedList<Direction> turns = new LinkedList<Direction>();
        turns.add(Direction.RIGHT);
        for (int i = 0; i < recursions; i++){
            LinkedList<Direction> newTurns = (LinkedList<Direction>) turns.clone();
            LinkedList<Direction> reversedTurns = new LinkedList<Direction>();
            while (newTurns.size()>0)
            {
                reversedTurns.add(newTurns.getLast());
                newTurns.removeLast();
            }
            turns.add(Direction.RIGHT);
            for (int j = 0; j<reversedTurns.size(); j++)
            {
                if(reversedTurns.get(j).equals(Direction.RIGHT)) turns.add(Direction.LEFT);
                else if(reversedTurns.get(j).equals(Direction.LEFT)) turns.add(Direction.RIGHT);
            }
        }
        return turns;
    }

    /**
     * Generate the real dragon curve using the value returned by #dragonTurns
     * @param width world-width
     * @param height world-height
     * @param recursions number of recursions of the algorithm
     * @param base starting point (middle of the screen)
     * @return an array containing all the coordinates of all the systems in the galaxy
     */
    public static float[] generateDragonCurve(float width, float height, int recursions, Vector2 base){
        LinkedList<Direction> turns = DragonCurveGenerator.dragonTurns(recursions);
        Vector2 head = new Vector2(2*width/4, 2*height/4);
        Vector2 heading = base;
        float[] curve = new float[(turns.size() + 1) * 2];
        int i = 0;
        curve[i++] = head.x;
        curve[i++] = head.y;
        for(int k = 0; k<turns.size(); k++)
        {
            heading = Direction.turn(heading,turns.get(k));
            head.add(heading);
            curve[i++] = head.x;
            curve[i++] = head.y;
        }
        return curve;
    }
}
