import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;
import java.util.Set;

public class EndNode {
	private int index;
	private int x;
	private int y;
	private int width;
	private int correction;
	private boolean can_paint = false;

	public EndNode(Set<Integer> map, int width, int n_block) {
		Random rnd = new Random();
		index = (int) map.toArray()[rnd.nextInt(map.size())];
		correction = 500 - (n_block / 2 * width);
		this.width = width;
		x = index / n_block * width + correction + width / 2;
		y = index % n_block * width + correction + width / 2;
	}
	
	public boolean isThisEnd(int character_index) {
		if(character_index==index) {
			return true;
		}
		return false;
	}
	
	public void setPermission(boolean b) {
		can_paint = b;
	}

	public void draw(Graphics g) {
		if (can_paint) {
			Graphics2D gn = (Graphics2D) g;
			gn.setColor(Color.lightGray);
			gn.fillRect(x - width / 4, y - width / 4, 10, 10);
		}
	}
}
