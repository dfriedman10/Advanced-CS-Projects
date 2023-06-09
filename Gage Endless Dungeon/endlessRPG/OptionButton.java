package endlessRPG;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class OptionButton extends Rectangle {
	
	// Declare variables
	private int playerAction;
	private boolean isSpell = false;
	private int spellChoice;
	private String title;
	private String cost;
	private Image img;
	private int wMod, hMod;
	
	OptionButton(String title, String cost, int x, int y, int width, int height, int playerAction, String image, int wMod, int hMod){
		super(x, y, width, height);
		
		this.setPlayerAction(playerAction);
		
		this.setTitle(title);
		
		// Initialize image
		try {
			img = ImageIO.read(new File(image));

		}
		catch (IOException e) {
			e.printStackTrace();
        }
		
		this.wMod = wMod;
		this.hMod = hMod;
		
		this.cost = cost;
	}
	
	OptionButton(String title, String cost, int x, int y, int width, int height, int playerAction, int spellChoice, String image, int wMod, int hMod){
		super(x, y, width, height);
		
		this.setPlayerAction(playerAction);
		
		isSpell = true;
		
		this.setSpellChoice(spellChoice);
		
		this.setTitle(title);
		
		// Initialize image
		try {
			img = ImageIO.read(new File(image));

		}
		catch (IOException e) {
			e.printStackTrace();
        }
		
		this.wMod = wMod;
		this.hMod = hMod;
		
		this.cost = cost;
	}
	
	// Draws the button
	public void draw(Graphics g, int x, int y) {
		this.x = x;
		this.y = y;
		
		g.setColor(Color.white);
		g.fillRect(x, y, width, height);
		g.drawImage(img, x + 5, y + 15, width-10 + wMod, height-25 + hMod, null);
		g.setColor(Color.black);
		g.drawString(title, x + 1, y + height - 2);
		g.drawString(cost, x + 1, y + 12);
	}
	
	// Returns true if the mouse clicks the button
	public boolean isOn(MouseEvent e) {		
		if(e.getX() < x + width && e.getX() > x && e.getY() < y + height && e.getY() > y) return true;
		
		return false;
	}
	
	// Getter and setter functions for variables
	public int getPlayerAction() {
		return playerAction;
	}

	public void setPlayerAction(int playerAction) {
		this.playerAction = playerAction;
	}
	
	public boolean isSpell() {
		return isSpell;
	}

	public int getSpellChoice() {
		return spellChoice;
	}

	public void setSpellChoice(int spellChoice) {
		this.spellChoice = spellChoice;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
} 
