package spacegame;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Background {
	/*
	 * De achtergrond die we gaan gebruiken leggen we twee keer aan. De main is te zien in ons venster, terwijl de
	 * secondary virtueel boven het venster is geplaatst. Dit doen we omdat we een achtergrond willen simuleren die op
	 * het oog naar beneden scrolled. Door de secondary er virtueel boven te plaatsen scrolled die mee naar beneden en
	 * zien we dus geen hiaten. Als we dan met de main de onderkant hebben bereikt schuiven we 'stiekem' alles weer
	 * terug naar hun startpositie. Dit geeft het effect van een continue scrollende background
	 */
	private BufferedImage mainBackgroundImage;
	private BufferedImage secondaryBackgroundImage;
	
	private Vector2D mainBackgroundPosition;
	private Vector2D secondaryBackgroundPosition;
	
	private double backgroundMoveSpeed = 2.0;
	
	public Background() {
		BufferedImageLoader loader = new BufferedImageLoader();
		
		mainBackgroundImage = loader.loadSprite("res/background2.jpg");
		secondaryBackgroundImage = loader.loadSprite("res/background2.jpg");
		
		mainBackgroundPosition = new Vector2D(0,0);
		secondaryBackgroundPosition = new Vector2D(0, -(mainBackgroundImage.getHeight()));
	}

	public void tick() {
		mainBackgroundPosition.y += backgroundMoveSpeed;
		secondaryBackgroundPosition.y += backgroundMoveSpeed;
		
		if (mainBackgroundPosition.y >= (double) mainBackgroundImage.getHeight()) {
			mainBackgroundPosition.y = 0.0;
			secondaryBackgroundPosition.y = -(mainBackgroundImage.getHeight());
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(mainBackgroundImage, 0, 
				(int) mainBackgroundPosition.y, 
				mainBackgroundImage.getWidth(), 
				mainBackgroundImage.getHeight(), null);
		
		g.drawImage(secondaryBackgroundImage, 0, 
				(int) secondaryBackgroundPosition.y, 
				secondaryBackgroundImage.getWidth(), 
				secondaryBackgroundImage.getHeight(), null);
	}
}
