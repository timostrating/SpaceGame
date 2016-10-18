package spacegame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class BitmapFont {
	public static final int PADDING_TOP = 0;
	public static final int PADDING_RIGHT = 1;
	public static final int PADDING_BOTTOM = 2;
	public static final int PADDING_LEFT = 3;
	public static final int SPACING_HORIZONTAL = 0;
	public static final int SPACING_VERTICAL = 1;
	private Character[] fontTable;
	private int numOfChars;
	private String fontName;
	private String fontFile;
	private String fontImageFile;
	private int lineheight = 0;
	private int base = 0;
	private int fontSize = 0;
	private int[] spacing = new int[2];
	private int[] padding = new int[4];
	private int fontColor = 0xFFFFFF;
	public boolean first = false;

	public BitmapFont(String fontfile, int fntColor) {
		this.fontFile = fontfile;
		this.fontColor = fntColor;

		loadFontTable();
	}

	private void processTokens(String tokenName, String tokenValue) {
		if (tokenName.equals("name")) {
			this.fontName = tokenValue.trim();
		} else if (tokenName.equals("size")) {
			this.fontSize = Integer.parseInt(tokenValue.trim());
		} else if (tokenName.equals("padding")) {
			// tokenValue = ##,##,##,##
			int tokenDelimiter = tokenValue.lastIndexOf(",");
			int index = 3;

			while (tokenDelimiter > 0) {
				this.padding[index--] = Integer.parseInt(tokenValue.substring(
						tokenDelimiter + 1).trim());
				tokenValue = tokenValue.substring(0, tokenDelimiter).trim();
				tokenDelimiter = tokenValue.lastIndexOf(",");
			}
		} else if (tokenName.equals("spacing")) {
			// tokenValue = ##,##
			int tokenDelimiter = tokenValue.indexOf(",");
			this.spacing[BitmapFont.SPACING_VERTICAL] = Integer
					.parseInt(tokenValue.substring(tokenDelimiter + 1).trim());
			this.spacing[BitmapFont.SPACING_VERTICAL] = Integer
					.parseInt(tokenValue.substring(0, tokenDelimiter).trim());
		} else if (tokenName.equals("lineheight")) {
			this.lineheight = Integer.parseInt(tokenValue.trim());
		} else if (tokenName.equals("base")) {
			this.base = Integer.parseInt(tokenValue.trim());
		} else if (tokenName.equals("file")) {
			this.fontImageFile = tokenValue.trim();
		} else if (tokenName.equals("chars count")) {
			this.numOfChars = Integer.parseInt(tokenValue.trim());
		}
	}

	private void readHeader(BufferedReader input) {
		String stringRead = "";
		String tokenName = "";
		String tokenValue = "";
		int tokenDelimiter = 0;

		try {
			do {
				stringRead = input.readLine();

				if (stringRead.length() > 0) {
					tokenDelimiter = stringRead.indexOf("=");
					tokenValue = stringRead.substring(tokenDelimiter + 1)
							.trim();
					tokenName = stringRead.substring(0, tokenDelimiter)
							.toLowerCase();

					processTokens(tokenName, tokenValue);
				}
			} while (stringRead.length() > 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private void processCharacterLine(String line, BufferedImage chars, int currentCharIndex) {
		int id, x, y, w, h, xo, yo, xa;
		String l = line;
		// Char ID
		id = Integer.parseInt(l.substring(l.indexOf("=") + 1, 13).trim());
		l = l.substring(14);

		// x
		x = Integer.parseInt(l.substring(l.indexOf("=") + 1, 7).trim());
		l = l.substring(8);

		// y
		y = Integer.parseInt(l.substring(l.indexOf("=") + 1, 7).trim());
		l = l.substring(8);

		// w
		w = Integer.parseInt(l.substring(l.indexOf("=") + 1, 11).trim());
		l = l.substring(12);

		// h
		h = Integer.parseInt(l.substring(l.indexOf("=") + 1, 12).trim());
		l = l.substring(13);

		// xo
		xo = Integer.parseInt(l.substring(l.indexOf("=") + 1, 13).trim());
		l = l.substring(14);

		// yo
		yo = Integer.parseInt(l.substring(l.indexOf("=") + 1, 13).trim());
		l = l.substring(14);

		// xa
		xa = Integer.parseInt(l.substring(l.indexOf("=") + 1, 14).trim());


		fontTable[currentCharIndex] = new spacegame.Character(
				chars.getSubimage(x, y, w, h),
				id, w, h, xo, yo, xa);
	}

	private void readCharacters(BufferedReader input) {
		String lineRead = "";
		BufferedImageLoader loader = new BufferedImageLoader();
		BufferedImage fontImages = loader.loadSprite("res/" + this.fontImageFile);
		int charCount = 0;

		try {
			while((lineRead = input.readLine()) != null) {
				processCharacterLine( lineRead, fontImages, charCount );
				charCount++;
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private void loadFontTable() {
		try {
			FileReader inputStream = new FileReader(this.fontFile);
			BufferedReader input = new BufferedReader(inputStream);

			readHeader(input);
			fontTable = new Character[this.numOfChars];
			readCharacters(input);

			input.close();
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BufferedImage getChar( int index ) {
		if (index >= 0 && index < this.numOfChars)
			return fontTable[index].ch;

		return null;
	}

	public BufferedImage getChar (char c) {
		for (int i = 0; i < this.numOfChars; i++) {
			if (fontTable[i].chCode == (int) c)
				return fontTable[i].ch;
		}

		return null;
	}

	public char getAsciiCode( int index ) {
		if (index >= 0 && index < this.numOfChars)
			return (char)fontTable[index].chCode;

		return ' ';
	}

	public int getIndex(char c) {
		for(int i = 0; i < this.numOfChars; i++) {
			if (fontTable[i].chCode == (int) c)
				return i;
		}

		return 0;
	}

	public BufferedImage getString(String s) {
		BufferedImage textLine = null;
		int w = 0;
		// Calculate length and height of the line for the total string
		for (int i = 0; i < s.length(); i++) {
			w += fontTable[getIndex(s.charAt(i))].chXAdvance + this.spacing[SPACING_HORIZONTAL];
		}

		// Create the image
		textLine = new BufferedImage(w, this.lineheight, BufferedImage.TYPE_INT_ARGB);

		// Add the character images to the textLine
		Graphics2D g = (Graphics2D) textLine.getGraphics();
		int x = 0, y = 0;
		for(int i = 0; i < s.length(); i++) {
			g.drawImage(fontTable[getIndex(s.charAt(i))].ch, //.getCh(fontColor), 
					x + fontTable[getIndex(s.charAt(i))].chXOffset,
					y + fontTable[getIndex(s.charAt(i))].chYOffset,
					null);
			x += fontTable[getIndex(s.charAt(i))].chXAdvance + this.spacing[SPACING_HORIZONTAL];
		}

		g.dispose();

		return textLine;
	}

	public void drawString(Graphics g, String s, int x, int y) {
		BufferedImage str = getString(s);

		for (int row = 0; row < str.getHeight(); row++) {
			for (int col = 0; col < str.getWidth(); col++) {
				//g.draw

			}
		}
	}
}
