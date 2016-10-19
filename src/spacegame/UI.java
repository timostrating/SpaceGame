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

    private int FPS = 0;					        // Frames Per Second
    private int UPS = 0;				            // Updates Per Second
    private int frames = 0;                         // hoeveel frames tot nu toe
    private int updates = 0;                        // hoeveel updates tot nu toe

    private String geraaktTekst = "";		        // Slechts bedoeld voor controle
    private BitmapFont font;				        // Een Bitmap Font die we gaan gebruiken in de code

    private Player player;
    long timer = System.currentTimeMillis();



    public UI(Player player) { // TODO   Not the best way
        this.player = player;
        awake();
    }

    public void awake() {
        BufferedImageLoader loader = new BufferedImageLoader();
        font = new BitmapFont("res/robotomedium_2a.fnt", 0xffffffff);
        healthBar = loader.loadSprite("res/healthbar.fw.png");
        scoreBar = loader.loadSprite("res/score.fw.png");
    }

    public void update() {  // TODO  dit zou een debug class moeten doen
        updates++;
        if (System.currentTimeMillis() - timer > 1000) {
            timer += 1000;
            UPS = updates;
            FPS = frames;
            updates = frames = 0;  // both 0
        }
    }

    public void render(Graphics2D g) {
        frames++;

        // Healthbar van de speler tonen
        g.setFont(new Font("Roboto", 0, 24));
        g.drawImage(healthBar, 0, 0, 152, 42, null);	// FIXME hardcoded
        g.setColor(Color.GREEN);
        g.fillRect(32, 7, (int) player.getHealth(), 10);

        // Score bar van de speler tonen
        g.drawImage(scoreBar, Game.bounds.width - 142, 0, 152, 42, null);	// FIXME hardcoded
        g.setColor(Color.WHITE);
        String scoreText = "" + Level.totalScore;

        //Met onze eigen font class vormen we een tekst om naar een afbeelding.
        BufferedImage scoreTextImg = font.getString(scoreText);
        g.drawImage(scoreTextImg, (Game.bounds.width - scoreTextImg.getWidth()) - 15, 3, null);

        // DEBUG FPS info
        g.setColor(Color.YELLOW);
        g.drawString(geraaktTekst, 10, 900 - 80);  // TODO Game.getHeight()
        g.drawString(UPS + " updates - "+ FPS + " fps", 10, 900 - 40); // TODO Game.getHeight()


        // Als de speler dood is dan hier tonen
        if (Game.gameOver) { // TODO  rendering a frame and checking if we need to show the gameover text is different
            // Show Game Over message
            Font levelFont = new Font("Amethyst", 0, 72);
            g.setColor(new Color(50, 255, 255, 128)); 	// FIXME hardcoded color
            g.setFont( levelFont );
            FontMetrics f = g.getFontMetrics(levelFont);
            int textLength = f.stringWidth(TEXT_GameOver);
            int textHeight = f.getHeight();
            int textX = ((Game.bounds.width - textLength) / 2);
            int textY = ((Game.bounds.height - textHeight) / 2);

            g.drawString(TEXT_GameOver, textX, textY);
            textLength = f.stringWidth("SCORE: " + Level.totalScore);
            textHeight = f.getHeight();
            textX = ((Game.bounds.width - textLength) / 2);
            textY = ((Game.bounds.height - textHeight) / 2);
            g.drawString("SCORE: " + Level.totalScore,
                    textX,
                    textY + 80);
        }

        // Als we naar een volgend level gaan dan laten we hieronder een boodschap zien voor een bepaald aantal ms
        if (TEXT_NextLevelText.length() > 0) {
            Font levelFont = new Font("Amethyst", 0, 72);
            g.setColor(new Color(50, 255, 255, 128));  	// TODO hardcoded color
            g.setFont( levelFont );
            FontMetrics f = g.getFontMetrics(levelFont);
            int textLength = f.stringWidth(TEXT_NextLevelText);
            int textHeight = f.getHeight();
            int textX = ((Game.bounds.width - textLength) / 2);
            int textY = ((Game.bounds.height - textHeight) / 2);

            g.drawString(TEXT_NextLevelText,
                    textX,
                    textY
            );
            if (LevelTextDurationTimer >= 1.0) {
                TEXT_NextLevelText = "";
                LevelTextDurationTimer = 0.0;
            } else {
                LevelTextDurationTimer += LevelTextDuration;
            }
        }

//        g.dispose();
    }
}
