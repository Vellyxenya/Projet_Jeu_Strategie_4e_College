package com.mygdx.game.storyline;

import com.mygdx.game.Const;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains the script of the adventure
 */
public class Script {

    /**
     * Some hashmap to assign certain values to certains keys
     */
    public static Map<String, String> rocketUnlocks = new HashMap<String, String>();
    public static Map<String, String> moduleUnlocks = new HashMap<String, String>();
    public static Map<String, String> rocketInformations = new HashMap<String, String>();
    public static Map<String, String> moduleInformations = new HashMap<String, String>();
    public static Map<String, String> materialInformations = new HashMap<String, String>();
    public static Map<String, String> materialLocations = new HashMap<String, String>();

    /**
     * Initialize all the HashMaps
     */
    public Script(){
        rocketUnlocks.clear();
        rocketUnlocks.put("Orbiter", "Already Unlocked");
        rocketUnlocks.put("Cargo"  , "Unlocked once you've discovered a worm hole in the galaxy !!");
        rocketUnlocks.put("Lander" , "Unlocked by discovering water on a planet !");

        moduleUnlocks.clear();
        moduleUnlocks.put("Scanner3" , "Already Unlocked");
        moduleUnlocks.put("Scanner2" , "Unlocked by finding water and oxygen on a planet !");
        moduleUnlocks.put("Scanner1" , "Unlocked by discovering a strange matter called 'Lentilium'");
        moduleUnlocks.put("dOxygen"  , "Unlocked by discovering 10 planets !");
        moduleUnlocks.put("dWater"   , "Unlocked by finding a planet with oxygen and a magnetic field !");
        moduleUnlocks.put("dMagnetic", "Unlocked by using the 'dOxygen' on 10 planets !");
        moduleUnlocks.put("Robot"    , "Unlocked by discovering water on a planet !");
        moduleUnlocks.put("Cpu"      , "Unlocked by discovering a strange matter called 'Lentilium'");
        moduleUnlocks.put("Container", "Already Unlocked");
        moduleUnlocks.put("Singularizer", "Reward for a valuable action");

        rocketInformations.clear();
        rocketInformations.put("Orbiter", "The 'Orbiter' is able to orbit around an planet. " +
                "Equip it of detectors ('dOxygen', 'dWater', ...) will make you able to gather " +
                "some useful data of a planet. The orbiter can come back on " + Const.PLANET_NAME
                + ", what could save you kerosene");
        rocketInformations.put("Cargo"  , "The 'Cargo' will be used in case of success. He will " +
                "transport the population of " + Const.PLANET_NAME + " to his new destination.");
        rocketInformations.put("Lander" , "The 'Lander' is able to land on the surface of a planet."
                + " It is well to carry the robot.");

        moduleInformations.clear();
        moduleInformations.put("Scanner3", "The Scanner is made to spot planets in the galaxy. " +
                "The 'Scanner3' has a range of 15.");
        moduleInformations.put("Scanner2", "The Scanner is made to spot planets in the galaxy. " +
                "The 'Scanner2' has a range of 30.");
        moduleInformations.put("Scanner1", "The Scanner is made to spot planets in the galaxy. " +
                "The 'Scanner1' has a range of 65.");
        moduleInformations.put("dOxygen", "The 'dOxygen' parses the atmosphere of planets around" +
                " which it orbits and on which he had landed.");
        moduleInformations.put("dWater", "The 'dWater' parses the humidity of planets around" +
                " which it orbits and on which he had landed.");
        moduleInformations.put("dMagnetic", "The 'dMagnetic' parses the magnetic field of planets" +
                " around which it orbits and on which he had landed.");
        moduleInformations.put("Robot", "The 'Robot' parses the ground of the planet on which it" +
                " rolls and calculates its density and fertility.");
        moduleInformations.put("Cpu", "The 'Cpu' can be boarded on all rockets. It calculates" +
                " the trajectory of the rocket to optimize it and thus save kerosene");
        moduleInformations.put("Container", "The 'Container' makes you able to transport" +
                " materials that you had gather on a planet.");
        moduleInformations.put("Singularizer", "The 'Singularizer' makes you able to transform" +
                " a Z-type star to a temporary black hole.");

        materialInformations.clear();
        materialInformations.put("Iron","Fe. Indispensable material to build" +
                " any rocket or module.");
        materialInformations.put("Aluminium","Al. Indispensable material of any rocket or module.");
        materialInformations.put("Steel","Iron and carbon alloy. Indispensable material of any " +
                "rocket or module.");
        materialInformations.put("Kerosene", "The 'Kerosene is the fuel of any rocket.");
        materialInformations.put("Sodium", "Na. Indispensable material tu build the 'dWater'.");
        materialInformations.put("Radium", "Ra. Indispensable material to use any scanner");
        materialInformations.put("Silicium", "Si. Indispensable material to build the 'Robot'.");
        materialInformations.put("Geniusium", "This material cannot be found on " + Const.PLANET_NAME
                + ". We know the there is some in the galaxy. If you find some, you will be able" +
                " to build a powerful computer, the 'Cpu'.");
        materialInformations.put("Lentilium", "This material cannot be found on " + Const.PLANET_NAME
                + ". We know the there is some in the galaxy. If you find some, you will be able" +
                " to build a powerful scanner, the 'Scanner 1'.");

        materialLocations.clear();
        materialLocations.put("Iron"     , Const.PLANET_NAME);
        materialLocations.put("Aluminium", Const.PLANET_NAME);
        materialLocations.put("Steel"    , Const.PLANET_NAME);
        materialLocations.put("Kerosene" , Const.PLANET_NAME);
        materialLocations.put("Sodium"   , Const.PLANET_NAME);
        materialLocations.put("Radium"   , Const.PLANET_NAME);
        materialLocations.put("Silicium" , Const.PLANET_NAME);
        materialLocations.put("Geniusium", "X-coordinates between 434 and 548, " +
                "Y-coordinates between 603 and 778.");
        materialLocations.put("Lentilium", "In an annulus of center (604;612), small radius 85 " +
                "and big radius 156.");
    }
}

