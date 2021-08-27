import java.awt.*;
import javax.swing.ImageIcon;

// Shooter class
public class Enemy2 extends Enemy{
	
	// Images
	private static final ImageIcon SHOOTER_IMAGE_R = new ImageIcon("Images//Gunner_Right.png");
	private static final ImageIcon SHOOTER_IMAGE_L = new ImageIcon("Images//Gunner_Left.png");
	private static final ImageIcon ANIMATE_ENEMY2[] = {SHOOTER_IMAGE_L,SHOOTER_IMAGE_R};
	
	// Constructor to initialize location and other attributes of shooter
	public Enemy2(int x, int y, int health) {
		super(x, y, health);
		type = 2;
	}
	
	// Render shooter enemy
	public void render(Graphics g){
		g.drawImage(ANIMATE_ENEMY2[dir].getImage(),x,y,null);
		
		// Timer for shooting delay
		if(bulDelay > 0) {
			bulDelay--;
		}
	}
	
	// Detection rectangle
	public Rectangle detection(){
		return new Rectangle(x-900,y+25,1800,height-30);
	}
	
	// Method to shoot bullets
	public Bullet fire() {
	
		// Shoot according to direction
		if(dir == 0) {
			return new Bullet((int)(x -width/2),(int)(y+height/2), -1, 10);
		}
		return new Bullet((int)(x+width/2),(int)(y+height/2), 1, 10);
	}
}
