package endlessRPG;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;


public abstract class MagicEffect {
	
	// Declare variables
	private Image img;
	private int delay, index = 0, count = 0;
	private int x, y, width, height;
	private int startX, startY;
	private double yVelo, xVelo;
	private double yAcceleration, xAcceleration;
	private int destination;
	private int damage;
	private String type;
	
	// Declares abstract methods all MagicEffects will use (fly and explode)
	public abstract boolean fly();
	public abstract boolean explode();
	
	// Moves the MagicEffect
		public void move() {
			setXVelo(xVelo + xAcceleration);
			setYVelo(yVelo + yAcceleration);

			setX(x + (int)xVelo);
			setY(y + (int)yVelo);
			
		}
		
		// Draws MagicEffect
		public void draw(Graphics g) {
			g.drawImage(img, x, y, width, height, null);
		}
		
		// Resets index to 0
		public void resetIndex() {
			index = 0;
		}
	
	// Getter and setter for all variables
	public Image getImg() {
		return img;
	}
	public void setImg(Image img) {
		this.img = img;
	}
	public int getDelay() {
		return delay;
	}
	public void setDelay(int delay) {
		this.delay = delay;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getStartX() {
		return startX;
	}
	public void setStartX(int startX) {
		this.startX = startX;
	}
	public int getStartY() {
		return startY;
	}
	public void setStartY(int startY) {
		this.startY = startY;
	}
	public double getYVelo() {
		return yVelo;
	}
	public void setYVelo(double yVelo) {
		this.yVelo = yVelo;
	}
	public double getYAcceleration() {
		return yAcceleration;
	}
	public void setYAcceleration(double yAcceleration) {
		this.yAcceleration = yAcceleration;
	}
	public double getXVelo() {
		return xVelo;
	}
	public void setXVelo(double xVelo) {
		this.xVelo = xVelo;
	}
	public double getXAcceleration() {
		return xAcceleration;
	}
	public void setXAcceleration(double xAcceleration) {
		this.xAcceleration = xAcceleration;
	}
	public int getDestination() {
		return destination;
	}
	public void setDestination(int destination) {
		this.destination = destination;
	}
	public int getDamage() {
		return damage;
	}
	public void setDamage(int damage) {
		this.damage = damage;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
