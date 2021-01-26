package me.ghost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import me.ghost.Characters.MoveableCharacter;
import me.ghost.Characters.Npc;
import me.ghost.ResourceEnum.FontType;
import me.ghost.ResourceEnum.TextureType;
import org.jsfml.graphics.*;
import org.jsfml.window.VideoMode;

public class Game {

    private final RenderWindow window = new RenderWindow(new VideoMode(640, 480), "Welcome Wizards");;
    private Dialogue interaction = new Dialogue(FontType.ROBOTO.getFont(), TextureType.BOARD.getTexture(), "REPLACE ME", "Content Placeholder");
    private BattleWindow battleWindow = new BattleWindow();
    private final MoveableCharacter wizard = new MoveableCharacter("Name Placeholder", 320, 240, TextureType.SQUARE16.getTexture());
    private final List<Drawable> toDraw = new ArrayList<>();;
    private final Map<String, Boolean> keyPresses = new CaseInsensitiveMap<>();
    private Npc npc = new Npc("Name Placeholder", 250, 300, TextureType.SQUARE16.getTexture());
    private Mechanics game = new Mechanics(keyPresses, window, npc, interaction);
    private Drawable[] itemsToDraw = {wizard, npc};

    /**
     * Constructor for the game class
     */
    public Game() {
        game.initKeyPressesMap();
        toDraw.addAll(Arrays.asList(itemsToDraw));
        interaction.setCharacterName(npc.getName());

        //Limit the framerate
        window.setFramerateLimit(120);
    }

    /**
     * Runs the window including inputs and updating the window
     */
    public void run() {
        while (window.isOpen()) {
            game.handleEvents();
            wizard.moveCharacter(keyPresses, toDraw);
            updateWindow();
        }
    }


    private void isDialogue() {
        //If its the first time space is pressed, set the text
        if((keyPresses.get("SPACE"))){
            interaction.draw(window, null);
        }
        if((keyPresses.get("B"))){
            battleWindow.draw(window, null);
        }
    }

    /**
     * Updates the window
     */
    private void updateWindow(){
        window.clear(Color.RED);


        for(Drawable item : toDraw){
            window.draw(item);
        }
        isDialogue();

        window.display();
    }
}