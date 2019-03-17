import java.awt.*;
import javax.swing.ImageIcon;

// Gems class
public class Gems extends Rectangle{
	
	// Image
	private static final ImageIcon GEM_IMAGE = new ImageIcon("Images\\Gem.png"); 
	
	// Attributes
	private static int nGems;
	
	// Constructor to initialize location of gem
	public Gems(int x, int y){
		super(x, y, 50, 50);
	}
	
	// Render the gems
	public void render(Graphics g){
		g.drawImage(GEM_IMAGE.getImage(), x, y, null);
	}
	
	// Getter for number of gems
	public static int getGems(){
		return nGems;
	}
	
	// Set the amount of gems present
	public static void setGems(int n){
		nGems = n;
	}
}
