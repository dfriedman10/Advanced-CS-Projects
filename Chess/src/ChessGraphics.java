import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class ChessGraphics extends JPanel {
	
	private ChessButton[][] display = new ChessButton[8][8];
	
	ChessBoard board = new ChessBoard();
	
	private boolean twoPlayer;
	
	ChessButton pressed = null;
	
	HashMap<Piece, ImageIcon> images = new HashMap<Piece, ImageIcon>();
	
	public class ChessButton extends JButton {
		final Color origColor;
		final int i, j;
		public ChessButton(Color c, int i, int j, ImageIcon img) {
			origColor = c;
			this.i = i; this.j = j;
			if (img != null) {
				super.setIcon(img);
			}
			super.setOpaque(true);
			super.setBackground(c);
			super.setBorder( new LineBorder(Color.black) );
			super.addActionListener(new ChessButtonListener());
		}
		
		public void setImage(ImageIcon img) {
			super.setIcon(img);
		}
		public void resetBackground() {
			super.setBackground(origColor);
		}
	}
	
	public class ChessButtonListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			ChessButton triggerButton  = (ChessButton)e.getSource();
		
			if (pressed == null) {
				Piece p = board.getPiece(triggerButton.i, triggerButton.j);
				if (p == null || p.team != board.turn) return;

				triggerButton.setBackground(Color.yellow);
				pressed = triggerButton;
			}
			
			else {
				boolean moved = false;
				for (int[] possMove : board.getPiece(pressed.i, pressed.j).getMoves(board, pressed.i, pressed.j)) {
					if (triggerButton.i == possMove[0] && triggerButton.j == possMove[1]) {
						Team winner = board.move(pressed.i,pressed.j,triggerButton.i,triggerButton.j);
						moved = true;	
						
						triggerButton.setImage(images.get(board.getPiece(triggerButton.i,triggerButton.j)));
						pressed.setImage(null);
						
						if (winner != null) {
					        JOptionPane.showMessageDialog(null, (winner == Team.WHITE ? "White" : "Black") + " team wins!", "Chess", JOptionPane.INFORMATION_MESSAGE);
					        System.exit(0);
						}
						break;
					}
				}
				pressed.resetBackground();
				pressed = null;
				
				if (moved) {
					if (board.check())
				        JOptionPane.showMessageDialog(null, "Check!", "Chess", JOptionPane.INFORMATION_MESSAGE);

					if (!twoPlayer) {
						System.out.println("moving ai");
						int[] aiMove = board.moveAI();
						display[aiMove[0]][aiMove[1]].setImage(null);
						display[aiMove[2]][aiMove[3]].setImage(images.get(board.getPiece(aiMove[2],aiMove[3])));
						
						if (aiMove.length == 5) {
					        JOptionPane.showMessageDialog(null, (aiMove[4] == 1 ? "White" : "Black") + " team wins!", "Chess", JOptionPane.INFORMATION_MESSAGE);
					        System.exit(0);
						}
						if (board.check())
					        JOptionPane.showMessageDialog(null, "Check!", "Chess", JOptionPane.INFORMATION_MESSAGE);
					}
				}

			}
		}
	}

	public ChessGraphics() {
		twoPlayer = JOptionPane.showConfirmDialog(null, "Two Player?", "Chess", JOptionPane.YES_NO_OPTION) == 0;

		this.setLayout(new GridLayout(8,8));
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				ChessButton b;
				Piece p = board.getPiece(i, j);
				
				if (p == null) {
					b = new ChessButton((i+j)%2==0 ? new Color(18, 140, 95) : new Color(214, 240, 229), i, j, null);

				}
				else {
					try {
						String imgName = "resources/" + (p.team == Team.WHITE ? "White":"Black") + p.getClass().getSimpleName() + ".png";
						images.put(p, new ImageIcon(ImageIO.read(getClass().getResource(imgName))));
					} catch (IOException e) {
						e.printStackTrace();
					}  
					b = new ChessButton((i+j)%2==0 ? new Color(18, 140, 95) : new Color(214, 240, 229), i, j, images.get(board.getPiece(i,j)));
				}
				this.add(b);
				display[i][j] = b;
			}
		}
	}
}
