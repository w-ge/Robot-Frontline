import java.awt.*;
import javax.swing.ImageIcon;
// Spike class
public class Spikes extends Rectangle{
	
	// Image
	private static final ImageIcon SPIKE_IMAGE = new ImageIcon("Images//spikes.png");
	
	// Constructor to initialize location of portal
	public Spikes(int x, int y){
		super(x, y, 50, 50);
	}
	
	// Render image of portal
	public void render(Graphics g){
		g.drawImage(SPIKE_IMAGE.getImage(), x, y, null);
	}
}
