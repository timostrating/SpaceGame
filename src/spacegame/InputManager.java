package spacegame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Timo Strating on 19-Oct-16.
 */
public final class InputManager implements KeyListener {

	//one for each ascii character.
	private boolean[] key_state_up = new boolean[256];  // true if pressed
	private boolean[] key_state_down = new boolean[256]; // true if not pressed

	private boolean keyPressed = false; // variable that indicates when any key(s) are being pressed.
	private boolean keyReleased = false; // variable that indicates that some key was released this frame. cleared every frame.

	private static InputManager instance = new InputManager();  // SINGLETON
	public static InputManager getInstance() {
		return instance;
	}



	// constructor
	protected InputManager() {
	}

	public void update() {
		key_state_up = new boolean[256];
		keyReleased = false;
	}

	public void keyPressed(KeyEvent e) {
//		System.out.println("InputManager: A key has been pressed code=" + e.getKeyCode());
		if( e.getKeyCode() >= 0 && e.getKeyCode() < 256 ) {
			key_state_down[e.getKeyCode()] = true;
			key_state_up[e.getKeyCode()] = false;
			keyPressed = true;
			keyReleased = false;
		}
	}


	public void keyReleased(KeyEvent e) {
//		System.out.println("InputManager: A key has been released code=" + e.getKeyCode());
		if( e.getKeyCode() >= 0 && e.getKeyCode() < 256 ) {
			key_state_up[e.getKeyCode()] = true;
			key_state_down[e.getKeyCode()] = false;
			keyPressed = false;
			keyReleased = true;
		}
	}

	// TODO er zou een manier moeten zijn om een smoothed motion te kunnen opvragen
	// TODO ^^ ADSR patern toe voegen (Attack, Decay, Sustain and Release)
	// TODO ^^ Gravity, Dead, Sensitivity zou genoeg moeten zijn.


	public void keyTyped(KeyEvent e) {
	} // we are not using this, we could add a cache of all buttons pressed

	public boolean isKeyDown( KeyEvent e ) { return isKeyDown(e.getKeyCode()); }
	public boolean isKeyUp( KeyEvent e ) { return isKeyUp(e.getKeyCode()); }

	public boolean isKeyDown( int key ) { return key_state_down[key]; }
	public boolean isKeyUp( int key ) {	return key_state_up[key]; }

	public boolean isAnyKeyDown() {	return keyPressed;	}
	public boolean isAnyKeyUp() { return keyReleased; } //TODO Het niet direct duidelijk welke data je terug krijgt hier
}
