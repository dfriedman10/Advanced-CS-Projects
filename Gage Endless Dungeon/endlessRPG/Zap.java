package endlessRPG;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Zap extends MagicEffect {
	
	// Initialize image arrays for all actions
	private Image[] move = new Image[5];
	private Image[] electrify = new Image[6];
	
	Zap(int playerX, int playerY, int delay, int destinationX){
		
		setType("zap");
		
		// Initialize all images
		try {
			for(int i = 0; i < move.length; i++) {
				move[i] = ImageIO.read(new File("rpgImages/zap" + (i+1) + ".png"));
			}
			for(int i = 0; i < electrify.length; i++) {
				electrify[i] = ImageIO.read(new File("rpgImages/electric" + (i+1) + ".png"));
			}
			
			setImg(move[0]);
			
		}
		catch (IOException e) {
			e.printStackTrace();
        }

		// Set variables to starting values
		setX(playerX + 30);
		setY(playerY + 30);
		setWidth(80);
		setHeight(80);
		
		setXVelo(2);
		
		setYVelo(0);
		setXAcceleration(0.2);
		setYAcceleration(0);
		
		setDestination(destinationX);
		setDelay(delay);
		
		setDamage(7);
	}
	
	// Flies
	public boolean fly() {
		
		setImg(move[getIndex()]);
		
		move();
				
		setCount(getCount() + 1);
		
		if(getCount() > getDelay()) {
			if(getIndex() != move.length - 1) {
				setIndex(getIndex() + 1);
			}
			setCount(0);
		}
		
		if(getX() >= getDestination()) {
			resetIndex();
			setY(getY() - 130);
			return true;
		} else {
			return false;
		}
	}
	
	// Explodes
	public boolean explode() {
				
		setX(getDestination()-100);
		
		setWidth(250);
		setHeight(200);
		
		setImg(electrify[getIndex()]);
				
		setCount(getCount() + 1);
		
		if(getCount() > getDelay()) {
			setIndex(getIndex() + 1);
			setCount(0);
		}
		
		if(getIndex() >= electrify.length) {
			return true;
		} else {
			return false;
		}
	}
}

