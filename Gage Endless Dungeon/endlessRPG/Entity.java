package endlessRPG;

import java.awt.Graphics;
import java.awt.Image;

public abstract class Entity {
	
	// Declares variables
	private Image img;
	private int delay, index, count;
	private int x, y, width, height;
	private int startX, startY;
	private int idleWidth, idleHeight;
	private int currentHealth, totalHealth;
	private int level;
	
	// Draws entity
	public void draw(Graphics g) {
		g.drawImage(img, x, y, width, height, null);
	}
	
	// Entity takes damage (loses health)
	public void takeDamage(int damage) {
		currentHealth -= damage;
		
		if(currentHealth <= 0) currentHealth = 0;
	}
	
	// Getter and setter for all variables
	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
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

	public int getIdleWidth() {
		return idleWidth;
	}

	public void setIdleWidth(int idleWidth) {
		this.idleWidth = idleWidth;
	}

	public int getIdleHeight() {
		return idleHeight;
	}

	public void setIdleHeight(int idleHeight) {
		this.idleHeight = idleHeight;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
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

	public Image getImg() {
		return img;
	}

	public void setImg(Image img) {
		this.img = img;
	}

	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void resetIndex() {
		count = 0;
		index = 0;
	}
	
	public void resetLoc() {
		x = startX;
		y = startY;
	}
	
	public void moveX(int x) {
		this.x += x;
	}
	
	public void moveY(int y) {
		this.y += y;
	}

	public int getCurrentHealth() {
		return currentHealth;
	}

	public void setCurrentHealth(int currentHealth) {
		this.currentHealth = currentHealth;
	}

	public int getTotalHealth() {
		return totalHealth;
	}

	public void setTotalHealth(int totalHealth) {
		this.totalHealth = totalHealth;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
