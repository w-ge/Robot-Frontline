import java.awt.*;
import javax.swing.ImageIcon;

// Player class
public class Player extends Rectangle{
	
	// Images
	private static final ImageIcon PLAYER_IMAGE_RIGHT= new ImageIcon("Images//Player_Right.png");
	private static final ImageIcon PLAYER_IMAGE_RIGHT2= new ImageIcon("Images//Player_Right2.png");
	private static final ImageIcon PLAYER_IMAGE_RIGHT3= new ImageIcon("Images//Player_Right3.png");
	private static final ImageIcon PLAYER_IMAGE_RIGHT4= new ImageIcon("Images//Player_Right4.png");
	private static final ImageIcon PLAYER_IMAGE_RIGHT5= new ImageIcon("Images//Player_Right5.png");
	private static final ImageIcon PLAYER_IMAGE_RIGHT6= new ImageIcon("Images//Player_Right6.png");
	private static final ImageIcon PLAYER_IMAGE_RIGHT7= new ImageIcon("Images//Player_Right7.png");
	private static final ImageIcon PLAYER_IMAGE_LEFT= new ImageIcon("Images//Player_Left.png");
	private static final ImageIcon PLAYER_IMAGE_LEFT2= new ImageIcon("Images//Player_Left2.png");
	private static final ImageIcon PLAYER_IMAGE_LEFT3= new ImageIcon("Images//Player_Left3.png");
	private static final ImageIcon PLAYER_IMAGE_LEFT4= new ImageIcon("Images//Player_Left4.png");
	private static final ImageIcon PLAYER_IMAGE_LEFT5= new ImageIcon("Images//Player_Left5.png");
	private static final ImageIcon PLAYER_IMAGE_LEFT6= new ImageIcon("Images//Player_Left6.png");
	private static final ImageIcon PLAYER_IMAGE_LEFT7= new ImageIcon("Images//Player_Left7.png");
	private static final ImageIcon PLAYER_IMAGE_JUMPR= new ImageIcon("Images//Player_JumpR.png");
	private static final ImageIcon PLAYER_IMAGE_JUMPL= new ImageIcon("Images//Player_JumpL.png");
	private static final ImageIcon ANIMATE_PLAYER[][] = {{PLAYER_IMAGE_RIGHT,PLAYER_IMAGE_RIGHT2,PLAYER_IMAGE_RIGHT3,PLAYER_IMAGE_RIGHT4,PLAYER_IMAGE_RIGHT,PLAYER_IMAGE_RIGHT5,PLAYER_IMAGE_RIGHT6,PLAYER_IMAGE_RIGHT7},
									  {PLAYER_IMAGE_LEFT,PLAYER_IMAGE_LEFT2,PLAYER_IMAGE_LEFT3,PLAYER_IMAGE_LEFT4,PLAYER_IMAGE_LEFT,PLAYER_IMAGE_LEFT5,PLAYER_IMAGE_LEFT6,PLAYER_IMAGE_LEFT7},
									  {PLAYER_IMAGE_JUMPR},{PLAYER_IMAGE_JUMPL}};
	
	// Attributes
	protected int velX;
	protected int velY;
	protected int dir;
	protected int health;
	private boolean inAir, shoot;
	protected int bulDelay;
	private int nGems;
	private int nBoost;
	private int interfaceX;
	private int interfaceY;
	private int frame, animeCount;
		
	// Constructor to initialize location, size, and other attributes of player
	public Player(int x, int y, int w, int h, int health){
		super(x, y, w, h);
		inAir = true;
		shoot =  false;
		dir = 0;
		setLocation(x, y);
		this.health = health;
		nGems = 0;
		frame = 0;
		animeCount = 0;
		nBoost = 0;
		bulDelay = 0;
		interfaceX = x;
		interfaceY = y;
	}
		
	// Render image of player
	public void render(Graphics g){
	
		// Display the player falling if he is in the air
		if(inAir) {
			g.drawImage(ANIMATE_PLAYER[dir+2][0].getImage(), x, y, null);
		}
		
		// Animate walking
		else {
			if(animeCount++ >= 4 && Math.abs(velX) == 5) {
				frame = (frame + 1)%8;
				animeCount = 0;
			}
			g.drawImage(ANIMATE_PLAYER[dir][frame].getImage(), x, y, null);
		}
			
		// Add time delay to bullet firing
		if(bulDelay > 0) {
			bulDelay--;
		}
	}
	
	// Getter and setters for interface
	public void setInterface(int x, int y){
		interfaceX = x;
		interfaceY = y;
	}
	public int getInterX(){
		return interfaceX;
	}
	public int getInterY(){
		return interfaceY;
	}
	
	// Collision rectangles for player
	public Rectangle getRight() {
		return new Rectangle(x+width-2,y,2,height-10);
	}
	public Rectangle getLeft() {
		return new Rectangle(x,y,2,height-10);
	}
	public Rectangle getBottom() {
		return new Rectangle(x+15,y+height-4,width-30,4);
	}
	public Rectangle getTop() {
		return new Rectangle(x+15,y,width-30,4);
	}
		
	// Method for applying gravity to the player
	public void fall(){
		y+=10;
	}
		
	// Update player location
	public void move() {
		x += velX;
		if(inAir && velY < 0){
			velY++;
		}
		y += velY;
		inAir = true;
	}
		
	// Reset player location and health
	public void reset(int x, int y, int h){
		this.x = x;
		this.y = y;
		health = h;
	}
		
	// Getter for number of Boost
	public int getnBoost(){
		return nBoost;
	}
		
	// Add one to Boost count
	public void addBoost(){
		nBoost++;
	}
		
	// Remove one from Boost count
	public void removeBoost(){
		nBoost--;
	}
	
	// Shoot bullets
	public Bullet fire(){
	
		// Fire normal bullets
		if(nBoost == 0){
			
			// Fire bullets according to player direction
			if(dir==0){
				return new Bullet((int)(x+width),y+30,1,30);
			}
			return new Bullet((int)(x-width),y+30,-1,30);
		}
		
		// Fire big bullets
		else{
			
			// Fire big bullets according to player direction
			if(dir==0){
				return new BigBullet((int)(x+width),y+20,1,30);
			}
			return new BigBullet((int)(x-4*width),y+20, -1,30);
		}
	}
		
	// Getter for x-velocity
	public int getVX() {
		return velX;
	}
		
	// Setter for x-velocity
	public void setVX(int velX) {
		this.velX = velX;
	}
		
	// Getter for y-velocity
	public int getVY() {
		return velY;
	}
		
	// Setter for y-velocity
	public void setVY(int velY) {
		this.velY = velY;
	}
		
	// Set if player is in the air or not
	public void setAir(boolean b){
		inAir = b;
	}
		
	// Getter for inAir
	public boolean getInAir(){
		return inAir;
	}
		
	// Set the bullet firing delay
	public void setBulDelay(int n) {
		bulDelay = n;
	}
	
	// Getter for bullet firing delay
	public int getBulDelay() {
		return bulDelay;
	}
			
	// Set direction of player
	public void setDir(int i) {
		dir = i;
	}
			
	// Get direction of player
	public int getDir() {
		return dir;
	}
			
	// Getter for health
	public int getHealth(){
		return health;
	}
		
	// Setter for player health
	public void setHealth(int i) {
		health = i;
	}
		
	// Make player lose health
	public void loseHealth(int l){
		health -= l;
	}
		
	// Getter for amount of collected gems
	public int getGems(){
		return nGems;
	}
		
	// Add a gem to the amount of gems obtained
	public void addGem(){
		nGems++;
	}
		
	// Setter for frame
	public void setFrame(int i) {
		frame = i;
	}
	
	// Getter for shoot
	public boolean getShoot(){
		return shoot;
	}
	
	// Setter for shoot
	public void setShoot(boolean shoot){
		this.shoot = shoot;
	}
}
