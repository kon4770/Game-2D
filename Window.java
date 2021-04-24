import javax.swing.JFrame;

public class Window extends JFrame{
	private static final long serialVersionUID = 1L;

	public Window() {
		GamePanel gp = new GamePanel();
		this.setTitle("COS");
		this.setSize(1000, 1000);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setIgnoreRepaint(true);
		this.add(gp);
		this.setVisible(true);
    }
	
}
