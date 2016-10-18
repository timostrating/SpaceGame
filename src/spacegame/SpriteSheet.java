package spacegame;

import java.awt.image.BufferedImage;

public class SpriteSheet {
	private BufferedImage[] sprites;
	private String spriteSheetFileName;
	private int numberOfSprites = 0;
	private int spriteWidth = 0;
	private int spriteHeight = 0;
	
	public SpriteSheet(String spritesheetFile, int width, int height, int startingX, int startingY, int numOfSprites) {
		this.spriteSheetFileName = spritesheetFile;
		this.spriteHeight = height;
		this.spriteWidth = width;
		if (numOfSprites > 0)
			this.numberOfSprites = numOfSprites;
		else
			this.numberOfSprites = 1;
		
		loadSprites(startingX, startingY);
	}
	
	private void loadSprites(int startingX, int startingY) {
		BufferedImageLoader loader = new BufferedImageLoader();
		BufferedImage spriteSheet = loader.loadSprite(spriteSheetFileName);

		sprites = new BufferedImage[ numberOfSprites ];
		for (int i = 0; i < numberOfSprites; i++) {
			sprites[i] = spriteSheet.getSubimage(startingX + i * spriteWidth, startingY, spriteWidth, spriteHeight);
		}		
	}
	
	public BufferedImage getSprite(int col) {
		if (col >= 0 && col < numberOfSprites)
			return sprites[col];
		else
			return null;
	}

	public int getNumberOfSprites() {
		return numberOfSprites;
	}

	public void setNumberOfSprites(int numberOfSprites) {
		this.numberOfSprites = numberOfSprites;
	}

	public int getSpriteWidth() {
		return spriteWidth;
	}

	public void setSpriteWidth(int spriteWidth) {
		this.spriteWidth = spriteWidth;
	}

	public int getSpriteHeight() {
		return spriteHeight;
	}

	public void setSpriteHeight(int spriteHeight) {
		this.spriteHeight = spriteHeight;
	}
}
