package spacegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;



// Hier start de game. In deze class zit de algemene functie main waarmee Java een programma opstart
public class Game extends Canvas implements Runnable, KeyListener {

	private static final long serialVersionUID = 1248653701434724090L;
	public static final int WIDTH = 800;	// De breedte van het venster
	public static final int HEIGHT = 900;	// De hoogte van het venster
	public final String TITLE = "2D Space Game Versie 0.0.3";   // TODO all text should be placed at one position
	/*
	 * Dimension is een hulp class of ook wel een container class genoemd, waarin we data kunnen bewaren die met elkaar
	 * verband houden. Zoals hier de afmetingen van een venster.
	 */
	public static final Dimension bounds = new Dimension(WIDTH, HEIGHT);
	
	// isGameRunning gebruiken we om in de thread te kunnen controleren of de thread z'n werk moet blijven doen.
	private boolean isGameRunning = false;
	/*
	 * mainGameThread is de thread waarin onze game loopt Een THREAD is het apart en naast elkaar laten draaien van
	 * programmacode. Deze techniek zorgt voor een goede verdeling van de kracht van een processor over verschillende
	 * stukken code, waardoor deze stukken code niet op elkaar hoeven te wachten. Windows werkt ook op deze manier,
	 * daardoor kan Windows Multitasking simuleren.
	 */
	private Thread mainGameThread;
	private int FPS = 0;					// Hier houden we onze FPS bij
	private String geraaktTekst = "";		// Slechts bedoeld voor controle
	private BitmapFont font;				// Een Bitmap Font die we gaan gebruiken in de code
	
	private BufferedImage healthBar;		// De afbeelding van de healthbar
	private BufferedImage scoreBar;			// De afbeelding van de score bar
	
	/*
	 * Dit is een class waarmee we willekeurige getallen kunnen produceren. We noemen dit een random number generator.
	 * Hier zit een ingewikkelde wiskundige formule achter.
	 */
	private Random r = new Random();
	
	private String NextLevelText = "L E V E L 1";
	private double LevelTextDuration = 0.005;	// Hoe lang moet de tekst hierboven In het venster blijven staan
	private double LevelTextDurationTimer = 0.0;// Hier tellen we de seconden

	private Background background;				// Bevat onze achtergrond
	private Player player;						// De class die alle data van de speler bevat inc. de sprite van de
	
	/*
	 * Een ArrayList is een dynamische lijst die dus al naar behoefte kan groeien en krimpen. Door dit dynamisch bij te
	 * kunnen houden kunnen we voorkomen dat ons intern geheugen vol gaat lopen met verschillende soorte objecten in ons
	 * speelvenster.
	 * 
	 * Met <MoveableObject> geven we aan wat voor objecten we in zo'n lijst willen bewaren.
	 * 
	 * Hieronder hebben we 3 lijsten, allemaal van het type MoveableObject. Alle beweegbare objecten in onze game
	 * stammen af van dit ene type. In dit ene type worden alle gemeenschappelijke zaken beschreven. Omdat we de kogels
	 * iets anderswillen gaan behandelen straks maken we hiervan een aparte lijst. hetzelfde geldt voor vijanden.
	 * Explosies zijn in feite ook beweegbare objecten hoewel ze niet van plaats veranderen,maar ze hebben een animatie
	 * met de andere beweegbare objecten.
	 */
	private ArrayList<MoveableObject> enemies;		// Alle vijanden
	private ArrayList<MoveableObject> bullets;		// Alle kogels die we afschieten
	private ArrayList<MoveableObject> explosions;	// Alle explosies
	
	private boolean pauseGame = false;			// Moeten we de game pauzeren?
	private boolean gameOver = false;			// Is het Game Over?  // TODO  this looks like we have multiple states
	private double gameOverTimer = 1.0;			// Hoe lang moet het bericht "Game over" in het venster blijven staan?

	
	// Hier starten we de game lus en initialiseren we de game
	private synchronized void start() {
		if (isGameRunning)
			return;

		isGameRunning = true;		// We gaan de game als een thread starten dus zetten we deze variabele op true;
		mainGameThread = new Thread(this); 	// Hier maken we de thread aan.
		mainGameThread.start();				// Hier starten we de thread en dus de oneindige game lus.
	}

	// Om de game netjes af te sluiten moeten we een aantal zaken regelen. Dit doen we in de onderstaande methode
	private synchronized void stop() {
		if (!isGameRunning)
			return;

		isGameRunning = false;
		/*
		 * Een Try Catch blok vangt onvoorspelbare fouten op zodat onze game niet crashed. In het try gedeelte plaatsen
		 * we de statement die onvoorspelbare fouten kan opleveren. In ons geval is dat het weer samenvoegen van de
		 * thread moet ons hoofdprogramma, zodat de game netjes kan worden afgesloten. In het catch blok vertellen we
		 * dan wat we gaan doen met de onvoorspelbare fout. Hier laten we een standaard boodschap in de console zien
		 * zodat we de opgetreden fout kunnen traceren.
		 */
		try {
			mainGameThread.join();			// Samenvoegen van de thread met onze hoofdthread.
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);						// Sluit het programma af
	}

	/*
	 * Hier initialiseren we alle objecten/variabelen en meer voor we de game lus in gaan. Dit is nodig om alles klaar
	 * te hebben staan zodat we er in de lus mee kunnen werken.
	 */
	public void init() {  // TODO this function will have to many pesponsebilities,
						  // TODO \ init shout be called at the object that needs it
		BufferedImageLoader loader = new BufferedImageLoader();

		font = new BitmapFont("res/robotomedium_2a.fnt", 0xffffffff);
		background = new Background();
		healthBar = loader.loadSprite("res/healthbar.fw.png");
		scoreBar = loader.loadSprite("res/score.fw.png");
		
		player = new Player(300, 800, Game.bounds);
		bullets = new ArrayList<MoveableObject>();		// TODO  object pool
		enemies = new ArrayList<MoveableObject>();		// TODO  object pool
		explosions = new ArrayList<MoveableObject>();	// TODO  object pool

		Level.totalScore = 0;
		Level.levelScore = 0;
		
		createEnemies();
		
		JukeBox.init();
		JukeBox.load("res/bullet.wav", "firing");
		JukeBox.load("res/explosion.wav", "explosion");
	}

	/*
	 * Creeren van vijanden op basis van het level.In de eerste for lus gaan we eerst alle huidige vijanden verwijderen.
	 * Dan is de dynamische lijst leeg en kunnen we die weer vullen met nieuwe vijanden.
	 */
	private void createEnemies() {  // TODO this should be placed in a spawner
		for (int i = 0; i < enemies.size(); i++) {
			enemies.remove(i);
			i--;
		}
		
		// Hier maken we de vijaden voor een level en voegen die aan de dynamische lijst toe. Tevens bepalen we hier het
		// effect van een nieuw level voor de speler, zoals hoeveel hitpoints heeft een vijand, dus hoe lang duurt het
		// met onze kogels om een vijand te doden.
		for(int i = 0; i < Level.numberOfEnemies; i++) {  //
			if (Level.currentLevel > 0 && Level.currentLevel < 3) {
				player.setHitpoints(20.0); // FIXME  hardcoded
				enemies.add(new SmallEnemy(
						r.nextInt(bounds.width), -(r.nextInt(bounds.height)), Game.bounds
						));
			} else if (Level.currentLevel >= 3 && Level.currentLevel <= 6) {
				player.setHitpoints(30.0); // FIXME  hardcoded
				enemies.add(new MediumEnemy(
						r.nextInt(bounds.width), -(r.nextInt(bounds.height)), Game.bounds
						));
			} else if(Level.currentLevel > 6 && Level.currentLevel <= 10) {
				player.setHitpoints(40.0); // FIXME  hardcoded
				enemies.add(new LargeEnemy(
						r.nextInt(bounds.width), -(r.nextInt(bounds.height)), Game.bounds
						));
			}

		}
	}
	

	/*
	 * De run method zal worden gestart wanneer de thread aangemaakt wordt.
	 * Dit is de hoofdlus van onze game. Hier gaan we tevens middels het berekenen van de tijd tussen iedere doorloop
	 * van de lus het aantal frames per second beperken. Als we dit niet doen is het aantal FPS te hoog en kan het
	 * problemen veroorzaken met de processor of de graphicsprocessor.
	 */
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		final double amountOfTicks = 60.0; // Frames per second
		double nanoSeconds = 1000000000 / amountOfTicks;
		double deltaTime = 0; // Time passed
		@SuppressWarnings("unused")
		int updates = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();

		init();
		// Game Loop
		while (isGameRunning) {
			long currentTime = System.nanoTime();
			deltaTime += (currentTime - lastTime) / nanoSeconds;  // FIXME: this is not how deltaTime works
			lastTime = currentTime;

			if (deltaTime >= 1) {
				tick(); // Actions per tick
				deltaTime--;
				updates++;

			}
			render(); 	// Render per frame
			frames++;	// Als we gerendered hebben dan hebben we weer een frame geproduceerd

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				FPS = frames;	// FPS dient er voor
								// om het aantal FPS ook in onze gamevenster
								// te kunnen tonen
				updates = 0;
				frames = 0;
			}
		}
	}

	/*
	 * Dit is de method (functie) die in de game lus wordt aangeroepen bij iedere tick die de processor aan onze game
	 * beschikbaar stelt. In feite updaten we in deze method de staat van onze game. Dus nieuwe posities van alle
	 * objecten animaties en meer. We geven hier nog niks weer op het scherm.
	 */
	public void tick() {  // TODO inplement Behavior patern (Awake, Start, Update, ...)
		if (pauseGame) return;
		
		if (gameOver && explosions.size() <= 0 && gameOverTimer == 0.0) {
			System.exit(0);
		} else {
			gameOverTimer -= 0.2;
		}
		
		/*
		 * Ook alle objecten hebben een tick functie om specifieke updates voor dat object te kunnen uitvoeren. Die
		 * moeten dan wel hier aangeroepen oftewel opgestart worden.
		 */
		background.tick();
		player.tick();

		/*
		 * Voor we verder gaan eerst even controleren of de speler inmiddels dood is, want als dat zo is hoeven we niets
		 * anders meer te doen.
		 */
		if (player.getHealth() <= 0.0 && !gameOver) {
			// Player is dead
			gameOver = true;
			explosions.add(
					new Explosion( 
							player.x,
							player.y,
							bounds
							)
					);
			JukeBox.play("explosion");
		}
		
		// De update van alle vijanden in de lijst uitvoeren
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).tick();
		}
		
		// Zijn er nog bullets die gewist kunnen worden?
		for (int i = 0; i < bullets.size(); i++) {
			Bullet b = (Bullet) bullets.get(i);
			if (b.getY() < 0) {
				bullets.remove(i);
				i--;
			}
		}

		// De updates van alle aanwezige bullets uitvoeren
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).tick();
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
					i--;
				}
			}
		}
		
		// Controleren of de animatie van een explosie afgerond is Want als dat zo is dan kan die betreffende explosie
		// als object uit de lijst verwijderd worden.
		for(int i = 0; i < explosions.size(); i++) {
			Explosion e = (Explosion) explosions.get(i);
			
			if (e.isDone) {
				explosions.remove(i);
				i--;
			} else {
				e.tick();
			}
		}
		
		// Test collision   // TODO this is something for the enemy / player not the game master
		if (player.hasCollided(enemies)) {  // FIXME  we could also be game over
			geraaktTekst = "Geraakt";
			player.setHealth(player.getHealth() - 0.1);
		} else {
			geraaktTekst = "";
		}
		
		if (enemies.size() <= 0) {
			pauseGame = true;
			Level.nextLevel();
			createEnemies();
			NextLevelText = "L E V E L " + Level.currentLevel;
			pauseGame = false;
		}
	}

	/*
	 * In de game lus wordt ook om de zoveel milliseconden deze methode gestart om na het updaten van alle objecten
	 * alles weer te tonen in het venster.
	 */
	public void render() {    // TODO look for a beter system to handle the render part
		if (pauseGame) return;
		
		BufferStrategy bufferStrategy = this.getBufferStrategy();

		if (bufferStrategy == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(
		        RenderingHints.KEY_FRACTIONALMETRICS,
		        RenderingHints.VALUE_FRACTIONALMETRICS_ON);	
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());

		// //////////////////////////////////////////////
		// Tussen deze strepen en de onderste strepen //
		// komt het eigenlijke tekenen van het scherm //
		// //////////////////////////////////////////////

		background.render(g);
		//g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);

		// Healthbar van de speler tonen
		g.setFont(new Font("Roboto", 0, 24));
		g.drawImage(healthBar, 0, 0, 152, 42, null);	// FIXME hardcoded color
		g.setColor(Color.GREEN);
		g.fillRect(32, 7, (int) player.getHealth(), 10);
		
		// Score bar van de speler tonen
		g.drawImage(scoreBar, bounds.width - 142, 0, 152, 42, null);	// FIXME hardcoded color
		g.setColor(Color.WHITE);
		String scoreText = "" + Level.totalScore;
		
		/*
		 * Met onze eigen font class vormen we een tekst om naar een afbeelding.
		 * Op deze manier updaten we in het venster de score van de speler.
		 */
		BufferedImage scoreTextImg = font.getString(scoreText);
		g.drawImage(scoreTextImg, (bounds.width - scoreTextImg.getWidth()) - 15, 3, null);
		
		// Tonen van het aantal FPS, alleen voor onze controle
		g.setColor(Color.YELLOW);
		g.drawString(FPS + " fps", 10, getHeight() - 40);
		g.drawString(geraaktTekst, 100, getHeight() - 40);
		
		// Als speler nog niet dood is dan moet hij getoond worden
		if (!gameOver) player.render(g);  // FIXME this makes no sense here

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

		// Als de speler dood is dan hier tonen
		if (gameOver) { // TODO  rendering a frame and checking if we need to show the gameover text is different
			// Show Game Over message
			Font levelFont = new Font("Amethyst", 0, 72);
			g.setColor(new Color(50, 255, 255, 128)); 	// FIXME hardcoded color
			g.setFont( levelFont );
			FontMetrics f = g.getFontMetrics(levelFont);
			int textLength = f.stringWidth("G A M E  O V E R");
			int textHeight = f.getHeight();
			int textX = ((bounds.width - textLength) / 2);
			int textY = ((bounds.height - textHeight) / 2);
			
			g.drawString("G A M E  O V E R", 
					textX,
					textY
				);
			textLength = f.stringWidth("SCORE: " + Level.totalScore);
			textHeight = f.getHeight();
			textX = ((bounds.width - textLength) / 2);
			textY = ((bounds.height - textHeight) / 2);
			g.drawString("SCORE: " + Level.totalScore,
					textX,
					textY + 80);
		}
		
		// Als we naar een volgend level gaan dan laten we hieronder een boodschap zien voor een bepaald aantal ms
		if (NextLevelText.length() > 0) {
			Font levelFont = new Font("Amethyst", 0, 72);
			g.setColor(new Color(50, 255, 255, 128));  	// TODO hardcoded color
			g.setFont( levelFont );
			FontMetrics f = g.getFontMetrics(levelFont);
			int textLength = f.stringWidth(NextLevelText);
			int textHeight = f.getHeight();
			int textX = ((bounds.width - textLength) / 2);
			int textY = ((bounds.height - textHeight) / 2);
			
			g.drawString(NextLevelText, 
					textX,
					textY
				);
			if (LevelTextDurationTimer >= 1.0) {
				NextLevelText = "";
				LevelTextDurationTimer = 0.0;
			} else {
				LevelTextDurationTimer += LevelTextDuration;
			}
		}
		// //////////////////////////////////////////////
		// Einde //
		// //////////////////////////////////////////////
		g.dispose();
		bufferStrategy.show();
	}

	/*
	 * Hier vangen we de toetsen af en doen we alleen iets met de toetsen die wij willen gebruiken in de game voor de
	 * besturing van de speler sprite  // TODO this should be placed in a Input manager / player controller
	 */
	@Override
	public void keyPressed(KeyEvent e) { // TODO  the caller of this fuction is build for typing not real input
		switch (e.getKeyCode()) {  // TODO  een switch verwerkt de input niet goed als we meerder knoppen tegelijk
			                       // TODO  \ proberen op te vangen
			case KeyEvent.VK_UP:
				player.setVelocityY(-Player.SPEED);
				break;

			case KeyEvent.VK_DOWN:
				player.setVelocityY(Player.SPEED);
				break;

			case KeyEvent.VK_LEFT:
				player.setVelocityX(-Player.SPEED);
				break;

			case KeyEvent.VK_RIGHT:
				player.setVelocityX(Player.SPEED);
				break;

			case KeyEvent.VK_SPACE: // Fire button
				createBullet(player.getX(), player.getY()); // TODO this should be handeld in a player controller
				break;

			case KeyEvent.VK_ESCAPE:
				System.exit(0);
				break;
		}
	}

	/*
	 * Als er op spatiebalk is gedrukt dan moet een nieuwe bullet aangemaakt worden, aangezien dat teveel code is om in
	 * de vorige method te plakken en we misschien dat ook op andere plaatsen in de code willen doen plaatsen we dit in
	 * een eigen method. We vertellen de method met double x en double y waar de bullet moet starten in het venster en
	 * we laten een geluidje horen.
	 */
	private void createBullet(double x, double y) {  // TODO this should not be here, this is for the player / gun
		bullets.add(new Bullet(
				player.getX()
						+ (player.getSpriteWidth() / 2 - 
								(Bullet.SPRITE_SIZE_WIDTH / 2)),
				player.getY() - Bullet.SPRITE_SIZE_HEIGHT, 
				player.getHitpoints(),
				bounds));
		JukeBox.play("firing");
		//JukeBox.play("firing");
	}

	/*
	 * Als we een toets hebben ingedrukt moeten we die ook weer loslaten Dit loslaten kan gevolgen hebben in onze game.
	 * Wat de gevolgen daarvan zijn programmeren we in de onderstaande method. Deze method wordt aangeroepen op het
	 * moment dat een speler een toets weer loslaat.
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			player.setVelocityY(0.0);
			break;

		case KeyEvent.VK_DOWN:
			player.setVelocityY(0.0);
			break;

		case KeyEvent.VK_LEFT:
			player.setVelocityX(0.0);
			break;

		case KeyEvent.VK_RIGHT:
			player.setVelocityX(0.0);
			break;

		case KeyEvent.VK_SPACE: // Fire button
			break;
		}
	}

	/*
	 * Onderstaande functie moet er staan, want Java verlangt dat als we gebruik willen maken van de keyboard afvang
	 * voorzieningen in de taal Maar we vullen de method niet met opdrachten
	 */
	@Override
	public void keyTyped(KeyEvent e) {
	} // Not used

	
	public static void main(String[] args) {
		/*
		 * Om van alle voorzieningen in onze game gebruik te maken moeten we wel een object van onze Game class
		 * aanmaken. Objecten zijn namelijk re�el, terwijl classes gewoon een beschrijving zijn van een object. Dit is
		 * te vergelijken met je ID-kaart en jijzelf. Je ID-kaart geeft een bepaalde beschrijving van jou, maar de
		 * ID-kaart is niet re�el. Een class is dan te vergelijken met he ID-kaart. Terwijl een object dan te
		 * vergelijken is met jou.
		 */
		Game game = new Game();

		game.setPreferredSize(Game.bounds);
		game.setMaximumSize(Game.bounds);
		game.setMinimumSize(Game.bounds);

		JFrame frame = new JFrame(game.TITLE);
		frame.add(game);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(game);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		game.start();
	}
}
