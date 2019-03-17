import java.io.*;

// Class to reset high scores
public class ResetScores {
	public static void main(String args[]) throws Throwable {
		
		// Write into file
		FileOutputStream fos = new FileOutputStream(new File("Highscore.txt"));
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		
		// Reset high scores
		for(int i = 0; i < 5; i++) {
			oos.writeObject(new Highscore(" ",-1));
		}
		oos.close();
		fos.close();
	}
}
