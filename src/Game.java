/*
 
Final Project
David Lan and Bill Ge
Ms. Strelkovska
ICS4U1
January 19, 2017
*/

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

// GamePanel class
class GamePanel extends JPanel implements KeyListener{
	
	// Attributes
	private static Player player;
	private static ArrayList<Ground> ground = new ArrayList<Ground>();
	private static ArrayList<Bullet> pBullets = new ArrayList<Bullet>();
	private static ArrayList<Bullet> eBullets = new ArrayList<Bullet>();
	private static ArrayList<BigBullet> bigBullets = new ArrayList<BigBullet>();
	private static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private static ArrayList<Gems> gems = new ArrayList<Gems>();
	private static ArrayList<Boost> boosts = new ArrayList<Boost>();
	private static ArrayList<Explosion> explosion = new ArrayList<Explosion>();
	private static ArrayList<Highscore> highScore = new ArrayList<Highscore>();
	private static ArrayList<Spikes> spikes = new ArrayList<Spikes>();
	private static Portal portal;
	private static int level;
	private static int startX, startY;
	private static int portalX, portalY;
	private static int redoes;
	private static boolean endGame;
	private static Ground targetGround; 
	private static ImageIcon background;
	private static boolean cheat;
	
	// Constructor to set preferences for GamePanel
	public GamePanel() {
		super();
		addKeyListener(this);
		setLayout(null);
		
		// Create new game
		level = 0;
		loadLevel();
		redoes = 0;
		endGame = false;
		targetGround = new Ground(-100,-100, 0, false, 0);
		cheat = false;
		setSize(1000,700);
	}
	
	// Render game elements
	public void paintComponent(Graphics g) {
		
		try {
			super.paintComponent(g);
			
			// Render level background
			background = new ImageIcon("Images\\Level" + level + " Background.png");
			g.drawImage(background.getImage(),0,0,null);
			
			// Display level 1, 2, 3, 4, 5, 6 objectives
			if(level < 7){
				
				// Translate screen so player is in middle of screen: collecting gems
				g.translate(-(int)player.getX()+getWidth()/2,-(int)player.getY()+getHeight()/2);
				
				// If the playe objective is completed, render the portal
				if(player.getGems()==Gems.getGems()){
					portal = new Portal(portalX, portalY);
					portal.render(g);
					
					// Load next level if player enters the portal
					if(player.intersects(portal)){
						loadLevel();
					}
				}
				
				// Render gems
				for(int i = 0; i < gems.size(); i++){
					gems.get(i).render(g);
					
					// Remove and collect gem when player and gem intersect
					if(player.intersects(gems.get(i))){
						gems.remove(i);
						player.addGem();
						break;
					}
				}
			}
			
			// Display level 7 objective: kill the boss enemy
			else if(level == 7){
				
				// Adjust screen relative to player
				g.translate(-(int)player.getX()+getWidth()/2,-(int)player.getY()+getHeight()/2);
				
				// If boss is defeated, render portal
				if(enemies.size()==0){
					portal = new Portal(portalX, portalY);
					portal.render(g);
					
					// Store high score if player has a top 5 score
					if(player.intersects(portal)){
						try {
							loadLevel();
							FileInputStream fis = new FileInputStream (new File("Highscore.txt"));
							ObjectInputStream ois = new ObjectInputStream(fis);
							
							// Store original five high scores
							for(int i = 0; i < 5; i++) {
								highScore.add((Highscore) ois.readObject());
							}
							
							// Run through high scores to see if player beat any of them
							for(int i = 0; i < 5; i++) {
								
								// If player beats a high score, their score and name is stored
								if(redoes < highScore.get(i).getScore() || highScore.get(i).getScore()==-1) {
									String newHighScoreName = JOptionPane.showInputDialog(null, "You have a new high score. Please enter your name", 
											"New High Score", JOptionPane.CLOSED_OPTION);
									
									if(newHighScoreName != null){
										
										// Make player name N/A if they don't enter anything
										if(newHighScoreName.equals("")){
											newHighScoreName = "N/A";
										}
										
										// Store score
										highScore.add(i, new Highscore(newHighScoreName,redoes));
									}
									break;
								}
							}
							
							// Store top five scores onto the Highscore file
							FileOutputStream fos = new FileOutputStream(new File("Highscore.txt"));
							ObjectOutputStream oos = new ObjectOutputStream(fos);
							for(int i = 0; i < 5; i++) {
								oos.writeObject(highScore.get(i));
							}
							fis.close();
							fos.close();
							oos.close();
							ois.close();
							
							// Congratulate player if they beat the game
							int end = JOptionPane.showConfirmDialog(null, "You Beat the Game. Return to Main Menu?", "Game Over", JOptionPane.YES_NO_OPTION);
							
							// Return to main menu
							if(end == 0) {
								endGame = true;
							}
							
							// Exit game
							else if(end == 1) {
								System.exit(0);
							}
						}catch(Exception e) {e.printStackTrace();}
					}
				}
			}
								
			// Render player and move player with regards to gravity
			player.render(g);
			player.setInterface((int)player.getX(),(int) player.getY());
			player.fall();
			player.move();
			
			// Shoot bullets
			if(player.getShoot()){
				
				// If time delay isn't in effect, shoot a bullet
				if(player.getBulDelay() == 0) {
					
					// If player doesn't have Boosts, shoot a regular bullet
					if(player.getnBoost()<=0){
						
						// Fire bullets according to player direction and set time delay for firing bullets
						pBullets.add(player.fire());
						player.setBulDelay(15);
					}
					else {
						
						// Fire big bullets according to player direction and set time delay for firing bullets
						bigBullets.add((BigBullet) player.fire());
						player.setBulDelay(15);
						
						// Take away a boost
						player.removeBoost();
					}
				}	
			}
			
			// Render boosts
			for(int i = 0; i < boosts.size(); i++){
				boosts.get(i).render(g);
				if(player.intersects(boosts.get(i))){
					boosts.remove(i);
					player.addBoost();
					break;
				}
			}
			
			// Render and fire player bullets
			for(int i = 0; i < pBullets.size(); i++) {
							
				g.setColor(Color.blue);
				pBullets.get(i).render(g);
				pBullets.get(i).fire();
							
				// Remove bullets if they are off the screen
				if(pBullets.get(i).getX()>player.getX()+getWidth()/2|| pBullets.get(i).getX()<player.getX()-getWidth()/2) {
					pBullets.remove(i);
					break;
				}
				
				// If bullets hit an enemy, remove the bullet and enemy loses one health
				else{
					for(int j = 0; j < enemies.size(); j++) {
						if(enemies.get(j).intersects(pBullets.get(i))){
							pBullets.remove(i);
							enemies.get(j).loseHealth(1);
										
							// If enemy health is less than or equal to zero, create dead enemy for explosion animation and remove enemy
							if(enemies.get(j).getHealth()<=0){
								explosion.add(new Explosion((int)enemies.get(j).getX(),(int)enemies.get(j).getY()-50));
								enemies.remove(j);
							}
							break;
						}
					}
				}	
			}
						
			// Render and fire enemy bullets
			for(int i = 0; i < eBullets.size(); i++){
				g.setColor(Color.red);
				eBullets.get(i).render(g);
				eBullets.get(i).fire();
							
				// If bullets hit player, remove bullet and player loses two health
				if(player.intersects(eBullets.get(i))){
					eBullets.remove(i);
					
					// Player only loses health if they don't use cheats
					if(!cheat){
						player.loseHealth(2);
					}
								
					// Level resets if player health goes to zero
					if(player.getHealth()<=0){
						level--;
						redoes++;
						loadLevel();
					}
					break;
				}
			}
			
			// Render and fire big bullets
			for(int i = 0; i < bigBullets.size(); i++) {
				bigBullets.get(i).render(g);
				bigBullets.get(i).fire();
										
				// Remove bullets if they are off the screen
				if(bigBullets.get(i).getX()>player.getX()+getWidth()/2|| bigBullets.get(i).getX()<player.getX()-getWidth()/2) {
					bigBullets.remove(i);
					break;
				}
				
				// If bullets hit an enemy, remove the bullet and enemy loses ten health
				else{
					for(int j = 0; j < enemies.size(); j++) {
						if(enemies.get(j).intersects(bigBullets.get(i))){
							bigBullets.remove(i);
							enemies.get(j).loseHealth(6);
													
							// If enemy health is less than equal to zero, create dead enemy for explosion animation and remove enemy
							if(enemies.get(j).getHealth()<=0){
								explosion.add(new Explosion((int)enemies.get(j).getX(),(int)enemies.get(j).getY()-50));
								enemies.remove(j);
							}
							break;
						}
					}
				}	
			}
			
			// Render and move ground tiles
			for(int i = 0; i < ground.size(); i++){
				ground.get(i).render(g);
				ground.get(i).move();
				
				// Player stops falling if bottom of player intersects with ground
				if(player.getBottom().intersects(ground.get(i))){
					if(!ground.get(i).getVertical() && ground.get(i).getVel()!=0){
						player.setLocation((int)player.getX()+ground.get(i).getVel()*ground.get(i).getDir(),(int)player.getY());
					}
					player.setLocation((int)player.getX(),(int)(ground.get(i).getY()-player.getHeight()));
					player.setAir(false);
					targetGround = ground.get(i);
				}
				
				// Player stops moving up when top of player intersects with ground
				if(player.getTop().intersects(ground.get(i))){
					player.setLocation((int)player.getX(),(int)(ground.get(i).getY()+ground.get(i).getHeight()));
					player.setVY(0);
				}
				
				// Player stops moving left/right when player left/right intersects with ground
				if(player.getRight().intersects(ground.get(i))){
					player.setLocation((int)(ground.get(i).getX()-player.getWidth()),(int)player.getY());
				}
				if(player.getLeft().intersects(ground.get(i))){
					player.setLocation((int)(ground.get(i).getX()+ground.get(i).getWidth()),(int)player.getY());
				}
				
				// Check for enemy collision with ground and stop enemy accordingly
				for(int j = 0; j < enemies.size(); j++){
					if(enemies.get(j).getType()!=3){
						if(enemies.get(j).getBottom().intersects(ground.get(i))){
							enemies.get(j).setLocation((int)enemies.get(j).getX(),(int)(ground.get(i).getY()-enemies.get(j).getHeight()));
							enemies.get(j).setAir(false);
						}
						if(enemies.get(j).getTop().intersects(ground.get(i))){
							enemies.get(j).setLocation((int)enemies.get(j).getX(),(int)(ground.get(i).getY()+ground.get(i).getHeight()));
							enemies.get(j).setVY(0);
						}
						if(enemies.get(j).getRight().intersects(ground.get(i))){
							enemies.get(j).setLocation((int)(ground.get(i).getX()-enemies.get(j).getWidth()),(int)enemies.get(j).getY());
						}
						if(enemies.get(j).getLeft().intersects(ground.get(i))){
							enemies.get(j).setLocation((int)(ground.get(i).getX()+ground.get(i).getWidth()),(int)enemies.get(j).getY());
						}
					}
				}
				
				// If ground and bullets intersect, remove bullets
				for(int j = 0; j < pBullets.size(); j++) {
					if(ground.get(i).intersects(pBullets.get(j))){
						pBullets.remove(j);
						break;
					}
				}
				for(int j = 0; j < eBullets.size(); j++) {
					if(ground.get(i).intersects(eBullets.get(j))){
						eBullets.remove(j);
						break;
					}
				}
			}
			
			// Load spikes
			for(int i = 0; i < spikes.size(); i++){
				spikes.get(i).render(g);
				
				// If player hits spikes and they aren't cheating, level resets
				if(player.intersects(spikes.get(i)) && !cheat){
					level--;
					redoes++;
					loadLevel();
				}
			}
			
			// Render enemies and make enemies be affected by gravity and have movement
			for(int i = 0; i < enemies.size(); i++){
				enemies.get(i).render(g);
				if(enemies.get(i).getType()!=3){
					enemies.get(i).fall();
				}
				enemies.get(i).move();
				
				// Check if player intersects with enemy detection
				if(player.intersects(enemies.get(i).detection())) {
				
					// If enemy is a rusher, they move closer to player if they are on the same ground as player
					if(enemies.get(i).getType()==1){
						
						// Enemies dies and player loses health if they collide
						if(enemies.get(i).intersects(player)){
							if(!cheat){
								player.loseHealth(14);
							}
							
							// Create explosion
							explosion.add(new Explosion((int)enemies.get(i).getX(), (int)enemies.get(i).getY()-50));
							
							// Rusher dies when they collide with player
							enemies.remove(i);
							
							// Level resets if player health goes to zero
							if(player.getHealth()<=0){
								level--;
								redoes++;
								loadLevel();
							}
							break;
						}
						
						// Set target ground
						for(int j = 0; j < ground.size(); j++){
							if(player.getBottom().intersects(ground.get(j))){
								targetGround = ground.get(j);
								break;
							}
						}
						
						// Enter by jumping from left of ground
						if(enemies.get(i).intersects(targetGround.getBottomLeftEnter())){
							enemies.get(i).setVX(0);
							enemies.get(i).setDir(1);
									
							// Make sure enemy doesn't jump in the air
							for(int j = 0; j < ground.size(); j++){
								if(enemies.get(i).getBottom().intersects(ground.get(j))){
									enemies.get(i).setAir(false);
								}
							}
								
							// Jump from the left side
							if(!enemies.get(i).getInAir()){
								enemies.get(i).setDir(1);
								enemies.get(i).setVX(8);
								enemies.get(i).setVY(-35);
								enemies.get(i).setAir(true);
							}
						}
						
						// Enter by jumping from right of ground
						else if(enemies.get(i).intersects(targetGround.getBottomRightEnter())){
							enemies.get(i).setVX(0);
							enemies.get(i).setDir(0);
									
							// Make sure enemy doesn't jump in the air
							for(int j = 0; j < ground.size(); j++){
								if(enemies.get(i).getBottom().intersects(ground.get(j))){
									enemies.get(i).setAir(false);
								}
							}
									
							// Jump from right side
							if(!enemies.get(i).getInAir()){
								enemies.get(i).setDir(0);
								enemies.get(i).setVX(-8);
								enemies.get(i).setVY(-35);
								enemies.get(i).setAir(true);
							}
						}
						
						// If enemy doesn't intersect bottom entrances
						else{
							// If enemy is below target ground
							if(enemies.get(i).getBottom().getY() > targetGround.getY()){
								
								// Enemy moves relative to player being on left or right and if they have different x-values when they on same ground
								if(enemies.get(i).getBottom().intersects(targetGround)){
									
									if((int)(Math.abs(enemies.get(i).getX()-player.getX()))!=0){
										
										// Enemy moves closer to player on x-axis
										int vel = (int)(enemies.get(i).getX()-player.getX())/(int)(Math.abs(enemies.get(i).getX()-player.getX()))*-4;
										enemies.get(i).setVX(vel);
										// Set direction of enemy relative to player being on right or left of them
										if(vel < 0){
											enemies.get(i).setDir(0);
										}
										else if(vel > 0){
											enemies.get(i).setDir(1);
										}
									}
								}
								
								// Check if enemy can jump on target ground
								else if(!enemies.get(i).intersects(targetGround.getBottomLeftEnter()) || !enemies.get(i).intersects(targetGround.getBottomRightEnter()) ){
									
									// Move enemy closer to bottom left if it is closer to it
									if(Math.abs(targetGround.getBottomLeftEnter().getX()-enemies.get(i).getX()) < Math.abs(targetGround.getBottomRightEnter().getX()-enemies.get(i).getX())){
										if((int)(Math.abs(enemies.get(i).getX()-targetGround.getBottomLeftEnter().getX()))!=0){
											int vel = (int)(enemies.get(i).getX()-targetGround.getBottomLeftEnter().getX())/(int)(Math.abs(enemies.get(i).getX()-targetGround.getBottomLeftEnter().getX()))*-4;
											enemies.get(i).setVX(vel);
											// Set direction of enemy relative to player being on right or left of them
											if(vel < 0){
												enemies.get(i).setDir(0);
											}
											else if(vel > 0){
												enemies.get(i).setDir(1);
											}
											enemies.get(i).setVX(vel);
										}
									}
									
									// Move enemy closer to bottom right if it is closer to it
									else if(Math.abs(targetGround.getBottomLeftEnter().getX()-enemies.get(i).getX()) > Math.abs(targetGround.getBottomRightEnter().getX()-enemies.get(i).getX())){
										if((int)(Math.abs(enemies.get(i).getX()-targetGround.getBottomRightEnter().getX()))!=0){
											int vel = (int)(enemies.get(i).getX()-targetGround.getBottomRightEnter().getX())/(int)(Math.abs(enemies.get(i).getX()-targetGround.getBottomRightEnter().getX()))*-4;
											enemies.get(i).setVX(vel);
											// Set direction of enemy relative to player being on right or left of them
											if(vel < 0){
												enemies.get(i).setDir(0);
											}
											else if(vel > 0){
												enemies.get(i).setDir(1);
											}
											enemies.get(i).setVX(vel);
										}
									}
								}
							}
							
							// If otherwise, they move in same direction
							else{
								if((int)(Math.abs(enemies.get(i).getX()-player.getX()))!=0){
									
									// Move enemy closer to player on x-axis
									int vel = (int)(enemies.get(i).getX()-player.getX())/(int)(Math.abs(enemies.get(i).getX()-player.getX()))*-4;
									
									enemies.get(i).setVX(0);
									if(Math.abs(enemies.get(i).getCenterX()-player.getCenterX()) > 10){
										enemies.get(i).setVX(vel);
									}
									
									// Set direction of enemy relative to player being on right or left of them
									if(vel < 0){
										enemies.get(i).setDir(0);
									}
									else if(vel > 0){
										enemies.get(i).setDir(1);
									}
								}
							}
						}
					}
					
					// If enemy is a shooter, they adjust their direction relative to the player being on their right or left
					else if(enemies.get(i).getType()==2){
						if(player.getX()<enemies.get(i).getX()){
							enemies.get(i).setDir(0);							
						}
						else{
							enemies.get(i).setDir(1);
						}
						
						// Fire bullet and set delay to firing of weapon
						if(enemies.get(i).getBulDelay() == 0){
							// Shoot according to direction
							eBullets.add(((Enemy2) enemies.get(i)).fire());
							enemies.get(i).setBulDelay(40);
						}
					}
					
					// If enemy is a boss monster
					else if(enemies.get(i).getType()==3){
							
						enemies.get(i).setVX(0);
						if((int)(Math.abs(enemies.get(i).getCenterX()-player.getCenterX()))>100){
							if((int)(Math.abs(enemies.get(i).getX()-player.getX()))!=0){
										
								// Move enemy closer to player on x-axis
								int vel = (int)(enemies.get(i).getX()-player.getX())/(int)(Math.abs(enemies.get(i).getX()-player.getX()))*-2;
										
								enemies.get(i).setVX(0);
								enemies.get(i).setVX(vel);
								
								// Set direction of enemy relative to player being on right or left of them
								if(player.getCenterX()<enemies.get(i).getCenterX()){
									enemies.get(i).setDir(0);
								}
								else{
									enemies.get(i).setDir(1);
								}
							}
						}
						// Fire bullet and set delay to firing of weapon if player is in firing range
						if(enemies.get(i).getBulDelay() == 0 && player.intersects(((BossEnemy) enemies.get(i)).shootingRange())){
							eBullets.add(((BossEnemy) enemies.get(i)).fire());
							enemies.get(i).setBulDelay(30);
						}
								
						// If player is in detection range, move enemy closer to player on y-axis
						if(enemies.get(i).getCenterY()==player.getCenterY()){
							enemies.get(i).setVY(0);
						}
						else {
							enemies.get(i).setVY(0);
							if((int)(Math.abs(enemies.get(i).getY()+25-player.getY())) != 0){
								enemies.get(i).setVY((int)(enemies.get(i).getY()+25-player.getY())/(int)(Math.abs(enemies.get(i).getY()+25-player.getY()))*-2);
							}
						}
					}
				}
				
				// Enemy is stationary if it doesn't detect player
				else{
					enemies.get(i).setVY(0);
					enemies.get(i).setVX(0);
				}
			}
			
			// Create explosion animation for every dead enemy
			for(int i = 0; i < explosion.size(); i++) {
				explosion.get(i).explode(g,explosion.get(i).getX(),explosion.get(i).getY(), 100, explosion.get(i).getFrame());
				
				// Change the frame if the timer reaches 0
				if(explosion.get(i).getAnimeCount() == 0) {
					explosion.get(i).setFrame();
				}
				explosion.get(i).resetCount();	
				
				// Remove the explosion after a certain time
				if(explosion.get(i).getFrame() == 20) {
					explosion.remove(i);
				}
			}
			
			// Re-orientate the coordinate grid to render the interface
			g.translate((int)(player.getInterX()-getWidth()/2), (int)(player.getInterY()-getHeight()/2));
			
			// Health bar
			g.setColor(Color.green);
			g.fillRect(50, 50, player.getHealth()*5, 50);
			
			// Text
			if(level <= 2 || level == 4){
				g.setColor(Color.black);
			}
			else{
				g.setColor(Color.white);
			}
			g.drawString("Health", 50, 45);
			g.drawRect(50, 50, 200, 50);
			if(level < 7){
				g.drawString("Collect Gems: " + player.getGems() + "/" + Gems.getGems(), 50, 120);
				
				// Notify that portal opens if player completes task
				if(Gems.getGems()==player.getGems()){
					g.drawString("A portal has opened!", 50, 180);
				}
			}
			
			// Display level 7 objective: kill boss enemy
			else if(level == 7){
				g.drawString("Kill The Boss Monster", 50, 120);
				
				// Boss health
				g.setColor(Color.red);
				if(enemies.size()>0){
					g.fillRect(700, 50, enemies.get(0).getHealth()*4, 50);
				}
				
				// Display information for player
				else if(enemies.size()==0){
					g.setColor(Color.white);
					g.drawString("A portal has opened!", 50, 200);
				}
				g.setColor(Color.white);
				g.drawRect(700, 50, 200, 50);
				g.drawString("Number of Boosts: " + player.getnBoost(), 50, 180);
				g.drawString("Boss Health", 700, 45);
			}
				
			// Display amount of redoes used
			g.drawString("Total Redoes: " + redoes, 50, 140);
			
			// Display if cheat is being used
			if(cheat){
				g.drawString("Cheating turned on", 50, 160);
			}
			
			// Set focus
			setFocusable(true);
			requestFocusInWindow();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// Key pressed actions
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		// Display instructions for player when player presses "i"
		if(key == KeyEvent.VK_I){
			int instructions = JOptionPane.showConfirmDialog(null, "Use WASD to move. Press space to shoot. \n" + 
					"Complete the objective on the top left corner and enter portals to move onto the next level. If you die, the level resets. \n" +
					"You can cheat by pressing \"C\" in the game or \"N\" to go to the next level.", 
					"How to Play", JOptionPane.CLOSED_OPTION);
		}
		
		// Fire bullets if player presses space
		if(key == KeyEvent.VK_SPACE){
			player.setShoot(true);
		}
		
		// Cheat
		if(key == KeyEvent.VK_C){
			cheat = !cheat;
		}
		
		// Move player based on the key pressed and set new target ground
		if(key == KeyEvent.VK_A){	
			player.setVX(-5);
			player.setDir(1);
			for(int j = 0; j < ground.size(); j++){
				if(ground.get(j).intersects(player.getBottom())){
					targetGround = ground.get(j);
				}
			}
		}
		if(key == KeyEvent.VK_D){
			player.setVX(5);
			player.setDir(0);
			for(int j = 0; j < ground.size(); j++){
				if(ground.get(j).intersects(player.getBottom())){
					targetGround = ground.get(j);
				}
			}
		}
		if(key == KeyEvent.VK_W){
			if(!player.getInAir()){
				player.setVY(-35);
				player.setAir(true);
			}
			for(int j = 0; j < ground.size(); j++){
				if(ground.get(j).intersects(player.getBottom())){
					targetGround = ground.get(j);
				}
			}
		}
		
		// For demonstration purposes only, load next level
		if(key == KeyEvent.VK_N){
			if(level < 7){
				loadLevel();
			}
		}
	}
	
	// Key released actions
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		// Stop player from shooting
		if(key == KeyEvent.VK_SPACE){
			player.setShoot(false);
		} 
		
		// Stop player from moving if left/right movement keys are released
		if(key == KeyEvent.VK_A){
			player.setVX(0);
			player.setFrame(0);
		}
		if(key == KeyEvent.VK_D){
			player.setVX(0);
			player.setFrame(0);
		}
		
		// Have the player jump lower if they release the jump key early
		if(key == KeyEvent.VK_W){
			player.setVY((int)(player.getVY()/1.5));
		}
	}
	public void keyTyped(KeyEvent e) {}
	
	// Method to load next level
	public void loadLevel(){
		try {
			
			// Variables
			int i = -1;
			int left = 0;
			int x = 0, y = 0;
			String row[];
			String str;
			
			// Remove all game objects
			enemies.clear();
			ground.clear();
			pBullets.clear();
			eBullets.clear();
			gems.clear();
			explosion.clear();
			spikes.clear();
			boosts.clear();
			
			// Move onto next level
			level++;
			
			// Read level file
			BufferedReader br = new BufferedReader(new FileReader(new File("Maps\\Level"+level)));
			while((str = br.readLine() ) != null){
				i++;
				row = str.split(" ");
				
				// Loop through read array
				for(int j = 0; j < row.length; j++) {
					
					// Create a ground instance if file reads "l" in specified x and y to the next "r" and set width to difference in distance
					if(row[j].equals("l")){
						x = j*50;
						y = i*50;
						left = j;
						while(!row[j].equals("r")){
							j++;
						}
						ground.add(new Ground(x, y, (j - left + 1)*50, 0, false, 0));
					}
					
					// Create ground that is one block wide if file reads "1" in specified x and y
					else if(row[j].charAt(0) == '1') {
						ground.add(new Ground(j*50,i*50, 0, false, 0));
					}
					// Create a gem instance if file reads "2" in specified x and y
					else if(row[j].charAt(0) == '2') {
						gems.add(new Gems(j*50,i*50));
					}
					// Create a rusher instance if file reads "3" in specified x and y
					else if(row[j].charAt(0) == '3') {
						enemies.add(new Enemy(j*50,i*50,2));
					}
					// Store location of portal if file reads "4" in specified x and y
					else if(row[j].charAt(0) == '4') {
						portalX = j*50;
						portalY = i*50;
					}
					// Store starting location and create player instance in specified x and y if file reads "5"
					else if(row[j].charAt(0) == '5'){
						startX = j*50;
						startY = i*50;
						player = new Player(startX, startY, 50, 80,40);
					}
					// Create shooter instance if file reads "6" in specified x and y
					else if(row[j].charAt(0) == '6'){
						enemies.add(new Enemy2(j*50,i*50,1));
					}
					// Create boss monster instance if file reads "7" in specified x and y
					else if(row[j].charAt(0) == '7'){
						enemies.add(new BossEnemy(j*50,i*50,50));
					}
					// Create boost instance if file reads "8" in specified x and y
					else if(row[j].charAt(0) == '8'){
						boosts.add(new Boost(j*50,i*50));
					}
					// Create spike instance if file reads "9" in specified x and y
					else if(row[j].charAt(0) == '9'){
						spikes.add(new Spikes(j*50,i*50));
					}
					
					// Create moving block if the length is greater than 1
					else if(row[j].length() > 1){
						// First letter indicates direction, second number indicates distance it travels
						if(row[j].charAt(0) == 'r'){
							ground.add(new MovingBlock(j*50, i*50, 3, false, 1, Integer.parseInt(row[j].substring(1))*50));
						}
						else if(row[j].charAt(0) == 'l'){
							ground.add(new MovingBlock(j*50, i*50, 3, false, -1, Integer.parseInt(row[j].substring(1))*50));
						}
						else if(row[j].charAt(0) == 'u'){
							ground.add(new MovingBlock(j*50, i*50, 3, true, 1, Integer.parseInt(row[j].substring(1))*50));
						}
						else if(row[j].charAt(0) == 'd'){
							ground.add(new MovingBlock(j*50, i*50, 3, true, -1, Integer.parseInt(row[j].substring(1))*50));
						}
					}
				}
			}
			// Set amount of gems
			Gems.setGems(gems.size());
		}
		catch(Exception e) {}
	}
	
	// Getter for end game
	public boolean getEndGame(){
		return endGame;
	}
}

// Game class
public class Game extends JFrame implements ActionListener{	
	
	// Attributes
	private JButton b, b2, b3;
	private Container c;
	private CardLayout cardL;
	private static GamePanel gp;
	private JLabel title;
	private JPanel p;
	private static Timer myTimer;
	private static TimerTask myTimerTask;
	private static final int FPS = 60;
	private static final int TIME = 1000/FPS;
	
	// Set preferences for game
	public Game() {
		super("Robot Frontline");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create start menu
		cardL = new CardLayout();
		c = getContentPane();
		c.setLayout(cardL);
		p = new JPanel();
		p.addKeyListener(gp);
		p.setLayout(null);
		addKeyListener(gp);
		
		// Add title
		title = new JLabel("Robot Frontline");
		title.setBounds(370, 50, 300, 100);
		title.setFont(new Font("Serif", Font.BOLD, 40));
		p.add(title, BorderLayout.CENTER);
		
		// Add play button
		b = new JButton("Play");
		b.addKeyListener(gp);
		b.setBounds(380, 250, 250, 100);
		p.add(b, BorderLayout.CENTER);
		b.addActionListener(this);
		
		// Add instructions button
		b2 = new JButton("Instructions");
		b2.setBounds(380, 350, 250, 100);
		p.add(b2, BorderLayout.CENTER);
		b2.addActionListener(this);
		
		// Add high scores button
		b3 = new JButton("High Scores");
		b3.setBounds(380, 450, 250, 100);
		p.add(b3, BorderLayout.CENTER);
		b3.addActionListener(this);
		
		// Add menu and game to container
		c.add(p, "Menu");
		cardL.show(c,"Menu");
		setSize(1000, 700);
		setVisible(true);
	}
	
	// Main method
	public static void main(String args[]) {
		
		// Create instance of game
		Game game = new Game();
	}
	public void actionPerformed(ActionEvent e) {
		
		// Load game if player clicks play
		if(e.getSource() == b) {
			gp = new GamePanel();
			c.add(gp, "Game");
			cardL.show(c, "Game");
			
			// Game Loop
			myTimer= new java.util.Timer();
			myTimerTask = new TimerTask(){
				public void run(){
					gp.repaint();
					
					// Stop the game loop if the player wins the game
					if(gp.getEndGame()){
						myTimerTask.cancel();
						myTimer.purge();
						cardL.show(c,"Menu");
					}
				}
			};
		
			myTimer.scheduleAtFixedRate(myTimerTask,0,TIME);
		}
		
		// Show instructions if player clicks for instructions
		if(e.getSource() == b2){
			int instructions = JOptionPane.showConfirmDialog(null, "Use WASD to move. Press space to shoot. \n" + 
					"Complete the objective on the top left corner and enter portals to move onto the next level. If you die, the level resets. \n" +
					"You can cheat by pressing \"C\" in the game or \"N\" to go to the next level.", 
					"How to Play", JOptionPane.CLOSED_OPTION);
		}
		
		// Display top scores and players if player clicks for top scores
		if(e.getSource() == b3) {
			try {
				
				// Read scores from file
				FileInputStream fis = new FileInputStream(new File("Highscore.txt"));
				ObjectInputStream ois = new ObjectInputStream(fis);
				Highscore hs;
				
				// Format
				String scores = String.format("%6s%16s\n", "Name", "Redoes");
				for(int i = 0; i < 5; i++) {
					String line;
					hs = ((Highscore) ois.readObject());
					
					// Display blanks if score is -1
					if(hs.getScore()==-1) {
						line = String.format((i+1)+". %s%18s\n", "-", "-");
					}
					
					// Display scores and names
					else{
						
						int len = hs.getName().length();
						if(len > 6){
							len = 6;
						}
						
						// Cut off long name and display information
						line = String.format((i+1)+". %-"+(25-len)+"."+len+"s%d\n", hs.getName(), hs.getScore());
						
					}
					scores += line;
				}
				
				// Display scores
				int topScores = JOptionPane.showConfirmDialog(null, scores, "High Scores", JOptionPane.CLOSED_OPTION);
				fis.close();
				ois.close();
			}catch(Exception e1) {e1.printStackTrace();}
		}
	}
}
