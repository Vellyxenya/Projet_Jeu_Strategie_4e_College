package com.mygdx.game;

/**
 * Class containg some constants and the introducton text
 */
public class Const {
    public static String PLAYER_NAME = "";
    public static String PLANET_NAME = "";
    public static long INITIAL_TIME;
    public static int SCENARIO = 2;

    public static final float WORLD_WIDTH = 1600.0f;
    public static final float WORLD_HEIGHT = 1000.0f;
    static final int HOME_STARS_MAX_RADIUS = 4;
    static final int HOME_STARS_NUMBER = 300;
    public static final int MAX_DIST_MOUSE_STAR = 150;
    public static final float HOME_SCREEN_BUTTONS_POSITION = 5*Const.WORLD_HEIGHT/8;

    public static String[] texts = new String[5];

    public static void initTexts(){
        texts[0] = "Cher "+PLAYER_NAME+". Notre planète, "+PLANET_NAME+", est\n" +
                        "au plus mal : la pollution ne cesse d'augmenter, ce qui engendre un réchauffement climatique plus\n" +
                        "rapide que jamais. En effet, nous sommes à un point de non retour, la température baisse de 5°\n" +
                        "par an. Les glaciers fondent, de nombreuses îles sont sous la menace de la montée des eaux. ";
        texts[1] = "Une telle augmentation de la température ambiante ravagera notre écosystème. La planète est\n" +
                        "condamnée, faisons en sorte de faire survivre l'humanité. Nous vous écrivons pour vous demander\n" +
                        "de l'aide. Vous seul, vos connaissances et votre talent, pourront accomplir cette énorme tâche :\n" +
                        "trouver une nouvelle planète pour l'humanité. ";
        texts[2] = "La planète devra remplir certaines conditions, nécessaires à la vie humaine. Elle devra avoir des\n" +
                        "ressources suffisantes en oxygène et en eau. Un champ magnétique devra la protéger des rayons\n" +
                        "cosmiques et des vents solaires. Elle devra être suffisamment grande et lourde, histoire de nous\n" +
                        "maintenir au sol. Sa terre devra être fertile et la température devra y être idéale. ";
        texts[3] = "Pour cette mission, vous disposez de la part du gouvernement de x $. Chaque signe encourageant\n" +
                        "sera récompensé par les investisseurs. Vous disposez également de tous nos laboratoires, faites en\n" +
                        "bon usage. ";
        texts[4] = "Cher "+PLAYER_NAME+", on compte sur vous, trouvez la planète de nos rêves. ";
    }
}