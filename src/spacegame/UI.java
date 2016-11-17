package spacegame;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Timo Strating on 19-Oct-16.
 */
public class UI {
    private double LevelTextDuration = 0.005;	    // Hoe lang moet de tekst hierboven In het venster blijven staan
    private double LevelTextDurationTimer = 0.0;    // Hier tellen we de seconden
    private String TEXT_GameOver = "G A M E  O V E R";
    private String TEXT_NextLevelText = "L E V E L 1";
//    private String TEXT_Geraakt = "Geraakt";

    private BufferedImage healthBar;		        // De afbeelding van de healthbar
    private BufferedImage scoreBar;			        // De afbeelding van de score bar

    private String geraaktTekst = "";		        // Slechts bedoeld voor controle
    private BitmapFont font;				        // Een Bitmap Font die we gaan gebruiken in de code
    private Font levelFont;

    private Player player;




    public UI(Player player) { // TODO   Not the best way
        this.player = player;
        awake();
    }

    public void awake() {
        BufferedImageLoader loader = new BufferedImageLoader();
        font = new BitmapFont("res/robotomedium_2a.fnt", 0xffffffff);
        levelFont = new Font("Amethyst", 0, 72);
        healthBar = loader.loadSprite("res/healthbar.fw.png");
        scoreBar = loader.loadSprite("res/score.fw.png");
    }

    public void render(Graphics2D g) {
        // Healthbar van de speler tonen
        g.setFont(new Font("Roboto", 0, 24));
        g.drawImage(healthBar, 0, 0, 152, 42, null);	// FIXME hardcoded
        g.setColor(Color.GREEN);
        g.fillRect(32, 7, (int) player.getHealth(), 10);  // TODO dit werkt niet als de spelers health dynamisch is

        // Score bar van de speler tonen
        g.drawImage(scoreBar, Game.bounds.width - 142, 0, 152, 42, null);	// FIXME hardcoded
        g.setColor(Color.WHITE);
        String scoreText = "" + Level.totalScore;

        //Met onze eigen font class vormen we een tekst om naar een afbeelding.
        BufferedImage scoreTextImg = font.getString(scoreText);
        g.drawImage(scoreTextImg, (Game.bounds.width - scoreTextImg.getWidth()) - 15, 3, null);

        g.setColor(Color.YELLOW);                  // TODO a screen effect looks better
        g.drawString(geraaktTekst, 10, Game.bounds.height - 80);


        // Als de speler dood is dan hier tonen
        if (Game.gameOver) { // TODO  rendering a frame and checking if we need to show the gameover text is different
            drawStringAtCenter(g, Game.bounds, TEXT_GameOver);
            Dimension tmp = new Dimension( Game.bounds.width, Game.bounds.height -160);
            drawStringAtCenter(g, tmp, ("SCORE: " + Level.totalScore));
        }

        if (TEXT_NextLevelText.length() > 0) {   // Volgend level bootschap dat voor enkele tijd wordt getoont
            drawStringAtCenter(g, Game.bounds, TEXT_NextLevelText);

            if (LevelTextDurationTimer >= 1.0) {
                TEXT_NextLevelText = "";
                LevelTextDurationTimer = 0.0;
            } else {
                LevelTextDurationTimer += LevelTextDuration;
            }
        }
    }


    private void drawStringAtCenter(Graphics2D g, Dimension bounds, String text) {
        g.setColor(new Color(50, 255, 255, 128));  	// TODO hardcoded color
        g.setFont(levelFont);
        FontMetrics f = g.getFontMetrics(levelFont);
        int textLength = f.stringWidth(text);
        int textHeight = f.getHeight();
        int textX = ((bounds.width - textLength) / 2);
        int textY = ((bounds.height - textHeight) / 2);

        g.drawString(text, textX, textY );
    }
}
