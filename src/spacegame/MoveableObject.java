package spacegame;

import java.awt.*;
import java.util.ArrayList;

public abstract class MoveableObject {    // TODO add Mobi
	protected double x;
	protected double y;  // TODO if we have a Vector2D why are we not using it
	protected SpriteSheet objectSprites;
	protected int currentSprite = 0;
	protected double animationTick = 0.1;
	protected double animationTimer = 0.0;
	protected Dimension bounds;
	protected double velocityX = 0;
	protected double velocityY = 0;
	protected double health;
	protected double maxHealth;
	protected double hitPoints = 0;
	
	public MoveableObject(double x, double y, Dimension bounds) {
		this.x = x;
		this.y = y;
		this.bounds = bounds;
	}
	
	public abstract void update();
	public abstract void render(Graphics g);
	public abstract boolean hasCollided(MoveableObject withObject);  // TODO this should be not be set abstract here
	public abstract boolean hasCollided(ArrayList<MoveableObject> withObjects); // TODO ^^


	public void gotHit( double hitpoints ) {
		this.health -= hitpoints;
	}
	public double getHealth() {
		return health;
	}
	public double getMaxHealth() {
		return maxHealth;
	}
	public void setHealth( double hp ) {
		this.health = hp;
		this.maxHealth = hp;
	}
	public void setHealth( int hp ) {
		this.health = (double) hp;
		this.maxHealth = (double) hp;
	}
	public double getHitpoints() {
		return this.hitPoints;
	}


	public double getX() {
		return x;
	}
	public void setX(int x) {
		this.x = (double) x;
	}
	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}
	public void setY(int y) {
		this.y = (double) y;
	}
	public void setY(double y) {
		this.y = y;
	}


	public double getMaxX() {
		return (x + objectSprites.getSpriteWidth());
	}
	public double getMaxY() {
		return (y + objectSprites.getSpriteHeight());
	}


	// TODO Velocity is part of physics not movement
	public double getVelocityX() {
		return velocityX;
	}
	public void setVelocityX(double velocityX) { this.velocityX = velocityX; }

	public double getVelocityY() {
		return velocityY;
	}
	public void setVelocityY(double velocityY) {
		this.velocityY = velocityY;
	}
	
	public void setVelocity(double veloX, double veloY) {
		this.velocityX = veloX;
		this.velocityY = veloY;
	}


	public int getSpriteWidth() {
		return objectSprites.getSpriteWidth();
	}
	public int getSpriteHeight() {
		return objectSprites.getSpriteHeight();
	}
}
