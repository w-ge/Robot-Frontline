import java.awt.*;

// Bullet class
public class Bullet extends Rectangle{
	
	// Attributes
	private int dir;
	protected int speed;
	
	// Constructor to initialize bullet location, direction, and speed
	public Bullet(int x, int y, int dir, int speed){
		super(x, y, 40, 5);
		this.dir = dir;
		this.speed = speed;
	}
	
	// Render bullet as a filled rectangle
	public void render(Graphics g){
		g.fillRect(x, y, width, height);
	}
	
	// Make bullet move
	public void fire(){
		x += dir*speed;
	}
	
	// Getter for speed
	public int getSpeed(){
		return speed;
	}
}
