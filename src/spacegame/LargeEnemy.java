package spacegame;

import java.awt.*;

public class LargeEnemy extends Enemy {

	public LargeEnemy(double x, double y, Dimension bounds) {
		super(x, y, bounds);
		
		SPRITE_SIZE_WIDTH = 70;
		SPRITE_SIZE_HEIGHT = 50;
		SPRITE_STARTING_Y = 402;
		SPRITE_STARTING_X = 0;
		SPRITE_COUNT = 12;	
		animationTick = 0.3;
		setHealth(200.0);
		
		objectSprites = new SpriteSheet("res/spritesheet_new.fw.png", 
				SPRITE_SIZE_WIDTH, SPRITE_SIZE_HEIGHT, 
				SPRITE_STARTING_X, SPRITE_STARTING_Y, 
				SPRITE_COUNT);	
	}

}
