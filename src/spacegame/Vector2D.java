package spacegame;

public class Vector2D {  // Nice
	public double x;
	public double y;
	
	public Vector2D(double x, double y) {
		setVector2D(x,y);
	}
	
	public void setVector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setVector2D(int x, int y) {
		this.x = (double) x;
		this.y = (double) y;
	}
}
