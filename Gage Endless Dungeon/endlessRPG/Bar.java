package endlessRPG;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bar extends Rectangle {
	
	// Initializes variables
	private String title;
	private int current = 0;
	private Color color;
	
	Bar(String title, int x, int y, int width, int height, Color color){
		super(x, y, width, height);
				
		this.setTitle(title);
		
		current = width;
		
		this.setColor(color);
	}
	
	// Draws the bar
	public void draw(Graphics g, int curAmount, int totAmount) {
		g.setColor(Color.black);
		g.fillRect(x, y, width, height);
		g.setColor(color);
		
		current = width * curAmount/totAmount;
		if(current > width) current = width;
		g.fillRect(x, y, current, height);
		
		g.setColor(color.white);
		g.drawString(curAmount + "/" + totAmount + " " + title, x, y + height - 2);
	}

	// Getter and setter for all variables
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String s) {
		title = s;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
