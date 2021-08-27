import java.awt.*;
import javax.swing.ImageIcon;

// Portal class
public class Portal extends Rectangle{
	
	// Image
	private static final ImageIcon PORTAL_IMAGE = new ImageIcon("Images//portal.png");
	
	// Constructor to initialize location of portal
	public Portal(int x, int y){
		super(x, y, 100, 100);
	}
	
	// Render image of portal
	public void render(Graphics g){
		g.drawImage(PORTAL_IMAGE.getImage(), x, y, null);
	}
}
