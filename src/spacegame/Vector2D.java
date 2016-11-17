package spacegame;

public class Vector2D {  // Nice
	public float x;
	public float y;
	
	public Vector2D(float x, float y) {
		setVector2D(x,y);
	}
	
	public void setVector2D(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setVector2D(int x, int y) {
		this.x = (float) x;
		this.y = (float) y;
	}
}
