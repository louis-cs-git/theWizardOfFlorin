package me.ghost.battle.dodge;

import me.ghost.Characters.MoveableCharacter;
import me.ghost.Characters.Npc;
import me.ghost.ResourceEnum.TextureType;
import me.ghost.battle.BattleWindow;
import org.jsfml.graphics.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class DodgeGame implements Drawable{

    private Npc battleNpc;
    private Stack<Projectile> projectileStack = new Stack<>();
    private MoveableCharacter wizard;
    private final ArrayList<Drawable> toDraw;
    private Stack<Projectile> projectileInMotion = new Stack<>();
    private boolean battleOpen;

    public DodgeGame(Npc setBattleNpc/*, MoveableCharacter setWizard*/) {
        BattleWindow battleWindow = new BattleWindow();
        battleOpen = true;
        toDraw = battleWindow.getToDraw();
        this.battleNpc = new Npc(setBattleNpc.getName(), battleWindow.getGhostAreaCentre().x - 16, battleWindow.getGhostAreaCentre().y - 16, (Texture) setBattleNpc.getTexture());
        this.wizard = new MoveableCharacter("Wizard", battleWindow.getPlayerAreaCentre().x - 16, battleWindow.getPlayerAreaCentre().y - 16, TextureType.SQUARE16.getTexture());
        this.addProjectilesToStack(1000);
        toDraw.add(this.battleNpc);
        toDraw.add(this.wizard);
        throwObject();
        run();
    }

    private void addProjectilesToStack(int numberProjectiles){
        Random rand = new Random();
        int minSides = 0;
        int maxSides = 10;
        for(int i = 0; i < numberProjectiles; i++){
            projectileStack.push(new Projectile(4, rand.nextInt(maxSides - minSides + 1)));
        }
    }

    private void throwObject() {
        while (projectileStack.size() > 0) {
            projectileInMotion.push(projectileStack.pop());
        }
        for(Projectile projectile : projectileInMotion){
            toDraw.add(projectile);
        }
        }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        for(Drawable item : toDraw) {
            renderTarget.draw(item);
        }
    }

    private void run(){
       /* while(battleOpen){
            for (Projectile projectile : projectileInMotion) {
                if(projectile.getPosition().y < 200) {
                    projectile.move(projectile.velocity.x / 100, projectile.velocity.y / 100);
                }
            }
        }
        updateWindow();

        */
    }

    private void updateWindow(){

    }
}
