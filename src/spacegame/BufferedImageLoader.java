package spacegame;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class BufferedImageLoader {
	private BufferedImage spriteImage = null;

	public BufferedImageLoader() {
	}

	public final BufferedImage loadSprite(String path) {

		try {
			spriteImage = ImageIO.read(
					new File(path)
					//this.getClass().getResource(path)
				);
		} catch(IOException ex) {
			ex.printStackTrace();
		}

		return spriteImage;
	}
}
