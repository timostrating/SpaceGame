package spacegame;

import java.awt.*;
import java.util.ArrayList;

public class Bullet extends MoveableObject {
	public static final int SPRITE_SIZE_WIDTH = 20;
	public static final int SPRITE_SIZE_HEIGHT = 20;
	private final int SPRITE_STARTING_Y = 0;
	private final int SPRITE_STARTING_X = 400;
	private final int SPRITE_COUNT = 1;
	public static double SPEED = -6.0;

	public Bullet(double x, double y, double hitPoints, Dimension bounds) {
		super(x, y, bounds);
		
		this.hitPoints = hitPoints;
		
		objectSprites = new SpriteSheet("res/spritesheet_new.fw.png", 
				SPRITE_SIZE_WIDTH, SPRITE_SIZE_HEIGHT, 
				SPRITE_STARTING_X, SPRITE_STARTING_Y, 
				SPRITE_COUNT);
	}

	@Override
	public void update() {
		y += SPEED;
	}

	@Override
	public void render(Graphics g) {
		//g.drawRect((int) x, (int) y, objectSprites.getSpriteWidth(), objectSprites.getSpriteHeight());

		g.drawImage(objectSprites.getSprite(currentSprite), 
				(int) x, (int) y, 
				objectSprites.getSpriteWidth(), objectSprites.getSpriteHeight(), 
				null);		
	}

	@Override
	public boolean hasCollided(MoveableObject withObject) {
		boolean collisionDetected = false;
		
		Rectangle pRect = new Rectangle((int) getX(), (int) getY(), (int) this.getSpriteWidth(), (int) getSpriteHeight());
		Rectangle eRect = new Rectangle((int) withObject.getX(), (int) withObject.getY(), (int) withObject.getSpriteWidth(), (int) withObject.getSpriteHeight());
		
		collisionDetected = pRect.intersects(eRect);
		
		return collisionDetected;
	}
	
	@Override
	public boolean hasCollided(ArrayList<MoveableObject> withObjects) {
		boolean collisionDetected = false;
		
		for (int i = 0; i < withObjects.size(); i++) {
			MoveableObject obj = withObjects.get(i);

			if (hasCollided(obj)) {
				collisionDetected = true;
				break;
			}
		}
		
		return collisionDetected;
	}
}
