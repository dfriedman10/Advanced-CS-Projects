package endlessRPG;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class LightningStrike extends MagicEffect {
	
	// Initialize image arrays for all actions
	private Image[] move = new Image[10];
	private Image[] electrify = new Image[6];
	
	LightningStrike(int targetX, int delay, int destinationY){
		
		setType("zap");
		
		// Initialize all images
		try {
			for(int i = 0; i < move.length; i++) {
				move[i] = ImageIO.read(new File("rpgImages/lightning" + (i+1) + ".png"));
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
		setX(targetX + 40);
		setY(0);
		setWidth(100);
		setHeight(100);
		
		setXVelo(0);
		
		setYVelo(0);
		setXAcceleration(0);
		setYAcceleration(0.5);
		
		setDestination(destinationY);
		setDelay(delay);
		
		setDamage(15);
	}
	
	// Flies
	public boolean fly() {
		
		setImg(move[getIndex()]);
		
		
		if(getY() <= getDestination()) move();
	
		setCount(getCount() + 1);
		
		if(getCount() > getDelay()) {
			if(getIndex() != move.length - 1) {
				setIndex(getIndex() + 1);
			}
			setCount(0);
		}
		
		if(getIndex() >= move.length - 1) {
			resetIndex();
			setX(getX() - 90);
			return true;
		} else {
			return false;
		}
	}
	
	// Explodes
	public boolean explode() {
		
		setY(getDestination()-150);
		
		setWidth(300);
		setHeight(300);
		
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
