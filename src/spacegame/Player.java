package spacegame;

import java.awt.*;
import java.util.ArrayList;

public class Player extends MoveableObject {
	private final int SPRITE_SIZE_WIDTH = 64;
	private final int SPRITE_SIZE_HEIGHT = 94;
	private final int SPRITE_STARTING_X = 0;
	private final int SPRITE_STARTING_Y = 0;
	private final int SPRITE_COUNT = 2;
	public static double SPEED = 5.0;
	private double health = 100.0;
	private double hitPoints = 10;
	
	public Player(double x, double y, Dimension bounds) {
		super(x, y, bounds);
		
		objectSprites = new SpriteSheet("res/spritesheet_new.fw.png", 
				SPRITE_SIZE_WIDTH, SPRITE_SIZE_HEIGHT, 
				SPRITE_STARTING_X, SPRITE_STARTING_Y, 
				SPRITE_COUNT);
	}
	
	public void tick() {
		if(animationTimer >= 1.0) {
			animationTimer = 0.0;
			currentSprite++;
			if(currentSprite >= objectSprites.getNumberOfSprites())
				currentSprite = 0;
		} else {
			animationTimer += animationTick;
		}
		
		this.x += this.velocityX;
		if (x < 0.0)
			x = 0.0;
		else if(x > (double)(bounds.width - objectSprites.getSpriteWidth()))
			x = (double)(bounds.width - objectSprites.getSpriteWidth());
		
		this.y += this.velocityY;
		if (y < 0.0)
			y = 0.0;
		else if(y > (double)(bounds.height - objectSprites.getSpriteHeight()))
			y = (double)(bounds.height - objectSprites.getSpriteHeight());
	}
	
	public void render(Graphics g) {
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

	public double getHealth() {
		return health;
	}

	public void setHealth(double health) {
		this.health = health;
	}

	public void setHitpoints( double hp ) {
		this.hitPoints = hp;
	}
	
	public double getHitpoints() {
		return this.hitPoints;
	}
}
