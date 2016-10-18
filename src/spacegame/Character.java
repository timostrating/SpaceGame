package spacegame;

import java.awt.image.BufferedImage;

public class Character {
	public BufferedImage ch = null;
	public int chCode = 0;
	public int chXOffset = 0;
	public int chYOffset = 0;
	public int chXAdvance = 0;		// Hoeveel pixels opschuiven na char op x-as
	public int chHeight = 0;
	public int chWidth = 0;

	
	public Character(BufferedImage img, int id, int w, int h, int xoffset, int yoffset, int xa) {
		chCode = id;
		chXOffset = xoffset;
		chYOffset = yoffset;
		chHeight = h;
		chWidth = w;
		chXAdvance = xa;
		processCharImage(img);
	}
	
	private String getHexValue(int v) {
		String rv = "";
		String[] symbols = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
		
		int a = (v >> 24) & 0xFF;
		int r = (v >> 16) & 0xFF;
		int g = (v >> 8) & 0xFF;
		int b = v & 0xFF;
		
		int lnibble = a & 0x0F;
		int hnibble = (a >> 4) & 0xFF;
		rv = rv + symbols[hnibble] + symbols[lnibble];
		
		lnibble = r & 0x0F;
		hnibble = (r >> 4) & 0xFF;
		rv = rv + symbols[hnibble] + symbols[lnibble];
		
		lnibble = g & 0x0F;
		hnibble = (g >> 4) & 0xFF;
		rv = rv + symbols[hnibble] + symbols[lnibble];
		
		lnibble = b & 0x0F;
		hnibble = (b >> 4) & 0xFF;
		rv = rv + symbols[hnibble] + symbols[lnibble];		
		
		return rv;
	}

	private void processCharImage(BufferedImage img) {
		ch = new BufferedImage(chWidth, chHeight, BufferedImage.TYPE_INT_ARGB);
		
		for(int x = 0; x < chWidth; x++) {
			for(int y = 0; y < chHeight; y++) {
				int rgbColor = img.getRGB(x, y);
				int r = (rgbColor >> 16) & 0xFF;
				int g = (rgbColor >> 8) & 0xFF;
				int b = (rgbColor & 0xFF);
				int a = (rgbColor >> 24) & 0xFF;
				int newColor = (r << 16) | (g << 8) | b;
			
				if(a == 255 && newColor == 0) {
					ch.setRGB(x, y, 0x00000000);
				} else {
					ch.setRGB(x, y, 
							((a << 24) | (r << 16) | (g << 8) | b)
							);
				}
			}
		}
	}
	
	public BufferedImage getCh(int color) {
		BufferedImage img = new BufferedImage(chWidth, chHeight, BufferedImage.TYPE_INT_ARGB);
		
		for(int x = 0; x < chWidth; x++) {
			for(int y = 0; y < chHeight; y++) {
				int rgbColor = ch.getRGB(x, y);
				int r = (rgbColor >> 16) & 0xFF;
				int g = (rgbColor >> 8) & 0xFF;
				int b = (rgbColor & 0xFF);
				int a = (rgbColor >> 24) & 0xFF;
				int newColor = (r << 16) | (g << 8) | b;
			
				if(a == 255 && newColor > 0x777777) {
					img.setRGB(x, y, color);
				} else {
					img.setRGB(x, y, 
							((a << 24) | (r << 16) | (g << 8) | b)
							);
				}
			}
		}
		
		
		return img;
	}
}
