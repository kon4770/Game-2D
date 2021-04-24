import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements KeyListener, ActionListener {

	private static final long serialVersionUID = 1L;
	private Map map;
	private Player pl;
	private Lazer lazer;
	private HashSet<Enemy> enemy = new HashSet<>();
	private int mode = 3;
	private int edge_n = 3;
	private Timer timer = new Timer(16, this);

	private boolean[] key = { false, false, false, false, false };
	private boolean next_game = false;

	public GamePanel() {
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		this.setBackground(Color.darkGray);
		timer.setDelay(1000);
		timer.start();
		map = new Map(edge_n, 20, 40);
		lazer = new Lazer(map.getAllMoves(), 20, 40);
		System.out.println("cos " + map.getStart() + " aa " + map.getStart() / 40 + " " + map.getStart() % 40);
		pl = new Player(20, 40, map.getStart());
		for (int i = 0; i < edge_n; i++) { 
			enemy.add(new Enemy(map.getAllMoves(), 20, 40, map.getStart()));
		}
		EnemyManagment m = new EnemyManagment(map.getAllMoves(),edge_n, 20,40);
	}

	public void reset(int nMode) {
		mode = 3;
		map = new Map(nMode, 20, 40);
		lazer = new Lazer(map.getAllMoves(), 20, 40);
		System.out.println("cos " + map.getStart() / 40 + map.getStart() % 40);
		pl = new Player(20, 40, map.getStart());
		enemy.clear();
		for (int i = 0; i < edge_n; i++) { 
			enemy.add(new Enemy(map.getAllMoves(), 20, 40, map.getStart()));
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D gn = (Graphics2D) g;
		if (next_game) {
			timer.setDelay(1000);
			gn.setColor(Color.RED);
			gn.setFont(new Font("Dialog", Font.BOLD, 35));
			gn.drawString("Resetting...", 450, 400);
		}
		paintState(gn);
		pl.draw(g);
		map.draw(g);
		lazer.draw(g);
		enemy.forEach(e -> {
			e.draw(g);
		});
	}

	private void paintState(Graphics2D g) {
		if (mode >= 1) {
			g.setColor(Color.CYAN);
			// timer = new Timer(1000,this);
			timer.setDelay(1000);
			g.setFont(new Font("Dialog", Font.PLAIN, 35));
			System.out.println(mode);
			g.drawString("Start in " + mode, 450, 450);
			mode--;
			System.out.println(mode + " " + mode);
		} else if (mode == 0) {
			next_game = false;
			g.setColor(Color.green);
			g.setFont(new Font("Dialog", Font.PLAIN, 35));
			g.drawString("GO", 450, 450);
			timer.setDelay(4);
			map.setPermission(true);
			pl.setPermission(true);
			System.out.println(mode--);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		HashSet<Integer> pos_codes = map.getPossibleMoves(pl.getPlaceIndex());
		if (key[0]) {
			pl.move(1, pos_codes);
		}
		if (key[1]) {
			pl.rotate(-2, pos_codes);
		}
		if (key[2]) {
			pl.move(-1, pos_codes);
		}
		if (key[3]) {
			pl.rotate(2, pos_codes);
		}
		if (key[4]) {
			lazer.setCoor(pl.getX(), pl.getY(), pl.getAngle());
			lazer.shoot();
			lazer.setPermission(true);
			enemy.forEach(en -> {
				en.start(pl.getX(), pl.getY());
			});
		}
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W) {
			key[0] = true;
		} else if (e.getKeyCode() == KeyEvent.VK_A) {
			key[1] = true;
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			key[2] = true;
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			key[3] = true;
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			key[4] = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_R) {
			reset(++edge_n);
			next_game = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W) {
			key[0] = false;
		} else if (e.getKeyCode() == KeyEvent.VK_A) {
			key[1] = false;
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			key[2] = false;
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			key[3] = false;
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			key[4] = false;
		}
	}

}
