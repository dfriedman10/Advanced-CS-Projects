package endlessRPG;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class StatMenu {
    private int statPoints = 15; // Initial stat points available
    private int strength = 5;
    private int constitution = 5;
    private int intelligence = 5;
    private int wisdom = 5;
    private int luck = 5;
    private int x, y;
    
    ArrayList<Button> buttons = new ArrayList<Button>();

    StatMenu(int x, int y){
    	this.x = x;
    	this.y = y;
    	
    	int buttonX = x + 150;
        int buttonY = y + 35;
    	
    	buttons.add(new Button(buttonX, buttonY, "Increase Strength"));
    	buttons.add(new Button(buttonX, buttonY + 30, "Increase Constitution"));
    	buttons.add(new Button(buttonX, buttonY + 60, "Increase Intelligence"));
    	buttons.add(new Button(buttonX, buttonY + 90, "Increase Wisdom"));
    	buttons.add(new Button(buttonX, buttonY + 120, "Increase Luck"));

    }
    
    public void drawStatMenu(Graphics g) {
    	
    	int labelX = x + 10;
        int labelY = y + 20;
        int buttonX = x + 150;
        int buttonY = y + 35;
        
        g.setColor(Color.gray);
        g.fillRect(x, y, 350, 200); // Background for the stat menu
        
        g.setColor(Color.white);
        g.drawRect(x, y, 350, 200); // Border for the stat menu
        
        g.setFont(g.getFont().deriveFont(Font.BOLD));

        g.drawString("Stat Points: " + getStatPoints(), labelX, labelY);
        g.drawString("Strength: " + getStrength(), labelX, labelY + 30);
        g.drawString("Constitution: " + getConstitution(), labelX, labelY + 60);
        g.drawString("Intelligence: " + getIntelligence(), labelX, labelY + 90);
        g.drawString("Wisdom: " + getWisdom(), labelX, labelY + 120);
        g.drawString("Luck: " + getLuck(), labelX, labelY + 150);

        for(Button button : buttons) {
        	button.draw(g);
        }
    }
    
    public void drawEndStats(Graphics g) {
    	x = 450;
    	y = 200;
    	
    	int labelX = x + 10;
        int labelY = y + 20;
        
        g.setColor(Color.gray);
        g.fillRect(x, y, 175, 160); // Background for the stat menu
        
        g.setColor(Color.white);
        g.drawRect(x, y, 175, 160); // Border for the stat menu
        
        g.setFont(g.getFont().deriveFont(Font.BOLD));

        g.drawString("Strength: " + getStrength(), labelX, labelY);
        g.drawString("Constitution: " + getConstitution(), labelX, labelY + 30);
        g.drawString("Intelligence: " + getIntelligence(), labelX, labelY + 60);
        g.drawString("Wisdom: " + getWisdom(), labelX, labelY + 90);
        g.drawString("Luck: " + getLuck(), labelX, labelY + 120);
    }
    
    public boolean checkButtons(MouseEvent e) {
    	for(Button button : buttons) {
    		if(getStatPoints() > 0) {
	    		if(button.isOn(e)) {
	    			setStatPoints(getStatPoints() - 1);
	    			if(button.label.equals("Increase Strength")) {
	    				setStrength(getStrength() + 1);
	    			} else if(button.label.equals("Increase Constitution")) {
	    				setConstitution(getConstitution() + 1);
	    			} else if(button.label.equals("Increase Intelligence")) {
	    				setIntelligence(getIntelligence() + 1);
	    			} else if(button.label.equals("Increase Wisdom")) {
	    				setWisdom(getWisdom() + 1);
	    			} else if(button.label.equals("Increase Luck")) {
	    				setLuck(getLuck() + 1);
	    			}
	    			
	    			return true;
	    		}
    		}
    	}
    	
    	return false;
    }
    
    public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getWisdom() {
		return wisdom;
	}

	public void setWisdom(int wisdom) {
		this.wisdom = wisdom;
	}

	public int getConstitution() {
		return constitution;
	}

	public void setConstitution(int constitution) {
		this.constitution = constitution;
	}

	public int getLuck() {
		return luck;
	}

	public void setLuck(int luck) {
		this.luck = luck;
	}

	public int getIntelligence() {
		return intelligence;
	}

	public void setIntelligence(int intelligence) {
		this.intelligence = intelligence;
	}

	public int getStatPoints() {
		return statPoints;
	}

	public void setStatPoints(int statPoints) {
		this.statPoints = statPoints;
	}

	private class Button {
    	
    	final int WIDTH = 180, HEIGHT = 20;
    	
    	int x, y;
    	String label;
    	
    	Button(int x, int y, String label){
    		this.x = x;
    		this.y = y;
    		this.label = label;
    	}
		
		public void draw(Graphics g) {
			g.drawRect(x, y, WIDTH, HEIGHT); // Button outline
		    g.drawString(label, x + 20, y + 15); // Button label
		}
		
		public boolean isOn(MouseEvent e) {		
			if(e.getX() < x + WIDTH && e.getX() > x && e.getY() < y + HEIGHT && e.getY() > y) return true;
			
			return false;
		}
    }
}
