import java.awt.*;

// Class for explosion
public class Explosion {
	
	// Attributes
	private int x, y;
	private int frame;
	private int animeCount;
	
	// Constructor to initialize location, frame, and animation count
	public Explosion(int x, int y) {
		this.x = x;
		this.y = y;
		frame = 0;
		animeCount = 0;
	}
	
	// Method to recursively draw spiral explosion
	public void explode(Graphics g, int x, int y, int width, int frame) {
		
		// Base Case for if width is less than 10
		if(width < 10) {
			return;
		}
		
		// Draw red spiral and call explode spiral with smaller size
		else {
			
			// Draw an arc with an angle of 180 or less depending on the frame
			g.setColor(Color.red);
			g.drawArc(x, y, width, width, 180, -1*(Math.min(3, frame)*60));
			
			// Draw a second smaller half-circle if the first half-circle is completed
			if(Math.min(3, frame)*60 == 180){
				frame -= 3;
				g.drawArc(x+20, y+10, width-20, width-20, 360, -1*(Math.min(3, frame)*60));
				
				// If the second half-circle is completed, run explode with a smaller size
				if(frame*60>=180){
					explode(g, x + 20, y+20, width - 40, frame - 3);
				}
			}
		}
	}
	
	// Reset animation count if it is equal to four
	public void resetCount() {
		if(animeCount++ == 4) {
			animeCount = 0;
		}
	} 
	
	// Setter for frame
	public void setFrame() {
		frame++;
	}
	
	// Getter for frame
	public int getFrame() {
		return frame;
	}
	
	// Getter for x
	public int getX() {
		return x;
	}
	
	// Getter for y
	public int getY() {
		return y;
	}
	
	// Getter for animation count
	public int getAnimeCount() {
		return animeCount;
	}
}
