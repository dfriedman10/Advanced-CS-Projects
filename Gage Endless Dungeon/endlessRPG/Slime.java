package endlessRPG;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Slime extends Mob {
	
	// Initialize image arrays for all animations
	private Image[] idle = new Image[4];
	private Image[] attack = new Image[9];
	private Image[] die = new Image[2];
	
	// Initialize ints for attack height and width
	private int attackHeight = 300, attackWidth = 300;
	
	// Declare an int to represent the floor
	private int floor;
	
	// Initialize an int to represent what step of the attack the monster is on
	private int attackStep = 1;
	
	// Initialize a boolean to tell if the attack is launching
	private boolean attackSecondPhase = false;
	
	// Declare int for attack damage
	private int attackDamage = 11;
	
	// Declare int for monster destination
	private int destinationX;
	
	Slime(int monsterX, int delay, int destinationX, int HEIGHT, int monsterHomeX, int targetLevel, boolean possibleBoss, int playerLevel) {
		
		// Initialize all images
		try {
			for(int i = 0; i < idle.length; i++) {
				idle[i] = ImageIO.read(new File("rpgImages/slimeIdle" + (i+1) + ".png"));
			}
			for(int i = 0; i < attack.length; i++) {
				attack[i] = ImageIO.read(new File("rpgImages/slimeAttack" + (i+1) + ".png"));
			}
			for(int i = 0; i < die.length; i++) {
				die[i] = ImageIO.read(new File("rpgImages/slimeDie" + (i+1) + ".png"));
			}
			
			setImg(idle[0]);
			
		}
		catch (IOException e) {
			e.printStackTrace();
        }
		
		// Set up monster variables with starting values
		setIdleSize();

		floor = HEIGHT;
		this.destinationX = destinationX;
		
		setX(monsterX);
		setY(floor - getHeight());
		
		setLevel((int)(Math.random() * 4 + targetLevel - 2));
		
		if(getLevel() < 1) setLevel(1);
		
		if(possibleBoss && Math.random() > 0.6) {
			setLevel((int)(getLevel()*2));
			setBoss(true);
		}
		
		setStartX(monsterHomeX);
		setStartY(getY());
		
		setDelay(delay);
		
		resetIndex();
		
		setTotalHealth((int)((30 * Math.random() + 60) * getLevel())*100);
		setCurrentHealth(getTotalHealth());
		
		setXP(getLevel()*18);
		
		if(getLevel() < playerLevel) {
			for(int i = getLevel(); i < playerLevel; i++) {
				setXP(getXP() * 4/5);
			}
		} else if(getLevel() > playerLevel) {
			for(int i = playerLevel; i < getLevel(); i++) {
				setXP(getXP() * 5/4);
			}
		}
	}
	
	// Monster idles
	@Override
	public void idle() {
		resetLoc();
		
		if(getIndex() >= idle.length) {
			resetIndex();
		}
		
		setImg(idle[getIndex()]);
		
		setIdleSize();
		
		setCount(getCount() + 1);
		
		if(getCount() > getDelay()) {
			setIndex(getIndex() + 1);
			setCount(0);
		}	
		
	}
	
	// Monster attacks and returns true when the attack is complete
	@Override
	public boolean attack1() {
		
		// Monster goes through its attacking motion
		if(attackStep == 1) {
			setImg(attack[getIndex()]);
			
			setAttackSize();
			
			moveX(-5);

			
			setCount(getCount() + 1);
			
			if(getCount() > getDelay()) {
				setIndex(getIndex() + 1);
				setCount(0);
			}
			
			// If the motion is almost complete, initialize the projectile and go to next step
			if(getIndex() >= attack.length - 4) {
				attackStep = 2;
			} 
			return false;
			
		} 
		// Finishes the attack motion
		else if(attackStep == 2) {
			
			setImg(attack[getIndex()]);
			
			setCount(getCount() + 1);
			
			if(getIndex() == 5) moveY(-10);
			if(getIndex() == 7) moveY(10);
			
			moveX(-10);
			
			if(getCount() > getDelay()) {
				setIndex(getIndex() + 1);
				setCount(0);
			}
			
			if(getIndex() >= attack.length ) {
				attackStep = 1;
				return true;
			}
			return false;
			
		} 
		
		return false;
	}
	
	// Monster dies then returns true when action is complete
	@Override
	public boolean die() {
		
		if(getIndex() >= die.length) {
			resetIndex();
		}
		
		setImg(die[getIndex()]);
		
		setDieSize();
		
		setCount(getCount() + 1);
		
		if(getCount() > getDelay()) {
			setIndex(getIndex() + 1);
			setCount(0);
		}	
		
		if(getIndex() >= die.length) return true;
		
		return false;
		
	}
	
	// Sets monster size to idle size
	private void setIdleSize() {
		setWidth(300);
		setHeight(300);
		
		setY(getStartY());
	}
	
	// Sets monster size to die size
	private void setDieSize() {
		setWidth(300);
		setHeight(320);
		
		setY(getStartY() + 20);

	}
	
	// Sets monster size to attack size
	private void setAttackSize() {
		setHeight(attackHeight);		
		setWidth(attackWidth);
		
		setY(floor - attackHeight);
	}
	
	// Draws monster
	@Override
	public void draw(Graphics g) {
		super.draw(g);
	}
	
	// Returns attack damage
	@Override
	public int getAttack1Damage() {
		return attackDamage * (int) Math.pow(getLevel(), 6.0/5);
	}

}


