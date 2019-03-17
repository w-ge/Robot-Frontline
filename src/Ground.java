import java.awt.*;
import javax.swing.ImageIcon;

// Ground class
public class Ground extends Rectangle{
	
	// Image
	private static final ImageIcon GROUND_IMAGE = new ImageIcon("Images\\ground.png");
	
	// Attributes
	protected int vel;
	protected boolean vertical;
	protected int dir;
	
	// Constructor to initialize location of ground
	public Ground(int x, int y, int v, boolean vert, int d){
		super(x, y, 50, 50);
		vertical = vert;
		vel = v;
		dir = d;
	}
	
	// Constructor to initialize location of ground and direction it can be jumped on from
	public Ground(int x, int y, int w, int v, boolean vert, int d){
		this(x, y, v, vert, d);
		width = w;
	}
	
	// Render image of ground
	public void render(Graphics g){
		for(int i = 0; i < width/50; i++){
			g.drawImage(GROUND_IMAGE.getImage(), x + i*50, y, null);
		}
	}
	
	// Return bottom left entrance to ground
	public Rectangle getBottomLeftEnter(){
		return new Rectangle(x - 85, y+height, 30, 250);
	}
	
	// Return bottom right entrance to ground
	public Rectangle getBottomRightEnter(){
		return new Rectangle(x + width+55, y+height, 30, 250);
	}
	
	// Move method which will be overridden 
	public void move(){
	}
	
	// Getter for velocity
	public int getVel(){
		return vel;
	}
	
	// Getter for whether or not the block moves vertically
	public boolean getVertical(){
		return vertical;
	}
	
	// Getter for direction
	public int getDir(){
		return dir;
	}
}
