package spacegame;

import java.awt.*;
import java.util.ArrayList;

public class Explosion extends MoveableObject {  // TODO this should extend from FX or Mobi
	private final int SPRITE_SIZE_WIDTH = 50;
	private final int SPRITE_SIZE_HEIGHT = 50;
	private final int SPRITE_STARTING_Y = 100;
	private final int SPRITE_STARTING_X = 0;
	private final int SPRITE_COUNT = 16;
	
	public boolean isDone = false;

	public Explosion(double x, double y, Dimension bounds) {
		super(x, y, bounds);
		
		animationTick = 0.5;
		
		objectSprites = new SpriteSheet("res/spritesheet_new.fw.png", 
				SPRITE_SIZE_WIDTH, SPRITE_SIZE_HEIGHT, 
				SPRITE_STARTING_X, SPRITE_STARTING_Y, 
				SPRITE_COUNT);

	}

	@Override
	public void tick() {
		if(animationTimer >= 1.0 && !isDone) {
			animationTimer = 0.0;
			currentSprite++;
			if(currentSprite >= objectSprites.getNumberOfSprites())
				isDone = true;
		} else {
			animationTimer += animationTick;
		}
	}

	@Override
	public void render(Graphics g) {
		if(!isDone)
			g.drawImage(objectSprites.getSprite(currentSprite), 
					(int) x, (int) y, null);
	}

	@Override
	public boolean hasCollided(MoveableObject withObject) {
		return false;
	}

	@Override
	public boolean hasCollided(ArrayList<MoveableObject> withObjects) {
		return false;
	}

}
