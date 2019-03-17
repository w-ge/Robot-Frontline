// Moving block class
public class MovingBlock extends Ground{
	
	// Attributes
	private final int DISTANCE;
	private int startX, startY;
	private int startDir;
	
	// Constructor to initialize moving block direction and speed
	public MovingBlock(int x, int y, int v, boolean vert, int d, int dis){
		super(x, y, 150, v, vert, d);
		startX = x;
		startY = y;
		startDir = d;
		DISTANCE = dis;
		setLocation(x, y);
	}
	
	// Move the moving block
	public void move(){
	
		// If the block moves vertically
		if(vertical){
		
			// Move on y-axis
			y += vel*dir;
			
			// If it is a down block
			if(startDir == 1){
				
				// Reverse direction if it reaches the bottom limit
				if(y<startY-DISTANCE){
					dir = -dir;
				}
				
				// Reverse direction if it reaches the original position
				else if(y>startY){
					dir = -dir;
				}
			}
			
			else{
				// Reverse direction if it reaches the top limit
				if(y>startY+DISTANCE){
					dir = -dir;
				}
				// Reverse direction if it reaches the original position
				else if(y<startY){
					dir = -dir;
				}
			}
		}
		
		// If the block moves horizontally
		else{
		
			// Move on x-axis
			x += vel*dir;
			
			// If it is the block moving left
			if(startDir == -1){
				
				// If it reaches the left limit, reverse direction
				if(x<startX-DISTANCE){
					dir = -dir;
				}
				
				// Block reverses direction if it reaches original position
				else if(x>startX){
					dir = -dir;
				}
			}
			
			// Right moving block
			else{
				
				// If reaches right limit, reverse direction
				if(x>startX+DISTANCE){
					dir = -dir;
				}
				
				// If it reaches original position, reverse direction
				else if(x<startX){
					dir = -dir;
				}
			}
		}
		setLocation(x, y);
	}
}
