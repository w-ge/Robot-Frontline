import java.awt.*;

// Big bullet class
public class BigBullet extends Bullet{
	
	// Constructor for BigBullet
	public BigBullet(int x, int y, int dir, int speed) {
		super(x, y, dir, speed);
		width = 200;
		height = 20;
	}
	
	// Render big bullet as big green rectangle
	public void render(Graphics g) {
		g.setColor(Color.green);
		g.fillRect(x, y, width, height);
	}
}
