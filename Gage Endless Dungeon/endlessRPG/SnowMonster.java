package endlessRPG;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SnowMonster extends Mob {
	
	// Initialize image arrays for all animations
	private Image[] idle = new Image[6];
	private Image[] attack = new Image[10];
	private Image[] die = new Image[5];
	
	// Declare a snowball projectile
	private Snowball projectile;
	
	// Initialize ints for attack height and width
	private int attackHeight = 280, attackWidth = 220;
	
	// Declare an int to represent the floor
	private int floor;
	
	// Initialize an int to represent what step of the attack the monster is on
	private int attackStep = 1;
	
	// Initialize a boolean to tell if the projectile is exploding
	private boolean projSecondPhase = false;
	
	// Declare int for projectile damage
	private int projectileDamage;
	
	// Declare int for monster destination
	private int destinationX;
	
	SnowMonster(int monsterX, int delay, int destinationX, int HEIGHT, int monsterHomeX, int targetLevel, boolean possibleBoss, int playerLevel) {
		
		// Initialize all images
		try {
			for(int i = 0; i < idle.length; i++) {
				idle[i] = ImageIO.read(new File("rpgImages/snowMonsterIdle" + (i+1) + ".png"));
			}
			for(int i = 0; i < attack.length; i++) {
				attack[i] = ImageIO.read(new File("rpgImages/snowMonsterAttack" + (i+1) + ".png"));
			}
			for(int i = 0; i < die.length; i++) {
				die[i] = ImageIO.read(new File("rpgImages/snowMonsterDie" + (i+1) + ".png"));
			}
			
			setImg(idle[0]);
			
		}
		catch (IOException e) {
			e.printStackTrace();
        }
		
		// Set up monster variables with starting values
		setIdleSize();

		floor = HEIGHT - 25;
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
		
		setTotalHealth((int)((30 * Math.random() + 60) * getLevel()));
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
			
			if(getIndex() == 1) {
				moveX(5);
			}
			
			setCount(getCount() + 1);
			
			if(getCount() > getDelay()/3) {
				setIndex(getIndex() + 1);
				setCount(0);
			}
			
			// If the motion is almost complete, initialize the projectile and go to next step
			if(getIndex() >= attack.length - 2) {
				attackStep = 2;
				projectile = new Snowball(getX(), getY(), getDelay(), destinationX);
				projectileDamage = projectile.getDamage();
				projectile.fly();
			} 
			return false;
			
		} 
		// Finishes the attack motion
		else if(attackStep == 2) {
			
			setImg(attack[getIndex()]);
			
			setCount(getCount() + 1);
			
			if(getCount() > getDelay()/3) {
				setIndex(getIndex() + 1);
				setCount(0);
			}
			
			if(getIndex() >= attack.length - 1) {
				attackStep = 3;
			}
			return false;
			
		} 
		// Flings the projectile
		else if(attackStep == 3) {
			
			setImg(attack[getIndex()]);
			
			// If the projectile is flying, make it fly
			if(!projSecondPhase) {
				if(projectile.fly()) {
					projSecondPhase = true;
				}
			} 
			// Else, make the projectile explode
			else {
				if(projectile.explode()) {
					projSecondPhase = false;
					attackStep = 1;
					projectile = null;
					return true;
				}
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
		setWidth(220);
		setHeight(220);
		
		setY(getStartY());
	}
	
	// Sets monster size to die size
	private void setDieSize() {
		setWidth(260);
		setHeight(200);
		
		setY(getStartY() + 20);

	}
	
	// Sets monster size to attack size
	private void setAttackSize() {
		setHeight(attackHeight);		
		setWidth(attackWidth);
		
		setY(floor - attackHeight);
	}
	
	// Draws monster and projectile
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		
		if(projectile != null) {
			projectile.draw(g);
		}
	}
	
	// Returns attack 1 damage
	@Override
	public int getAttack1Damage() {
		return projectileDamage * (int) Math.pow(getLevel(), 6.0/5);
	}

}
