package spacegame;

import java.awt.*;

public class MediumEnemy extends Enemy {

	public MediumEnemy(double x, double y, Dimension bounds) {
		super(x, y, bounds);
		
		SPRITE_SIZE_WIDTH = 70;
		SPRITE_SIZE_HEIGHT = 50;
		SPRITE_STARTING_Y = 352;
		SPRITE_STARTING_X = 0;
		SPRITE_COUNT = 12;	
		animationTick = 0.3;
		setHealth(150.0);
		
		objectSprites = new SpriteSheet("res/spritesheet_new.fw.png", 
				SPRITE_SIZE_WIDTH, SPRITE_SIZE_HEIGHT, 
				SPRITE_STARTING_X, SPRITE_STARTING_Y, 
				SPRITE_COUNT);	
	}

}
