import java.io.Serializable;

// High score class
public class Highscore implements Serializable{
	
	// Attributes
	private String name;
	private int score;
	
	// Constructor to initialize high score and name
	public Highscore(String name, int score) {
		this.name = name;
		this.score = score;
	}
	
	// Getter for score
	public int getScore() {
		return score;
	}
	
	// Getter for name
	public String getName() {
		return name;
	}
}
