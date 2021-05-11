import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.HashSet;

public class EnemyManagment {

	private int target_index;
	private int b_width;
	private int n_block;
	private int correction;
	private HashSet<Enemy> enemies = new HashSet<>();

	public EnemyManagment(HashMap<Integer, HashSet<Integer>> map_raw, int target_index, int enemy_count, int width, int n_block) {
		
		b_width = width;
		this.n_block = n_block;
		correction = 500 - (n_block / 2 * width);
		
		this.target_index = target_index;
		
		for (int i = 0; i < enemy_count; i++) {
			enemies.add(new Enemy(map_raw, width, n_block, target_index));
		}
	}

	public void move(int new_target_x, int new_target_y) {		
		int new_target_id = (new_target_x-correction)/ b_width  * n_block + (new_target_y-correction)/ b_width;
		
		if(target_index != new_target_id) {
			enemies.forEach(n -> {
				n.start(new_target_id);
			});
		}
		target_index = new_target_id;
	}
	
	public void draw(Graphics g) {
		enemies.forEach(n -> {
			n.draw(g);
		});
	}
	
	public boolean willItKill(int new_target_x, int new_target_y) {
		boolean rezult = false;
		for(Enemy e : enemies) {
			if(e.willKill(new_target_x, new_target_y)) {
				rezult = true;
			}
		}
		return rezult;
	}
	
	public void killIfHit(int[] tabx, int[] taby) {
		HashSet<Line2D> lines = new HashSet<>();
		for(int i=0;i<tabx.length-1;i++) {
			Line2D l = new Line2D.Double();
			l.setLine(tabx[i],taby[i],tabx[i+1],taby[i+1]);
			lines.add(l);
		}
		HashSet<Enemy> to_remove = new HashSet<>();
		for(Enemy en : enemies) {
			int index = en.getCurrentPossision();
			int x = index / n_block * b_width + correction + b_width / 2;
			int y = index % n_block * b_width + correction + b_width / 2;
			Rectangle rectangle = new Rectangle(x - b_width / 4, y - b_width / 4, 10, 10);
			for(Line2D l : lines) {
				if(rectangle.intersectsLine(l)) {
					to_remove.add(en);
				}
			}
		}
		enemies.removeAll(to_remove);
	}

}
