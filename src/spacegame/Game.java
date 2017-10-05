package spacegame;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;



// Hier start de game. In deze class zit de algemene functie main waarmee Java een programma opstart
public class Game extends Canvas implements Runnable {

	//	private static final long serialVersionUID = 1248653701434724090L; // TODO
	private String TEXT_Title = "2D Space Game Versie 0.0.5";
	private static final int WIDTH = 800;			// De breedte van het venster
	private static final int HEIGHT = 900;			// De hoogte van het venster
	public static final Dimension bounds = new Dimension(WIDTH, HEIGHT);  // de afmetingen van het venster

	private boolean isGameRunning = false;			// isGameRunning,  moet thread z'n werk nog blijven doen.
	private Thread mainGameThread;  				// mainGameThread is de thread waarin onze game loopt. Een THREAD

	private Random r = new Random(); 				// Dit is onze pseudo random number generator ook wel bekent als rng

	private Background background;					// Bevat onze achtergrond
	private UI ui;									// Alle UI,
	private Player player;							// De class die alle data van de speler bevat inc. de sprite
	private DebugFPS debugFPS;						// fps drawer
	private InputManager inputManager;				// our inputManager

	//we plaatsten alle beweegbare objecten nu nog in apparte lijsten
	private ArrayList<MoveableObject> enemies;		// Alle vijanden
	private ArrayList<MoveableObject> bullets;		// Alle kogels die we afschieten
	private ArrayList<MoveableObject> explosions;	// Alle explosies

	public static boolean pauseGame = false;		// Moeten we de game pauzeren?
	public static boolean gameOver = false;			// TODO  this looks like we have multiple states
	private double gameOverTimer = 1.0;				// Hoe lang moet "Game over" in het venster blijven staan?






	public static void main(String[] args) {
		Game game = new Game();
	}

	public Game() {
		new Window(this, TEXT_Title);
		startThreads();  // Let's start
	}


	// Hier starten we de game lus en initialiseren we de game
	private synchronized void startThreads() {
		if (isGameRunning)
			return;

		isGameRunning = true;		// We gaan de game als een thread starten dus zetten we deze variabele op true;
		mainGameThread = new Thread(this); 	// Hier maken we de thread aan.
		mainGameThread.start();				// Hier starten we de thread en dus de oneindige game lus.
	}


	// Om de game netjes af te sluiten moeten we een aantal zaken regelen. Dit doen we in de onderstaande methode
	private synchronized void stopThreads() {
		if (isGameRunning == false)
			return;

		isGameRunning = false;

		try {
			mainGameThread.join();			// Samenvoegen van de thread met onze hoofdthread.
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);						// Sluit het programma af
	}


	// Hier initialiseren we alle objecten/variabelen en meer voor we de game lus in gaan.
	public void awake() {  // TODO this function will have to many pesponsebilities,
						   // TODO \ awake should be called at the object that needs it
		background = new Background();
		inputManager = InputManager.getInstance();

		player = new Player(300, 800, Game.bounds);

		ui = new UI(player);

		bullets = new ArrayList<MoveableObject>();		// TODO  object pool
		enemies = new ArrayList<MoveableObject>();		// TODO  object pool
		explosions = new ArrayList<MoveableObject>();	// TODO  object pool

		Level.totalScore = 0;
		Level.levelScore = 0;
		
		createEnemies();
		
		JukeBox.init();
		JukeBox.load("res/bullet.wav", "firing");
		JukeBox.load("res/explosion.wav", "explosion");

		debugFPS = new DebugFPS();
	}


	// Creeren van vijanden op basis van het level.
	private void createEnemies() {  // TODO this should be placed in a spawner
		enemies.clear();
		
		// Hier maken we de vijaden voor een level en voegen die aan de dynamische lijst toe.
		for(int i = 0; i < Level.numberOfEnemies; i++) {  //
			if (Level.currentLevel < 3)
				enemies.add(new SmallEnemy( r.nextInt(bounds.width), -(r.nextInt(bounds.height)), Game.bounds));
			else if (Level.currentLevel <= 6)
				enemies.add(new MediumEnemy(r.nextInt(bounds.width), -(r.nextInt(bounds.height)), Game.bounds ));
			else if(Level.currentLevel <= 10)
				enemies.add(new LargeEnemy(	r.nextInt(bounds.width), -(r.nextInt(bounds.height)), Game.bounds ));

			// TODO, don't spawn in the screen
		}
	}
	

	/*
	 * De run method zal worden gestart wanneer de thread aangemaakt wordt.
	 * Dit is de hoofdlus van onze game. Hier gaan we tevens middels het berekenen van de tijd tussen iedere doorloop
	 * van de lus het aantal frames per second beperken. Als we dit niet doen is het aantal FPS te hoog en kan het
	 * problemen veroorzaken met de processor of de graphicsprocessor.
	 */

	/* The Game loop patern will look like this
        awake();
        while(true) {
            while( < 16.66 ms) { 		// unlimited updates
            	processInput();
            	update();
			}
            render(); 					// 60fps
        }
	 */
	@Override
	public void run() {
		final double DESIRED_FRAMES_PER_SECOND = 60.0; // Frames per second
		final double MS_PER_FRAME = 1000 / DESIRED_FRAMES_PER_SECOND;
		long lastTime = System.currentTimeMillis();
		long lag = 0L;

		awake();

		while (isGameRunning) { 	// Game Loop
			long currentTime = System.currentTimeMillis();
			long elapsed = currentTime - lastTime;
			lastTime = currentTime;
			lag += elapsed;

			while (lag >= MS_PER_FRAME)	{ // TODO  not perfect jet
				processInput();
				update();
				lag -= MS_PER_FRAME;
			}
			render(); 	// Render per frame
		}
	}


	public void processInput() {
		inputManager.update();
	}


	// Onze Game Loop roept zo vaak mogelijk deze fuctie aan als mogelijk, deze zorgt ervoor dat alles wordt geUpdate
	// Dit is nog voor de render  ook mogelijk nog voor een ander update
	public void update() {  // TODO inplement Behavior patern (Awake, Start, Update, ...)
		if (pauseGame) return;
		
		if (gameOver && explosions.size() <= 0 && gameOverTimer == 0.0) {
			System.exit(0);
		} else {
			gameOverTimer -= 0.2;
		}

		// losse objecten
		background.update();
		player.update();
		debugFPS.update();

		// game over ?
		if (player.getHealth() <= 0.0 && gameOver == false) {  // TODO this is should be controlled by the player not the Game Master
			// Player is dead
			gameOver = true;
			explosions.add(new Explosion(player.x, player.y, bounds));
			JukeBox.play("explosion");
		}
		
		// De update van alle vijanden in de lijst uitvoeren
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).update();
		}
		
		// Zijn er nog bullets die gewist kunnen worden?
		for (int i = 0; i < bullets.size(); i++) {
			Bullet b = (Bullet) bullets.get(i);
			if (b.getY() < 0) {  // FIXME kan NullPointerException geven
				bullets.remove(i);
				i--; // TODO  while loop is better
			}
		}

		// De updates van alle aanwezige bullets uitvoeren
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).update();
		}
		
		// Test Enemy - Bullet collision
		for(int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).hasCollided(bullets)) {
				if (enemies.get(i).getHealth() <= 0.0) {
					//Level.levelScore += Level.killScore;
					Level.totalScore += Level.killScore;
					explosions.add(
							new Explosion( 
									enemies.get(i).x,
									enemies.get(i).y,
									bounds
									)
							);
					JukeBox.play("explosion");
					enemies.remove(i);
					i--;  // TODO  while loop is better
				}
			}
		}
		
		// Controleren of de animatie van een explosie afgerond is Want als dat zo is dan kan die betreffende explosie
		// als object uit de lijst verwijderd worden.
		for(int i = 0; i < explosions.size(); i++) {
			Explosion e = (Explosion) explosions.get(i);
			
			if (e.isDone) {
				explosions.remove(i);
				i--;  // TODO  while loop is better
			} else {
				e.update();
			}
		}
		
		// Test collision   // TODO this is something for the enemy / player not the game master
		if (gameOver == false && player.hasCollided(enemies)) {  // FIXME  enemy list could contain null
//			geraaktTekst = TEXT_Geraakt;
			player.setHealth(player.getHealth() - 1); // FIXME this makes no sense
		} else {
//			geraaktTekst = "";
		}
		
		if (enemies.size() <= 0) {
			pauseGame = true;
			Level.nextLevel();
			createEnemies();
//			TEXT_NextLevelText = "L E V E L " + Level.currentLevel;
			pauseGame = false;
		}
	}


	// Deze fuctie wordt door de Game Loop 60 x per seconde aangeroepen, zodat we hier het scherm kunnen vernieuwen
	public void render() {    // TODO look for a beter system to handle the render part
		if (pauseGame) return;
		
		BufferStrategy bufferStrategy = getBufferStrategy();

		if (bufferStrategy == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());  // default black background

		////////////////////////////////////////////////////////////////////////////////////// DRAW FRAME

		background.render(g);
		ui.render(g);

		// Als speler nog niet dood is dan moet hij getoond worden
		if (gameOver == false) player.render(g);  // FIXME this makes no sense here

		// Alle vijanden op hun nieuwe posities tonen
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).render(g);
		}
		
		// Alle bullets op hun nieuwe posities tonen
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
		}
		
		// Alle animaties van explosies verder afwerken
		for (int i = 0; i < explosions.size(); i++) {
			explosions.get(i).render(g);
		}

		debugFPS.render(g);
		////////////////////////////////////////////////////////////////////////////////////// THE END

		g.dispose();
		bufferStrategy.show();
	}
}
