package endlessRPG;

import java.awt.Graphics;
import java.awt.Image;

public abstract class Mob extends Entity {
	
	// Declare an int for the experience value of the monster
	private int XP;
	
	// Declare a boolean to determine whether or not the monster is a boss
	private boolean boss = false;
	
	// Abstract functions that all mobs share
	public abstract void idle();
	
	public abstract boolean attack1();
		
	public abstract boolean die();
	
	public abstract int getAttack1Damage();

	public int getXP() {
		return XP;
	}

	public void setXP(int xP) {
		XP = xP;
	}

	public boolean isBoss() {
		return boss;
	}

	public void setBoss(boolean boss) {
		this.boss = boss;
	}
		
}
