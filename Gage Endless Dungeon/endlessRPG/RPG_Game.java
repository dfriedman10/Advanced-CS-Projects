package endlessRPG;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class RPG_Game {
	
	// Declare screen size
	final int WIDTH = 1200, HEIGHT = 600;
	
	// Determines the speed of all the game's animations (12 is base)
	final int animationDelay = 12;
	
	// The location where the monster will stop and fight
	int monsterHomeX = 900;
	
	// Declare an image for the dungeon
	Image dungeon = null;
	
	// Declare fonts
	Font basicFont = new Font("Arial", Font.PLAIN, 14);
	Font critFont = new Font("Impact", Font.BOLD, 48);
	Font levelFont = new Font("Arial", Font.BOLD, 20);
	
	// Declare instances for the player and its assets
	Player player;
	MagicEffect playerSpell;
	Bar HPBar;
	Bar XPBar;
	Bar SPBar;
	Bar MPBar;
	
	// Initialize variables to track player values
	int lifetimeXP = 0;
	int totalXP = 100;
	int curXP = 0;
	int totalMana = 60;
	int curMana = totalMana;
	int manaRegen = 0;
	int totalStamina = 50;
	int curStamina = totalStamina;
	int staminaRegen = 0;
	int crit = 1;
	boolean critHit = false;
	int critValue = 0;
	int critCount = 0;
	int intelligenceMultiplier = 0;
	int strengthMultiplier = 0;
	
	// Declare instance for monster and its assets
	Mob monster;
	Bar monsterHealthBar;
	int killCount = 0;
	int countToDetermineIfBoss = 0;
	
	// Declare a list of strings that represent all mob types
	ArrayList<String> mobs = new ArrayList<String>();
	
	// Declare variables for the background images
	int dungeon1X, dungeon1Y, dungeon2X, dungeon2Y, dungeonWidth, dungeonHeight;
	
	// Declare a boolean that says if the game is over or not
	boolean end = false;
	
	// Declare a boolean that says if the game is paused or not
	boolean paused = true;
	
	// Declare a boolean that represents whether a spell is moving or exploding
	boolean spell_first_stage = false;
	
	// Declare a boolean the determines of the player options are visible or not
	boolean playerOptionsVisible = false;
	
	/* 0  is startMenu
	 * 1  is walking
	 * 2  is finding enemy
	 * 3  is playerTurn
	 * 4  is monsterTurn
	 * 5  is fighting
	 * 6  is pauseMenu
	 * 7  is playerLevelUp
	 * 8  is gameOver
	 */
	int scene = 1;
	
	// Indexing variables to help with game logic
	int lastScene = 0, count = 0, goal = 0, step = 0;
	
	/* 0  is idle
	 * 1  is walk (dungeon moves to create illusory movement)
	 * 2  is swing
	 * 3  is stab
	 * 4  is dash
	 * 5  is hurt
	 * 6  is win
	 * 7  is die
	 * 8  is moveWalk (across screen with static background)
	 * 9  is cast
	 * 10 is giant swing
	 * 11 is quick stab
	 * 12 is meditate
	 * 13 is breathe
	 * 14 is heal
	 * 15 is mega heal
	 */
	int playerAction = 0, lastPlayerAction = 0;
	
	/* 0  is BasicFireball
	 * 1  is Zap
	 * 2  is LightningStrike
	 */
	int spellChoice = 0;
	
	/* 0  is idle
	 * 1  is attack1
	 * 2  is die
	 */
	int monsterAction = 0, lastMonsterAction = 0;
	
	// Declare ints representing the damage values of different skills
	int swingDamage = 5, stabDamage = 6, quickStabDamage = 8, giantSwingDamage = 15;
	int baseSwingDamage = 5, baseStabDamage = 6, baseQuickStabDamage = 8, baseGiantSwingDamage = 15, baseZapDamage = 7, baseLightningDamage = 15, baseBasicFireballDamage = 9;
	
	StatMenu statMenu;
	
	// Declare a list of all option buttons
	ArrayList<OptionButton> allOptions = new ArrayList<OptionButton>();
	
	// Declare a list of new options buttons to choose from
	ArrayList<OptionButton> newOptions = new ArrayList<OptionButton>();
	
	// Declare a boolean that determines whether option selection is visible or not
	boolean selectSkill = false;
	
	// Declare a list of the current options
	ArrayList<OptionButton> playerOptions = new ArrayList<OptionButton>();
	
	public void start() {
		
		monster = null;
		
		// Set variables that must start at specific values to starting values
		allOptions = new ArrayList<OptionButton>();
		newOptions = new ArrayList<OptionButton>();
		playerOptions = new ArrayList<OptionButton>();
		end = false;
		paused = true;
		scene = 1;
		lastScene = 0;
		lifetimeXP = 0;
		lifetimeXP = 0;
		totalXP = 100;
		curXP = 0;
		totalMana = 60;
		curMana = totalMana;
		manaRegen = 0;
		totalStamina = 50;
		curStamina = totalStamina;
		staminaRegen = 0;
		crit = 1;
		critHit = false;
		critValue = 0;
		critCount = 0;
		killCount = 0;
		countToDetermineIfBoss = 0;
		playerOptionsVisible = false;
		
		// Initializes stat menu
		statMenu = new StatMenu(330, 100);
		
		// Initializes player
		player = new Player(animationDelay, monsterHomeX);
		
		// Initializes health bar
		HPBar = new Bar("HP", 50, 25, 200, 13, Color.red);
		
		// Initializes exp bar and ints
		XPBar = new Bar("XP", 50, 10, 200, 13, new Color(255, 198, 0));
		
		// Initializes stamina bar and ints
		SPBar = new Bar("SP", 50, 40, 200, 13, new Color(22, 140, 33));
		
		// Initializes mana bar and ints
		MPBar = new Bar("MP", 50, 55, 200, 13, new Color(45, 51, 224));
		
		// Adds all mob types to mobs
		mobs.add("SnowMonster");
		mobs.add("Goblin");
		mobs.add("Slime");
		
		// Adds the starting option buttons
		playerOptions.add(new OptionButton("Slash", "Stam: -" + 8*baseSwingDamage, 100, 200, 80, 100, 2, "rpgImages/slash.png", 0, 0));
		playerOptions.add(new OptionButton("Stab", "Stam: -" + 8*baseStabDamage, 220, 200, 80, 100, 3, "rpgImages/stab.png", 0, 0));
		playerOptions.add(new OptionButton("Zap", "Mana: -" + baseZapDamage * 8, 340, 200, 80, 100, 9, 1, "rpgImages/zap1.png", 80, 40));
		playerOptions.add(new OptionButton("Meditate", "Mana++", 460, 200, 80, 100, 12, "rpgImages/med.png", 0, 0));
		playerOptions.add(new OptionButton("Breathe", "Stamina++", 580, 200, 80, 100, 13, "rpgImages/breathe.png", 0, 0));
		
		// Adds the remaining options
		allOptions.add(new OptionButton("Lightning", "Mana: -" + baseLightningDamage * 8 , 580, 200, 80, 100, 9, 2, "rpgImages/lightning1.png", 0, 0));
		allOptions.add(new OptionButton("Fireball","Mana: -" + baseBasicFireballDamage * 8, 580, 200, 80, 100, 9, 0, "rpgImages/basicFireball1.png", 0, 0));
		allOptions.add(new OptionButton("Giant Slash", "Stam: -" + 20*baseGiantSwingDamage, 580, 200, 80, 100, 10, "rpgImages/giantSlash.png", 0, 0));
		allOptions.add(new OptionButton("Quick Stab", "Stam: -" + 12*baseQuickStabDamage,  580, 200, 80, 100, 11, "rpgImages/quickStab.png", 0, 0));
		allOptions.add(new OptionButton("Heal", "-1/3 Mana", 580, 200, 80, 100, 14, "rpgImages/heal.png", 0, 0));
		allOptions.add(new OptionButton("Mega Heal", "-2/3 Mana", 580, 200, 80, 100, 15, "rpgImages/megaHeal.png", 0, 0));
		
		// Set up images
		imageSetUp();
				
	}
	
	RPG_Game() {
		
		start();
		
		// Starts background music
		backgroundSound();
		
		Timer soundTimer = new Timer();
        soundTimer.schedule(new BackgroundMusicTask(), 0, (3 * 60 + 13) * 1000); // Schedule the background music to play every 3 minutes and 13
        soundTimer.schedule(new RunSoundTask(), 0, (1) * 1000); // Schedule the run sound to play every 1 second
		
		
		// Start graphics
		JFrame frame = new JFrame();
		
		frame.setSize(WIDTH, HEIGHT);
		
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel canvas = new JPanel() {
			
			public void paint(Graphics g) {
				
				draw(g);
			}
		};
		canvas.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub	
				
				// If a player option is clicked, change the necessary variables to represent that change
				if(playerOptionsVisible && !paused && !selectSkill) {
					for(OptionButton option : playerOptions) {
						if(option.isOn(e)) {
							
							scene = 5;
							
							playerAction = option.getPlayerAction();
							
							if(option.isSpell()) spellChoice = option.getSpellChoice();
							
							clickSound();
						}
					}
				} 
				
				else if(!paused && selectSkill) {
					if(count != 2) {
						for(OptionButton option : playerOptions) {
							if(option.isOn(e)) {
								allOptions.add(option);
								playerOptions.remove(option);
								count = 2;
								clickSound();
								break;
							}
						}
					}
					else if(count == 2) {
						for(OptionButton option : newOptions) {
							if(option.isOn(e)) {
								playerOptions.add(option);
								allOptions.remove(option);
								clickSound();
								selectSkill = false;
							}
						}
					}
					
				}
				
				// Check if buttons in the pause menu are clicked
				if(paused) {
					if(statMenu.checkButtons(e)) {
						updatePlayer();
						clickSound();
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		canvas.setSize(new Dimension(WIDTH, HEIGHT));
		canvas.setLayout(null);
		canvas.setBounds(0, 0, WIDTH, HEIGHT);
		
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), " ");
		canvas.getActionMap().put(" ", new SpaceAction());
		
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0, false), "R");
		canvas.getActionMap().put("R", new RAction());
		
		frame.add(canvas);
		
		frame.setVisible(true);
				
		// Loop that runs the game logic and then animates
		while(true) {
			if(!end && !paused) {
				logic();
				
				animate();
			}
			frame.getContentPane().repaint();
			try {Thread.sleep(animationDelay - 2);} 
			catch (InterruptedException e) {}
		} 
	
	}
	
	// Starts the game
	public static void main(String[] args) {
		new RPG_Game();
	}
	
	// Draws the graphics
	private void draw(Graphics g) {
		
		// Draw dungeon background
		g.drawImage(dungeon, dungeon1X, dungeon1Y, dungeonWidth, dungeonHeight, null);
		g.drawImage(dungeon, dungeon2X, dungeon2Y, dungeonWidth, dungeonHeight, null);
		
		if(!end) {
			g.setFont(basicFont);
			
			// Draw the player bars
			HPBar.draw(g, player.getCurrentHealth(), player.getTotalHealth());
			XPBar.draw(g, curXP, totalXP);
			SPBar.draw(g, curStamina, totalStamina);
			MPBar.draw(g, curMana, totalMana);
			
			// Draws the player options
			if(playerOptionsVisible) {
				int i = 0;
				
				for(OptionButton option : playerOptions) {
					option.draw(g, 100 + i*120, 200);
					i++;
				}
			}
			
			// Draws new options
			if(selectSkill) {		
				int i = 0;
				
				if(count != 2) {
					g.setColor(Color.white);
					g.setFont(critFont);
					g.drawString("Select a Skill to Forget", 400, 100);
					g.setFont(basicFont);
					
					for(OptionButton option : playerOptions) {
						option.draw(g, 100 + i*120, 200);
						i++;
					}
				}
				
				i = 0;
				
				if(count == 2) {
					g.setColor(Color.white);
					g.setFont(critFont);
					g.drawString("Select a Skill to Learn", 400, 100);
					g.setFont(basicFont);
					
					for(OptionButton option : newOptions) {
						option.draw(g, 100 + i*120, 200);
						i++;
					}
				}
			}
			
			// Draws the monster and its health bar
			if(monster != null) {
				if(monsterHealthBar != null) {
					monsterHealthBar.draw(g, monster.getCurrentHealth(), monster.getTotalHealth());
					
					//Draws the monster level
					g.setColor(Color.white);
					g.setFont(levelFont);
					g.fillRect(WIDTH - 105, 10, 70, 25);
					g.setColor(new Color(252, 199, 40));
					g.drawString("Lvl: " + monster.getLevel(), WIDTH - 100, 30);
				}
	
				monster.draw(g);
			}
			
			// Draws the player
			player.draw(g);
			
			// Draws the player level
			g.setColor(Color.white);
			g.fillRect(3, 10, 40, 25);
			g.setColor(new Color(252, 199, 40));
			g.setFont(levelFont);
			g.drawString("" + player.getLevel(), 15, 30);
			
			g.setColor(Color.white);
			g.setFont(basicFont);
			
			// Draws the player's spell
			if(playerSpell != null) {
				playerSpell.draw(g);
			}
			
			// Displays critical hit message
			if(critHit && !selectSkill) {
				critCount++;
				g.setColor(new Color(247, 101, 101));
				g.setFont(critFont);
				g.drawString("Critical Hit x" + critValue + "!", 400, 100);
				g.setFont(basicFont);
				if(critCount >= 100) {
					critHit = false;
					critCount = 0;
				}
				g.setColor(Color.white);
			}
			
			// Displays boss incoming message
			if(scene == 2 && monster != null) {
				if(monster.isBoss()) {
					g.setColor(new Color(219, 22, 22));
					g.setFont(critFont);
					g.drawString("BOSS INCOMING", 400, 100);
					g.setFont(basicFont);
					g.setColor(Color.white);
				}
			}
			
			// If the game is paused, draw the pause menu
			if(paused) {
				statMenu.drawStatMenu(g);
			}
		}
		
		if(end) {
			g.setColor(Color.gray);
		    g.fillRect(440, 50, 230, 340); // Background for the stat menu
		        
		    g.setColor(Color.white);
		    g.drawRect(440, 50, 230, 340); // Border for the stat menu
			
			g.setColor(new Color(219, 22, 22));
			g.setFont(critFont);
			g.drawString("GAME OVER", 450, 100);
			g.setFont(levelFont);
			g.setColor(Color.white);
			g.drawString("Lifetime XP: " + lifetimeXP, 450, 150);
			g.drawString("Final Stats:", 450, 190);
			statMenu.drawEndStats(g);
			g.drawString("Press \"R\" to restart", 450, 380);
		}
		
	}
	
	// Changes the variables that control animation based on where the game is in its sequence
	// Runs the game's logic
	private void logic() {
		
		// Reset all indexing variables and prepares for a new scene if the scene changes
		if(scene != lastScene) {
			count = 0;
			goal = 0;
			step = 0;
			lastScene = scene;
			if(player.getCurrentHealth() <= 0) {
				scene = 8;
				lastScene = scene;
			}
		}
		
		// Start Menu
		if(scene == 0) {
			
		}
		// Walking to find next monster
		else if(scene == 1) {
			if(count == 0) {
				goal = (int)(Math.random()*1000);
				runSound();
			}
			if(count % 20 == 0) {
				curStamina += 1 + staminaRegen/20;
				if(curStamina > totalStamina) curStamina = totalStamina;
				curMana += 1 + manaRegen/20;
				if(curMana > totalMana) curMana = totalMana;
			}
			
			playerAction = 1;
			
			count++;
			
			if(count > goal) {
				scene = 2;
			}
		}
		// Encountering the Enemy
		else if(scene == 2) {
			if(count == 0) monsterSpawn();
			
			monsterAction = 0;
			playerAction = 1;
						
			monster.moveX(-1);
						
			count++;
						
			if(monster.getX() <= monsterHomeX) {
				
				if(Math.random() > 0.5) {
					scene = 3;
				} else {
					scene = 4;
				}

				// Initializes monster health bar
				monsterHealthBar = new Bar("HP", WIDTH - 400, 20, 200, 13, Color.red);
				
			}
			
		}
		// Player selecting their move
		else if(scene == 3) {
			if(count == 0) monster.resetLoc();
			
			monsterAction = 0;
			playerAction = 0;
			
			playerOptionsVisible = true;
			
			count++;
			
		}
		// Monster selecting its move
		else if(scene == 4) {
			monsterAction = 1;
			playerAction = 0;
			scene = 5;
		}
		// Fighting animation
		else if(scene == 5) {
			
			if(monster.getCurrentHealth() <= 0) {
				monsterAction = 2;
			}

			playerOptionsVisible = false;
			
		}
		// Pause Menu
		else if(scene == 6) {
			
		}
		// Player levels up
		else if(scene == 7) {
			if(count == 0) {
				goal = 3;
			}
			playerAction = 6;
			
			if(count == goal) {
				playerLevelUp();
				paused = true;
				scene = 1;
			}
			
		}
		// Gome Over
		else if(scene == 8) {
			if(count == 0) {
				dieSound();
				count += 1;
			}
			
			playerAction = 7;
			
		}
		
		// Select a new skill
		else if(scene == 9) {
			if(count == 0) {
				selectSkill = true;
				
				newOptions = new ArrayList<OptionButton>();
				
				int i = 0;
				int lim = 3;
				if(allOptions.size() < 3) lim = allOptions.size();
				while(i < 3) {
					int random = (int)(Math.random() * allOptions.size());
					
					boolean unique = true;
					
					for(OptionButton option : newOptions) {
						if(option.getTitle().equals(allOptions.get(random).getTitle())){	
							unique = false;
							break;
						}
					}
					
					if(unique) {
						newOptions.add(allOptions.get(random));
						
						i++;
					}
					count+= 1;
				}
			}
			
			if(!selectSkill) {
				if(curXP >= totalXP) {
					scene = 7;
					levelSound();
				} else {
					scene = 1;
				}
			}
		}
	}
	
	// Animates the graphics
	private void animate() {
		
		// If the player action changes, reset the player's index and location
		if(playerAction != lastPlayerAction) {
			player.resetIndex();
			player.resetLoc();
			lastPlayerAction = playerAction;
			
			updatePlayer();
		}
		
		// Player idles
		if(playerAction == 0) {
			player.idle();
		}
		
		// Player walks and dungeon moves
		else if (playerAction == 1) {
			
			// If the action is complete, reset the player's index
			if(walk()) {
				player.resetIndex();
			}
		}
		
		// Player slashes
		else if (playerAction == 2) {
			
			// If the action is complete, have the player idle, monster take damage, and change to next scene
			if(player.swing()) {
				playerAction = 0;
				
				monster.takeDamage(swingDamage);
				
				curStamina -= 8 * baseSwingDamage;
				
				// Checks for stamina overflow and subtracts health if so
				if(curStamina < 0) {
					player.takeDamage(-curStamina * 2);
					curStamina = 0;
				}
				
				// Determines if previous hit was a crit and displays Critical Hit! if it was
				if(crit > 1) {
					critHit = true;
					critValue = crit;
				}
				
				scene = 4;
			}	
		}
		
		// Player stabs
		else if (playerAction == 3) {
			
			// If the action is complete, have the player idle, monster take damage, and change to next scene
			if(player.stab()) {
				playerAction = 0;
				
				monster.takeDamage(stabDamage);
				
				curStamina -= 8 * baseStabDamage;
				
				// Checks for stamina overflow and subtracts health if so
				if(curStamina < 0) {
					player.takeDamage(-curStamina * 2);
					curStamina = 0;
				}
				
				// Determines if previous hit was a crit and displays Critical Hit! if it was
				if(crit > 1) {
					critHit = true;
					critValue = crit;
				}
				
				scene = 4;

			}
		}
		
		// Player dashes
		else if (playerAction == 4) {
			
			// If the action is complete, have the player idle
			if(player.dashAnimation()) {
				playerAction = 0;
			}
		}
		
		// Player is hurt
		else if (playerAction == 5) {
			
			// If the action is complete...
			if(player.hurt()) {
				
				// If player is dead, change player action to die
				if(player.getCurrentHealth() <= 0) {
					scene = 8;
				} 
				// Else, set the player to idle and go to next scene
				else {
					playerAction = 0;
					scene = 3;
				}
			}
		}
		
		// Player levels up
		else if (playerAction == 6) {
			
			// If the action is complete, have the player idle
			if(player.win()) {
				count += 1;
				player.resetIndex();
			}	
		}
		
		// Player dies
		else if (playerAction == 7) {
			
			// If the action is complete, game over
			if(player.die()) {
				end = true;
			}
		}
		
		// Player walks
		else if (playerAction == 8) {
			
			// If the action is complete, have the player idle
			if(player.moveWalk()) {
				playerAction = 0;
			}
		}
		
		// Player casts a spell
		else if (playerAction == 9) {
			
			// If the action is complete, have the player idle
			if(cast()) {
				playerAction = 0;
			}
		}
		
		// Player performs a giant slash
		else if (playerAction == 10) {
			
			// If the action is complete, have the player idle, monster take damage, change to next scene
			if(player.giantSwing()) {
				playerAction = 0;
				
				monster.takeDamage(giantSwingDamage);
				
				curStamina -= 20 * baseGiantSwingDamage;
				
				// Checks for stamina overflow and subtracts health if so
				if(curStamina < 0) {
					player.takeDamage(-curStamina * 2);
					curStamina = 0;
				}
				
				// Determines if previous hit was a crit and displays Critical Hit! if it was
				if(crit > 1) {
					critHit = true;
					critValue = crit;
				}
				
				scene = 4;

			}
		}
		
		// Player performs a quick stab
		else if (playerAction == 11) {
			
			// If the action is complete, have the player idle, monster take damage, change to next scene
			if(player.quickStab()) {
				playerAction = 0;
				
				monster.takeDamage(quickStabDamage);
				
				curStamina -= 12 * baseQuickStabDamage;
				
				// Checks for stamina overflow and subtracts health if so
				if(curStamina < 0) {
					player.takeDamage(-curStamina * 2);
					curStamina = 0;
				}
				
				// Determines if previous hit was a crit and displays Critical Hit! if it was
				if(crit > 1) {
					critHit = true;
					critValue = crit;
				}
				
				scene = 4;

			}
		}
		
		// Player meditates
		else if (playerAction == 12) {
			
			player.idle();
			
			// If the action is complete, have the player idle, regain mana, change to next scene
			if(count == 0) {
				goal = manaRegen;
			}
			count++;
			curMana++;
			
			if(curMana > totalMana) curMana = totalMana;
			
			if(count >= goal) {
				playerAction = 0;
				scene = 4;
			}
		}
		
		// Player breathes
		else if (playerAction == 13) {
			
			player.idle();
			
			// If the action is complete, have the player idle, regain mana, change to next scene
			if(count == 0) {
				goal = staminaRegen;
			}
			count++;
			curStamina++;
			
			if(curStamina > totalStamina) curStamina = totalStamina;
			
			if(count >= goal) {
				playerAction = 0;
				scene = 4;
			}
		}
		
		// Player heals
		else if (playerAction == 14) {
			
			player.idle();
			
			// If the action is complete, have the player idle, regain mana, change to next scene
			if(count == 0) {
				goal = totalMana * 1/3;
			}
			count++;
			curMana--;
			if(curMana < 0) {
				player.takeDamage(2);
				curMana = 0;
			}
			player.setCurrentHealth(player.getCurrentHealth() + 1);
			if(player.getCurrentHealth() > player.getTotalHealth()) player.setCurrentHealth(player.getTotalHealth());
						
			if(count >= goal) {
				playerAction = 0;
				scene = 4;
			}
		}
		
		// Player mega heals
		else if (playerAction == 15) {
			
			player.idle();
			
			// If the action is complete, have the player idle, regain mana, change to next scene
			if(count == 0) {
				goal = totalMana * 2/3;
			}
			count++;
			curMana--;
			if(curMana < 0) {
				player.takeDamage(3);
				curMana = 0;
			}
			player.setCurrentHealth(player.getCurrentHealth() + 1*(intelligenceMultiplier/2));
			if(player.getCurrentHealth() > player.getTotalHealth()) player.setCurrentHealth(player.getTotalHealth());
						
			if(count >= goal) {
				playerAction = 0;
				scene = 4;
			}
		}
		
		// If a spell is cast...
		if(playerSpell != null) {
			
			// If the spell is flying, have the spell fly
			if(spell_first_stage) {
				
				// If the action is complete, change the spell to second (explosion) stage
				if(playerSpell.fly()) {
					spell_first_stage = false;
					
					if(playerSpell.getType().equals("fire")) {
						fireSound();
					} else if(playerSpell.getType().equals("zap")) {
						zapSound();
					}
					
				}
			}
			
			// If the spell is exploding, have the spell explode
			if(!spell_first_stage) {
				
				//  If the action is complete, have the monster idle, monster take damage, change to next scene, spell is null
				if(playerSpell.explode()) {
					
					monsterAction = 0;
					
					double coefficient = 1;
					if(monster.getClass().equals(new Goblin(0, 0, 0, 0, 0, 0, false, 0).getClass())) {
						coefficient = 0.05;
					} else if(monster.getClass().equals(new Slime(0, 0, 0, 0, 0, 0, false, 0).getClass())) {
						coefficient = 120;
					}
					
					monster.takeDamage((int)((playerSpell.getDamage() * statMenu.getIntelligence() * crit * intelligenceMultiplier / 2 + statMenu.getWisdom() * intelligenceMultiplier)*coefficient));
					
					curMana -= playerSpell.getDamage() * 8;
					
					if(curMana < 0) {
						player.takeDamage(-curMana * 2);
						curMana = 0;
					}
					
					// Determines if previous hit was a crit and displays Critical Hit! if it was
					if(crit > 1) {
						critHit = true;
						critValue = crit;
					}
					
					scene = 4;

					playerSpell = null;
				}
			}
		}
		
		if(monster != null) {
			
			// If the monster changes actions, reset monster's index
			if(monsterAction != lastMonsterAction) {
				monster.resetIndex();
				lastMonsterAction = monsterAction;
			}
			
			// Monster idle
			if(monsterAction == 0) {
				monster.idle();
				
			}
			
			// Monster attack 1
			if(monsterAction == 1) {
				
				// If the action is complete, have the monster idle, player hurts, player takes damage
				if(monster.attack1()) {
					monsterAction = 0;
					playerAction = 5;
					
					player.takeDamage(monster.getAttack1Damage());
				}
			}
			
			// Monster dies
			if(monsterAction == 2) {
				
				// If the action is complete, monster removed
				if(monster.die()) {
					
					curXP += monster.getXP();
					lifetimeXP += monster.getXP();
					
					if(monster.isBoss()) {
						scene = 9;
					}
					else if(curXP >= totalXP) {
						scene = 7;
						levelSound();
					} else {
						scene = 1;
					}
					
					monster = null;
					monsterHealthBar = null;
					
					
				}
			}
		}
	}
	
	// Set up background images
	private void imageSetUp() {
		
		try {
			dungeon = ImageIO.read(new File("rpgImages/dungeon1.png"));
			
		}
		catch (IOException e) {
			e.printStackTrace();
        }
		
		// Initialize background image locations
		dungeon1X = 0; dungeon1Y = 0; dungeon2X = WIDTH; dungeon2Y = 0; dungeonWidth = WIDTH; dungeonHeight = HEIGHT; 
	}
	
	// Player walks in place while background scrolls
	private boolean walk() {
		
		if(dungeon1X < -WIDTH + 1) {
			dungeon1X = WIDTH;
		}
		if(dungeon2X < -WIDTH + 1) {
			dungeon2X = WIDTH;
		}
		dungeon1X += -1;
		dungeon2X += -1;
		
		return player.walkAnimation();
	}
	
	// Player casts a spell
	private boolean cast() {
		
		int targetX = WIDTH;
		int targetY = HEIGHT - 150;
		
		if(monster != null) {
			targetX = monster.getX() + (int)(1.0/3*monster.getWidth());
			targetY = monster.getY();
		}
		
		if (spellChoice == 0) {
			return castBasicFireball(targetX);
		}
		else if (spellChoice == 1) {
			return castZap(targetX);
		}
		else if (spellChoice == 2) {
			return castLightningStrike(targetY);
		}
		
		return false;
	}
	
	// cast Basic Fireball
	private boolean castBasicFireball(int targetX) {
		if (player.cast()) {
			playerSpell = new BasicFireball(player.getX(), player.getY(), animationDelay, targetX);
			spell_first_stage = true;
			return true;
		}
		return false;
	}
	
	// cast Zap
	private boolean castZap(int targetX) {
		if (player.cast()) {
			playerSpell = new Zap(player.getX(), player.getY(), animationDelay, targetX);
			spell_first_stage = true;
			return true;
		}
		return false;
	}
	
	// cast Lightning Strike
	private boolean castLightningStrike(int targetX) {
		if (player.cast()) {
			playerSpell = new LightningStrike(monsterHomeX, animationDelay, targetX);
			spell_first_stage = true;
			return true;
		}
		return false;
	}
	
	// Spawns in a random new monster from the list of mobs
	private void monsterSpawn() {
		String choice = mobs.get((int) (Math.random() * mobs.size()));
		
		int targetLevel = 2;
		
		if(killCount > 6) {
			targetLevel = killCount/2;
		}
		
		if(choice.equals("SnowMonster")) monster = new SnowMonster(WIDTH, animationDelay, player.getX(), HEIGHT, monsterHomeX, targetLevel, countToDetermineIfBoss > 5, player.getLevel());
		if(choice.equals("Goblin")) monster = new Goblin(WIDTH, animationDelay, player.getX(), HEIGHT, monsterHomeX, targetLevel, countToDetermineIfBoss > 5, player.getLevel());
		if(choice.equals("Slime")) monster = new Slime(WIDTH, animationDelay, player.getX(), HEIGHT, monsterHomeX, targetLevel, countToDetermineIfBoss > 5, player.getLevel());

		
		if(monster.isBoss()) {
			countToDetermineIfBoss = 0;
		}
		
		
		killCount++;
		countToDetermineIfBoss++;
	}
	
	// This updates the player's damage values and bars to reflect stats and game play
	private void updatePlayer() {
		
		// Determines Strength multiplier
		strengthMultiplier = 0;
		for(int i = 0; i <= statMenu.getStrength()*0.1; i++) {
			strengthMultiplier += 1;
		}
		// Determines Strength multiplier
		intelligenceMultiplier = 0;
		for(int i = 0; i <= statMenu.getIntelligence()*0.1; i++) {
			intelligenceMultiplier += 2;
		}
		// Determines Strength multiplier
		int wisdomMultiplier = 0;
		for(int i = 0; i <= statMenu.getWisdom()*0.1; i++) {
			wisdomMultiplier += 2;
		}
		// Determines health multiplier
		int constitutionMultiplier = 0;
		for(int i = 0; i <= statMenu.getConstitution()*0.1; i++) {
			constitutionMultiplier += 1;
		}
		
		// Determines whether or not this is a critical hit
		crit = 1;
		if(Math.random() > 0.6) {
			for(int i = 0; i < statMenu.getLuck()*0.2; i++) {
				if(Math.random()*10*Math.pow(100, i/2) < statMenu.getLuck() * (int)(1 + 0.2*statMenu.getIntelligence() * intelligenceMultiplier)* (int)(1 + 0.1*statMenu.getWisdom() * wisdomMultiplier)) crit *= 2;
			}
		}
		
		// Determines damage values
		swingDamage = crit * strengthMultiplier * baseSwingDamage * statMenu.getStrength() + baseSwingDamage * (int)(1 + 0.2*statMenu.getWisdom() * wisdomMultiplier);
		stabDamage = crit * strengthMultiplier * baseStabDamage * statMenu.getStrength() * (int)(1 + 0.2*statMenu.getWisdom() * wisdomMultiplier) / 10;
		quickStabDamage = crit * strengthMultiplier * baseQuickStabDamage * statMenu.getStrength() + baseSwingDamage * (int)(1 + 0.4*statMenu.getWisdom() * wisdomMultiplier);
		giantSwingDamage = crit * strengthMultiplier * baseGiantSwingDamage * statMenu.getStrength() + baseSwingDamage * (int)(1 + 0.2*statMenu.getWisdom() * wisdomMultiplier) * (int)(1 + 0.2*statMenu.getConstitution());
		
		// Determines player health
		player.setCurrentHealth(player.getCurrentHealth() + statMenu.getConstitution() * 10 * constitutionMultiplier - player.getTotalHealth());
		player.setTotalHealth(statMenu.getConstitution() * 10 * constitutionMultiplier);
		
		// Determines player stamina
		curStamina = curStamina + statMenu.getConstitution() * 5 * constitutionMultiplier + statMenu.getStrength() * 5 - totalStamina;
		totalStamina = statMenu.getConstitution() * 5 * constitutionMultiplier + statMenu.getStrength() * 5;
		staminaRegen = statMenu.getConstitution()/2 * constitutionMultiplier + statMenu.getStrength()/5 * strengthMultiplier;
		
		// Determines player mana
		curMana = curMana + statMenu.getIntelligence() * 5 * intelligenceMultiplier + statMenu.getIntelligence() * wisdomMultiplier - totalMana;
		totalMana = statMenu.getIntelligence() * 5 * intelligenceMultiplier + statMenu.getIntelligence() * wisdomMultiplier;
		manaRegen = statMenu.getWisdom() * (wisdomMultiplier + intelligenceMultiplier) - statMenu.getStrength()/5;
		
	}
	
	// Checks if the player levels up
	private void playerLevelUp() {
		
		int overflowXP = curXP - totalXP;
		
		// If the player levels up, give them more statpoints and update experience
		totalXP = (int) (Math.pow(2, player.getLevel()) * 100);
		player.setLevel(player.getLevel() + 1);
		curXP = overflowXP;
		statMenu.setStatPoints(statMenu.getStatPoints() + 5);
		
		// Set player to full health
		player.setCurrentHealth(player.getTotalHealth());

	}
	
	// Restart the game if GAMEOVER and R is pressed
	private class RAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			if(end) start();
		}
	}
	
	// Switch to determine if paused when space is pressed
	private class SpaceAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			paused = !paused;
		}
	}
	
	// Game Audio
	public synchronized void zapSound() {
		new Thread(new Runnable() {

			public void run() {

				try {
					Clip clip = AudioSystem.getClip();
					
					BufferedInputStream in = new BufferedInputStream(new FileInputStream("rpgSound/zapSound.wav"));
					
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(in);
					
					clip.open(inputStream);
					clip.start();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start(); 
	}
	
	public synchronized void clickSound() {
		new Thread(new Runnable() {

			public void run() {

				try {
					Clip clip = AudioSystem.getClip();
					
					BufferedInputStream in = new BufferedInputStream(new FileInputStream("rpgSound/clickSound.wav"));
					
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(in);
					
					clip.open(inputStream);
					clip.start();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start(); 
	}
	
	public synchronized void dieSound() {
		new Thread(new Runnable() {

			public void run() {

				try {
					Clip clip = AudioSystem.getClip();
					
					BufferedInputStream in = new BufferedInputStream(new FileInputStream("rpgSound/dieSound.wav"));
					
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(in);
					
					clip.open(inputStream);
					clip.start();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start(); 
	}
	
	public synchronized void backgroundSound() {
		new Thread(new Runnable() {

			public void run() {

				try {
					Clip clip = AudioSystem.getClip();
					
					BufferedInputStream in = new BufferedInputStream(new FileInputStream("rpgSound/backgroundSound.wav"));
					
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(in);
					
					clip.open(inputStream);
					clip.start();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start(); 
	}
	
	public synchronized void runSound() {
		new Thread(new Runnable() {

			public void run() {

				try {
					Clip clip = AudioSystem.getClip();
					
					BufferedInputStream in = new BufferedInputStream(new FileInputStream("rpgSound/runSound.wav"));
					
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(in);
					
					clip.open(inputStream);
					clip.start();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start(); 
	}
	public synchronized void fireSound() {
		new Thread(new Runnable() {

			public void run() {

				try {
					Clip clip = AudioSystem.getClip();
					
					BufferedInputStream in = new BufferedInputStream(new FileInputStream("rpgSound/fireSound.wav"));
					
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(in);
					
					clip.open(inputStream);
					clip.start();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start(); 
	}
	public synchronized void levelSound() {
		new Thread(new Runnable() {

			public void run() {

				try {
					Clip clip = AudioSystem.getClip();
					
					BufferedInputStream in = new BufferedInputStream(new FileInputStream("rpgSound/levelSound.wav"));
					
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(in);
					
					clip.open(inputStream);
					clip.start();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start(); 
	}
	
	class BackgroundMusicTask extends TimerTask {
		@Override
		public void run() {
	        backgroundSound(); // Plays the background music
	    }
	}
	
	class RunSoundTask extends TimerTask {
		@Override
		public void run() {
	        if((playerAction == 1 || playerAction == 8) && !paused && !end) runSound(); // Plays the run sound
	    }
	}
}
