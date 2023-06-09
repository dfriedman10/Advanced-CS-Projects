package endlessRPG;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Snowball extends MagicEffect {
	
	// Initialize image arrays for all actions
	private Image[] move = new Image[8];
	private Image[] explode = new Image[8];
	
	Snowball(int monsterX, int monsterY, int delay, int destinationX){
		
		// Initialize all images
		try {
			for(int i = 0; i < move.length; i++) {
				move[i] = ImageIO.read(new File("rpgImages/snowBall" + (i+1) + ".png"));
			}
			for(int i = 0; i < explode.length; i++) {
				explode[i] = ImageIO.read(new File("rpgImages/snowExplode" + (i+1) + ".png"));
			}
			
			setImg(move[0]);
			
		}
		catch (IOException e) {
			e.printStackTrace();
        }
		
		// Set variables to starting values
		setX(monsterX + 5);
		setY(monsterY + 30);
		setWidth(110);
		setHeight(90);
		
		setXVelo(-13);
		
		setYVelo(-4);
		setXAcceleration(0);
		setYAcceleration(0.2);
		
		setDestination(destinationX);
		setDelay(delay);
		
		setDamage(12);
	}
	
	// Projectile flies
	public boolean fly() {
		
		setImg(move[getIndex()]);
		
		move();
				
		setCount(getCount() + 1);
		
		if(getCount() > getDelay()*1/3 ) {
			if(getIndex() != move.length - 1) {
				setIndex(getIndex() + 1);
			} else {
				setIndex(0);
			}
			setCount(0);
		}
		
		if(getX() <= getDestination()) {
			resetIndex();
			setY(getY() - 30);
			return true;
		} else {
			return false;
		}
	}
	
	// Projectile explodes
	public boolean explode() {
		
		setX(getDestination());
		
		setWidth(80);
		setHeight(80);
		
		setImg(explode[getIndex()]);
				
		setCount(getCount() + 1);
		
		if(getCount() > getDelay()/2) {
			setIndex(getIndex() + 1);
			setCount(0);
		}
		
		if(getIndex() >= explode.length) {
			return true;
		} else {
			return false;
		}
	}
}
