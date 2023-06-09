package endlessRPG;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Player extends Entity {
	
	// Initialize image arrays for all animations
	private Image[] idle = new Image[6];
	private Image[] walk = new Image[6];
	private Image[] swing = new Image[6];
	private Image[] stab = new Image[4];
	private Image[] dash = new Image[3];
	private Image[] cast = new Image[5];
	private Image[] win = new Image[2];
	private Image[] die = new Image[9];
	private Image[] hurt = new Image[2];
		
	// Initialize final ints for various functions
	private final int SWINGHEIGHT = 110;
	private final int SWINGWIDTH = 120;
	private final int SWINGY = 450;
	private final int STABHEIGHT = 110;
	private final int STABWIDTH = 130;
	private final int STABY = 450;
	private final int DASHHEIGHT = 90;
	private final int DASHWIDTH = 100;
	private final int DASHY = 470;
	private final int WINHEIGHT = 170;
	private final int WINWIDTH = 80;
	private final int WINY = 390;
	
	// Declares a variable for the monster's base x value
	private int monsterHomeX;
	
	// Initialize a variable for the phase of an action
	private int phase = 0;
	
	private int curMana;
	private int totalMana;
	
	private int curStamina;
	private int totalStamina;
	
	// Initialize an int to track available stat points
	private int statPoints = 25;
	
	Player(int delay, int monsterHomeX) {
		
		// Initialize all images
		try {
			
			for(int i = 0; i < idle.length; i++) {
				idle[i] = ImageIO.read(new File("rpgImages/idle" + (i+1) + ".png"));
			}
			for(int i = 0; i < walk.length; i++) {
				walk[i] = ImageIO.read(new File("rpgImages/walk" + (i+1) + ".png"));
			}
			for(int i = 0; i < swing.length; i++) {
				swing[i] = ImageIO.read(new File("rpgImages/swing" + (i+1) + ".png"));
			}
			for(int i = 0; i < stab.length; i++) {
				stab[i] = ImageIO.read(new File("rpgImages/stab" + (i+1) + ".png"));
			}
			for(int i = 0; i < dash.length; i++) {
				dash[i] = ImageIO.read(new File("rpgImages/dash" + (i+1) + ".png"));
			}
			for(int i = 0; i < cast.length; i++) {
				cast[i] = ImageIO.read(new File("rpgImages/cast" + (i+1) + ".png"));
			}
			for(int i = 0; i < win.length; i++) {
				win[i] = ImageIO.read(new File("rpgImages/win" + (i+1) + ".png"));
			}
			for(int i = 0; i < die.length; i++) {
				die[i] = ImageIO.read(new File("rpgImages/die" + (i+1) + ".png"));
			}
			for(int i = 0; i < hurt.length; i++) {
				hurt[i] = ImageIO.read(new File("rpgImages/hurt" + (i+1) + ".png"));
			}
			
			setImg(idle[1]);
			
		}
		catch (IOException e) {
			e.printStackTrace();
        }
		
		// Set starting values for player
		setDelay(delay); setCount(0); setIndex(0);
		
		setStartX(100); setStartY(460);
		
		setIdleHeight(100); setIdleWidth(70);
		
		setHeight(getIdleHeight());		
		setWidth(getIdleWidth());	
		
		setX(getStartX());
		setY(getStartY());
		
		setTotalHealth(50);
		setCurrentHealth(getTotalHealth());
		
		totalMana = 10;
		curMana = totalMana;
		
		totalStamina = 10;
		curStamina = totalStamina;
		
		this.monsterHomeX = monsterHomeX;
		
		setLevel(1);
		
	}
	
	// Player idles
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

	// Player swings then returns true
	private boolean swingAnimation() {
		
		if(getIndex() == 0 && getCount() == 0) slashSound();
		
		setImg(swing[getIndex()]);
		
		setHeight(SWINGHEIGHT);		
		setWidth(SWINGWIDTH);	
		
		setY(SWINGY);
		
		if(getIndex() == 1) {
			moveX(5);
		}
		
		setCount(getCount() + 1);
		
		if(getCount() > getDelay()) {
			setIndex(getIndex() + 1);
			setCount(0);
		}
		
		if(getIndex() >= swing.length) {
			return true;
		} else {
			return false;
		}
	}
	
	// Player swings (attacking monster) then returns true
	public boolean swing() {
		
		if(phase == 0) {
			setX(monsterHomeX - 3*getWidth());
			moveWalk();
			phase = 1;
		} else if (phase == 1) {
			if(moveWalk()) {
				resetIndex();
				phase = 2;
			}
		} else if (phase == 2) {
			return swingAnimation();
		}
		
		return false;
		
	}
	
	// Player stabs then returns true
	private boolean stabAnimation() {
		
		if(getIndex() == 0 && getCount() == 0) stabSound();
		
		setImg(stab[getIndex()]);
		
		setHeight(STABHEIGHT);		
		setWidth(STABWIDTH);
		
		setY(STABY);
		
		if(getIndex() < stab.length) {
			moveX(2);
		}
		
		setCount(getCount() + 1);
		
		if(getCount() > getDelay()) {
			setIndex(getIndex() + 1);
			setCount(0);
		}
		
		if(getIndex() >= stab.length) {
			return true;
		} else {
			return false;
		}
	}
	
	// Player stabs (attacking monster) then returns true
	public boolean stab() {
		
		if(phase == 0) {
			setX(monsterHomeX - 3*getWidth());
			moveWalk();
			phase = 1;
		} else if (phase == 1) {
			if(moveWalk()) {
				resetIndex();
				phase = 2;
			}
		} else if (phase == 2) {
			return stabAnimation();
		}
		
		return false;
		
	}
	
	// Player walks (in place) then returns true
	public boolean walkAnimation() {
		
		setImg(walk[getIndex()]);
		
		setIdleSize();
		
		setCount(getCount() + 1);
		
		if(getCount() > getDelay()) {
			setIndex(getIndex() + 1);
			setCount(0);
		}
		
		if(getIndex() >= walk.length) {
			return true;
		} else {
			return false;
		}
	}
	
	// Player walks (physically moving) then returns true
	public boolean moveWalk() {
		
		moveX(1);
		
		return walkAnimation();
	}
	
	// Player dashes then returns true
	public boolean dashAnimation() {
		
		setImg(dash[getIndex()]);
		
		setHeight(DASHHEIGHT);		
		setWidth(DASHWIDTH);	
		
		setY(DASHY);
		
		if(getIndex() < dash.length) {
			moveX(5);
		}
		
		setCount(getCount() + 1);
		
		if(getCount() > getDelay()) {
			setIndex(getIndex() + 1);
			setCount(0);
		}
		
		if(getIndex() >= dash.length) {
			return true;
		} else {
			return false;
		}
		
	}
	
	// Player casts a spell then returns true
	public boolean cast() {
		
		setImg(cast[getIndex()]);
		
		setIdleSize();
		
		setCount(getCount() + 1);
		
		if(getCount() > getDelay()) {
			setIndex(getIndex() + 1);
			setCount(0);
		}
		
		if(getIndex() >= cast.length) {
			return true;
		} else {
			return false;
		}
	}
	
	// Player does victory animation then returns true
	public boolean win() {
		
		setImg(win[getIndex()]);
		
		setHeight(WINHEIGHT);		
		setWidth(WINWIDTH);	
		
		setY(WINY);
		
		setCount(getCount() + 1);
		
		if(getCount() > getDelay()) {
			setIndex(getIndex() + 1);
			setCount(0);
		}
		if(getIndex() >= win.length) {
			return true;
		} else {
			return false;
		}
	}
	
	// Player does hurt animation then returns true
	public boolean hurt() {
		
		if(getIndex() == 0 && getCount() == 0) {
			hurtSound();
		}
		
		setImg(hurt[getIndex()]);
		
		setIdleSize();
		
		setCount(getCount() + 1);
		
		if(getCount() > getDelay()) {
			setIndex(getIndex() + 1);
			setCount(0);
		}
		
		if(getIndex() >= hurt.length) {
			return true;
		} else {
			return false;
		}
		
	}
	
	// Player does death animation then returns true
	public boolean die() {
		
		setImg(die[getIndex()]);
		
		setIdleSize();
		
		setCount(getCount() + 1);
		
		if(getCount() > getDelay()) {
			setIndex(getIndex() + 1);
			setCount(0);
		}
		
		if(getIndex() >= die.length) {
			return true;
		} else {
			return false;
		}
	}
	
	// Sets player size idle size
	public void setIdleSize() {
		setHeight(getIdleHeight());		
		setWidth(getIdleWidth());
		
		setY(getStartY());
	}
	
	// Sets player size to giant swing size
	private void setGiantSwingSize(){
		setHeight(SWINGHEIGHT*2);		
		setWidth(SWINGWIDTH*2);
		
		setY(SWINGY - SWINGHEIGHT);
	}
	
	// Player grows giant and swings then returns true
	private boolean giantSwingAnimation() {
		
		setImg(swing[getIndex()]);
		
		setGiantSwingSize();
		
		if(getIndex() == 1) {
			moveX(20);
		}
		
		setCount(getCount() + 1);
		
		if(getCount() > getDelay()) {
			setIndex(getIndex() + 1);
			setCount(0);
		}
		
		if(getIndex() >= swing.length) {
			return true;
		} else {
			return false;
		}
	}
	
	// Player grows giant and swings (attacking monster) then returns true
	public boolean giantSwing() {
		
		if(phase == 0) {
			setGiantSwingSize();
			setX(monsterHomeX - (int)(1.7*getWidth()));
			giantSwingAnimation();
			phase = 1;
		} 
		else if (phase == 1) {
			return giantSwingAnimation();
		}
		
		return false;
	}
	
	// Player dashes and stabs (attacking monster) then returns true
	public boolean quickStab() {
		
		if(phase == 0) {
			setX(monsterHomeX - 5*getWidth());
			dashAnimation();
			phase = 1;
		}
		else if(phase == 1) {
			if(dashAnimation()) {
				resetIndex();
				phase = 2;
			}
		}
		else if (phase == 2) {
			if(stabAnimation()) {
				phase = 0;
				return true;
			}
		}
		return false;
	}
	
	// Resets player index and phase to 0
	public void resetIndex() {
		super.resetIndex();
		
		phase = 0;
	}

	// Getter and Setters
	public int getCurMana() {
		return curMana;
	}

	public void setCurMana(int curMana) {
		this.curMana = curMana;
	}

	public int getTotalMana() {
		return totalMana;
	}

	public void setTotalMana(int totalMana) {
		this.totalMana = totalMana;
	}

	public int getCurStamina() {
		return curStamina;
	}

	public void setCurStamina(int curStamina) {
		this.curStamina = curStamina;
	}

	public int getTotalStamina() {
		return totalStamina;
	}

	public void setTotalStamina(int totalStamina) {
		this.totalStamina = totalStamina;
	}
	
	
	// Player audio
	public synchronized void hurtSound() {
		new Thread(new Runnable() {

			public void run() {

				try {
					Clip clip = AudioSystem.getClip();
					
					BufferedInputStream in = new BufferedInputStream(new FileInputStream("rpgSound/hurtSound.wav"));
					
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(in);
					
					clip.open(inputStream);
					clip.start();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start(); 
	}
	public synchronized void stabSound() {
		new Thread(new Runnable() {

			public void run() {

				try {
					Clip clip = AudioSystem.getClip();
					
					BufferedInputStream in = new BufferedInputStream(new FileInputStream("rpgSound/stabSound.wav"));
					
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(in);
					
					clip.open(inputStream);
					clip.start();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start(); 
	}
	public synchronized void slashSound() {
		new Thread(new Runnable() {

			public void run() {

				try {
					Clip clip = AudioSystem.getClip();
					
					BufferedInputStream in = new BufferedInputStream(new FileInputStream("rpgSound/slashSound.wav"));
					
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(in);
					
					clip.open(inputStream);
					clip.start();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start(); 
	}
}
