package spacegame;

import java.awt.event.KeyEvent;

/**
 * Created by Sneeuwpopsneeuw on 19-Oct-16.
 */
public class PlayerController {

    InputManager input= InputManager.getInstance();


    public void update() {
        Vector2D velocity = new Vector2D(0,0);

        velocity.y += (input.isKeyDown(KeyEvent.VK_UP))     ? 1 : 0;  // Boolean naar Integer
        velocity.y -= (input.isKeyDown(KeyEvent.VK_DOWN))   ? 1 : 0;
        velocity.x += (input.isKeyDown(KeyEvent.VK_RIGHT))  ? 1 : 0;
        velocity.x -= (input.isKeyDown(KeyEvent.VK_LEFT))   ? 1 : 0;
    }
}