import java.awt.*;
import javax.swing.ImageIcon;

// Boost class
public class Boost extends Rectangle{
	
	// Image for the boost power-up
	private static final ImageIcon BOOST_IMAGE= new ImageIcon("Images\\boost.png");
	
	// Constructor for Boost
	public Boost(int x, int y) {
		super(x,y,50,50);
	}
	
	// Render boost
	public void render(Graphics g) {
		g.drawImage(BOOST_IMAGE.getImage(), x, y, null);
	}
}
