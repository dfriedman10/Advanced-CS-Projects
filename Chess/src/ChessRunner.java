import javax.swing.JFrame;

public class ChessRunner{
	public static final int WIDTH = 750, HEIGHT = 750;	

	
	public ChessRunner() {
		JFrame frame = new JFrame();
		frame.setSize(WIDTH+2, HEIGHT+24);
		
		ChessGraphics display = new ChessGraphics();
		
		frame.add(display);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new ChessRunner();
	}

}


