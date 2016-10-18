package spacegame;

import java.awt.*;

public class SmallEnemy extends Enemy {

	public SmallEnemy(double x, double y, Dimension bounds) {
		super(x, y, bounds);
		
		SPRITE_SIZE_WIDTH = 50;
		SPRITE_SIZE_HEIGHT = 50;
		SPRITE_STARTING_Y = 452;
		SPRITE_STARTING_X = 0;
		SPRITE_COUNT = 12;	
		animationTick = 0.3;
		setHealth(100.0);
		
		objectSprites = new SpriteSheet("res/spritesheet_new.fw.png", 
				SPRITE_SIZE_WIDTH, SPRITE_SIZE_HEIGHT, 
				SPRITE_STARTING_X, SPRITE_STARTING_Y, 
				SPRITE_COUNT);	
	}
}
