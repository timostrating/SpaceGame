package spacegame;

import java.awt.*;
import java.util.ArrayList;

public class Player extends MoveableObject {
	private final int SPRITE_SIZE_WIDTH = 64;
	private final int SPRITE_SIZE_HEIGHT = 94;
	private final int SPRITE_STARTING_X = 0;
	private final int SPRITE_STARTING_Y = 0;
	private final int SPRITE_COUNT = 2;
	public static float SPEED = 5.0F;
	private double health = 100.0;
	private double hitPoints = 25;
	
	public Player(double x, double y, Dimension bounds) {
		super(x, y, bounds);
		
		objectSprites = new SpriteSheet("res/spritesheet_new.fw.png", 
				SPRITE_SIZE_WIDTH, SPRITE_SIZE_HEIGHT, 
				SPRITE_STARTING_X, SPRITE_STARTING_Y, 
				SPRITE_COUNT);
	}
	
	public void update() {
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

			if (hasCollided(obj)) { // FIXME kan NullPointerException geven
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

	public double getHitpoints() {
		return this.hitPoints;
	}
	public void setHitpoints( double hitP ) {
		this.hitPoints = hitP;
	}



//	// shiet bullet af vanaf de Speler
//	private void createBullet() {  // TODO this should not be here, this is for the player / gun
//		bullets.add(new Bullet(
//				player.getX()
//						+ (player.getSpriteWidth() / 2 -
//								(Bullet.SPRITE_SIZE_WIDTH / 2)),
//				player.getY() - Bullet.SPRITE_SIZE_HEIGHT,
//				player.getHitpoints(),
//				bounds));
//		JukeBox.play("firing");
//	}
//
//	private void createBullet() {  // TODO this should not be here, this is for the player / gun
//		bullets.add(new Bullet(
//				player.getX()
//						+ (player.getSpriteWidth() / 2 -
//								(Bullet.SPRITE_SIZE_WIDTH / 2)),
//				player.getY() - Bullet.SPRITE_SIZE_HEIGHT,
//				player.getHitpoints(),
//				bounds));
//		JukeBox.play("firing");
//	}
}
