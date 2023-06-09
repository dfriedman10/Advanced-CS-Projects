package endlessRPG;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Axe extends MagicEffect {
	
	// Initialize image arrays for all actions
	private Image[] move = new Image[8];
	
	Axe(int monsterX, int monsterY, int delay, int destinationX){
		
		// Initialize all images
		try {
			for(int i = 0; i < move.length; i++) {
				move[i] = ImageIO.read(new File("rpgImages/axe" + (i+1) + ".png"));
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
		
		setXVelo(-16);
		
		setYVelo(-6);
		setXAcceleration(0);
		setYAcceleration(0.2);
		
		setDestination(destinationX);
		setDelay(delay);
		
		setDamage(25);
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
		return true;
	}
}
