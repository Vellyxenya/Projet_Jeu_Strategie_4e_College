package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.game.Assets;
import com.mygdx.game.Const;
import com.mygdx.game.ProjectSurvival;

public class LoadingScreen extends MyScreens {

    public LoadingScreen(ProjectSurvival game){
        super(); //On exécute le constructeur de la classe-mère (MyScreens), important pour initialiser des trucs.
        this.game = game; //On passe le 'game' en paramètre, c'est aussi important sinon il est perdu j'ai l'impression.
        layout = new GlyphLayout(); //On initialise le layout.
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/goodTimes.ttf")); //on crée le générateur à partir de la police qui s'appelle goodTimes.ttf
        //(c'est un fichier font qui se trouve sous assets/adroid/fonts/...
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter(); //on initialise l'objet qui permettra de modifier la police
        parameter.size = 36; //taille de la police
        parameter.borderColor = Color.RED; //couleur de la bordure de la police
        parameter.borderWidth = 1; //largeur de la bordure
        font = generator.generateFont(parameter); //et on finit par générer la police à proprement parler.
        //Pour créer des polices différentes, suffit de créer de nouveaux objets font, generator, parameter et faire la même chose mais avec d'autres paramètres/polices.
        Assets.load();
    }

    @Override
    public void render(float delta) {
        super.render(delta); //On demande à la classe-mère (MyScreens) d'exécuter sa propre méthode render d'abord
        batch.begin(); //Ici, l'objet batch a déjà été initialisé dans le constructeur de la classe mère (MyScreens), on peut donc directement lui demander de 'démarrer'
        layout.setText(font, "Loading..."); //on assigne un texte au layout (Ici : "Loading...")
        font.draw(batch, layout, Const.WORLD_WIDTH/2-layout.width/2, 600); //Ensuite, on demande à la police d'écrire en utilisant le batch déjà initialisé
        // en prenant comme paramètre le layout
        // On a enfin les coordonnées où on veut mettre le texte.
        batch.end(); //On termine le batch (obligatoire, si ça bug)

        if(Assets.manager.update()) { //once all assets are loaded
            game.setScreen(new ObservatoryScreen(game)); //move to the observatory screen
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        Assets.dispose();
    }
}
