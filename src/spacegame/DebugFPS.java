package spacegame;

import java.awt.*;

/**
 * Created by Timo Strating on 19-Oct-16.
 */
public class DebugFPS {

    long timer = System.currentTimeMillis();
    private int FPS = 0;					        // Frames Per Second
    private int UPS = 0;				            // Updates Per Second
    private int frames = 0;                         // hoeveel frames tot nu toe
    private int updates = 0;                        // hoeveel updates tot nu toe

    public void update() {
        if(Settings.DEBUG == false) return;

        updates++;
        if (System.currentTimeMillis() - timer > 1000) {
            timer += 1000;
            UPS = updates;
            FPS = frames;
            updates = frames = 0;  // both 0
        }
    }

    public void render(Graphics2D g) {
        if(Settings.DEBUG == false) return;

        frames++;

        g.setColor(Color.YELLOW);
        g.drawString(UPS + " updates - "+ FPS + " fps", 10, Game.bounds.height - 40);
    }
}
