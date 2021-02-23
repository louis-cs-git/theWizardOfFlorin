package me.ghost.character;

import me.ghost.data.TextureType;
import me.ghost.Item;
import me.ghost.map.GameMap;
import me.ghost.map.Tile;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.View;
import org.jsfml.system.Vector2f;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class MoveableCharacter extends Character {

    private boolean wizardColliding;
    private int stepIndex = 0;
    private int walkFrameControl = 0;
    private int walkingPace = 2;
    private final List<Tile> nearbyTiles = new ArrayList<>();

    public MoveableCharacter(String characterName, float xPosition, float yPosition, Texture characterTexture) {
        super(characterName, xPosition, yPosition, characterTexture);
        wizardColliding = false;
    }

    public MoveableCharacter(String characterName, float xPosition, float yPosition, Texture characterTexture, ArrayList<Item> items) {
        super(characterName, xPosition, yPosition, characterTexture, items);
        wizardColliding = false;
    }

    public void moveCharacter(Map<String, Boolean> keyPresses, List<Drawable> toDraw, View worldView, GameMap currentMap) {
        Npc npcCollide = null;
        Item itemCollide = null;

        for (Drawable obstacle : toDraw) {
            if (this.collides(obstacle)) {
                if (obstacle instanceof Npc) {
                    if (((Npc) obstacle).shouldDraw()) {
                        npcCollide = (Npc) obstacle;
                        wizardColliding = true;
                    }
                }
                if (obstacle instanceof Item) {
                    if (!((Item) obstacle).isFound() && ((Item) obstacle).availableToCollect()) {
                        itemCollide = (Item) obstacle;
                        wizardColliding = true;
                    }
                }
            }
        }
        if (!wizardColliding) {
            if ((keyPresses.get("RIGHT") && !keyPresses.get("SPACE"))) {
                if (this.getPosition().x - currentMap.getMapBounds().width < 0) {
                    this.walkRight();
                    this.move(walkingPace, 0);
                }
            }
            if ((keyPresses.get("LEFT") && !keyPresses.get("SPACE"))) {
                if (this.getPosition().x > currentMap.getMapBounds().left) {
                    this.walkLeft();
                    this.move(-(walkingPace), 0);
                }
            }
            if ((keyPresses.get("UP") && !keyPresses.get("SPACE"))) {
                if (this.getPosition().y > currentMap.getMapBounds().top) {
                    if (keyPresses.get("LEFT")) {
                        walkLeft();
                    } else if (keyPresses.get("RIGHT")) {
                        walkRight();
                    } else {
                        walkBack();
                    }
                    this.move(0, -(walkingPace));
                }
            }
            if ((keyPresses.get("DOWN") && !keyPresses.get("SPACE"))) {
                if (this.getPosition().y - currentMap.getMapBounds().height < 0) {
                    if (keyPresses.get("LEFT")) {
                        walkLeft();
                    } else if (keyPresses.get("RIGHT")) {
                        walkRight();
                    } else {
                        walkForward();
                    }
                    this.move(0, (walkingPace));
                }
            }
            setViewPosition(worldView, this.getPosition(), currentMap);
        } else if (npcCollide != null) {
            handleCollide(this.collisionRectangle(npcCollide), npcCollide.getPosition());
        } else if (itemCollide != null) {
            handleCollide(this.collisionRectangle(itemCollide), itemCollide.getPosition());
        }
        for (Tile tile : nearbyTiles) {
            if (this.collidesTile(tile)) {
                handleCollide(this.tileCollision(tile), tile.getPosition());
            }
        }

        nearbyTiles.clear();
    }

    private void handleCollide(FloatRect floatRect, Vector2f objectPosition) {
        float xDifference = floatRect.width;
        float yDifference = floatRect.height;
        if (Math.abs(objectPosition.y - this.getPosition().y) < floatRect.height) {
            if (objectPosition.x > this.getPosition().x) {
                this.move(-xDifference, 0);
            }
            if (objectPosition.x < this.getPosition().x) {
                this.move(xDifference, 0);
            }
        } else {
            if (objectPosition.y > this.getPosition().y) {
                this.move(0, -yDifference);
            }
            if (objectPosition.y < this.getPosition().y) {
                this.move(0, yDifference);
            }
        }

        wizardColliding = false;
    }

    private void walkLeft(){
        this.setTexture(TextureType.getLeftTextureByIndex(stepIndex));
        walkFrameControl++;
        if(walkFrameControl%16 == 0){
            stepIndex++;
            walkFrameControl=0;
        }
        if(stepIndex%7 == 0){
            stepIndex=0;
        }
    }

    private void walkRight(){
        this.setTexture(TextureType.getRightTextureByIndex(stepIndex));
        walkFrameControl++;
        if(walkFrameControl%16 == 0){
            stepIndex++;
            walkFrameControl=0;
        }
        if(stepIndex%7 == 0){
            stepIndex=0;
        }
    }

    private void walkBack(){
        if(stepIndex>3){
            stepIndex=0;
        }
        this.setTexture(TextureType.getBackTextureByIndex(stepIndex));
        walkFrameControl++;
        if(walkFrameControl%16 == 0){
            stepIndex++;
            walkFrameControl=0;
        }
        if(stepIndex%3 == 0){
            stepIndex=0;
        }
    }

    private void walkForward(){
        if(stepIndex>3){
            stepIndex=0;
        }
        this.setTexture(TextureType.getFrontTextureByIndex(stepIndex));
        walkFrameControl++;
        if(walkFrameControl%16 == 0){
            stepIndex++;
            walkFrameControl=0;
        }
        if(stepIndex%3 == 0){
            stepIndex=0;
        }
    }

    private boolean collides(Object obstacle) {
        if (obstacle instanceof Npc || obstacle instanceof Item) {
            return this.getGlobalBounds().intersection(((Sprite) obstacle).getGlobalBounds()) != null;
        }
        return false;
    }

    private FloatRect collisionRectangle(Drawable obstacle) {
        if (obstacle instanceof Npc || obstacle instanceof Item) {
            return this.getGlobalBounds().intersection((((Sprite) obstacle).getGlobalBounds()));
        }
        return null;
    }

    private boolean collidesTile(Tile tile){
        return this.getGlobalBounds().intersection(tile.getGlobalBounds()) != null;
    }

    private FloatRect tileCollision(Tile tile){
        return this.getGlobalBounds().intersection(tile.getGlobalBounds());
    }

    public boolean dialogueAreaCollide(Drawable obstacle) {
        if (obstacle instanceof Npc) {
            return this.getGlobalBounds().intersection(((Npc) obstacle).dialogueArea(4)) != null;
        } else if (obstacle instanceof Item) {
            return this.getGlobalBounds().intersection(((Item) obstacle).dialogueArea(4)) != null;
        }
        return false;
    }

    private void setViewPosition(View mapView, Vector2f position, GameMap currentMap) {
        // new Vector2f(0, 0);
        float x = this.getPosition().x;
        float y = this.getPosition().y;
        boolean changed = false;
        if (position.x > (currentMap.getDrawWidth() + 1) * 16 - mapView.getSize().x / 2) {
            x = (currentMap.getDrawWidth() + 1) * 16 - mapView.getSize().x / 2;
            changed = true;
        }
        if (position.x < mapView.getSize().x / 2) {
            x = mapView.getSize().x / 2;
            changed = true;
        }
        if (position.y > (currentMap.getDrawHeight() + 1) * 16 - mapView.getSize().y / 2) {
            y = (currentMap.getDrawHeight() + 1) * 16 - mapView.getSize().y / 2;
            changed = true;
        }
        if (position.y < mapView.getSize().y / 2) {
            y = mapView.getSize().y / 2;
            changed = true;
        }
        Vector2f viewPosition;
        if (!changed) {
            viewPosition = this.getPosition();
        } else {
            viewPosition = new Vector2f(x, y);
        }
        mapView.setCenter(viewPosition);
    }

    public void setNearbyTiles(Tile nearbyTile) {
        this.nearbyTiles.add(nearbyTile);
    }
}
