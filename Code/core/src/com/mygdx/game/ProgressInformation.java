package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * A text block that gives the player some informations on the progression of the game and what he has to do next
 */
public class ProgressInformation extends Clickable {

    int a, b, c, j, viewable, drawn;
    String dimension[][];
    char table[];
    String display;
    float textWidth, textHeight;
    private Vector2 dimensions = new Vector2(1400, 800);
    final int gap = 6;
    final int shift = 3;
    private Color filling;
    private Color border;
    private Color border2;
    public static boolean visible = false;
    public ActionButton accept;
    public ActionButton refuse;
    private final int LETTERES_PER_LINE = 50;

    /**
     * The different informations that will be displayed as the game progresses
     */
    private String informations[] = {
            "Effectue ton premier vol spatial avec les moyens déjà débloqués, peut-être débouchera-t-il sur une " +
                    "découverte. Ne va pas trop loin, les planètes proches de "+Const.PLANET_NAME+" pourraient " +
                    "avoir une température rentrant dans les conditions. ",
            "Bravo ! En découvrant 10 planètes, tu débloques le dOxygen. Va voir dans l'inventaire des " +
                    "équipements pour plus de détails sur cet objet. Trouve ensuite dans le système solaire une planète" +
                    "ayant de l'oxygène. ",
            "Il semblerait que tu t'es bien approprié le dOxygen. Tu débloques donc le dMagnetic. Va voir dans " +
                    "l'inventaire des équipements pour plus de détails sur cet objet. Trouve ensuite une planète " +
                    "ayant un puissant champ magnétique. ",
            "Waouw ! Cette planète a l'air intéressante. Afin que tu puisses l'analyser plus en profondeur, tu " +
                    "débloques le dWater. Va voir dans l'inventaire des équipements pour plus de détails sur cet objet. ",
            "Excellent ! Il y a de l'eau sur cette planète ! Les investisseurs sont ravis, tu reçois une prime de x $. " +
                    "Les chercheurs ont travaillé dur : tu débloques le robot et le lander, essaye-les sur cette planète ! " +
                    "Va voir dans l'inventaire des équipements et des vaisseaux pour plus de détails sur ces objets. ",
            "Malheureusement, la masse volumique du sol nous indique que la gravité est insuffisante pour une " +
                    "vie humaine sur cette planète. Essaie d'explorer de nouveaux horizons. Pour t'encourager dans ta quête, " +
                    "tu débloques le 'scan2'. Va voir dans l'inventaire des équipements pour plus de détails sur cet objet. ",
            "On aurait un grand avantage si l'on pouvait récolter des matières trouvées sur les planètes (voire même en découvrir" +
                    "d'autres) et les ramener sur "+Const.PLANET_NAME+". Pour cela, les scientifique ont développé le 'Container'. ",
            "Enorme découverte ! Tu viens de trouver une nouvelle matière, déjà repérée auparavant mais " +
                    "jamais récoltée. Il s'agit du geniusium. Tu débloques également le cpu. Va voir dans l'inventaire " +
                    "des matériaux et équipements pour plus de détails sur cet objet. ",
            "Enorme découverte ! Tu viens de trouver une nouvelle matière, déjà repérée auparavant mais " +
                    "jamais récoltée. Il s'agit du lentilium. Tu débloques également le scan1. Va voir dans l'inventaire " +
                    "des matériaux et équipements pour plus de détails sur cet objet. ",
            "Tu ne possèdes pas assez d'argent pour envoyer le scan1. Nous avons reçu une proposition de la " +
                    "part de 'Carbostry', la plus grosse usine à charbon de la planète. Elle est responsable de 15 % des émissions " +
                    "de gaz à effet de serre de tout "+Const.PLANET_NAME+"! Cependant, je me dois de te faire part " +
                    "de leur proposition. Si tu acceptes de déclarer publiquement que leurs activités n'ont aucun " +
                    "mauvais impact sur l'environnement (ce qui est en réalité complètement faux), ils pourront " +
                    "continuer leur production et t'offriront une énorme somme d'argent, te permettant d'effectuer de " +
                    "longs voyages. Accepter cette proposition ne sera pas sans conséquence. Ils pourront augmenter " +
                    "leur production et la moitié de la population de "+Const.PLANET_NAME+" disparaîtra en une " +
                    "année. Réfléchis-bien. Acceptes-tu l'offre ou la déclines-tu? "
    };

    /**
     * These two scenarios depend on the decision made by the player and lead to 2 different endings
     */
    String scenarii[][] = {
            {
                    "Ta déclaration fait le buzz, tu reçois 2000000 $. Tu peux maintenant construire le Scanner1 et explorer comme " +
                            "jamais auparavant. ",
                    "",
                    "",
                    "C'est un grand jour dans notre histoire. Tu l'as fait ! Tu es le héros de l'humanité ! " +
                            "Il te reste une dernière mission à accomplir : organiser le voyage amenant la population sur sa " +
                            "nouvelle planète à l'aide du cargo que tu viens de débloquer. ",
                    "Ce que tu viens de faire est exceptionnel. Tu as sauvé x humains. Tu deviens le nouveau héros de " +
                            "tous ! "
            },
            {
                    "Les scientifiques et la population te sont reconnaissants de ne pas avoir accepté cette offre. Cependant," +
                            "il va falloir redoubler d'efforts. Certains scientifiques pensent que ce serait possible de créer" +
                            "un trou de vers à partir de 2 trous noirs. De nombreux essais ont été menés en laboratoire. Il est temps" +
                            "d'essayer notre nouveau prototype en temps réel. Equipe un 'Singularizer' sur un de tes vaisseaux et utilise-le sur" +
                            "une étoile de type Z pour temporairement créer une singularité simulant un trou noir. ",
                    "C'est du jamais vu dans l'histoire! Voilà l'aboutissement de siècles de recherche : Le premier trou noir créé" +
                            "par l'homme! La prochaine étape est d'en créer un autre afin de les exploiter générant un trou de vers entre les deux" +
                            "Il te suffit de sélectionner 2 trous noirs pour les relier. ",
                    "C'est du jamais vu dans l'histoire ! Nous sommes désormais capables de créer un trou de vers ! Tu peux l'utiliser pour te " +
                            "déplacer aussi rapidement que jamais. Il apparaît maintenant dans l'observatoire, tu peux y voir " +
                            "l'entrée et la sortie. Les investisseurs t'offrent une somme de  $ pour cette découverte incroyable. " +
                            "N'hésite donc pas à enfin construire le Scanner1, c'est un vrai bijou de technologie! ",
                    "C'est un grand jour dans notre histoire. Tu l'as fait ! Tu es le héros de l'humanité ! " +
                            "Il te reste une dernière mission à accomplir, organiser le voyage amenant la population sur sa " +
                            "nouvelle planète à l'aide du cargo que tu viens de débloquer. Le voyage sera rude, alors rends-le " +
                            "le plus court possible en te servant de trous de vers. ",
                    "Ce que tu viens de faire est exceptionnel. Tu as sauvé x humains. Tu deviens le nouveau héros de " +
                            "tous ! "
            }
    };

    /**
     * initialize some variables and paramters, plus the text
     * @param game
     * @param renderer
     * @param batch
     * @param informationId
     */
    public ProgressInformation(ProjectSurvival game, ShapeRenderer renderer, SpriteBatch batch, int informationId){
        if(informationId <= 9){
            this.display = informations[informationId];
        }
        if(informationId >= 10) {
            this.display = scenarii[Const.SCENARIO][informationId-10];
        }
        this.game = game;
        this.batch = batch;
        this.renderer = renderer;
        position = new Vector2(Const.WORLD_WIDTH/2, Const.WORLD_HEIGHT/2);

        table = display.toUpperCase().toCharArray();
        dimension = new String[LETTERES_PER_LINE][(int)Math.ceil(table.length/LETTERES_PER_LINE)+2];
        initVar();
        calculateText();

        filling = Color.BLACK;
        border = new Color(1, 0.3f, 0, 1);
        border2 = Color.RED;
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ErikaType.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        layout = new GlyphLayout();
        parameter.size = 30;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        font = generator.generateFont(parameter);

        if(informationId == 9){ //Normally == 9
            accept = new ActionButton(game, batch, renderer, new Vector2(Const.WORLD_WIDTH/2, 200), new Vector2(200, 15), "Accept");
            refuse = new ActionButton(game, batch, renderer, new Vector2(Const.WORLD_WIDTH/2, 150), new Vector2(200, 15), "Refuse");
        }
    }

    /**
     * Display the text area, sentences etc...
     */
    public void render(){
        renderer.begin();
        renderer.set(ShapeRenderer.ShapeType.Filled);

        if (mouseOn(dimensions.x, dimensions.y)){
            drawBorder(border);
        }
        else {
            drawBorder(border2);
        }
        renderer.end();
        batch.begin();
        layout.setText(font, "PROGRESSION");
        textWidth = layout.width;
        textHeight = layout.height;
        font.draw(batch, layout, position.x - textWidth/2, position.y + dimensions.y/2 -18);
        batch.end();
        write();
        if(accept != null) accept.render();
        if(refuse != null) refuse.render();
    }

    private void initVar(){
        viewable = 0;
        drawn = 0;
        a = 0;
        b = 0;
        c = 0;
        j = 0;
    }

    /**
     * Process the text and format it
     */
    private void calculateText(){
        for(int i = 0; i<table.length; i++) {
            if(table[i] == ' ') {
                dimension[a][b] = Character.toString(table[i]);
                a++;
            }
            else if(table[i] != ' ') {
                c = i;
                j = 0;
                while(table[c] != ' ') {
                    if(c<table.length) {
                        c++;
                        j++;
                    }
                }
                if(j<LETTERES_PER_LINE-a) {
                    dimension[a][b] = Character.toString(table[i]);
                    a++;
                }
                else {
                    b++;
                    a = 0;
                    dimension[a][b] = Character.toString(table[i]);
                    a++;
                }
            }
            if(a==LETTERES_PER_LINE) {
                a = 0;
                b++;
            }
        }
    }

    /**
     * Write the text a letter after another
     */
    public void write() {
        drawn = 0;
        batch.begin();
        for(int i = 0; i<b+1; i++){
            for(int k = 0; k<LETTERES_PER_LINE; k++){
                drawn++;
                try{
                    if(drawn<viewable) font.draw(batch, dimension[k][i], 120f+28f*(float)k, 830f-35f*(float)i);
                }catch(NullPointerException e){}
                catch (ArrayIndexOutOfBoundsException ex){}
            }
        }
        batch.end();
        viewable++;
    }

    /**
     * Draw the border of the text area
     * @param color color of the border
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
}
