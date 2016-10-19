package spacegame;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Enemy extends MoveableObject {
	protected int SPRITE_SIZE_WIDTH = 70;
	protected int SPRITE_SIZE_HEIGHT = 50;
	protected int SPRITE_STARTING_Y = 352;
	protected int SPRITE_STARTING_X = 0;
	protected int SPRITE_COUNT = 12;
	
	protected double SPEED = 12.0;
	protected Random r = new Random();
	
	public Enemy(double x, double y, Dimension bounds) {
		super(x, y, bounds);
		
		animationTick = 0.3;
		velocityY = r.nextDouble() * SPEED;
		if (velocityY <= 6.0)
			velocityY = SPEED;		
	}
	
	@Override
	public void update() {
		if(animationTimer >= 1.0) {
			animationTimer = 0.0;
			currentSprite++;
			if(currentSprite >= objectSprites.getNumberOfSprites())
				currentSprite = 0;
		} else {
			animationTimer += animationTick;
		}
		
		y += r.nextDouble() * velocityY;  // FIXME this is heavy on performance
		if (y > (double)(bounds.height)) {
			y = -(r.nextInt(bounds.height));
			x = r.nextInt(bounds.width);
			if (x >= (double)(bounds.width - objectSprites.getSprite(currentSprite).getWidth()))
				x = (double)(bounds.width - objectSprites.getSprite(currentSprite).getWidth());
		} 
	}
	
	@Override
	public void render(Graphics g) {
		//g.drawRect((int) x, (int) y, objectSprites.getSpriteWidth(), objectSprites.getSpriteHeight());

		g.drawImage(objectSprites.getSprite(currentSprite), 
				(int) x, (int) y, 
				objectSprites.getSpriteWidth(), objectSprites.getSpriteHeight(), 
				null);
		g.setColor(Color.RED); 		// health bar rode onderlaag
		g.fillRect((int) x, 
				   (int) y - (objectSprites.getSpriteHeight() / 2), 
				   objectSprites.getSpriteWidth(), 
				   5);
		g.setColor(Color.GREEN); 	// health bar groen dat boven op rood licht
		g.fillRect((int) x, 
				   (int) y - (objectSprites.getSpriteHeight() / 2), 
				   (int) (objectSprites.getSpriteWidth() * (getHealth() / getMaxHealth())), 
				   5);
	}
	
	@Override
	public boolean hasCollided(MoveableObject withObject) {
		boolean collisionDetected = false;
		
		Rectangle pRect = new Rectangle((int) getX(), (int) getY(), (int) this.getSpriteWidth(), (int) getSpriteHeight()); 
		Rectangle eRect = new Rectangle((int) withObject.getX(), (int) withObject.getY(), (int) withObject.getSpriteWidth(), (int) withObject.getSpriteHeight());
		
		collisionDetected = pRect.intersects(eRect);
		
		if(collisionDetected) {
			this.health -= withObject.getHitpoints();
		}
		
		return collisionDetected;
	}
	
	@Override
	public boolean hasCollided(ArrayList<MoveableObject> withObjects) {
		boolean collisionDetected = false;
		
		for (int i = 0; i < withObjects.size(); i++) {
			MoveableObject obj = withObjects.get(i);

			if (hasCollided(obj)) {
				withObjects.remove(i);
				i--;
				collisionDetected = true;
				break;
			}
		}
		
		return collisionDetected;
	}

}
