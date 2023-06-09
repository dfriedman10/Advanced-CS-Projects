package endlessRPG;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BasicFireball extends MagicEffect {
	
	// Initialize image arrays for all actions
	private Image[] move = new Image[3];
	private Image[] explode = new Image[8];
	
	BasicFireball(int playerX, int playerY, int delay, int destinationX){
		
		setType("fire");
		
		// Initialize all images
		try {
			for(int i = 0; i < move.length; i++) {
				move[i] = ImageIO.read(new File("rpgImages/basicFireball" + (i+1) + ".png"));
			}
			for(int i = 0; i < explode.length; i++) {
				explode[i] = ImageIO.read(new File("rpgImages/explode" + (i+1) + ".png"));
			}
			
			setImg(move[0]);
			
		}
		catch (IOException e) {
			e.printStackTrace();
        }
		
		// Set variables to starting values
		setX(playerX + 30);
		setY(playerY + 40);
		setWidth(60);
		setHeight(40);
		
		setXVelo(7);
		
		setYVelo(0);
		setXAcceleration(0);
		setYAcceleration(0);
		
		setDestination(destinationX);
		setDelay(delay);
		
		setDamage(9);
	}
	
	// Flies
	public boolean fly() {
		
		setImg(move[getIndex()]);
		
		move();
				
		setCount(getCount() + 1);
		
		if(getCount() > getDelay() ) {
			if(getIndex() != move.length - 1) {
				setIndex(getIndex() + 1);
			}
			setCount(0);
		}
		
		if(getX() >= getDestination()) {
			resetIndex();
			setY(getY() - 30);
			return true;
		} else {
			return false;
		}
	}
	
	// Explodes
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
