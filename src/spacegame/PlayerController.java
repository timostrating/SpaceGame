package spacegame;

import java.awt.event.KeyEvent;

/**
 * Created by Sneeuwpopsneeuw on 19-Oct-16.
 */
public class PlayerController {

    InputManager input = InputManager.getInstance();

    Player player;


    public void awake(Player player) {
        this.player = player;
    }

    public void update() {
        Vector2D velocity = new Vector2D(0,0);

        velocity.y += (input.isKeyDown(KeyEvent.VK_UP))     ? 1 : 0;  // Boolean naar Integer
        velocity.y -= (input.isKeyDown(KeyEvent.VK_DOWN))   ? 1 : 0;
        velocity.x += (input.isKeyDown(KeyEvent.VK_RIGHT))  ? 1 : 0;
        velocity.x -= (input.isKeyDown(KeyEvent.VK_LEFT))   ? 1 : 0;

        player.setVelocity(velocity);
    }



//	/*
//	 * Hier vangen we de toetsen af en doen we alleen iets met de toetsen die wij willen gebruiken in de game voor de
//	 * besturing van de speler sprite  // TODO this should be placed in a Input manager / player controller
//	 */
//	@Override
//	public void keyPressed(KeyEvent e) { // TODO  the caller of this fuction is build for typing not real input
//		switch (e.getKeyCode()) {  // TODO switch verwerkt de input niet goed als we meerder knoppen tegelijk opvangen
//			case KeyEvent.VK_UP:
//				player.setVelocityY(-Player.SPEED);
//				break;
//
//			case KeyEvent.VK_DOWN:
//				player.setVelocityY(Player.SPEED);
//				break;
//
//			case KeyEvent.VK_LEFT:
//				player.setVelocityX(-Player.SPEED);
//				break;
//
//			case KeyEvent.VK_RIGHT:
//				player.setVelocityX(Player.SPEED);
//				break;
//
//			case KeyEvent.VK_SPACE: // Fire button
//				createBullet(); // TODO this should be handeld in a player controller
//				break;
//
//			case KeyEvent.VK_ESCAPE:
//				System.exit(0);
//				break;
//		}
//	}
//
//

//
//
//	// resetten van de velocitie als we een kop loslaten
//	@Override
//	public void keyReleased(KeyEvent e) {  // TODO zo gaat dat natuurlijk niet werken he
//		switch (e.getKeyCode()) {
//		case KeyEvent.VK_UP:
//			player.setVelocityY(0.0);
//			break;
//
//		case KeyEvent.VK_DOWN:
//			player.setVelocityY(0.0);
//			break;
//
//		case KeyEvent.VK_LEFT:
//			player.setVelocityX(0.0);
//			break;
//
//		case KeyEvent.VK_RIGHT:
//			player.setVelocityX(0.0);
//			break;
//
//		case KeyEvent.VK_SPACE: // Fire button
//			break;
//		}
//	}
//
//
//	// KeyListener wil dat we hier wat mee doen maar we hebben het niet nodig, dus laten we het leeg
//	@Override
//	public void keyTyped(KeyEvent e) {
//	} // Not used
}